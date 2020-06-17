package com.example.dogsappmvvm.viewmodel;

import android.app.Application;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.dogsappmvvm.model.DogBreed;
import com.example.dogsappmvvm.model.DogDao;
import com.example.dogsappmvvm.model.DogDataBase;
import com.example.dogsappmvvm.model.DogsApiService;
import com.example.dogsappmvvm.util.SharedPreferencesHelper;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    private final static  String TAG = "ListViewModel";

    public MutableLiveData<List<DogBreed>> dogList = new MutableLiveData<>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private DogsApiService dogsService = new DogsApiService();

    // CompositeDisposable --> collect disposable and destroy them when it needs to.
    private CompositeDisposable disposable = new CompositeDisposable();

    private AsyncTask<List<DogBreed>, Void, List<DogBreed>> insertTask;

    private AsyncTask<Void, Void, List<DogBreed>> retrieveTask;

    private SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getApplication());

    // 5 minutes
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L;

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){
        long updateTime = sharedPreferencesHelper.getUpdateTime();
        long currentTime = System.nanoTime();

        // sharedPreferencesHelper.getUpdateTime() return 0 as a default value meaning the database is empty
        if(updateTime != 0 && (currentTime - updateTime) < refreshTime){
            fetchFromDatabase();
        }
        else{
            fetchFromRemote();
        }
    }

    public void refreshBypassCache(){
        fetchFromRemote();
    }

    // fetch data from API
    private void fetchFromRemote(){

        // getDogs will return Single which is a type of observable

        // subscribeOn --> Since we don't know how long it will take to fetch information from the
        // Internet coming back to our application, we cannot use our main thread. If main thread will
        // be stuck for a long period, it blocks the user Api(user interaction) and may crush.
        // Therefore, we need to subscribe on another thread where this operation will be done in background.

        // observeOn --> Get that data from the Api to background thread but we cannot show it to user.
        // So when we finish background operation, we need to observe that info to get that data on MainThread

        // subscribeWith --> subscribe the observable to check whether the operation was success or failure

        // DisposableObserver --> observer which can be disposed ay any point to prevent out of memory issue.

        loading.setValue(true);
        disposable.add(
            dogsService.getDogs()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                        @Override
                        public void onSuccess(List<DogBreed> dogBreeds) {
                            // Use AsyncTask using a background thread
                            insertTask = new InsertDogsTask();
                            insertTask.execute(dogBreeds);
                            Toast.makeText(getApplication(), "Dogs retrieved from endpoint", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dogLoadError.setValue(true);
                            loading.setValue(false);
                            Log.d(TAG, "Here");
                            e.printStackTrace();
                        }
                    })
        );

    }

    private void fetchFromDatabase(){

        loading.setValue(true);
        retrieveTask = new RetrieveDogTask();
        retrieveTask.execute();

    }

    // parameters 1. data type of data input 3. return type of data output
    private class InsertDogsTask extends AsyncTask<List<DogBreed>, Void, List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list = lists[0];

            // get dao   Here we need context so chose AndroidViewModel
            DogDao dao = DogDataBase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();

            // save all dogs into database and return the uuid list
            List<Long> result = dao.insertAll(list.toArray(new DogBreed[0]));

            // after save dogs into database, record the current time
            sharedPreferencesHelper.saveUpdateTime(System.nanoTime());

            int i = 0;
            while(i < list.size()){
                list.get(i).uuid = result.get(i).intValue();
                ++i;
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
        }
    }


    private class RetrieveDogTask extends AsyncTask<Void, Void, List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return DogDataBase.getInstance(getApplication()).dogDao().getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT).show();
        }
    }

    private void dogsRetrieved(List<DogBreed> dogBreeds){

        // set the loaded dogBreeds to MutableLiveData dogList
        dogList.setValue(dogBreeds);
        dogLoadError.setValue(false);
        loading.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        if(insertTask != null){
            insertTask.cancel(true);
            insertTask = null;
        }

        if(retrieveTask != null){
            retrieveTask.cancel(true);
            retrieveTask = null;
        }
    }

}

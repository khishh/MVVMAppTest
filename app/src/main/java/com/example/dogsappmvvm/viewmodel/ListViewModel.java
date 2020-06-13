package com.example.dogsappmvvm.viewmodel;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.dogsappmvvm.model.DogBreed;
import com.example.dogsappmvvm.model.DogsApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    private final static  String TAG = "ListViewModel";

    public MutableLiveData<List<DogBreed>> dogList = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    private DogsApiService dogsService = new DogsApiService();

    // CompositeDisposable --> collect disposable and destroy them when it needs to.
    private CompositeDisposable disposable = new CompositeDisposable();

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){
        fetchFromRemote();
    }

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
                            dogList.setValue(dogBreeds);
                            dogLoadError.setValue(false);
                            loading.setValue(false);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}

package com.example.dogsappmvvm.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.dogsappmvvm.model.DogBreed;
import com.example.dogsappmvvm.model.DogDao;
import com.example.dogsappmvvm.model.DogDataBase;

public class DetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dog = new MutableLiveData<>();

    private AsyncTask<Integer, Void, DogBreed> retrieveDetailsTask;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetch(int uuid){
        retrieveDetailsTask = new RetrieveDetailsTask();
        retrieveDetailsTask.execute(uuid);
    }

    private class RetrieveDetailsTask extends AsyncTask<Integer, Void, DogBreed>{

        @Override
        protected DogBreed doInBackground(Integer... integers) {

            int uuid = integers[0];

            DogDao dao = DogDataBase.getInstance(getApplication()).dogDao();

            return dao.getDog(uuid);
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogDetailsRetrieved(dogBreed);
        }
    }

    private void dogDetailsRetrieved(DogBreed a_dog){
        dog.setValue(a_dog);
        Toast.makeText(getApplication(), "Dog Details retrieved from database", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if(retrieveDetailsTask != null){
            retrieveDetailsTask.cancel(true);
            retrieveDetailsTask = null;
        }
    }
}

package com.example.dogsappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.dogsappmvvm.model.DogBreed;

public class DetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dog = new MutableLiveData<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){
        DogBreed dog1 = new DogBreed("1", "corgi", "15 year", "corigi", "breadwhat", "temperament", null);
        dog.setValue(dog1);
    }

}

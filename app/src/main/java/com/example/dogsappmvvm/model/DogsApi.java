package com.example.dogsappmvvm.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface DogsApi {

    // end point
    @GET("DevTides/DogsApi/master/dogs.json")
    Single<List<DogBreed>> getDogs();

}

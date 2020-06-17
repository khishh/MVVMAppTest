package com.example.dogsappmvvm.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// define all methods need to access the database
@Dao
public interface DogDao {

    // the return List<Integer> is the set of uuid
    // ... notation means zero or more DogBreed objects may be passes as the argument fot this method
    // https://stackoverflow.com/questions/3158730/java-3-dots-in-parameters
    @Insert
    List<Long> insertAll(DogBreed... dogs);

    @Query("SELECT * FROM dogbreed")
    List<DogBreed> getAllDogs();

    @Query("SELECT * FROM dogbreed WHERE uuid == :dogId")
    DogBreed getDog(int dogId);

    @Query("DELETE FROM dogbreed")
    void deleteAllDogs();

}

package com.example.myapplication;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiMovies {

    @GET("/jsonData/moviesData.txt")
    Call<JsonObject> getFilm();

}

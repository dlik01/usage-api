package com.example.myapplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static String baseUrl = "https://jsonparsingdemo-cec5b.firebaseapp.com";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl);

    public static <S> S createService(Class<S> servce){
        httpClient.interceptors().clear();
        httpClient.readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS);
        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(servce);
    }
}

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {

    List<MoviesData> moviesDataList = null;
    ApiMovies apiMovies;
    MoviesData moviesData;
    ImageView image;
    TextView textView, textView2;
    String baseUrl = "https://jsonparsingdemo-cec5b.firebaseapp.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
        textView =findViewById(R.id.textView);
        textView2 =findViewById(R.id.textView2);
        new  ParseTask().execute(baseUrl);
    }





    public void getMoviData(){
    }


@SuppressLint("StaticFieldLeak")
private  class ParseTask extends AsyncTask {
    String baseUrl = "https://jsonparsingdemo-cec5b.firebaseapp.com";
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl);

    @Override
    protected List<MoviesData> doInBackground(Object[] objects) {
        try {

            List<MoviesData> moviesDataList= null;
            Response<JsonObject> response = getApiMovies().getFilm().execute();
            if(response.isSuccessful() && response.code() == 200){
                Log.e("@@@" , "code " + response.code());
                Log.e("@@@" , "code " + response.body());
                JSONObject parentJson = new JSONObject(String.valueOf(response.body()));
                JSONArray array =parentJson.getJSONArray("movies");
                Log.e("@@@", "array " + array.length());
                moviesDataList = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++){
                    JSONObject film = array.getJSONObject(i);
                    moviesData = gson.fromJson(film.toString(),MoviesData.class);
                    moviesDataList.add(moviesData);
                }
                return moviesDataList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Object o) {
        Picasso.with(getApplicationContext()).load(moviesData.getImage()).into(image);
        textView.setText(moviesData.getMovie());
        textView2.setText(moviesData.getStory());
    }

    public  <S> S createService(Class<S> servce){
        httpClient.interceptors().clear();
        httpClient.readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS);
        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(servce);
    }
    private ApiMovies getApiMovies(){
        return createService(ApiMovies.class);
    }

}

}

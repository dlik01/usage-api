package com.example.myapplication;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    MoviesData moviesData;
    ImageView image;
    TextView textView, textView2;
    final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        new ParseTask().execute();
        this.startService(new Intent(this, ServiceNotification.class));
    }


    public class ParseTask extends AsyncTask {

        @Override
        protected List<MoviesData> doInBackground(Object[] objects) {
            try {
                Response<JsonObject> response = MainApplication.getInstance().getApiMovies().getFilm().execute();
                if (response.isSuccessful() && response.code() == 200) {
                    JSONObject parentJson = new JSONObject(String.valueOf(response.body()));
                    JSONArray array = parentJson.getJSONArray("movies");
                    Gson gson = new Gson();
                    int i = random.nextInt(array.length());
                    JSONObject film = array.getJSONObject(i);
                    moviesData = gson.fromJson(film.toString(), MoviesData.class);

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

    }
}

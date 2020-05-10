package com.example.myapplication;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class ServiceNotification extends Service {
    MoviesData moviesData;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadData();
    }

    public void notification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(101, builder.build());
    }

    private void downloadData() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    List<MoviesData> moviesDataList;
                    Response<JsonObject> response = MainApplication.getInstance().getApiMovies().getFilm().execute();
                    if(response.isSuccessful() && response.code() == 200) {
                        JSONObject parentJson = new JSONObject(String.valueOf(response.body()));
                        JSONArray array = parentJson.getJSONArray("movies");
                        moviesDataList = new ArrayList<>();
                        Gson gson = new Gson();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject film = array.getJSONObject(i);
                            moviesData = gson.fromJson(film.toString(), MoviesData.class);
                            moviesDataList.add(moviesData);
                        }
                        notification(moviesData.getMovie(),moviesData.getStory());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}

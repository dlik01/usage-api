package com.example.myapplication.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.example.myapplication.MainApplication;
import com.example.myapplication.MoviesData;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Response;

public class Widget extends AppWidgetProvider {

    final Random random = new Random();
    MoviesData moviesData;
    RemoteViews updateViews;
    AppWidgetManager widgetManager;
    int[] widgetIds;
    Context mContext;
    Bitmap bitmap = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        if (mContext == null) {
            mContext = context;
        }
        widgetManager = appWidgetManager;
        widgetIds = appWidgetIds;
        updateViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout);
        downloadData();
    }

    private void downloadData() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Response<JsonObject> response = MainApplication.getInstance().getApiMovies().getFilm().execute();
                    if (response.isSuccessful() && response.code() == 200) {
                        JSONObject parentJson = new JSONObject(String.valueOf(response.body()));
                        JSONArray array = parentJson.getJSONArray("movies");
                        Gson gson = new Gson();
                        int i = random.nextInt(array.length());
                        JSONObject film = array.getJSONObject(i);
                        moviesData = gson.fromJson(film.toString(), MoviesData.class);
                        bitmap = BitmapFactory.decodeStream((InputStream) new URL(moviesData.getImage()).getContent());
                        updateViews.setImageViewBitmap(R.id.image, bitmap);
                        updateViews.setTextViewText(R.id.tvTitle, moviesData.getMovie());
                        widgetManager.updateAppWidget(widgetIds, updateViews);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}

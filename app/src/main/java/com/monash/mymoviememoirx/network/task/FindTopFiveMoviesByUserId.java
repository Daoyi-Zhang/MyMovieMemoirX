package com.monash.mymoviememoirx.network.task;

import android.os.AsyncTask;
import android.util.Log;

import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.DetailMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * input: userId
 * output top 5 movie watched in current year
 */
public class FindTopFiveMoviesByUserId extends AsyncTask<String, Void, List<DetailMovie>> {

    private static final String TAG = "FindTopFive";

    @Override
    protected List<DetailMovie> doInBackground(String... strings) {
        NetworkConnection nc = new NetworkConnection();
        List<DetailMovie> movies = new ArrayList<>();
        String response = nc.getTop5MoviesByUserId(strings[0]);
        Log.d(TAG, "doInBackground: server response: " + response);
        if (response != null) {
            try {
                JSONArray results = new JSONArray(response);
                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    DetailMovie movie = new DetailMovie();
                    movie.setMovieName(obj.getString("movieName"));
                    movie.setReleaseDate(obj.getString("releaseDate"));
                    movie.setUserRating(Double.valueOf(obj.getString("ratingScore")));
                    movies.add(movie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return movies;
    }
}

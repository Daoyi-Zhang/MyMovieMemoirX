package com.monash.mymoviememoirx.network.task;

import android.os.AsyncTask;

import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.Cinema;
import com.monash.mymoviememoirx.util.DetailMemoir;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindAllMemoirsByUerId extends AsyncTask<Integer, Void, List<DetailMemoir>> {
    @Override
    protected List<DetailMemoir> doInBackground(Integer... integers) {
        Integer userId = integers[0];
        NetworkConnection nc = new NetworkConnection();
        String response = nc.getAllMemoirsByuserId(userId.toString());
        List<DetailMemoir> memoirs = null;
        if (response != null) {
            try {
                memoirs = new ArrayList<>();
                JSONArray results = new JSONArray(response);
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    JSONObject cinemaJ = result.getJSONObject("cinemaId");
                    DetailMemoir newMemoir = new DetailMemoir();
                    newMemoir.setMovieName(result.getString("movieName"));
                    newMemoir.setReleaseDate(result.getString("releaseDate").split("T")[0]);
                    newMemoir.setWatchTime(result.getString("watchTime").split("\\+")[0].replace('T', ' '));
                    newMemoir.setComment(result.getString("comment"));
                    newMemoir.setRatingScore(result.getDouble("ratingScore"));
                    newMemoir.setMemoirId(result.getInt("memoirId"));
                    Cinema cinema = new Cinema(cinemaJ.getInt("cinemaId"), cinemaJ.getString("cinemaName"), cinemaJ.getString("locationPostcode"));
                    newMemoir.setCinemaId(cinema);
                    memoirs.add(newMemoir);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return memoirs;
    }

}

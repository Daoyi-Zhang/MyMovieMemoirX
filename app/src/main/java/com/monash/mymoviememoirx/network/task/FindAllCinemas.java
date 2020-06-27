package com.monash.mymoviememoirx.network.task;

import android.os.AsyncTask;

import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.Cinema;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FindAllCinemas extends AsyncTask<Void, Void, List<Cinema>> {
    @Override
    protected List<Cinema> doInBackground(Void... voids) {
        NetworkConnection nc = new NetworkConnection();
        String response = nc.getAllCinemas();
        List<Cinema> cinemas = null;
        if (response != null){
            try {
                cinemas = new ArrayList<>();
                JSONArray results = new JSONArray(response);
                for (int i = 0; i < results.length(); i++) {
                    int cinemaId = results.getJSONObject(i).getInt("cinemaId");
                    String cinemaName = results.getJSONObject(i).getString("cinemaName");
                    String postcode = results.getJSONObject(i).getString("locationPostcode");
                    Cinema foundCinema = new Cinema(cinemaId, cinemaName, postcode);
                    cinemas.add(foundCinema);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return cinemas;
    }
}

package com.monash.mymoviememoirx.network.task;

import android.os.AsyncTask;

import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.Cinema;

public class POSTCinema extends AsyncTask<Cinema, Void, String> {
    @Override
    protected String doInBackground(Cinema... cinemas) {
        Cinema newCinema = cinemas[0];
        NetworkConnection nc = new NetworkConnection();
        String response = nc.addCinema(newCinema);
        if (response != null){
            // "" succeed
            return response;
        }
        else {
            // null failed
            return null;
        }
    }
}

package com.monash.mymoviememoirx.network.task;

import android.os.AsyncTask;

import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.Memoir;

public class POSTMemoir extends AsyncTask<Memoir, Void, String> {
    @Override
    protected String doInBackground(Memoir... memoirs) {
        Memoir newMemoir = memoirs[0];
        NetworkConnection nc = new NetworkConnection();
        String response = nc.addMemoir(newMemoir);
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

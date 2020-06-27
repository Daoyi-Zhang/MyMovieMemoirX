package com.monash.mymoviememoirx.network.task;

import android.os.AsyncTask;

import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.Credential;
import com.monash.mymoviememoirx.pojo.User;

import org.json.JSONException;
import org.json.JSONObject;

public class POSTCredential extends AsyncTask<Credential, Void, String> {

    private static final String TAG = "FindCredential";

    @Override
    protected String doInBackground(Credential... credentials) {
        Credential newCredential = credentials[0];
        User newUser = newCredential.getUserId();
        NetworkConnection nc = new NetworkConnection();
        //1. POST the new user and GET the new user's userId
        String addUserResponse = nc.addUSer(newUser);
        if (addUserResponse != null && !addUserResponse.isEmpty()){
            try {
                JSONObject returnUser = new JSONObject(addUserResponse);
                int generatedId = returnUser.getInt("userId");
                newCredential.getUserId().setUserId(generatedId);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        //2. POST the new newCredential
        return nc.addCredential(newCredential);
    }
}

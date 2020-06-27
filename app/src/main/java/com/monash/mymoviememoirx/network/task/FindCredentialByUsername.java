package com.monash.mymoviememoirx.network.task;

import android.os.AsyncTask;
import android.util.Log;

import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.Credential;
import com.monash.mymoviememoirx.pojo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class will find credential from server
 * Input should be two Strings: 1. username(email address)
 * Returns the Credential object or null if not found
 * To use this in fragment or activity, remember to override onPostExecute
 */
public class FindCredentialByUsername extends AsyncTask<String, Void, Credential> {

    private static final String TAG = "FindCredential";

    @Override
    protected Credential doInBackground(String... strings) {
        NetworkConnection nc = new NetworkConnection();
        String result = nc.getCredentialsByUsername(strings[0]);
        Log.d(TAG, "doInBackground: server result: " + result);
        return resultProcess(result);
    }

    private Credential resultProcess(String result){
        Credential credential1 = null;
        if (result != null){
            JSONArray credential = null;
            JSONObject obj = null;
            try {
                credential = new JSONArray(result);
                obj = credential.getJSONObject(0);
                //password sent back
                int credentialId = obj.getInt("credentialId");
                String username = obj.getString("username");
                String password = obj.getString("password");
                String signUpDate = obj.getString("signUpDate");
                JSONObject userJ = obj.getJSONObject("userId");
                String address = userJ.getString("address");
                String dob = userJ.getString("dob");
                String gender = userJ.getString("gender");
                String name = userJ.getString("name");
                String postcode = userJ.getString("postcode");
                String state = userJ.getString("state");
                String surname = userJ.getString("surname");
                int userId = userJ.getInt("userId");
                User user = new User(name, surname, gender, dob, address, state, postcode, userId);
                credential1 = new Credential(username, password, signUpDate, user, credentialId);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "resultProcess: error");
                return null;
            }
        }
        return credential1;
    }
}

package com.monash.mymoviememoirx.network;

import android.util.Log;

import com.google.gson.Gson;
import com.monash.mymoviememoirx.pojo.Cinema;
import com.monash.mymoviememoirx.pojo.Credential;
import com.monash.mymoviememoirx.pojo.Memoir;
import com.monash.mymoviememoirx.pojo.User;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class is used for send HTTP request
 */
public class NetworkConnection {

    private static final String SERVER_BASE_URL = "http://10.0.0.16:8080/MyMovieMemoir/webresources/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "NetworkConnection";
    private OkHttpClient client;

    public NetworkConnection() {
        client = new OkHttpClient();
    }

    public String getAllMemoirsByuserId(String userId){
        final String methodPath = SERVER_BASE_URL + "pojo.memoirs/findByUserId/" + userId;
        return doGETRequest(methodPath);
    }
    public String countByTimeRange4EachPostcode(String userId, String start, String end){
        final String methodPath = SERVER_BASE_URL + "pojo.memoirs/countByTimeRange4EachPostcode/" + userId + "/" + start + "/" + end;
        return doGETRequest(methodPath);
    }
    public String countByYear4EachMonth(String userId, String year){
        final String methodPath = SERVER_BASE_URL + "pojo.memoirs/countByYear4EachMonth/" + userId + "/" + year;
        return doGETRequest(methodPath);
    }

    public String getAllCinemas() {
        final String methodPath = SERVER_BASE_URL + "pojo.cinemas/findAll";
        return doGETRequest(methodPath);
    }

    public String getTop5MoviesByUserId(String userId) {
        final String methodPath = SERVER_BASE_URL + "pojo.memoirs/findTop5MoviesRencentYear/" + userId;
        return doGETRequest(methodPath);
    }

    public String getCredentialsByUsername(String username) {
        final String url = SERVER_BASE_URL + "pojo.credentials/findByUsername/" + username;
        return doGETRequest(url);
    }

    public String addCredential(Credential credential){
        final String url = SERVER_BASE_URL + "pojo.credentials";
        return doPOSTRequest(url, credential);
    }

    public String addUSer(User user){
        final String url = SERVER_BASE_URL + "pojo.users";
        return doPOSTRequest(url, user);
    }

    public String addCinema(Cinema cinema){
        final String url = SERVER_BASE_URL + "pojo.cinemas";
        return doPOSTRequest(url, cinema);
    }

    public String addMemoir(Memoir memoir) {
        final String url = SERVER_BASE_URL + "pojo.memoirs";
        return doPOSTRequest(url, memoir);
    }
    /**
     * Do the POST request
     * @param url full rul passed required
     * @param object the object to be posted
     * @return "" or json for success, null for error
     */
    public String doPOSTRequest(String url, Object object){
        Gson gson = new Gson();
        String newJson = gson.toJson(object);
        Log.d(TAG, "doPOSTRequest: json: " + newJson);
        RequestBody body = RequestBody.create(newJson, JSON);
        Request request = new Request.Builder().url(url).post(body).build();
        String strResponse = null;
        try {
            Log.d(TAG, "doPOSTRequest: url: " + url);
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
            Log.d(TAG, "doPOSTRequest: response: " + strResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "doPOSTRequest: Bad response");
            return null;
        }
        return strResponse;
    }


    /**
     * Do the GET request
     * @param url full rul passed required
     * @return JSON if 200 OK, "" if others
     */
    public String doGETRequest(String url) {
        String results = null;
        try {
            Log.d(TAG, "doGETRequest: url: " + url);
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            results = response.body().string();
            Log.d(TAG, "doGETRequest: response: " + results);
        } catch (Exception e) {
            e.printStackTrace();
            results = "";
            Log.d(TAG, "doRequest: Bad response");
            return results;
        }
        if(results.equals("[]"))
            results = null;
        return results;
    }
}

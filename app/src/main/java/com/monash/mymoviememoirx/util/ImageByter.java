package com.monash.mymoviememoirx.util;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class helps to find a image bytes based on provide url
 */
public class ImageByter {
    public static final String TAG = "ImageByter";

    /**
     *  find a image bytes based on provide url
     * @param url full url required
     * @return the byte[] of the url, null for error
     */
    public static byte[] getImageByte(String url){
        byte[] imageBytes = null;

        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d(TAG, "getImageByte: url:" + url);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            imageBytes = response.body().bytes();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getImageByte: bad response");
            return null;
        }
        return imageBytes;
    }
}

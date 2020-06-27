package com.monash.mymoviememoirx.network.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.util.ImageByter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will assign bitmap for input movies, input movies should have full image urls
 * Returns a list of string telling if the assignment succeed for each movie
 * To use this in fragment or activity, remember to override onPostExecute
 */
public class AssignBitmapForMovies extends AsyncTask<List<DetailMovie>, Void, List<String>> {

    @Override
    protected List<String> doInBackground(List<DetailMovie>... lists) {
        List<DetailMovie> movies = lists[0];
        List<String> responses = new ArrayList<>();
        // for each movie assign a bitmap
        for(int i = 0; i < movies.size(); i++){
            DetailMovie movie = movies.get(i);
            byte[] bytes = ImageByter.getImageByte(movie.getPosterURL());
            if (bytes != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                movie.setBitmap(bitmap);
                responses.add(movie.getMovieName() + ": Succeed!");
            }
            else {
                responses.add(movie.getMovieName() + ": Failed!");
            }
        }
        return responses;
    }
}

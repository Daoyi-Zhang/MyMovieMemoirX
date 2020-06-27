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
 * This class will find movie from TMDB API
 * Input should be two Strings: 1. key words 2. movie release date ("-1" means no release date provided)
 * Returns the list of the movie or null if the movie is not found
 * To use this in fragment or activity, remember to override onPostExecute
 */
public class FindMoviesByKeywordsAndReleaseDate extends AsyncTask<String, Void, List<DetailMovie>> {

    //API key of the TMDB
    private static final String API_KEY = "83e093c70c018f8c5977847b6a1c3e4d";
    private static final String API_IMAGE_SEARCH_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String API_SEARCH_BASE_URL = "https://api.themoviedb.org/3/search/movie?api_key=";
    private static final String TAG = "FindMovieTask";
    private static final String GENRES_LIST = "{\"genres\":[{\"id\":28,\"name\":\"Action\"},{\"id\":12,\"name\":\"Adventure\"},{\"id\":16,\"name\":\"Animation\"},{\"id\":35,\"name\":\"Comedy\"},{\"id\":80,\"name\":\"Crime\"},{\"id\":99,\"name\":\"Documentary\"},{\"id\":18,\"name\":\"Drama\"},{\"id\":10751,\"name\":\"Family\"},{\"id\":14,\"name\":\"Fantasy\"},{\"id\":36,\"name\":\"History\"},{\"id\":27,\"name\":\"Horror\"},{\"id\":10402,\"name\":\"Music\"},{\"id\":9648,\"name\":\"Mystery\"},{\"id\":10749,\"name\":\"Romance\"},{\"id\":878,\"name\":\"Science Fiction\"},{\"id\":10770,\"name\":\"TV Movie\"},{\"id\":53,\"name\":\"Thriller\"},{\"id\":10752,\"name\":\"War\"},{\"id\":37,\"name\":\"Western\"}]}";

    @Override
    protected List<DetailMovie> doInBackground(String... strings) {
        int id = -1;
        String keywords = strings[0].trim().replace(" ", "+");
        String searchURL = null;
        String releaseYear = null;
        //if release date provided
        if (!strings[1].equals("-1")) {
            // release date format "yyyy-MM-dd"
            releaseYear = strings[1].split("-")[0];
            searchURL = API_SEARCH_BASE_URL + API_KEY + "&query=" + keywords + "&year=" + releaseYear;
        } else {
            searchURL = API_SEARCH_BASE_URL + API_KEY + "&query=" + keywords;
        }
        return resultProcess(searchURL);
    }

    private List<DetailMovie> resultProcess(String searchURL) {
        NetworkConnection nc = new NetworkConnection();
        // "" for no result, or one/multiple result(s)
        String searchResults = nc.doGETRequest(searchURL);
        Log.d(TAG, "resultProcess: TMDB result: " + searchResults);
        // if has result (JSONObject)
        List<DetailMovie> movies = new ArrayList<>();
        if (!searchResults.isEmpty()) {
            JSONArray results = null;
            try {
                results = new JSONObject(searchResults).getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    String movieName = result.getString("original_title");
                    String releaseDate = result.getString("release_date");
                    String posterURL = API_IMAGE_SEARCH_BASE_URL + result.getString("poster_path");
                    double publicRating = result.getDouble("vote_average");
                    JSONArray genreIds = result.getJSONArray("genre_ids");
                    String movieId = String.valueOf(result.getInt("id"));
                    List<String> genres = new ArrayList<>();
                    for (int j = 0; j < genreIds.length(); j++) {
                        genres.add(genresIdToString(genreIds.getInt(j)));
                    }
                    String overview = result.getString("overview");
                    DetailMovie detailMovie = new DetailMovie(movieName, releaseDate, posterURL, publicRating, genres, overview, movieId);
                    movies.add(detailMovie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            movies = null;
        }
        return movies;
    }

    private String genresIdToString(int id) {
        String name = "";
        JSONArray genres = null;
        try {
            genres = new JSONObject(GENRES_LIST).getJSONArray("genres");
            for (int i = 0; i < genres.length(); i++) {
                if (id == genres.getJSONObject(i).getInt("id")) {
                    name = genres.getJSONObject(i).getString("name");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
}

package com.monash.mymoviememoirx.network.task;

import android.os.AsyncTask;

import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.Crew;
import com.monash.mymoviememoirx.pojo.DetailMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * input a movie object
 */
public class AssignCrewsForMovie extends AsyncTask<DetailMovie, Void, Void> {

    private static final String API_KEY = "83e093c70c018f8c5977847b6a1c3e4d";
    private static final String API_IMAGE_SEARCH_BASE_URL = "https://api.themoviedb.org/3/movie/";

    @Override
    protected Void doInBackground(DetailMovie... detailMovies) {
        DetailMovie movie = detailMovies[0];
        String url = API_IMAGE_SEARCH_BASE_URL + movie.getMovieId() + "?api_key=" + API_KEY + "&append_to_response=credits";
        NetworkConnection nc = new NetworkConnection();
        String response = nc.doGETRequest(url);
        List<Crew> allCrews = null;
        if (response != null){
            try {
                allCrews = new ArrayList<>();
                JSONArray crews = new JSONObject(response).getJSONObject("credits").getJSONArray("crew");
                JSONArray casts = new JSONObject(response).getJSONObject("credits").getJSONArray("cast");
                //find director
                for (int i = 0; i < crews.length(); i ++){
                    Crew foundCrew = new Crew();
                    if (crews.getJSONObject(i).getString("job").equals("Director")){
                        foundCrew.setImageURL("https://image.tmdb.org/t/p/w500" + crews.getJSONObject(i).getString("profile_path"));
                        foundCrew.setCrewName(crews.getJSONObject(i).getString("name"));
                        foundCrew.setCrewCharacter("Director");
                        allCrews.add(foundCrew);
                    }
                }
                //find 5 casts
                for (int i = 0; i < 5; i ++){
                    Crew foundCrew = new Crew();
                    foundCrew.setCrewCharacter(casts.getJSONObject(i).getString("character"));
                    foundCrew.setCrewName(casts.getJSONObject(i).getString("name"));
                    foundCrew.setImageURL("https://image.tmdb.org/t/p/w500" + casts.getJSONObject(i).getString("profile_path"));
                    allCrews.add(foundCrew);
                }
                movie.setCrews(allCrews);

                JSONArray countries = new JSONObject(response).getJSONArray("production_countries");
                StringBuilder country = new StringBuilder();
                for (int i = 0; i < countries.length(); i++){
                    if (i != 0)
                        country.append("/");
                    country.append(countries.getJSONObject(i).getString("iso_3166_1"));
                }
                movie.setCountries(country.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}

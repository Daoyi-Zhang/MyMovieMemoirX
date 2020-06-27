package com.monash.mymoviememoirx.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.MainActivity;
import com.monash.mymoviememoirx.activity.MovieViewActivity;
import com.monash.mymoviememoirx.adapter.MovieRecyclerViewAdapter;
import com.monash.mymoviememoirx.network.task.AssignBitmapForMovies;
import com.monash.mymoviememoirx.network.task.FindMoviesByKeywordsAndReleaseDate;
import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.pojo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieSearchFragment extends Fragment implements MovieRecyclerViewAdapter.OnMovieListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "currentUser";

    private static final String TAG = "MovieSearchFragment";


    // TODO: Rename and change types of parameters
    private User currentUser;


    private List<DetailMovie> resultsMovies;
    private MovieRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public MovieSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user currentUser.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieSearchFragment newInstance(User user) {
        MovieSearchFragment fragment = new MovieSearchFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUser = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_movie_search, container, false);
        initRecyclerView(view);
        initSearchButton(view);
        return view;
    }

    private void initSearchButton(final View view){
        Button searchButton = view.findViewById(R.id.movie_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchEditText = view.findViewById(R.id.movie_search_edit_text);
                if (!searchEditText.getText().toString().isEmpty()) {
                    //TODO use TMDB AIP search movies
                    new FindMoviesByKeywordsAndReleaseDate(){
                        @Override
                        protected void onPostExecute(List<DetailMovie> detailMovies) {
                            resultsMovies = detailMovies;
                            //TODO assign image to each movie
                            new AssignBitmapForMovies(){
                                @Override
                                protected void onPostExecute(List<String> strings) {
                                    adapter.setMovies(resultsMovies);
                                }
                            }.execute(resultsMovies);
                        }
                    }.execute(searchEditText.getText().toString().trim(), "-1");

                } else {
                    Toast.makeText(getActivity(), "ERROR: Key word is empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initRecyclerView(View view) {
        if (resultsMovies == null)
            resultsMovies = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new MovieRecyclerViewAdapter(resultsMovies, this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //adapter.setMovies(resultsMovies);
    }

    @Override
    public void onMovieClicked(int position) {
        Log.d(TAG, "onMovieClicked: " + position);
        MainActivity main = (MainActivity) getActivity();
        DetailMovie movie = resultsMovies.get(position);
        Bundle bundle = new Bundle();
        DetailMovie pass = new DetailMovie();
        pass.setUserRating(movie.getUserRating());
        pass.setReleaseDate(movie.getReleaseDate());
        pass.setMovieName(movie.getMovieName());
        pass.setGenres(movie.getGenres());
        pass.setOverview(movie.getOverview());
        pass.setPosterURL(movie.getPosterURL());
        pass.setPublicRating(movie.getPublicRating());
        pass.setMovieId(movie.getMovieId());
        bundle.putParcelable("selected movie", pass);
        bundle.putParcelable("currentUser", currentUser);
        Intent intent = new Intent(main, MovieViewActivity.class);
        intent.putExtras(bundle);
        //main.startActivityForResult(intent, MainActivity.MOVIE_VIEW_CODE);
        main.startActivity(intent);
        //main.finish();
    }
}


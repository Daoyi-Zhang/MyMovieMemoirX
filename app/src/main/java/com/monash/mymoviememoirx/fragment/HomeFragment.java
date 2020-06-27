package com.monash.mymoviememoirx.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.MainActivity;
import com.monash.mymoviememoirx.activity.MovieViewActivity;
import com.monash.mymoviememoirx.adapter.MovieRecyclerViewAdapter;
import com.monash.mymoviememoirx.network.task.AssignBitmapForMovies;
import com.monash.mymoviememoirx.network.task.FindMoviesByKeywordsAndReleaseDate;
import com.monash.mymoviememoirx.network.task.FindTopFiveMoviesByUserId;
import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.pojo.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MovieRecyclerViewAdapter.OnMovieListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "currentUser";

    private static final String TAG = "HomeFragment";

    // TODO: Rename and change types of parameters
    private User currentUser;
    private List<DetailMovie> movies;
    private MovieRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public HomeFragment() {
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
    public static HomeFragment newInstance(User user) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findTopFive(view);
        return view;
    }

    private void findTopFive(final View view) {
        movies = new ArrayList<>();
        new FindTopFiveMoviesByUserId() {
            @Override
            protected void onPostExecute(List<DetailMovie> topFives) {
                //TODO find more detail for them
                if (topFives != null) {
                    for (final DetailMovie m :
                            topFives) {
                        new FindMoviesByKeywordsAndReleaseDate() {
                            @Override
                            protected void onPostExecute(List<DetailMovie> detailMovies) {
                                if (detailMovies != null) {
                                    DetailMovie detailMovie = detailMovies.get(0);
                                    detailMovie.setUserRating(m.getUserRating());
                                    movies.add(detailMovie);
                                    ArrayList<DetailMovie> dm = new ArrayList<>();
                                    dm.add(detailMovie);
                                    new AssignBitmapForMovies() {
                                        @Override
                                        protected void onPostExecute(List<String> strings) {
                                            initRecyclerView(view);
                                            initHomeText(view);
                                        }
                                    }.execute(dm);
                                }else {
                                    Toast.makeText(getActivity(), "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute(m.getMovieName(), m.getReleaseDate());
                    }
                }
                else {
                    Toast.makeText(getActivity(), "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(currentUser.getUserId().toString());
    }


    private void initHomeText(View view) {
        TextView homeTextView = view.findViewById(R.id.home_text_view);
        String homeText;
        if (adapter.getItemCount() != 0) {
            homeText = "Your Top " + adapter.getItemCount() + " in "
                    + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());

        } else {
            homeText = "You have no memoir for " + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        }
        homeTextView.setText(homeText);
    }

    private void initRecyclerView(View view) {
        if (movies == null) {
            movies = new ArrayList<>();
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new MovieRecyclerViewAdapter(movies, this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onMovieClicked(int position) {
        Log.d(TAG, "onMovieClicked: " + position);
        MainActivity main = (MainActivity) getActivity();
        DetailMovie movie = movies.get(position);
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

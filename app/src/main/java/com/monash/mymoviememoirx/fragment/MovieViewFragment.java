package com.monash.mymoviememoirx.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.MovieViewActivity;
import com.monash.mymoviememoirx.adapter.CrewRecyclerViewAdapter;
import com.monash.mymoviememoirx.network.task.AssignBitmapForCrews;
import com.monash.mymoviememoirx.network.task.AssignBitmapForMovies;
import com.monash.mymoviememoirx.network.task.AssignCrewsForMovie;
import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.pojo.User;
import com.monash.mymoviememoirx.util.WatchlistMovie;
import com.monash.mymoviememoirx.viewmodel.WatchListMovieViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "selectedMovie";
    private static final String ARG_PARAM2 = "currentUser";

    private static final String TAG = "MovieViewFragment";

    // TODO: Rename and change types of parameters
    private DetailMovie movie;
    private User currentUer;

    private CrewRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private WatchListMovieViewModel vm;

    public MovieViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 the movie selected
     * @return A new instance of fragment MovieViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieViewFragment newInstance(DetailMovie param1, User param2) {
        MovieViewFragment fragment = new MovieViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(ARG_PARAM1);
            currentUer = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view  = inflater.inflate(R.layout.fragment_movie_view, container, false);
        initShowView(view);

        //initAddToMemoir(view);
        return view;
    }

    private void initShowView(final View view){
        new AssignCrewsForMovie(){
            @Override
            protected void onPostExecute(Void aVoid) {
                new AssignBitmapForCrews(){
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        initRecyclerView(view);
                        initViewModel(view);
                        initAddToWatchList(view);
                        initDetails(view);
                        initAddToMemoir(view);
                    }
                }.execute(movie.getCrews());
            }
        }.execute(movie);
    }

    private void initAddToMemoir(View view){
        //TODO add to memoir
        Button add2Memoir = view.findViewById(R.id.movie_view_memoir);
        add2Memoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieViewActivity ac = (MovieViewActivity)getActivity();
                ac.replaceFragment(AddMemoirFragment.newInstance(movie, currentUer));
            }
        });

    }

    private void initDetails(final View view){
        ArrayList<DetailMovie> dm = new ArrayList<>();
        dm.add(movie);
        new AssignBitmapForMovies(){
            @Override
            protected void onPostExecute(List<String> strings) {
                ImageView movieImage = view.findViewById(R.id.movie_view_image);
                movieImage.setImageBitmap(movie.getBitmap());
            }
        }.execute(dm);

        TextView nameText = view.findViewById(R.id.movie_view_name);
        TextView movieDetail = view.findViewById(R.id.movie_view_detail);
        TextView movieRelease = view.findViewById(R.id.movie_view_release_date);
        ImageView star1 = view.findViewById(R.id.movie_view_stars_1);
        ImageView star2 = view.findViewById(R.id.movie_view_stars_2);
        ImageView star3 = view.findViewById(R.id.movie_view_stars_3);
        ImageView star4 = view.findViewById(R.id.movie_view_stars_4);
        ImageView star5 = view.findViewById(R.id.movie_view_stars_5);
        TextView movieSummary = view.findViewById(R.id.movie_view_summary);
        nameText.setText(movie.getMovieName());
        StringBuilder genreAndCountry = new StringBuilder();
        for(int i = 0; i < movie.getGenres().size(); i ++ ){
            genreAndCountry.append(movie.getGenres().get(i));
            genreAndCountry.append("/");
        }
        genreAndCountry.append("\n");
        genreAndCountry.append(movie.getCountries());
        movieDetail.setText(genreAndCountry);
        movieRelease.setText(movie.getReleaseDate());
        movieSummary.setText(movie.getOverview());

        int score = (int) (movie.getPublicRating() * 10);
        if (score <= 18 && score >= 10) {
            star1.setBackgroundResource(R.drawable.ic_star_half_black_24dp);
        }
        else if (score <= 27) {
            star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
        }
        else if (score <= 36) {
            star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star2.setBackgroundResource(R.drawable.ic_star_half_black_24dp);
        }
        else if (score <= 45) {
            star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
        }
        else if (score <= 54) {
            star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star3.setBackgroundResource(R.drawable.ic_star_half_black_24dp);
        }
        else if (score <= 63) {
            star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star3.setBackgroundResource(R.drawable.ic_star_black_24dp);
        }
        else if (score <= 72) {
            star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star3.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star4.setBackgroundResource(R.drawable.ic_star_half_black_24dp);
        }
        else if (score <= 81) {
            star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star3.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star4.setBackgroundResource(R.drawable.ic_star_black_24dp);
        }
        else if (score <= 90) {
            star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star3.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star4.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star5.setBackgroundResource(R.drawable.ic_star_half_black_24dp);
        }
        else if (score <= 99) {
            star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star3.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star4.setBackgroundResource(R.drawable.ic_star_black_24dp);
            star5.setBackgroundResource(R.drawable.ic_star_black_24dp);
        }
    }

    private void initAddToWatchList(View view){
        //TODO add to watch list
        Button add2Watchlist = view.findViewById(R.id.movie_view_watchlist);
        add2Watchlist.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorSilver));

        add2Watchlist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                movie.setUserId(currentUer.getUserId().toString());
                //movie.setAddDateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                movie.setAddDateTime("1995-11-11 12:12:12");
                vm.insert(new WatchlistMovie(movie));
            }
        });
    }

    private void initViewModel(final View view){
        vm = new ViewModelProvider(this).get(WatchListMovieViewModel.class);
        vm.initVars(getActivity().getApplication());
        vm.getAllMovies(currentUer.getUserId().toString()).observe(getActivity(), new Observer<List<WatchlistMovie>>(){
            @Override
            public void onChanged(List<WatchlistMovie> watchListMovies) {
                try {
                    WatchlistMovie foundMovie= vm.findByPK(
                            currentUer.getUserId().toString(),
                            movie.getMovieName(),
                            movie.getReleaseDate());
                    if(foundMovie == null){
                        Button button = view.findViewById(R.id.movie_view_watchlist);
                        button.setTextColor(ContextCompat.getColor(getActivity(),R.color.black));
                        button.setClickable(true);
                        Log.d(TAG, "onChanged: watch button set true");
                    }
                    else {
                        Button button = view.findViewById(R.id.movie_view_watchlist);
                        button.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorSilver));
                        button.setClickable(false);
                        Log.d(TAG, "onChanged: watch button set false");
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onChanged: Watchlist data changed");
                String t = "";
                for (WatchlistMovie m :
                        watchListMovies) {
                    t = t + m.getMovieName() + " " + m.getReleaseDate() + "\n";
                }
                Log.d(TAG, "onChanged: data: " + t);
            }
        });
    }

    private void initRecyclerView(View view){
        recyclerView = view.findViewById(R.id.movie_view_cast_recycler_view);
        adapter = new CrewRecyclerViewAdapter(movie.getCrews());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.HORIZONTAL));
        recyclerView.setAdapter(adapter);
    }
}

package com.monash.mymoviememoirx.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.MainActivity;
import com.monash.mymoviememoirx.adapter.WatchlistRecyclerViewAdapter;
import com.monash.mymoviememoirx.network.task.AssignBitmapForMovies;
import com.monash.mymoviememoirx.network.task.FindAllMemoirsByUerId;
import com.monash.mymoviememoirx.network.task.FindMoviesByKeywordsAndReleaseDate;
import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.pojo.User;
import com.monash.mymoviememoirx.util.DetailMemoir;
import com.monash.mymoviememoirx.util.WatchlistMovie;
import com.monash.mymoviememoirx.viewmodel.WatchListMovieViewModel;

import java.math.BigDecimal;
import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchlistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "currentUser";
    private static final String TAG = "WatchListFragment";
    // TODO: Rename and change types of parameters
    private User currentUser;
    private List<WatchlistMovie> movies;
    private WatchListMovieViewModel vm;
    private static HashMap<WatchlistMovie, DetailMovie> maps;

    private WatchlistRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment WatchlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchlistFragment newInstance(User param1) {
        WatchlistFragment fragment = new WatchlistFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
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
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        //init view model
        initViewModel();
        //init recycler view
        initRecyclerView(view);
        //initData(view);
        Log.d(TAG, "onCreateView: call");
        return view;
    }

    private void initViewModel() {
        vm = new ViewModelProvider(this).get(WatchListMovieViewModel.class);
        vm.initVars(getActivity().getApplication());
        LiveData<List<WatchlistMovie>> allMovies = vm.getAllMovies(currentUser.getUserId().toString());

        allMovies.observe(getActivity(), new Observer<List<WatchlistMovie>>() {
            @Override
            public void onChanged(List<WatchlistMovie> watchListMovies) {
                Log.d(TAG, "onChanged: Watchlist data changed");
                String t = "";
                for (WatchlistMovie m : watchListMovies) {
                    t = t + m.getMovieName() + " " + m.getReleaseDate() + "\n";
                }
                Log.d(TAG, "onChanged: data: \n" + t);
                movies = watchListMovies;
                adapter.setMovies(watchListMovies);
                //vm.deleteAll();
                initData();

            }
        });
    }

    public void initRecyclerView(View view) {
        movies = new ArrayList<>();
        recyclerView = view.findViewById(R.id.watch_list_recycler_view);
        adapter = new WatchlistRecyclerViewAdapter(movies);
        adapter.setVm(vm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //adapter.setMovies(resultsMovies);
    }


    private void initData() {
        maps = new HashMap<>();
        // TODO for each watchlist movie find its movie
        for (final WatchlistMovie m : movies) {
            new FindMoviesByKeywordsAndReleaseDate() {
                @Override
                protected void onPostExecute(List<DetailMovie> detailMovies) {
                    // TODO pick the first result and assign a bitmap for it
                    ArrayList<DetailMovie> dm = new ArrayList<>();
                    final DetailMovie detailMovie = detailMovies.get(0);
                    maps.put(m, detailMovie);
                    dm.add(detailMovie);
                    new AssignBitmapForMovies().execute(dm);
                }
            }.execute(m.getMovieName(), m.getReleaseDate());
        }
    }

    public static DetailMovie findTheMovie(WatchlistMovie watchlistMovie){
        return maps.get(watchlistMovie);
    }

}


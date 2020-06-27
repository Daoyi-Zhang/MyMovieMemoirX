package com.monash.mymoviememoirx.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.MainActivity;
import com.monash.mymoviememoirx.activity.MovieViewActivity;
import com.monash.mymoviememoirx.adapter.MemoirRecyclerViewAdapter;
import com.monash.mymoviememoirx.network.task.AssignBitmapForMovies;
import com.monash.mymoviememoirx.network.task.FindAllMemoirsByUerId;
import com.monash.mymoviememoirx.network.task.FindMoviesByKeywordsAndReleaseDate;
import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.pojo.Memoir;
import com.monash.mymoviememoirx.pojo.User;
import com.monash.mymoviememoirx.util.DetailMemoir;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoirFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoirFragment extends Fragment implements MemoirRecyclerViewAdapter.OnMemoirListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";

    private static final String TAG = "MemoirFragment";

    // TODO: Rename and change types of parameters
    private User user;
    private List<DetailMemoir> memoirs;
    private List<DetailMemoir> temp;
    private MemoirRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HashMap<DetailMemoir, DetailMovie> maps;

    public MemoirFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MemoirFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemoirFragment newInstance(User param1) {
        MemoirFragment fragment = new MemoirFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memoir, container, false);
        initData(view);
        return view;
    }

    @Override
    public void onMemoirClicked(int position) {
        Log.d(TAG, "onMovieClicked: " + position);
        MainActivity main = (MainActivity) getActivity();
        DetailMemoir selectedMemoir = memoirs.get(position);
        DetailMovie movie = maps.get(selectedMemoir);
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
        bundle.putParcelable("currentUser", user);
        Intent intent = new Intent(main, MovieViewActivity.class);
        intent.putExtras(bundle);
        //main.startActivityForResult(intent, MainActivity.MOVIE_VIEW_CODE);
        main.startActivity(intent);
        //main.finish();
    }

    @SuppressLint("StaticFieldLeak")
    private void initData(final View view) {
        //TODO Get all memoirs by user id
        new FindAllMemoirsByUerId() {
            @Override
            protected void onPostExecute(final List<DetailMemoir> detailMemoirs) {
                memoirs = detailMemoirs;
                maps = new HashMap<>();
                // TODO for each memoir find its movie
                for (final DetailMemoir m: memoirs) {
                    new FindMoviesByKeywordsAndReleaseDate(){
                        @Override
                        protected void onPostExecute(List<DetailMovie> detailMovies) {
                            // TODO pick the first result and assign a bitmap for it
                            ArrayList<DetailMovie> dm = new ArrayList<>();
                            final DetailMovie detailMovie = detailMovies.get(0);
                            maps.put(m, detailMovie);
                            dm.add(detailMovie);
                            new AssignBitmapForMovies(){
                                @Override
                                protected void onPostExecute(List<String> strings) {
                                    m.setBitmap(detailMovie.getBitmap());
                                    m.setGenres(detailMovie.getGenres());
                                    m.setImageURL(detailMovie.getPosterURL());
                                    m.setPublicRating(BigDecimal.valueOf(detailMovie.getPublicRating()));
                                    initRecyclerView(view);
                                }
                            }.execute(dm);
                        }
                    }.execute(m.getMovieName(), m.getReleaseDate());
                }
            }
        }.execute(user.getUserId());
    }

    private void initRecyclerView(View view){
        recyclerView = getView().findViewById(R.id.memoir2_recycler);
        adapter = new MemoirRecyclerViewAdapter(memoirs, this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        temp = new ArrayList<>(memoirs);
        initGenres(view);
        initApply(view);
    }

    public void initGenres(View view) {
        HashSet<String> genres = new HashSet<>();
        for (DetailMemoir d : memoirs) {
            genres.addAll(d.getGenres());
        }
        ArrayList<String> temp = new ArrayList<>(genres);
        temp.add(0, "None");
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, temp);
        Spinner spinner = view.findViewById(R.id.filter);
        spinner.setAdapter(spinnerAdapter);
    }

    private void initApply(final View view) {
        Button apply = view.findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Spinner sort = view.findViewById(R.id.sort);
                Spinner filter = view.findViewById(R.id.filter);
                int sortI = sort.getSelectedItemPosition();
                String filterC = filter.getSelectedItem().toString();
                temp = new ArrayList<>(memoirs);
                if (!filterC.equals("None")) {
                    temp = filterBy(temp, filterC);
                }
                if (sortI != 0) {
                    sortBy(temp, sortI);
                }
                adapter.setMemoirs(temp);
            }
        });
    }

    private ArrayList<DetailMemoir> filterBy(List<DetailMemoir> list, String name) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getGenres().size(); j++) {
                if (list.get(i).getGenres().contains(name)) {
                    indexes.add(i);
                    break;
                }
            }
        }
        ArrayList<DetailMemoir> temp2 = new ArrayList<>();
        for (int i = 0; i < indexes.size(); i++) {
            temp2.add(list.get(indexes.get(i)));
        }
        return temp2;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortBy(List<DetailMemoir> list, final int index) {
        list.sort(new Comparator<DetailMemoir>() {
            @Override
            public int compare(DetailMemoir o1, DetailMemoir o2) {
                int result = 0;
                if (index == 1) {
                    SimpleDateFormat sp = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
                    Date d1 = null;
                    Date d2 = null;
                    try {
                        d1 = sp.parse(o1.getWatchTime());
                        d2 = sp.parse(o2.getWatchTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (d1.getTime() < d2.getTime()) {
                        result = 1;
                    } else if (d1.getTime() > d2.getTime()) {
                        result = -1;
                    } else {
                        result = 0;
                    }
                } else if (index == 2) {
                    if (o1.getRatingScore() < o2.getRatingScore()) {
                        result = 1;
                    } else if (o1.getRatingScore() > o2.getRatingScore()) {
                        result = -1;
                    } else {
                        result = 0;
                    }
                } else if (index == 3) {
                    if (o1.getPublicRating().doubleValue() < o2.getPublicRating().doubleValue()) {
                        result = 1;
                    } else if (o1.getPublicRating().doubleValue() > o2.getPublicRating().doubleValue()) {
                        result = -1;
                    } else {
                        result = 0;
                    }
                }
                return result;
            }
        });
    }
}

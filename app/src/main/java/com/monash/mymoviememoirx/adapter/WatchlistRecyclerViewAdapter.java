package com.monash.mymoviememoirx.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.MainActivity;
import com.monash.mymoviememoirx.activity.MovieViewActivity;
import com.monash.mymoviememoirx.fragment.WatchlistFragment;
import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.pojo.User;
import com.monash.mymoviememoirx.util.WatchlistMovie;
import com.monash.mymoviememoirx.viewmodel.WatchListMovieViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WatchlistRecyclerViewAdapter extends RecyclerView.Adapter<WatchlistRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "WatchlistAdapter";
    private List<WatchlistMovie> movies;
    private WatchListMovieViewModel vm;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService executor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public void setMovies(List<WatchlistMovie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void setVm(WatchListMovieViewModel vm) {
        this.vm = vm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View moviesView = inflater.inflate(R.layout.recycler_view_watch_list_item, parent, false);
        final WatchlistRecyclerViewAdapter.ViewHolder viewHolder = new WatchlistRecyclerViewAdapter.ViewHolder(moviesView, context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final WatchlistMovie movie = movies.get(position);

        ImageView movieImage = holder.movieImage;
        TextView movieName = holder.movieName;
        TextView movieReleaseYear = holder.movieReleaseYear;
        TextView addDate = holder.addDate;
        Button confirm = holder.confirm;
        Button cancel = holder.cancel;
        final Dialog dialog = holder.dialog;
        movieImage.setImageBitmap(movie.getBitmap());
        movieName.setText(movie.getMovieName());
        movieReleaseYear.setText(movie.getReleaseDate());
        addDate.setText(movie.getAddDate());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + position);
                vm.delete(movies.get(position));
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + position);
                dialog.dismiss();
            }
        });


        //start movie view activity
        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onMovieClicked: " + position);
                DetailMovie movie = WatchlistFragment.findTheMovie(movies.get(position));
                User currentUser = MainActivity.findTheUser();
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
                Intent intent = new Intent(holder.viewButton.getContext(), MovieViewActivity.class);
                intent.putExtras(bundle);
                holder.viewButton.getContext().startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vm.delete(movies.get(position));
                Log.d(TAG, "onClick: " + position);
                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView movieImage;
        TextView movieName;
        TextView movieReleaseYear;
        TextView addDate;
        Button viewButton;
        Button deleteButton;
        Dialog dialog;
        Button confirm;
        Button cancel;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.watchlist_movie_image);
            movieName = itemView.findViewById(R.id.watchlist_movie_name);
            movieReleaseYear = itemView.findViewById(R.id.watchlist_movie_release_year);
            addDate = itemView.findViewById(R.id.watchlist_add_time);
            viewButton = itemView.findViewById(R.id.watchlist_view_button);
            deleteButton = itemView.findViewById(R.id.watchlist_delete_button);
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.delete_alter);
            confirm = dialog.findViewById(R.id.confirm_button);
            cancel = dialog.findViewById(R.id.cancel_button);
        }
    }

    public WatchlistRecyclerViewAdapter(List<WatchlistMovie> movies) {
        this.movies = movies;
    }
}

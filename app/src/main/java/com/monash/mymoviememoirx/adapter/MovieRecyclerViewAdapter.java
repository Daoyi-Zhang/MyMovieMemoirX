package com.monash.mymoviememoirx.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.pojo.DetailMovie;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private List<DetailMovie> movies;
    private OnMovieListener mOnMovieListener;

    public MovieRecyclerViewAdapter(List<DetailMovie> movies, OnMovieListener onMovieListener) {
        this.movies = movies;
        this.mOnMovieListener = onMovieListener;
    }

    public void addMovie(DetailMovie movie) {
        this.movies.add(movie);
        notifyDataSetChanged();
    }

    public void setMovies(List<DetailMovie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView movieImage;
        TextView movieName;
        TextView movieReleaseYear;
        TextView movieScore;
        OnMovieListener onMovieListener;

        public ViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movie_image);
            movieName = itemView.findViewById(R.id.movie_name);
            movieReleaseYear = itemView.findViewById(R.id.movie_release_year);
            movieScore = itemView.findViewById(R.id.movie_score);
            this.onMovieListener = onMovieListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieListener.onMovieClicked(getAdapterPosition());
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View moviesView = inflater.inflate(R.layout.recycler_view_movie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(moviesView, mOnMovieListener);
        return viewHolder;
    }

    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DetailMovie movie = movies.get(position);
        // view holder binding with its data at the specified position
        final ImageView movieImage = holder.movieImage;
        TextView movieName = holder.movieName;
        TextView movieReleaseYear = holder.movieReleaseYear;
        TextView movieScore = holder.movieScore;
        movieName.setText(movie.getMovieName());
        String releaseYear = "Release Date: " + movie.getReleaseDate();
        movieReleaseYear.setText(releaseYear);
        String score;
        if (movie.getUserRating() != 0){
            score = "Rating Score: " + movie.getUserRating();
        }
        else {
            score = "Rating Score: " + movie.getPublicRating();
        }
        movieScore.setText(score);
        movieImage.setImageBitmap(movie.getBitmap());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnMovieListener{
        void onMovieClicked(int position);
    }
}
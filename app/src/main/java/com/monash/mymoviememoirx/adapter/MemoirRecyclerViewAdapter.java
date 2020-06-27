package com.monash.mymoviememoirx.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.util.DetailMemoir;

import java.util.List;

public class MemoirRecyclerViewAdapter extends RecyclerView.Adapter<MemoirRecyclerViewAdapter.ViewHolder>{

    public static final String TAG = "MemoirAdapter";
    private List<DetailMemoir> memoirs;
    private OnMemoirListener onMemoirListener;

    public MemoirRecyclerViewAdapter(List<DetailMemoir> memoirs, OnMemoirListener onMemoirListener) {
        this.memoirs = memoirs;
        this.onMemoirListener = onMemoirListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView movieImage;
        TextView movieName;
        TextView movieYear;
        RatingBar selfRating;
        RatingBar publicRating;
        TextView cinema;
        TextView watchTime;
        TextView comment;
        OnMemoirListener onMemoirListener;

        public ViewHolder(@NonNull View itemView, OnMemoirListener onMemoirListener) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.memoir2_movie_image);
            movieName = itemView.findViewById(R.id.memoir2_movie_name);
            movieYear = itemView.findViewById(R.id.memoir2_movie_release_year);
            selfRating = itemView.findViewById(R.id.memoir2_score_self);
            publicRating = itemView.findViewById(R.id.memoir2_score_public);
            cinema = itemView.findViewById(R.id.memoir2_cinema);
            watchTime = itemView.findViewById(R.id.memoir2_watch_time);
            comment = itemView.findViewById(R.id.memoir2_comment);
            this.onMemoirListener = onMemoirListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMemoirListener.onMemoirClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View moviesView = inflater.inflate(R.layout.recycler_view_memoir_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(moviesView, onMemoirListener);
        return viewHolder;
    }

    public float score2Stars(int score){
        float result = 0;
        if (score <= 18 && score >= 10) {
            result = (float) 0.5;
        }
        else if (score <= 27) {
            result = (float) 1.0;
        }
        else if (score <= 36) {
            result = (float) 1.5;
        }
        else if (score <= 45) {
            result = (float) 2.0;
        }
        else if (score <= 54) {
            result = (float) 2.5;
        }
        else if (score <= 63) {
            result = (float) 3.0;
        }
        else if (score <= 72) {
            result = (float) 3.5;
        }
        else if (score <= 81) {
            result = (float) 4.0;
        }
        else if (score <= 90) {
            result = (float) 4.5;
        }
        else if (score <= 99) {
            result = (float) 5.0;
        }
        return result;
    }
    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DetailMemoir memoir = memoirs.get(position);
        final ImageView movieImage = holder.movieImage;
        TextView movieName = holder.movieName;
        TextView movieYear = holder.movieYear;
        RatingBar selfRating = holder.selfRating;
        RatingBar publicRating = holder.publicRating;
        TextView cinema = holder.cinema;
        TextView watchTime = holder.watchTime;
        TextView comment = holder.comment;

        //TODO others
        movieImage.setImageBitmap(memoir.getBitmap());
        movieName.setText(memoir.getMovieName());
        movieYear.setText(memoir.getReleaseDate());
        selfRating.setRating((float) memoir.getRatingScore());
        selfRating.setIsIndicator(true);
        publicRating.setRating(memoir.getPublicRating().floatValue());
        publicRating.setIsIndicator(true);
        String np = memoir.getCinemaId().getCinemaName() + " " + memoir.getCinemaId().getLocationPostcode();
        cinema.setText(np);
        watchTime.setText(memoir.getWatchTime());
        comment.setText(memoir.getComment());
    }

    @Override
    public int getItemCount() {
        return memoirs.size();
    }

    public void setMemoirs(List<DetailMemoir> memoirs) {
        this.memoirs = memoirs;
        notifyDataSetChanged();
    }

    public interface OnMemoirListener{
        void onMemoirClicked(int position);
    }
}

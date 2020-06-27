package com.monash.mymoviememoirx.util;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.monash.mymoviememoirx.pojo.DetailMovie;

@Entity(tableName = "watch_list_movie", primaryKeys = {"user_id", "movie_name", "release_date"})
public class WatchlistMovie{
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;
    @NonNull
    @ColumnInfo(name = "movie_name")
    private String movieName;
    @NonNull
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @ColumnInfo(name = "add_date")
    private String addDate;

    @TypeConverters(BitmapConverter.class)
    @ColumnInfo(name = "bitmap")
    private Bitmap bitmap;

    public WatchlistMovie(DetailMovie detailMovie) {
        this.userId = detailMovie.getUserId();
        this.movieName = detailMovie.getMovieName();
        this.releaseDate = detailMovie.getReleaseDate();
        this.addDate = detailMovie.getAddDateTime();
        this.bitmap = detailMovie.getBitmap();
    }

    public WatchlistMovie(@NonNull String userId, @NonNull String movieName, @NonNull String releaseDate, String addDate) {
        this.userId = userId;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.addDate = addDate;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
}

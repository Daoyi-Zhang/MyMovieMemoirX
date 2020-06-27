package com.monash.mymoviememoirx.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.util.WatchlistMovie;

import java.util.List;

@Dao
public interface WatchListMovieDAO {
    @Query("SELECT * FROM watch_list_movie WHERE user_id = :userId")
    LiveData<List<WatchlistMovie>> getAll(String userId);

    @Query("SELECT * FROM watch_list_movie WHERE user_id = :userId AND movie_name = :movieName AND release_date = :releaseDate LIMIT 1")
    WatchlistMovie findByPK(String userId, String movieName, String releaseDate);

    @Insert
    long insert(WatchlistMovie movie);

    @Delete
    void delete(WatchlistMovie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWatchListMovies(WatchlistMovie... movies);

    @Query("DELETE FROM watch_list_movie WHERE user_id = :userId")
    void deleteAll(String userId);
}

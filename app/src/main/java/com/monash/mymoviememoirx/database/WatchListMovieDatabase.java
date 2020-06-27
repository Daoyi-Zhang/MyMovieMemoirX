package com.monash.mymoviememoirx.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.monash.mymoviememoirx.dao.WatchListMovieDAO;
import com.monash.mymoviememoirx.util.WatchlistMovie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {WatchlistMovie.class}, version = 5, exportSchema = false)
public abstract class WatchListMovieDatabase extends RoomDatabase {

    private static WatchListMovieDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public abstract WatchListMovieDAO watchListMovieDAO();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized WatchListMovieDatabase getInstance(final Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WatchListMovieDatabase.class, "WatchListMovieDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}

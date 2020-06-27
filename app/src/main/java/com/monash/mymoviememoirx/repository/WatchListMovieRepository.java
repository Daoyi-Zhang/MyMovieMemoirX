package com.monash.mymoviememoirx.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.monash.mymoviememoirx.dao.WatchListMovieDAO;
import com.monash.mymoviememoirx.database.WatchListMovieDatabase;
import com.monash.mymoviememoirx.util.WatchlistMovie;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class WatchListMovieRepository {
    private WatchListMovieDAO watchListMovieDAO;
    private LiveData<List<WatchlistMovie>> allMovies;
    private WatchlistMovie watchListMovie;

    public WatchListMovieRepository(Application application) {
        this.watchListMovieDAO = WatchListMovieDatabase.getInstance(application).watchListMovieDAO();
    }

    public LiveData<List<WatchlistMovie>> getAllMovies(String userId){
        this.allMovies = this.watchListMovieDAO.getAll(userId);
        return allMovies;
    }

    public WatchlistMovie findByPK(final String userId, final String movieName, final String releaseDate) throws ExecutionException, InterruptedException {
        Future future = WatchListMovieDatabase.databaseWriteExecutor.submit(new Callable<WatchlistMovie>() {
            @Override
            public WatchlistMovie call() throws Exception {
                return watchListMovieDAO.findByPK(userId, movieName, releaseDate);
            }
        });
        return (WatchlistMovie) future.get();
    }

    public void insert(final WatchlistMovie movie){
        WatchListMovieDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {
                watchListMovieDAO.insert(movie);
            }
        });
    }

    public void delete(final WatchlistMovie movie){
        WatchListMovieDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {
                watchListMovieDAO.delete(movie);
            }
        });
    }

    public void updateWatchListMovies(final WatchlistMovie... movies){
        WatchListMovieDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {
                watchListMovieDAO.updateWatchListMovies(movies);
            }
        });
    }

    public void deleteAll(final String userId){
        WatchListMovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                watchListMovieDAO.deleteAll(userId);
            }
        });
    }

    public void setWatchListMovie(WatchlistMovie watchListMovie) {
        this.watchListMovie = watchListMovie;
    }
}

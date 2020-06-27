package com.monash.mymoviememoirx.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.monash.mymoviememoirx.repository.WatchListMovieRepository;
import com.monash.mymoviememoirx.util.WatchlistMovie;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WatchListMovieViewModel extends ViewModel {
    private WatchListMovieRepository watchListMovieRepository;
    private MutableLiveData<List<WatchlistMovie>> allMovies;

    public WatchListMovieViewModel() {
        allMovies = new MutableLiveData<>();
    }

    public void initVars(Application application){
        watchListMovieRepository = new WatchListMovieRepository(application);
    }

    public void setAllMovies(List<WatchlistMovie> movies){
        allMovies.setValue(movies);
    }

    public LiveData<List<WatchlistMovie>> getAllMovies(String userId){
        return watchListMovieRepository.getAllMovies(userId);
    }

    public WatchlistMovie findByPK(String userId, String movieName, String releaseDate) throws ExecutionException, InterruptedException {
        return watchListMovieRepository.findByPK(userId, movieName, releaseDate);
    }

    public void insert(WatchlistMovie movie){
        watchListMovieRepository.insert(movie);
    }

    public void delete(WatchlistMovie movie){
        watchListMovieRepository.delete(movie);
    }

    public void updateWatchListMovies(WatchlistMovie... movies){
        watchListMovieRepository.updateWatchListMovies(movies);
    }

    public void deleteAll(String userId){
        watchListMovieRepository.deleteAll(userId);
    }
}

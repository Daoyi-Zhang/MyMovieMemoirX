package com.monash.mymoviememoirx.pojo;

import java.math.BigDecimal;

public class Memoir {
    private String movieName;

    private String releaseDate;

    private String watchTime;

    private String comment;

    private double ratingScore;

    private Integer memoirId;

    private Cinema cinemaId;

    private User userId;

    public Memoir() {
    }

    public Memoir(String movieName, String releaseDate, String watchTime, String comment, double ratingScore, Integer memoirId, Cinema cinemaId, User userId) {
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.watchTime = watchTime;
        this.comment = comment;
        this.ratingScore = ratingScore;
        this.memoirId = memoirId;
        this.cinemaId = cinemaId;
        this.userId = userId;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getWatchTime() {
        return watchTime;
    }

    public String getComment() {
        return comment;
    }

    public double getRatingScore() {
        return ratingScore;
    }

    public Integer getMemoirId() {
        return memoirId;
    }

    public Cinema getCinemaId() {
        return cinemaId;
    }

    public User getUserId() {
        return userId;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setWatchTime(String watchTime) {
        this.watchTime = watchTime;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRatingScore(double ratingScore) {
        this.ratingScore = ratingScore;
    }

    public void setMemoirId(Integer memoirId) {
        this.memoirId = memoirId;
    }

    public void setCinemaId(Cinema cinemaId) {
        this.cinemaId = cinemaId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}

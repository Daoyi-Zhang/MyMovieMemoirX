package com.monash.mymoviememoirx.pojo;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;


public class DetailMovie implements Parcelable {
    private String movieName;
    private String releaseDate;
    private String posterURL;
    private Bitmap bitmap;
    // 0 ~ 10 (step 0.1)
    private double publicRating;
    // 0 ~ 5 (step 0.5)
    private double userRating;
    private List<String> genres;
    private List<Crew> crews;
    private String overview;
    private String addDateTime;
    private String userId;
    private String movieId;
    private String countries;

    public DetailMovie() {
    }

    public DetailMovie(String movieName, String releaseDate, String posterURL, double publicRating, List<String> genres, String overview, String movieId) {
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.posterURL = posterURL;
        this.publicRating = publicRating;
        this.genres = genres;
        this.overview = overview;
        this.movieId = movieId;
    }

    protected DetailMovie(Parcel in) {
        movieName = in.readString();
        releaseDate = in.readString();
        posterURL = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        publicRating = in.readDouble();
        userRating = in.readDouble();
        genres = in.createStringArrayList();
        overview = in.readString();
        addDateTime = in.readString();
        userId = in.readString();
        movieId = in.readString();
        countries = in.readString();
    }

    public static final Creator<DetailMovie> CREATOR = new Creator<DetailMovie>() {
        @Override
        public DetailMovie createFromParcel(Parcel in) {
            return new DetailMovie(in);
        }

        @Override
        public DetailMovie[] newArray(int size) {
            return new DetailMovie[size];
        }
    };

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setPublicRating(double publicRating) {
        this.publicRating = publicRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setCrews(List<Crew> crews) {
        this.crews = crews;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setAddDateTime(String addDateTime) {
        this.addDateTime = addDateTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public double getPublicRating() {
        return publicRating;
    }

    public double getUserRating() {
        return userRating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<Crew> getCrews() {
        return crews;
    }

    public String getOverview() {
        return overview;
    }

    public String getAddDateTime() {
        return addDateTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getCountries() {
        return countries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieName);
        dest.writeString(releaseDate);
        dest.writeString(posterURL);
        dest.writeParcelable(bitmap, flags);
        dest.writeDouble(publicRating);
        dest.writeDouble(userRating);
        dest.writeStringList(genres);
        dest.writeString(overview);
        dest.writeString(addDateTime);
        dest.writeString(userId);
        dest.writeString(movieId);
        dest.writeString(countries);
    }
}

package com.monash.mymoviememoirx.util;

import android.graphics.Bitmap;
import com.monash.mymoviememoirx.pojo.Memoir;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DetailMemoir extends Memoir {

    private List<String> genres;
    private BigDecimal publicRating;
    private String ImageURL;
    private Bitmap bitmap;

    public DetailMemoir() {
        this.genres = new ArrayList<>();
        this.publicRating = new BigDecimal(0);
        this.setImageURL("");
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public List<String> getGenres() {
        return genres;
    }

    public BigDecimal getPublicRating() {
        return publicRating;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setPublicRating(BigDecimal publicRating) {
        this.publicRating = publicRating;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

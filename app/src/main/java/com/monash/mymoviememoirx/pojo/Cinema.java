package com.monash.mymoviememoirx.pojo;

public class Cinema {
    private Integer cinemaId;
    private String cinemaName;
    private String locationPostcode;

    public Cinema(String cinemaName, String locationPostcode) {
        this.cinemaName = cinemaName;
        this.locationPostcode = locationPostcode;
    }

    public Cinema(Integer cinemaId, String cinemaName, String locationPostcode) {
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.locationPostcode = locationPostcode;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getLocationPostcode() {
        return locationPostcode;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public void setLocationPostcode(String locationPostcode) {
        this.locationPostcode = locationPostcode;
    }

    public Integer getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    @Override
    public String toString() {
        return cinemaName + " " + locationPostcode;
    }
}

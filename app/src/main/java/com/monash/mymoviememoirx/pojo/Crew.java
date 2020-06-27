package com.monash.mymoviememoirx.pojo;

import android.graphics.Bitmap;

public class Crew {

    private String crewName;
    private String CrewCharacter;
    private String imageURL;
    private Bitmap crewImage;

    public Crew(){

    }

    public Crew(String crewName, String crewCharacter, String imageURL, Bitmap crewImage) {
        this.crewName = crewName;
        CrewCharacter = crewCharacter;
        this.imageURL = imageURL;
        this.crewImage = crewImage;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getCrewName() {
        return crewName;
    }

    public String getCrewCharacter() {
        return CrewCharacter;
    }

    public Bitmap getCrewImage() {
        return crewImage;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }

    public void setCrewCharacter(String crewCharacter) {
        CrewCharacter = crewCharacter;
    }

    public void setCrewImage(Bitmap crewImage) {
        this.crewImage = crewImage;
    }
}

package com.monash.mymoviememoirx.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String name;
    private String surname;
    private String gender;
    private String dob;
    private String address;
    private String state;
    private String postcode;
    private Integer userId;


    public User() {
    }

    public User(String name, String surname, String gender, String dob, String address, String state, String postcode, Integer userId) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.state = state;
        this.postcode = postcode;
        this.userId = userId;
    }


    protected User(Parcel in) {
        name = in.readString();
        surname = in.readString();
        gender = in.readString();
        dob = in.readString();
        address = in.readString();
        state = in.readString();
        postcode = in.readString();
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getPostcode() {
        return postcode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(gender);
        dest.writeString(dob);
        dest.writeString(address);
        dest.writeString(state);
        dest.writeString(postcode);
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
    }
}

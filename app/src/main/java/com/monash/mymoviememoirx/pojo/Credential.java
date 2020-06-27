package com.monash.mymoviememoirx.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Credential implements Parcelable {

    private String username;
    private String password;
    private String signUpDate;
    private User userId;
    private Integer credentialId;

    public Credential(String username, String password, String signUpDate, User userId, Integer credentialId) {
        this.username = username;
        this.password = password;
        this.signUpDate = signUpDate;
        this.userId = userId;
        this.credentialId = credentialId;
    }

    protected Credential(Parcel in) {
        username = in.readString();
        password = in.readString();
        signUpDate = in.readString();
        if (in.readByte() == 0) {
            credentialId = null;
        } else {
            credentialId = in.readInt();
        }
    }

    public static final Creator<Credential> CREATOR = new Creator<Credential>() {
        @Override
        public Credential createFromParcel(Parcel in) {
            return new Credential(in);
        }

        @Override
        public Credential[] newArray(int size) {
            return new Credential[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public User getUserId() {
        return userId;
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(signUpDate);
        if (credentialId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(credentialId);
        }
    }
}

package com.example.yan.yanweather.model;

import java.io.Serializable;

/**
 * Created by Yan on 10/7/2015.
 */
public class Location implements Serializable{
    private String mCountry;
    private String mCity;
    private String mState;

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public void setState(String state){
        this.mState = state;
    }

    public String getState(){
        return mState;
    }
}

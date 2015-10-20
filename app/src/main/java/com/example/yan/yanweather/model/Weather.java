package com.example.yan.yanweather.model;

/**
 * Created by Yan on 10/7/2015.
 */
public class Weather {
    public Location mLoc;
    public Temperature mTemperature = new Temperature();
    public String mWeatherIconURL;


//    public byte[] mIcon;

    public  class Temperature {
        private float mTemp;

        public float getTemp() {
            return mTemp;
        }
        public void setTemp(float temp) {
            this.mTemp = temp;
        }
    }

    public String[] mDates;
    public String[] mIconURL;
    public String[] mDescrip;
    public float[] mHTempC;
    public float[] mLTempC;
    public float[] mHTempF;
    public float[] mLTempF;
    public float[] mHTemp;
    public float[] mLTemp;
}
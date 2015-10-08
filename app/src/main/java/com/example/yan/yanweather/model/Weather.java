package com.example.yan.yanweather.model;

/**
 * Created by Yan on 10/7/2015.
 */
public class Weather {
    public Location mLocation;
    public Temperature mTemperature = new Temperature();

    public  class Temperature {
        private float mTemp;

        public float getTemp() {
            return mTemp;
        }
        public void setTemp(float temp) {
            this.mTemp = temp;
        }


    }


}
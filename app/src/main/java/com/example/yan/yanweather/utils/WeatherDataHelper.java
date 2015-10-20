package com.example.yan.yanweather.utils;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yan.yanweather.model.Weather;

import java.util.Arrays;

/**
 * Created by Yan on 10/16/2015.
 */
public class WeatherDataHelper {
    public TextView mCity;
    public TextView mTemp;
    public ImageView mWeatherIcon;
    public ProgressBar mProgressBar;
    public Activity mContext;
    public TextView mNotice;
    public ListView mListWeather;
    public int mNumOfDays;
    public String mUnit;
    public Weather mWeather;
    public Weather mListWeatherData = new Weather();
    public Weather getRequestedWeather(){
        mListWeatherData.mDates = Arrays.copyOfRange(mWeather.mDates, 0,mNumOfDays);
        mListWeatherData.mIconURL = Arrays.copyOfRange(mWeather.mIconURL, 0,mNumOfDays);
        mListWeatherData.mDescrip = Arrays.copyOfRange(mWeather.mDescrip, 0,mNumOfDays);
        mListWeatherData.mHTempC = Arrays.copyOfRange(mWeather.mHTempC, 0,mNumOfDays);
        mListWeatherData.mLTempC = Arrays.copyOfRange(mWeather.mHTempC, 0,mNumOfDays);
        mListWeatherData.mHTempF = Arrays.copyOfRange(mWeather.mHTempF, 0,mNumOfDays);
        mListWeatherData.mLTempF = Arrays.copyOfRange(mWeather.mHTempF, 0,mNumOfDays);

        if("Fahrenheit".equals(mUnit)){
            mListWeatherData.mHTemp = Arrays.copyOfRange(mWeather.mHTempF, 0,mNumOfDays);
            mListWeatherData.mLTemp = Arrays.copyOfRange(mWeather.mHTempF, 0,mNumOfDays);
        }
        else {
            mListWeatherData.mHTemp = Arrays.copyOfRange(mWeather.mHTempC, 0,mNumOfDays);
            mListWeatherData.mLTemp = Arrays.copyOfRange(mWeather.mHTempC, 0,mNumOfDays);
        }
        return mListWeatherData;
    }
}

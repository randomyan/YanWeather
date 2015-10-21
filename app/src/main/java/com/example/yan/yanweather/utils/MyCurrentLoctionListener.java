package com.example.yan.yanweather.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.yan.yanweather.model.Weather;

import org.json.JSONException;

/**
 * Created by Yan on 10/16/2015.
 */
public class MyCurrentLoctionListener implements LocationListener {
    private boolean mIsDetectingLocation = false;
    private final int TIMEOUT_IN_MS = 10000; //10 second timeout
//    private YanWeather mYanWeather;
    public JSONWeatherPull mJSONWeatherPull = null;
    private static final String TAG = "LocationFinder";
    private LocationManager mLocationManager;
    private WeatherDataHelper mWeatherDataHelper;
    private String[] location;
    public boolean invalidZipCode = false;
    private boolean defaultSearch = true;
    private boolean defaultSearchSucceed = false;
    private boolean newSearch = false;
    private boolean newSearchSucceed = false;
    private boolean oneSearchCancelled = false;

    public MyCurrentLoctionListener(WeatherDataHelper y){
        mWeatherDataHelper= y;
    }

    public void initiateJsonPull(){
        mJSONWeatherPull = new JSONWeatherPull();
    }
    @Override
    public void onLocationChanged(Location location) {
        defaultSearch = false;
        newSearch = true;
        oneSearchCancelled = true;
        location.getLatitude();
        location.getLongitude();
//        String myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();
        if(location!=null){
            if(mJSONWeatherPull!=null)
                mJSONWeatherPull.cancel(true);
            mJSONWeatherPull = new JSONWeatherPull();
            String latLong = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()) + ".json";
            mJSONWeatherPull.execute(new String[]{latLong});
        }

        else{
            mWeatherDataHelper.mNotice.setText("Can't find weather info for this location");
        }
 //       Toast.makeText(mWeatherDataHelper.mContext, myLocation, Toast.LENGTH_LONG).show();
        //I make a log to see the results
        //   Log.e("MY CURRENT LOCATION", myLocation);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class JSONWeatherPull extends AsyncTask<String,Integer,Weather> {
        @Override
        protected void onPreExecute() {
            mWeatherDataHelper.mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
        @Override
        protected Weather doInBackground(String... params){
            Weather weather= new Weather();
            HttpClient testHttp = new HttpClient();
            //    String data = ((new HttpClient()).getWeatherData(params[0]));
            //   String qur = testHttp.userQuery("autoip.json");
            String qur = testHttp.userQuery(params[0]);
            //    String qur = testHttp.userQuery(params[0]);

            if(qur!=null) {
                try {
                    location= JSONParser.getUserQuery(qur);
                } catch (JSONException e) {
                    e.printStackTrace();
                    invalidZipCode = true;
                    return null;
                }
                if(location[0]!=null) {
                    String data = testHttp.getWeatherData(location[0]);
                    //     String data = testHttp.getWeatherData(params[0]);
                    try {
                        weather = JSONParser.getWeather(data);
//                weather.mIcon = ((new HttpClient()).getImage(weather.mWeatherIconURL));
                    } catch (JSONException e) {
                        e.printStackTrace();
  //                      weather = null;
                    }
                }
            }
            else{

                    this.cancel(true);
                weather = null;
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather){
            mWeatherDataHelper.mProgressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(weather);
            if(defaultSearch) defaultSearchSucceed = true;
            if(newSearch) newSearchSucceed = true;
            if(invalidZipCode ==true){
               mWeatherDataHelper.mNotice.setText("Invalid zip code");
                return;
            }
          /*  if(weather.mIcon!=null &&weather.mIcon.length>0){
                Bitmap img = BitmapFactory.decodeByteArray(weather.mIcon, 0, weather.mIcon.length);
                mWeatherIcon.setImageBitmap(img);
            }
            */
            if(weather!=null){
                mWeatherDataHelper.mWeather =weather;
                mWeatherDataHelper.mCity.setText(location[1]);
                mWeatherDataHelper.mForeCast.setText("In "+mWeatherDataHelper.mNumOfDays + " days");
                WeatherListAdapter adapter = new WeatherListAdapter(mWeatherDataHelper.mContext,mWeatherDataHelper.getRequestedWeather(),mWeatherDataHelper.mNumOfDays,mWeatherDataHelper.mUnit);
                mWeatherDataHelper.mListWeather.setAdapter(adapter);
 //               Toast.makeText(mWeatherDataHelper.mContext, "Sorry, server shutdown", Toast.LENGTH_SHORT).show();
            }
            else{
                mWeatherDataHelper.mNotice.setText("Sorry, server shutdown");
            }
        }

        @Override
        protected void onCancelled(Weather weather) {
            mWeatherDataHelper.mProgressBar.setVisibility(View.INVISIBLE);
            if((defaultSearch &&!defaultSearchSucceed &&!newSearch)||(!newSearchSucceed)&&newSearch&&!oneSearchCancelled){
                mWeatherDataHelper.mNotice.setText("Error, no internet or server shutdown");
                mWeatherDataHelper.mConnected = false;
            }

 //           invalidZipCode = true;
        }
    }

    public void detectLocation(){
        if(mIsDetectingLocation == false){
            mIsDetectingLocation = true;

            if(mLocationManager == null){
                mLocationManager = (LocationManager)mWeatherDataHelper.mContext.getSystemService(Context.LOCATION_SERVICE);
            }


            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M|| ContextCompat.checkSelfPermission(mWeatherDataHelper.mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mWeatherDataHelper.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                    mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,this,null);
                startTimer();
            }
            else{
                endLocationDetection();
            }
/*
            if(mCurrentLocation!=null){
                String latLong = Double.toString(mCurrentLocation.getLatitude()) +"," +Double.toString(mCurrentLocation.getLongitude())+".json";
                pull.execute(new String[]{latLong});
            }
            else{
                pull.execute(new String[]{"autoip.json"});
            }
*/
        }
        else{
            Log.d(TAG, "already trying to detect location");
            mWeatherDataHelper.mNotice.setText("already trying to detect location");
        }
    }

    private void endLocationDetection(){
        if(mIsDetectingLocation) {
            mIsDetectingLocation = false;
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M|| ContextCompat.checkSelfPermission(mWeatherDataHelper.mContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mWeatherDataHelper.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mLocationManager.removeUpdates(this);
            }
        }
        Log.d(TAG, "no permission");
        mWeatherDataHelper.mNotice.setText("no permission");
    }

    private void startTimer(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsDetectingLocation) {
                    fallbackOnLastKnownLocation();
                }
            }
        }, TIMEOUT_IN_MS);

    }

    private void fallbackOnLastKnownLocation(){
        Location lastKnownLocation = null;
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M||ContextCompat.checkSelfPermission(mWeatherDataHelper.mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mWeatherDataHelper.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if(lastKnownLocation != null){
            String temp = "lat: "+Double.toString(lastKnownLocation.getLatitude()) +" lon:" + Double.toString(lastKnownLocation.getLongitude());
//            Toast.makeText(mWeatherDataHelper.mContext, temp, Toast.LENGTH_LONG).show();
            String latLong = Double.toString(lastKnownLocation.getLatitude()) +"," +Double.toString(lastKnownLocation.getLongitude())+".json";
            if(mJSONWeatherPull!=null) mJSONWeatherPull.cancel(true);
            mJSONWeatherPull = new JSONWeatherPull();
            mJSONWeatherPull.execute(new String[]{latLong});
        }
/*
        else{
            Log.d(TAG, "Time Out, no location found");
            Toast.makeText(getBaseContext(), "Time Out, no location found", Toast.LENGTH_LONG).show();
        }
        */
    }

}

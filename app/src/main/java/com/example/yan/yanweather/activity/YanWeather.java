package com.example.yan.yanweather.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.yan.yanweather.R;
import com.example.yan.yanweather.utils.MyCurrentLoctionListener;
import com.example.yan.yanweather.utils.WeatherDataHelper;

public class YanWeather extends AppCompatActivity {
    private WeatherDataHelper mWeatherDataHelper = new WeatherDataHelper();
 //   private TextView mCity;
//    private TextView mTemp;
 //   private ImageView mWeatherIcon;
//    private LocationManager mLocationManager;
//    private boolean mIsDetectingLocation = false;
//    private final int TIMEOUT_IN_MS = 10000; //10 second timeout
//    private static final String TAG = "LocationFinder";

//    private ProgressBar mProgressBar;
    private SearchView searchView = null;
    MyCurrentLoctionListener mLocationListener;
//    JSONWeatherPull pull = null;

 //   private Context mContext = this;

/*
    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public Context getContext() {
        return mContext;
    }


    public TextView getCity() {
        return mCity;
    }

    public TextView getTemp() {
        return mTemp;
    }

    public ImageView getWeatherIcon() {
        return mWeatherIcon;
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yan_weather);
        mWeatherDataHelper.mContext = this;
        /*
        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            */
        mWeatherDataHelper.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWeatherDataHelper.mCity = (TextView) findViewById(R.id.city_field);
        mWeatherDataHelper.mTemp = (TextView)findViewById(R.id.current_temperature_field);
        mWeatherDataHelper.mNotice = (TextView) findViewById(R.id.notice);
        mWeatherDataHelper.mWeatherIcon = (ImageView)findViewById(R.id.weather_icon);
        mLocationListener = new MyCurrentLoctionListener(mWeatherDataHelper);
        mLocationListener.initiateJsonPull();
        mLocationListener.mJSONWeatherPull.execute("autoip.json");

        mLocationListener.detectLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_yan_weather, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSubmitButtonEnabled(true);
        searchView.setIconified(true);
//        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("zip code");
        ComponentName cn = new ComponentName(this, SearchWeather.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(cn));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.search) {
  //          doMySearch("test");
        }
        return super.onOptionsItemSelected(item);
    }
/*
    public void doMySearch(String query){
        Log.e("test", query);

    }
*/
    @Override
    protected void onRestart() {
        super.onRestart();
        if(Build.VERSION.SDK_INT >= 11)
            invalidateOptionsMenu();
        if( searchView!=null){
            searchView.setQuery("", false);
            searchView.clearFocus();
        }

    }

/*
    public class MyCurrentLoctionListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            location.getLatitude();
            location.getLongitude();
            String myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();
            if(location!=null){
                if(pull!=null)
                pull.cancel(true);
                pull = new JSONWeatherPull();
                String latLong = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()) + ".json";
                pull.execute(new String[]{latLong});
            }

            else{
                myLocation ="Can't find weather info for this location";
            }
            Toast.makeText(getBaseContext(), myLocation, Toast.LENGTH_LONG).show();
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
    }
    private class JSONWeatherPull extends AsyncTask<String,Integer,Weather> {
        @Override
        protected void onPreExecute() {
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.VISIBLE);
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
            String qur = testHttp.userQuery(params[0]);
            if(qur!=null) {
                try {
                    qur = JSONParser.getUserQuery(qur);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(qur!=null) {
                    String data = testHttp.getWeatherData(qur);
                    //     String data = testHttp.getWeatherData(params[0]);
                    try {
                        weather = JSONParser.getWeather(data);
//                weather.mIcon = ((new HttpClient()).getImage(weather.mWeatherIconURL));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        weather = null;
                    }
                }
            }
            else{
                weather = null;
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather){
            mProgressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(weather);
            if(weather!=null){
                Picasso.with(YanWeather.this)
                        .load(weather.mWeatherIconURL)
                        .into(mWeatherIcon);
                mCity.setText(weather.mLocation.getCity() + ", " + weather.mLocation.getState() + ", " + weather.mLocation.getCountry());
                mTemp.setText("" + weather.mTemperature.getTemp() + " C");
            }
            else{
                Toast.makeText(getBaseContext(), "Sorry, server shutdown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void detectLocation(){
        if(mIsDetectingLocation == false){
            mIsDetectingLocation = true;

            if(mLocationManager == null){
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            }


            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                    mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,mLocationListener,null);
                startTimer();
            }
            else{
                endLocationDetection();
            }
        }
        else{
            Log.d(TAG, "already trying to detect location");
            Toast.makeText(this,"already trying",Toast.LENGTH_SHORT).show();
        }
    }

    private void endLocationDetection(){
        if(mIsDetectingLocation) {
            mIsDetectingLocation = false;
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M|| ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mLocationManager.removeUpdates(mLocationListener);
            }
        }
        Log.d(TAG, "no permission");
        Toast.makeText(getBaseContext(), "no permission", Toast.LENGTH_SHORT).show();
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
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if(lastKnownLocation != null){
            String temp = "lat: "+Double.toString(lastKnownLocation.getLatitude()) +" lon:" + Double.toString(lastKnownLocation.getLongitude());
            Toast.makeText(getBaseContext(), temp, Toast.LENGTH_LONG).show();
            String latLong = Double.toString(lastKnownLocation.getLatitude()) +"," +Double.toString(lastKnownLocation.getLongitude())+".json";
                if(pull!=null) pull.cancel(true);
                pull = new JSONWeatherPull();
            pull.execute(new String[]{latLong});
        }
    }
*/
}
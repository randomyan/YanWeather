package com.example.yan.yanweather.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yan.yanweather.R;
import com.example.yan.yanweather.model.Weather;
import com.example.yan.yanweather.utils.HttpClient;
import com.example.yan.yanweather.utils.JSONParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class YanWeather extends AppCompatActivity {
    private TextView mCity;
    private TextView mTemp;
    private ImageView mWeatherIcon;
    private LocationManager mLocationManager;


    public class MyCurrentLoctionListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            location.getLatitude();
            location.getLongitude();

            String myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();
            Toast.makeText(getBaseContext(), myLocation, Toast.LENGTH_LONG).show();

            //I make a log to see the results
            Log.e("MY CURRENT LOCATION", myLocation );

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
    MyCurrentLoctionListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yan_weather);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mLocationListener = new MyCurrentLoctionListener();
        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

        //   mLocationListener.onLocationChanged( mLocation);
/*
        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            */

        String city = "WA/Seattle.json";//CA/San_Francisco.json
        mCity = (TextView) findViewById(R.id.city_field);
        mTemp = (TextView)findViewById(R.id.current_temperature_field);
        mWeatherIcon = (ImageView)findViewById(R.id.weather_icon);
        JSONWeatherPull pull = new JSONWeatherPull();
        // pull.execute(new String[]{city});
        String qur = "WA/Seattle.json";
        pull.execute(new String[]{city, qur});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_yan_weather, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconified(true);
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
            doMySearch("test");
        }
        return super.onOptionsItemSelected(item);
    }

    public void doMySearch(String query){
        Log.e("test", query);

    }

    private class JSONWeatherPull extends AsyncTask<String,Void,Weather> {
        //TODO: add progressDialog while user waiting
        @Override
        protected Weather doInBackground(String... params){
            Weather weather = new Weather();
            HttpClient testHttp = new HttpClient();
            //    String data = ((new HttpClient()).getWeatherData(params[0]));
            String qur = testHttp.userQuery("autoip.json");
            //    String qur = testHttp.userQuery(params[0]);
            try{
                qur = JSONParser.getUserQuery(qur);
            }catch (JSONException e){
                e.printStackTrace();
            }
            String data = testHttp.getWeatherData(qur);
            //     String data = testHttp.getWeatherData(params[0]);
            try{
                weather = JSONParser.getWeather(data);
//                weather.mIcon = ((new HttpClient()).getImage(weather.mWeatherIconURL));
            }catch (JSONException e){
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather){
            super.onPostExecute(weather);
            Picasso.with(YanWeather.this)
                    .load(weather.mWeatherIconURL)
                    .into(mWeatherIcon);

          /*  if(weather.mIcon!=null &&weather.mIcon.length>0){
                Bitmap img = BitmapFactory.decodeByteArray(weather.mIcon, 0, weather.mIcon.length);
                mWeatherIcon.setImageBitmap(img);
            }
            */
            mCity.setText(weather.mLocation.getCity() + ", " + weather.mLocation.getState() + ", " + weather.mLocation.getCountry());
            mTemp.setText("" + weather.mTemperature.getTemp() + " C");
        }
    }
}
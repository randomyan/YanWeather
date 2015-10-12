package com.example.yan.yanweather;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yan_weather);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, mLocationListener);
        String city = "DC/Washington.json";
        mCity = (TextView) findViewById(R.id.city_field);
        mTemp = (TextView)findViewById(R.id.current_temperature_field);
        mWeatherIcon = (ImageView)findViewById(R.id.weather_icon);
        JSONWeatherPull pull = new JSONWeatherPull();
        pull.execute(new String[]{city});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yan_weather, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private class JSONWeatherPull extends AsyncTask<String,Void,Weather>{
        @Override
        protected Weather doInBackground(String... params){
            Weather weather = new Weather();
            String data = ((new HttpClient()).getWeatherData(params[0]));
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

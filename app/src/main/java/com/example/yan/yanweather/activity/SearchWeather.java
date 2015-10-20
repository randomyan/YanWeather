package com.example.yan.yanweather.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yan.yanweather.R;
import com.example.yan.yanweather.utils.MyCurrentLoctionListener;
import com.example.yan.yanweather.utils.WeatherDataHelper;

/**
 * Created by Yan on 10/14/2015.
 */
public class SearchWeather extends AppCompatActivity{
    private WeatherDataHelper mWeatherDataHelper= new WeatherDataHelper();
    MyCurrentLoctionListener mLocationListener;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yan_weather);
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_yan_weather, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconified(true);
        searchView.setQuery("", false);
        //      searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("zip code");
        ComponentName cn = new ComponentName(this, SearchWeather.class);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(cn));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.search) {
            //          doMySearch("test");
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if(searchView!=null) searchView.setQuery("", false);
 //           Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
            mWeatherDataHelper.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            mWeatherDataHelper.mCity = (TextView) findViewById(R.id.city_field);
            mWeatherDataHelper.mForeCast = (TextView)findViewById(R.id.current_temperature_field);
            mWeatherDataHelper.mNotice = (TextView) findViewById(R.id.notice);
            mLocationListener = new MyCurrentLoctionListener(mWeatherDataHelper);
            mLocationListener.initiateJsonPull();
            mLocationListener.mJSONWeatherPull.execute(query + ".json");
            if(mLocationListener.invalidZipCode==true) {
                mWeatherDataHelper.mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Invalid zip code",Toast.LENGTH_LONG).show();
            }

            //use the query to search
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(Build.VERSION.SDK_INT >= 11)
            invalidateOptionsMenu();


    }
}

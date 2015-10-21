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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.yan.yanweather.R;
import com.example.yan.yanweather.utils.MyCurrentLoctionListener;
import com.example.yan.yanweather.utils.PreferenceSetting;
import com.example.yan.yanweather.utils.WeatherDataHelper;
import com.example.yan.yanweather.utils.WeatherListAdapter;

public class YanWeather extends AppCompatActivity {
    private WeatherDataHelper mWeatherDataHelper = new WeatherDataHelper();
    private SearchView searchView = null;
    private PreferenceSetting mPreferenceSetting;
   private  MyCurrentLoctionListener mLocationListener;
    private static final String KEY_LAST_QUERY = "last_query";
    private static String mLastQuery;
    private ImageButton mRefreshButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yan_weather);
        mPreferenceSetting = new PreferenceSetting();
        String test = mPreferenceSetting.getValue(this,mPreferenceSetting.PREFS_DAYS);
        if(test==null){
            mPreferenceSetting.defautSetting(this);
            mWeatherDataHelper.mNumOfDays = 3;
            mWeatherDataHelper.mUnit = "Fahrenheit";
        }
        else{
            try{mWeatherDataHelper.mNumOfDays = Integer.parseInt(test);}
            catch (NumberFormatException e){
                mPreferenceSetting.clearSetting(this);
                mPreferenceSetting.defautSetting(this);
                mWeatherDataHelper.mNumOfDays = 3;
                mWeatherDataHelper.mUnit = "Fahrenheit";
            }
            mWeatherDataHelper.mUnit = mPreferenceSetting.getValue(this, mPreferenceSetting.PREFS_UNIT);
            if("Celsius".equals(mPreferenceSetting.getValue(this, mPreferenceSetting.PREFS_UNIT))){
                mWeatherDataHelper.mUnit = "Fahrenheit";
            }
            else {
                mWeatherDataHelper.mUnit = "Celsius";
            }

        }

        mWeatherDataHelper.mContext = this;
        mWeatherDataHelper.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWeatherDataHelper.mCity = (TextView) findViewById(R.id.city_field);
        mWeatherDataHelper.mForeCast = (TextView)findViewById(R.id.current_temperature_field);
        mWeatherDataHelper.mNotice = (TextView) findViewById(R.id.notice);
        mWeatherDataHelper.mListWeather =(ListView) findViewById(R.id.listview);

        mLocationListener = new MyCurrentLoctionListener(mWeatherDataHelper);
        mLocationListener.initiateJsonPull();
        if (savedInstanceState != null && mLastQuery!=null){
            mLastQuery = savedInstanceState.getString(KEY_LAST_QUERY);
            mLocationListener.mJSONWeatherPull.execute(mLastQuery);
        }
        else {
            mLocationListener.mJSONWeatherPull.execute("autoip.json");
            mLocationListener.detectLocation();
        }
        handleIntent(getIntent());

        mRefreshButton = (ImageButton) findViewById(R.id.refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationListener.invalidZipCode = false;
                mLocationListener.initiateJsonPull();
                mLocationListener.mJSONWeatherPull.execute("autoip.json");
                mLocationListener.detectLocation();
                mLastQuery = null;
                mWeatherDataHelper.mNotice.setText("");
            }
        });

        mWeatherDataHelper.mNotice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mWeatherDataHelper.mNotice.setText("");
            }
        });

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
        ComponentName cn = new ComponentName(this, YanWeather.class);
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
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.search) {
  //          doMySearch("test");
        }
        return super.onOptionsItemSelected(item);
    }

    //onRestart is called when going back from another activity
    @Override
    protected void onRestart() {
        super.onRestart();
        if(Build.VERSION.SDK_INT >= 11)
            invalidateOptionsMenu();
        if( searchView!=null){
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
        mWeatherDataHelper.mNumOfDays = Integer.parseInt(mPreferenceSetting.getValue(this, mPreferenceSetting.PREFS_DAYS));
        if("Celsius".equals(mPreferenceSetting.getValue(this,mPreferenceSetting.PREFS_UNIT))) {
            mWeatherDataHelper.mUnit = "Fahrenheit";
        }
        else  {
            mWeatherDataHelper.mUnit = "Celsius";
        }
        mWeatherDataHelper.mForeCast.setText("In " + mWeatherDataHelper.mNumOfDays + " days");
        if(mWeatherDataHelper.mConnected) {
            WeatherListAdapter adapter = new WeatherListAdapter(mWeatherDataHelper.mContext, mWeatherDataHelper.getRequestedWeather(), mWeatherDataHelper.mNumOfDays, mWeatherDataHelper.mUnit);
            mWeatherDataHelper.mListWeather.setAdapter(adapter);
        }
    }

    @Override
    protected  void onResume(){
        super.onResume();
    }

    @Override
    protected  void onStop(){
        super.onStop();
//        mWeatherDataHelper.mNotice.setText("TExt");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(KEY_LAST_QUERY, mLastQuery);
    }

        private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mLastQuery = query + ".json";
            if(searchView!=null) searchView.setQuery("", false);
            mLocationListener = new MyCurrentLoctionListener(mWeatherDataHelper);
            mLocationListener.initiateJsonPull();
            mLocationListener.mJSONWeatherPull.execute(mLastQuery);
            //use the query to search
        }
    }

}
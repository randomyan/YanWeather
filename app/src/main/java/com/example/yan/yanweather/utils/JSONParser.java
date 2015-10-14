package com.example.yan.yanweather.utils;

import com.example.yan.yanweather.model.Location;
import com.example.yan.yanweather.model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yan on 10/6/2015.
 */
public class JSONParser {
    public static Weather getWeather(String data) throws JSONException{
        Weather weather = new Weather();
        Location loc = new Location();
        JSONObject jObj= getObject("current_observation",new JSONObject(data));

        JSONObject jLocationObj = getObject("display_location",jObj);
        loc.setCity(getString("city", jLocationObj));
        loc.setCountry(getString("country", jLocationObj));
        loc.setState(getString("state", jLocationObj));
        weather.mLocation = loc;
        weather.mTemperature.setTemp(getFloat("temp_c",jObj));
        weather.mWeatherIconURL= getString("icon_url",jObj);
        //TODO: add weather description, such as "sunny etc"
        return weather;
    }

    public static String getUserQuery(String data) throws JSONException{
       String city;
        JSONObject jObj= getObject("location",new JSONObject(data));
        city = getString("requesturl", jObj);
        city = city.replace("US/","");
        city = city.replace(".html",".json");
        return city;
    }

    private static float getFloat(String tagName, JSONObject jObj)throws JSONException{
        return (float)jObj.getDouble(tagName);
    }


    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException{
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }
    private static String getString(String tagName, JSONObject jObj) throws JSONException{
        return jObj.getString(tagName);
    }
}

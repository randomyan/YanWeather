package com.example.yan.yanweather.utils;

import com.example.yan.yanweather.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yan on 10/6/2015.
 */
public class JSONParser {
    public static Weather getWeather(String data) throws JSONException{
        Weather weather = new Weather();
        JSONObject jObj= getObject("simpleforecast",getObject("forecast", new JSONObject(data)));
        JSONArray array = (JSONArray) jObj.get("forecastday");
        int n = array.length();
        weather.mDates = new String[n];
        weather.mIconURL = new String[n];
        weather.mDescrip = new String[n];
        weather.mHTempF = new float[n];
        weather.mHTempC = new float[n];
        weather.mLTempC = new float[n];
        weather.mLTempF =  new float[n];
        weather.mLTemp =  new float[n];
        weather.mHTempC = new float[n];
        for (int i = 0; i < array.length();i++){
            JSONObject ith = array.getJSONObject(i);
            JSONObject j = getObject("date",ith);
            weather.mDates[i] = getString("weekday",j);
            weather.mIconURL[i] = getString("icon_url",ith);
            weather.mDescrip[i]= getString("conditions", ith);
            JSONObject h = getObject("high",ith);
            weather.mHTempF[i] = getFloat("fahrenheit", h);
            weather.mHTempC[i] = getFloat("celsius", h);
            JSONObject l = getObject("low",ith);
            weather.mLTempF[i] = getFloat("fahrenheit",l);
            weather.mLTempC[i] = getFloat("celsius",l);
        }
        return weather;
    }

    public static String[] getUserQuery(String data) throws JSONException{
       String city;
        String[] location = new String[2];
        JSONObject jObj= getObject("location", new JSONObject(data));
        String temp = getString("country", jObj);
        if(!"US".equals(temp)) {
            if("CI".equals(temp)) temp = "CN"; //== tests for reference equality (whether they are the same object).
            city = temp+"/"+getString("city", jObj).replace(" ","_")+".json";
        }
        else{
            city = getString("requesturl", jObj);
            city = city.replace("US/","");
            city = city.replace(".html",".json");
        }
        location[0] = city;
        location[1] = getString("city", jObj)+", "+getString("state", jObj)+", "+getString("country_name", jObj);
        return location;
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

package com.example.yan.yanweather.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yan on 10/7/2015.
 */
public class HttpClient {
    private static String BASE_URL = "http://api.wunderground.com/api/839414bb65aedbbc/conditions/q/";

    public String userQuery(String loc){
        HttpURLConnection connection = null;
        InputStream is = null;

        try {
            connection = (HttpURLConnection) (new URL("http://api.wunderground.com/api/839414bb65aedbbc/geolookup/q/" + loc)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            StringBuffer buffer = new StringBuffer();
            is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");

            is.close();
            connection.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }
        return  null;
    }

    public String getWeatherData(String location) {
        HttpURLConnection connection = null;
        InputStream is = null;

        try {
            connection = (HttpURLConnection) (new URL(BASE_URL + location)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            StringBuffer buffer = new StringBuffer();
            is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");

            is.close();
            connection.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }
        return  null;
    }

    public byte[] getImage(String str){
        HttpURLConnection connection = null;
        InputStream is = null;
        try{
            connection=(HttpURLConnection)(new URL(str)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            is = connection.getInputStream();
            byte[] buffer = new byte[2096];
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            while (is.read(buffer)!=-1) bo.write(buffer);
            return bo.toByteArray();

        }catch (Throwable t){
            t.printStackTrace();
        }

        finally {
            try { is.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }
        return null;
    }
}

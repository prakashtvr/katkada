package com.katkada.Other;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
/**
 * Created by prakash on 15-12-2016.
 */
public class MyLocationListner implements LocationListener {
    LocationManager location_manager;
    String getLatitude = "12.939968";
    String getLongitude = "80.207030";
    @Override
    public void onLocationChanged(Location location) {
        getLatitude = "" + location.getLatitude();
        getLongitude = "" + location.getLongitude();
        // lati.setText(getLatitude);
        //longi.setText(getLongitude);
        Log.d("getLatitude", "onLocationChanged: " + getLatitude);
        Log.d("getLongitude", "onLocationChanged: " + getLongitude);
        //        x = location.getLatitude();
        //        y = location.getLongitude();
        //getLocationInfo();
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
    public JSONObject getLocationInfo() {
        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng=" + getLatitude + "," + getLongitude + "&sensor=true");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

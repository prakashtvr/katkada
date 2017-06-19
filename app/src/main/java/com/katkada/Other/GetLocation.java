package com.katkada.Other;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by prakash on 17-12-2016.
 */
public class GetLocation extends AsyncTask {
    public static String location_string = "press get Address";
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Object doInBackground(Object[] args) {
        String UserId = String.valueOf(args[0]);
        String googleurl = "http://maps.google.com/maps/api/geocode/json?latlng=" + String.valueOf(args[1]) + "," + String.valueOf(args[2]) + "&sensor=true";
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("sensor", UserId));
        JSONObject json = jsonParser.makeHttpRequest(googleurl, "POST", params);
        return json;
    }
    @Override
    protected void onPostExecute(Object o) {
        if (o != null) {
            JSONObject ret = (JSONObject) o;
            JSONObject location;
            try {
                location = ret.getJSONArray("results").getJSONObject(0);
                location_string = location.getString("formatted_address");
                Log.d("test", "formattted address:" + location_string);
                // Toast.makeText(getBaseContext(), "" + location_string, Toast.LENGTH_SHORT).show();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}

package com.katkada.Other;
import android.os.AsyncTask;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by prakash on 02-12-2016.
 */
public class LoadRegionCode extends AsyncTask {
    JSONParser jsonParser = new JSONParser();
    public static String RegionNumber, RegionKey;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected JSONObject doInBackground(Object[] args) {
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("connection_type", String.valueOf(args[0])));
        JSONObject json = jsonParser.makeHttpRequest(Config.GET_REGION_KEY_URL, "GET", params);
        return json;
    }
    @Override
    protected void onPostExecute(Object o) {
        JSONObject jresponse = (JSONObject) o;
        JSONArray jsonArray = null;
        String resultString = null;
        if (o != null) {
            JSONObject jobject = null;
            try {
                jobject = jresponse.getJSONObject("result");
                RegionKey = String.valueOf(jobject);
                ////  Toast.makeText((),"Coupon \n "+jobject.getString("amt"),Toast.LENGTH_LONG).show();
                // Toast.makeText(getContext(),"Coupon Value\n "+jresponse.getString("result"),Toast.LENGTH_LONG).show();
                ////   Toast.makeText(LoadRegionCode.this,"Coupon \n "+jobject.getString("msg"),Toast.LENGTH_LONG).show();
                if (jresponse.getString("error").equals("1")) {//Toast.makeText(getBaseContext(),"normal: "+String.valueOf(valid),Toast.LENGTH_LONG).show();
                    return;
                } else if (jresponse.getString("success").equals("1")) {
                    try {
                        jobject = new JSONObject(String.valueOf(o));
                        //  Log.d("jobject", "onPostExecute: "+jobject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        super.onPostExecute(o);
    }
}

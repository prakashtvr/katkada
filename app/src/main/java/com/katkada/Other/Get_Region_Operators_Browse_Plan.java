package com.katkada.Other;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by prakash on 29-05-2017.
 */
public class Get_Region_Operators_Browse_Plan extends AsyncTask<String, Void, String> {

    ProgressDialog progressDialogRegion;
    OkHttpClient client;
    MediaType JSON;
    public static String[] spinnerArray_Oprators;
    public static String[] spinnerArray_Oprators_Postpaid;
    public static String[] spinnerArray_Region;
    public static HashMap<String, String> spinnerMap_Opearators = new HashMap<String, String>();
    public static HashMap<String, String> spinnerMap_Opearators_Postpaid = new HashMap<String, String>();
    private Exception exception;
    public final static HashMap<String, String> spinnerMap_Region = new HashMap<String, String>();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");
//            progressDialogRegion = new ProgressDialog(PrepaidMobilePacks.this,
//                    R.style.AppTheme_Dark_Dialog);
//            progressDialogRegion.setIndeterminate(true);
//            progressDialogRegion.setMessage("Please wait...");
//            progressDialogRegion.show();
    }

    protected String doInBackground(String... urls) {

        try {
            String getResponse = get(Config.GET_OPERATOR_REGION_BROWSE_PLAN);
            return getResponse;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(String getResponse) {
        System.out.println(getResponse);
        try {
            if(getResponse!=null) {
                JSONObject jsonObject = new JSONObject(String.valueOf(getResponse));
                JSONObject jsonRegion,jsonOperators;
                JSONArray jsonArray_operators= new JSONArray();
                JSONArray jsonArray_operators_postpaid= new JSONArray();
                JSONArray jsonArray_region= new JSONArray();
                if(jsonObject.getInt("success")==1)
                {
                    if(jsonObject.has("data"))
                    {
                        // jsonObject.getJSONObject("data").getJSONObject("operators");
                        jsonArray_operators=   jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("prepaid_operator");
                        jsonArray_operators_postpaid=   jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("postpaid_operator");
                        //jsonArray
                        JSONObject jobj;

                        jsonArray_region= jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("location_list");


                        spinnerArray_Oprators = new String[jsonArray_operators.length()];
                        for (int i = 0; i < jsonArray_operators.length(); i++) {
                            jobj = jsonArray_operators.getJSONObject(i);
                            spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
                            //   spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
                            spinnerArray_Oprators[i] = jobj.getString("value");
                            Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);
                            Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                        }
                        spinnerArray_Oprators_Postpaid = new String[jsonArray_operators_postpaid.length()];
                        for (int i = 0; i < jsonArray_operators_postpaid.length(); i++) {
                            jobj = jsonArray_operators_postpaid.getJSONObject(i);
                            spinnerMap_Opearators_Postpaid.put(jobj.getString("value"), jobj.getString("key"));
                            //   spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
                            spinnerArray_Oprators_Postpaid[i] = jobj.getString("value");
                            Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators_Postpaid[i]);
                            Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                        }


                        spinnerArray_Region = new String[jsonArray_region.length()];
                        for (int i = 0; i < jsonArray_region.length(); i++) {
                            jobj = jsonArray_region.getJSONObject(i);
                            spinnerMap_Region.put(jobj.getString("value"), jobj.getString("key"));
                            //   spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
                            spinnerArray_Region[i] = jobj.getString("value");
                            Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Region[i]);
                            Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                        }



          Log.v("jsonArray_operators",""+jsonArray_operators.length());


                    }

                }
        }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}



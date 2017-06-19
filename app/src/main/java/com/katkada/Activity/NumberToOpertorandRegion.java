package com.katkada.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.katkada.Fragment.HomeScreenFragment;
import com.katkada.Other.Config;
import com.katkada.Other.JSONParser;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by prakash on 09-01-2017.
 */
public class NumberToOpertorandRegion extends AsyncTask {
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Object doInBackground(Object[] args) {
        String UserId = String.valueOf(args[0]);
        String Optid = "1";
        String Rgnid = "1";
        String ConType = "prepaid";
        String TotalMins = "100";
        String TotalSTD_Loc_Calls = "0";
        String TotalData = "1000";
        String TotalSMS = "100";
        String ConnectionType = "3G";
//            String Optid=manager.getPreferences(PlanDetailsActivity.this,"Shared_OptName");
//            String Rgnid="5";
//            String ConType=manager.getPreferences(PlanDetailsActivity.this,"Shared_ConnectionType");
//            String TotMinutes=manager.getPreferences(PlanDetailsActivity.this,"Shared_TotalMinutes");
//            String TotalData=manager.getPreferences(PlanDetailsActivity.this,"Shared_TotalData");
//            String TotalSMS=manager.getPreferences(PlanDetailsActivity.this,"Shared_TotalSMS");
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("securehash", "7a4a870320e6dc86847fdc356416e063943d430edf18f1f34fbf77d5e31e500a177c6c24b6ed4556f43fef788ec7669ae16ce3f59040287e53de3f3a23c43280"));
        params.add(new BasicNameValuePair("txnid", "abc"));
        params.add(new BasicNameValuePair("api_user_id", "EPj3bYVYYsA5NdLNjYCEnQCxMpL7gxV2YYRWxeebW4A3WWcr"));
        params.add(new BasicNameValuePair("type", ConType));
        params.add(new BasicNameValuePair("search_type", "mobile"));
        //  params.add(new BasicNameValuePair("datatotalusage", TotalData));
        //   params.add(new BasicNameValuePair("totalsms", TotalSMS));
        JSONObject json = jsonParser.makeHttpRequest(Config.NUMBER_TO_OPT_RGN_URL + "9095474568", "POST", params);
        return json;
    }
    @Override
    protected void onPostExecute(Object o) {
        JSONObject jresponse = null;
        JSONObject jsonObject = null;
        String resultString = null;
        JSONArray jsonArray;
        if (o == null) {
            Log.v("error", "error");
            //  Toast.makeText(getBaseContext(),"Unable to get Value",Toast.LENGTH_SHORT).show();
        } else {
            try {
                jresponse = new JSONObject(String.valueOf(o));
                Log.v("error", "error" + jresponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
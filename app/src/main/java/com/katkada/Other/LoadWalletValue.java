package com.katkada.Other;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.katkada.Activity.Login;
import com.katkada.Activity.PaymentOption;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by prakash on 02-12-2016.
 */
public class   LoadWalletValue extends AsyncTask {

    JSONParser jsonParser = new JSONParser();
    public static String WalletAmount, RegionKey;
    PreferenceHelper preferenceHelper;

    Context context;
    public LoadWalletValue(Context ctx){
        this.context = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        preferenceHelper=new PreferenceHelper(context);
    }
    @Override
    protected JSONObject doInBackground(Object[] args) {
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("user_id", preferenceHelper.getUserId()));
        JSONObject json = jsonParser.makeHttpRequest(Config.GET_USER_WALLET_URL, "POST", params);
        Log.v("WallerAMount",""+json);
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
                //  jobject=jresponse.getJSONObject("result");
                //     RegionKey= String.valueOf(jobject);
                ////  Toast.makeText((),"Coupon \n "+jobject.getString("amt"),Toast.LENGTH_LONG).show();
                // Toast.makeText(getContext(),"Coupon Value\n "+jresponse.getString("result"),Toast.LENGTH_LONG).show();
                ////   Toast.makeText(LoadRegionCode.this,"Coupon \n "+jobject.getString("msg"),Toast.LENGTH_LONG).show();
                if (jresponse.getString("error").equals("1")) { //Toast.makeText(LoadWalletValue.this,"Unable to load",Toast.LENGTH_LONG).show();
                    return;
                } else if (jresponse.getString("success").equals("1")) {
                    try {
                         //if (jresponse.getInt("amount")!=null) {
                            WalletAmount = String.valueOf(jresponse.getInt("amount"));
                             preferenceHelper.putWallet(jresponse.getString("amount"));
                            //PaymentOption.tv_walletValue.setText("\u20B9 " + WalletAmount);
                      // }
                        Log.v("WalletAmount",""+WalletAmount);
                         Log.d("jobject", "onPostExecute: "+jobject);
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

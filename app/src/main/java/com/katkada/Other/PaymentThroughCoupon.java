package com.katkada.Other;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.katkada.Activity.Login;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by prakash on 23-01-2017.
 */
public class PaymentThroughCoupon extends AsyncTask {
    JSONParser jsonParser = new JSONParser();
    private static Context context;
    public String conType, rechargeAmount, mobileNO, CouponCode, operator_name, location_name;
    public PaymentThroughCoupon(Context c) {
        context = c;
    }
    // double restAmount=Double.parseDouble(Copon_Amount)-Double.parseDouble(rechargeAmount);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        AndyUtils.showSimpleProgressDialog(context,null,"loading",true);
    }
    @Override
    protected Object doInBackground(Object[] args) {
        String type = String.valueOf(args[0]);
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("rechargeType", type));
        params.add(new BasicNameValuePair("user_id", Login.userID));
        params.add(new BasicNameValuePair("coupon_code", String.valueOf(args[1])));
        params.add(new BasicNameValuePair("amount", String.valueOf(args[2])));
        params.add(new BasicNameValuePair("mobile_no", String.valueOf(args[3])));//Phone Number
        params.add(new BasicNameValuePair("operator", String.valueOf(args[4]))); // OPerator
        params.add(new BasicNameValuePair("connection_type", String.valueOf(args[5])));//Rec_Connection
        params.add(new BasicNameValuePair("region", String.valueOf(args[6])));//Rec_Locatio
        params.add(new BasicNameValuePair("amount", String.valueOf(args[7])));// after reducing coupon amount
        params.add(new BasicNameValuePair("wallet_amt", String.valueOf(args[8]))); // after reducing wallet amount
     ///   Uri.Builder builder = new Uri.Builder();
//        http:
////katkada.com/test/Payment_api/pay_now/
//        builder.scheme("http")
//                .authority("katkada.com")
//                .appendPath("Payment_api")
//                .appendPath("pay_now")
//                .appendQueryParameter("rechargeType", type)
//                .appendQueryParameter("user_id", Login.userID)
//                .appendQueryParameter("coupon_code", String.valueOf(args[1]))
//                .appendQueryParameter("merchant_param1", String.valueOf(args[2]))
//                .appendQueryParameter("merchant_param4", String.valueOf(args[3]))
//                .appendQueryParameter("merchant_param3", String.valueOf(args[4]))
//                .appendQueryParameter("merchant_param2", String.valueOf(args[5]))
//                .appendQueryParameter("balance_camt", String.valueOf(args[6]))
//                .appendQueryParameter("balance_wamt", String.valueOf(args[7]))
//                .appendQueryParameter("balance_ca_amt", String.valueOf(args[8]));
//        String myUrl = builder.build().toString();
//        Log.d("Login.userID: ", Login.userID);
//        Log.d("conType: ", String.valueOf(args[3]));
//        Log.d("location_name: ", String.valueOf(args[4]));
//        Log.d("operator_name: ", String.valueOf(args[5]));
//        //   Log.d("Login.userID: ",Login.userID);
//        Log.d("mobileNO: ", String.valueOf(args[2]));
        //  Log.d("restAmount: ",""+restAmount);
        JSONObject json1 = jsonParser.makeHttpRequest(Config.PAYMENT_THROUGH_COUPON, "POST", params);
        return json1;
    }
    @Override
    protected void onPostExecute(Object o) {
        JSONObject jsonObject = null;
        super.onPostExecute(o);
        try {
            if (o != null) {
                AndyUtils.removeSimpleProgressDialog();
                jsonObject = new JSONObject(String.valueOf(o));
                if (jsonObject.has("success") && jsonObject.getString("success").equals("1")) {
                    if (jsonObject.getString("success").equals("1")) {
                        if (jsonObject.has("url")) {
                            String URL = String.valueOf(jsonObject.getString("url"));
                            // Toast.makeText(context, "URL:\n\t"+URL, Toast.LENGTH_SHORT).show();
                            // WebVIew_CCAvanue.mywebview.loadUrl(URL);
                            //mywebview.loadUrl(url);
                            LoadWalletValue loadWalletValue1 = new LoadWalletValue(context);
                            loadWalletValue1.execute(Login.userID);
                            Toast.makeText(context, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                        if (jsonObject.has("message")) {
                            LoadWalletValue loadWalletValue1 = new LoadWalletValue(context);
                            loadWalletValue1.execute(Login.userID);
                            Toast.makeText(context, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (jsonObject.getString("error").equals("1")) {
                    if (jsonObject.has("message")) {
                        Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        LoadWalletValue loadWalletValue1 = new LoadWalletValue(context);
                        loadWalletValue1.execute(Login.userID);
                        Toast.makeText(context, "" + jsonObject.getJSONObject("message"), Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("jsonObject", jsonObject.toString());
                //  Toast.makeText(context, ""+jsonObject, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }
}
package com.katkada.Other;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.katkada.Activity.Login;
import com.katkada.Activity.Register;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by prakash on 28-12-2016.
 */
public class CouponValidationGlobal extends AsyncTask {
    private static Context context;
    public String conType, rechargeAmount, mobileNO, CouponCode, operator_name, location_name;
    PreferenceHelper preferenceHelper;
    public CouponValidationGlobal(Context c) {
        context = c;
    }
    Fragment fragment = null;
    public SeekBar bar, bar_data, bar_sms;
    public static String OperatorType = "prepaid", OperatorIdNo = "1", RegionIdNo = "1";
    private static final String TAG = Register.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    private static final int PICK_CONTACT = 1234;
    public static String Copon_Message = "Coupon", Copon_Amount = "0", coupon_Code;
    public static List<String> list, Regions, Operators;
    JSONParser jsonParser = new JSONParser();
    public static String[] spinnerArray_Region;
    public final static HashMap<String, String> spinnerMap_Region = new HashMap<String, String>();
    // ProgressDialog loading1;
    @Override
    protected void onPreExecute() {
        //  loading1 = ProgressDialog.show(getContext(), "Fetching Data","Please Wait...",true,true);
        AndyUtils.showSimpleProgressDialog(context,null,"loading",true);
        super.onPreExecute();
    }
    @Override
    protected JSONObject doInBackground(Object[] args) {
        preferenceHelper= new PreferenceHelper(context);
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("connection_type", String.valueOf(args[0])));
        params.add(new BasicNameValuePair("location_name", String.valueOf(args[1])));
        params.add(new BasicNameValuePair("operator_name", String.valueOf(args[2])));
        params.add(new BasicNameValuePair("coupon_code", String.valueOf(args[3])));
        params.add(new BasicNameValuePair("amount", String.valueOf(args[4])));
        params.add(new BasicNameValuePair("rechargeType", preferenceHelper.getOperatorType()));
        params.add(new BasicNameValuePair("user_id",preferenceHelper.getUserId()));



        coupon_Code = String.valueOf(args[3]);
        conType = String.valueOf(args[0]);
        location_name = String.valueOf(args[1]);
        operator_name = String.valueOf(args[2]);
        CouponCode = String.valueOf(args[3]);
        rechargeAmount = String.valueOf(args[4]);
        mobileNO = String.valueOf(args[5]);
        JSONObject json = jsonParser.makeHttpRequest(Config.GET_USER_COUPON_URL, "POST", params);
        return json;
    }
    @Override
    protected void onPostExecute(Object o) {
        JSONObject jresponse = (JSONObject) o;
        Log.v("COupon validation",""+jresponse);
        // loading1.dismiss();
        JSONArray jsonArray = null;
        String resultString = null;
        if (o != null) {
            AndyUtils.removeSimpleProgressDialog();
            JSONObject jobject = null;
            try {
                if (jresponse.getString("success").equals("1")) {

                    if (jresponse.has("amount")) {
                        Copon_Message = jresponse.getString("amount").toString();
                        Copon_Amount = jresponse.getString("amount").toString();
                        showToastMethod(Copon_Message  );
                    }

                } else {

                    showToastMethod( jresponse.getString("message").toString());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        super.onPostExecute(o);
    }
    public static void showToastMethod(String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }
    public class CouponDatatoServer extends AsyncTask {
        double restAmount = Double.parseDouble(Copon_Amount) - Double.parseDouble(rechargeAmount);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] args) {
            String message;
            JSONObject item = new JSONObject();
            JSONObject json = new JSONObject();
            try {
                json.put("rechargeType", "student");
                //JSONArray array = new JSONArray();
                item.put("rechargeType", "Coupon");
                item.put("id", "1");
                item.put("user_id", "course1");
                item.put("coupon_code", "course1");
                item.put("res_amount", "course1");
                item.put("phone_no", "course1");
                item.put("operator_nm ", "course1");
                item.put("rec_connection", "course1");
                item.put("rec_location", "course1");
                item.put("user_id", "course1");
                item.put("user_id", "course1");
                Log.d("JSON: ", item.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("rechargeType", "Coupon"));
            params.add(new BasicNameValuePair("id", "1"));
            params.add(new BasicNameValuePair("user_id", Login.userID));
            // params.add(new BasicNameValuePair("connection_type", conType));
            params.add(new BasicNameValuePair("res_amount", "" + restAmount));
            params.add(new BasicNameValuePair("coupon_code", CouponCode));
            params.add(new BasicNameValuePair("phone_no", mobileNO));
            params.add(new BasicNameValuePair("rec_connection", conType));
            params.add(new BasicNameValuePair("rec_location", location_name));
            params.add(new BasicNameValuePair("operator_nm", operator_name));
            Log.d("Login.userID: ", Login.userID);
            Log.d("conType: ", conType);
            Log.d("location_name: ", location_name);
            Log.d("operator_name: ", operator_name);
            //   Log.d("Login.userID: ",Login.userID);
            Log.d("mobileNO: ", mobileNO);
            Log.d("restAmount: ", "" + restAmount);
            JSONObject json1 = jsonParser.makeHttpRequest(Config.PAYMENT_THROUGH_COUPON, "POST", params);
            return json1;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jsonObject = null;
            super.onPostExecute(o);
            try {
                if (o != null) {
                    jsonObject = new JSONObject(String.valueOf(o));
                    Log.d("jsonObject", jsonObject.toString());
                    // Toast.makeText(context, ""+jsonObject, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }
}
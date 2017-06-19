package com.katkada.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.Fragment.HomeScreenFragment;
import com.katkada.Other.AndyUtils;
import com.katkada.Other.Config;
import com.katkada.Other.CouponValidationGlobal;
import com.katkada.Other.JSONParser;
import com.katkada.Other.LoadWalletValue;
import com.katkada.Other.PaymentThroughCoupon;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import java.util.ArrayList;
public class PaymentOption extends AppCompatActivity {
    //  Bundle bundle=getIntent();
    RelativeLayout rl_walletrecharge, rl_ccavenuerecharge;
    Dialog dialog;
    final String[] items = {" PHP", " JAVA", " JSON", " C#", " Objective-C"};
    final ArrayList itemsSelected = new ArrayList();
    JSONParser jsonParser = new JSONParser();
    public static TextView tv_walletValue, tv_discount;
    TextView tv_amount, tv_Mobileno;
    public Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_payment_option);
        tv_walletValue = (TextView) findViewById(R.id.textView69);
        tv_amount = (TextView) findViewById(R.id.textView59);
        tv_Mobileno = (TextView) findViewById(R.id.textView63);
        tv_discount = (TextView) findViewById(R.id.textView70);
        tv_amount.setText("\u20B9 " + getIntent().getExtras().getString("amount"));
        tv_Mobileno.setText("for " + getIntent().getExtras().getString("mobileNo"));
        rl_walletrecharge = (RelativeLayout) findViewById(R.id.layout2);
        rl_ccavenuerecharge = (RelativeLayout) findViewById(R.id.layout3);
        if (LoadWalletValue.WalletAmount != null) {
            tv_walletValue.setText("\u20B9" + LoadWalletValue.WalletAmount);
        }
        if (getIntent().getExtras().containsKey("Coupoun")) ;
        {
            tv_discount.setText("Discount Rs. " + getIntent().getExtras().getString("Coupoun"));
        }
//        if(HomeScreenFragment.CouponAmount!=null)
//        {
//            tv_discount.setText("Discount Rs. "+HomeScreenFragment.CouponAmount);
//        }
        rl_walletrecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RechargeNow.isCoupon) {
                    if (Double.parseDouble(getIntent().getExtras().getString("remain_BAL_from_Coupon")) > Double.parseDouble(LoadWalletValue.WalletAmount)) {
                        View checkBoxView = View.inflate(getBaseContext(), R.layout.checkbox, null);
                        CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // Save to shared preferences
                            }
                        });
                        checkBox.setText("Include Wallet Amount");
                        AlertDialog alertDialog = new AlertDialog.Builder(PaymentOption.this).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Wallet Payment");
                        // Setting Dialog Message
                        alertDialog.setMessage("\nRecharge Amount " + "\u20B9 " + getIntent().getExtras().getString("amount") + "\n\n"
                                + "Discount Amount " + "\u20B9 " + getIntent().getExtras().getString("Coupoun") + "\n\n"
                                + "Wallet Amount " + "\u20B9" + LoadWalletValue.WalletAmount + "\n\n"
                                + "Amount Required ₹ " + (Double.parseDouble(LoadWalletValue.WalletAmount) - Double.parseDouble(getIntent().getExtras().getString("remain_BAL_from_Coupon")) + ""));
                        if (Double.parseDouble(LoadWalletValue.WalletAmount) > 0) {
                            alertDialog.setView(checkBoxView);
                        }
                        alertDialog.setCancelable(true);
                        // Setting Icon to Dialog
                        // alertDialog.setIcon(R.drawable.ic_wallet);
                        // Setting OK Button
                        alertDialog.setButton("Online Recharge", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //  Toast.makeText(getBaseContext(),"Type 4",Toast.LENGTH_SHORT).show();
                                PaymentThroughCCAvanue paymentThroughCCAvanue = new PaymentThroughCCAvanue(PaymentOption.this);
                                paymentThroughCCAvanue.execute("4",
                                        getIntent().getExtras().getString("coupounCode").toString(),
                                        getIntent().getExtras().getString("amount").toString(),
                                        getIntent().getExtras().getString("mobileNo").toString(),
                                        getIntent().getExtras().getString("Operator").toString(),
                                        getIntent().getExtras().getString("ConnectionType").toString(),
                                        getIntent().getExtras().getString("Region").toString(),
                                        CouponValidationGlobal.Copon_Amount,
                                        LoadWalletValue.WalletAmount);
/*
                          //  Intent i =new Intent(getBaseContext(),InitialScreenActivity.class);
                          //  startActivity(i);
                            Integer randomNum = ServiceUtility.randInt(0, 9999999);

                            Constants.ORDER_ID=randomNum.toString();
                            String vAccessCode = ServiceUtility.chkNull(Constants.ACCESS_CODE).toString().trim();
                            String vMerchantId = ServiceUtility.chkNull(Constants.MERCHANT_ID).toString().trim();
                            String vCurrency = ServiceUtility.chkNull(Constants.CURRENCY).toString().trim();
                            String vAmount = ServiceUtility.chkNull(Constants.AMOUNT).toString().trim();
                            if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
                                Intent intent = new Intent(PaymentOption.this,WebViewActivity.class);
                                intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(Constants.ACCESS_CODE).toString().trim());
                                intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(Constants.MERCHANT_ID).toString().trim());
                                intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(Constants.ORDER_ID).toString().trim());
                                intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(Constants.CURRENCY).toString().trim());
                                intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(getIntent().getExtras().getString("amount")).toString().trim());

                                intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(Constants.REDIRECT_URL).toString().trim());
                                intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(Constants.CANCEL_URL).toString().trim());
                                intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(Constants.GETRSA_KEY_URL).toString().trim());

                                startActivity(intent);
                            }else{
                                showToast("All parameters are mandatory.");
                            }
                        */
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    } else {
                        new AlertDialog.Builder(PaymentOption.this)
                                .setTitle("Wallet Money")
                                .setMessage("Are you sure you want to Recharge using your Wallet Money ?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
//                                    PaymentThroughCoupon paymentThroughCoupon=new PaymentThroughCoupon(PaymentOption.this);
//
//                                    paymentThroughCoupon.execute("2",getIntent().getExtras().getString("Coupoun"),
//                                            getIntent().getExtras().getString("mobileNo"),
//                                            getIntent().getExtras().getString("ConnectionType"),
//                                            getIntent().getExtras().getString("Region"),
//                                            getIntent().getExtras().getString("Operator"),"0","0","0",
//                                            getIntent().getExtras().getString("amount"));
                                        //  Toast.makeText(getBaseContext(),"Type 2",Toast.LENGTH_SHORT).show();
                                        PaymentThroughCoupon paymentThroughCoupon = new PaymentThroughCoupon(PaymentOption.this);
                                        paymentThroughCoupon.execute("2",
                                                getIntent().getExtras().getString("coupounCode").toString(),
                                                getIntent().getExtras().getString("amount").toString(),
                                                getIntent().getExtras().getString("mobileNo").toString(),
                                                getIntent().getExtras().getString("Operator").toString(),
                                                getIntent().getExtras().getString("ConnectionType").toString(),
                                                getIntent().getExtras().getString("Region").toString(),
                                                CouponValidationGlobal.Copon_Amount,
                                                LoadWalletValue.WalletAmount);
//                                    WalletRecharge walletRecharge=new WalletRecharge();
//                                    walletRecharge.execute(Login.userID,getIntent().getExtras().getString("ConnectionType"),
//                                            getIntent().getExtras().getString("Region"),
//                                            getIntent().getExtras().getString("Operator"),
//                                            getIntent().getExtras().getString("mobileNo"),
//                                         getIntent().getExtras().getString("amount"),
//                                            getIntent().getExtras().getString("remain_BAL_from_Coupon"));
//                                          finish();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } else {
                    if (Double.parseDouble(getIntent().getExtras().getString("amount")) > Double.parseDouble(LoadWalletValue.WalletAmount)) {
                        View checkBoxView = View.inflate(getBaseContext(), R.layout.checkbox, null);
                        CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // Save to shared preferences
                            }
                        });
                        checkBox.setText("Include Wallet Amount");
                        AlertDialog alertDialog = new AlertDialog.Builder(PaymentOption.this).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Wallet Payment");
                        // Setting Dialog Message
                        alertDialog.setMessage("\nRecharge Amount " + "\u20B9 " + getIntent().getExtras().getString("amount") + "\n\n"
                                + "Discount Amount " + "\u20B9 " + getIntent().getExtras().getString("Coupoun") + "\n\n"
                                + "Wallet Amount " + "\u20B9" + LoadWalletValue.WalletAmount + "\n\n"
                                + "Amount Required ₹ " + (Double.parseDouble(LoadWalletValue.WalletAmount) - Double.parseDouble(getIntent().getExtras().getString("remain_BAL_from_Coupon")) + ""));
                        if (Double.parseDouble(LoadWalletValue.WalletAmount) > 0) {
                            alertDialog.setView(checkBoxView);
                        }
                        alertDialog.setCancelable(true);
                        // Setting Icon to Dialog
                        // alertDialog.setIcon(R.drawable.ic_wallet);
                        // Setting OK Button
                        alertDialog.setButton("Online Recharge", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //   Toast.makeText(getBaseContext(),"Type 6",Toast.LENGTH_SHORT).show();
                                PaymentThroughCCAvanue paymentThroughCCAvanue = new PaymentThroughCCAvanue(PaymentOption.this);
                                paymentThroughCCAvanue.execute("6",
                                        "0",
                                        getIntent().getExtras().getString("amount").toString(),
                                        getIntent().getExtras().getString("mobileNo").toString(),
                                        getIntent().getExtras().getString("Operator").toString(),
                                        getIntent().getExtras().getString("ConnectionType").toString(),
                                        getIntent().getExtras().getString("Region").toString(),
                                        "0",
                                        LoadWalletValue.WalletAmount);
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    } else {
                        new AlertDialog.Builder(PaymentOption.this)
                                .setTitle("Wallet Money")
                                .setMessage("Are you sure you want to Recharge using your Wallet Money ?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //       Toast.makeText(getBaseContext(),"Type 5",Toast.LENGTH_SHORT).show();
                                        PaymentThroughCoupon paymentThroughCoupon = new PaymentThroughCoupon(PaymentOption.this);
                                        paymentThroughCoupon.execute("5",
                                                "0",
                                                getIntent().getExtras().getString("amount").toString(),
                                                getIntent().getExtras().getString("mobileNo").toString(),
                                                getIntent().getExtras().getString("Operator").toString(),
                                                getIntent().getExtras().getString("ConnectionType").toString(),
                                                getIntent().getExtras().getString("Region").toString(),
                                                "0",
                                                LoadWalletValue.WalletAmount);
                                       // finish();

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }
        });
        rl_ccavenuerecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent i =new Intent(getBaseContext(),InitialScreenActivity.class);
                //startActivity(i);
                //Toast.makeText(getApplicationContext(),RechargeNow.isCoupon+"",Toast.LENGTH_SHORT).show();
                if (RechargeNow.isCoupon) {
                    // if(Double.parseDouble(getIntent().getExtras().getString("remain_BAL_from_Coupon"))> Double.parseDouble(LoadWalletValue.WalletAmount))
                    // Toast.makeText(getBaseContext(),"Type 3",Toast.LENGTH_SHORT).show();
                    PaymentThroughCCAvanue paymentThroughCCAvanue = new PaymentThroughCCAvanue(PaymentOption.this);
                    paymentThroughCCAvanue.execute("3",
                            getIntent().getExtras().getString("coupounCode").toString(),
                            getIntent().getExtras().getString("amount").toString(),
                            getIntent().getExtras().getString("mobileNo").toString(),
                            getIntent().getExtras().getString("Operator").toString(),
                            getIntent().getExtras().getString("ConnectionType").toString(),
                            getIntent().getExtras().getString("Region").toString(),
                            CouponValidationGlobal.Copon_Amount,
                            LoadWalletValue.WalletAmount);
                    Log.d("amount: ", getIntent().getExtras().getString("amount").toString());
                    Log.d("mobileNo : ", getIntent().getExtras().getString("mobileNo").toString());
                    Log.d("Coupoun : ", getIntent().getExtras().getString("Coupoun").toString());
                    Log.d("Region", getIntent().getExtras().getString("Region").toString());
                    Log.d("Operator", getIntent().getExtras().getString("Operator").toString());
                    Log.d("ConnectionType", getIntent().getExtras().getString("ConnectionType").toString());
                    Log.d("jsonObject", getIntent().getExtras().getString("Coupoun").toString());
                    Log.d("jsonObject", getIntent().getExtras().getString("Coupoun").toString());
                } else {
                    //  Toast.makeText(getBaseContext(),"Type 7",Toast.LENGTH_SHORT).show();
                    PaymentThroughCCAvanue paymentThroughCCAvanue = new PaymentThroughCCAvanue(PaymentOption.this);
                    paymentThroughCCAvanue.execute("7",
                            "0",
                            getIntent().getExtras().getString("amount").toString(),
                            getIntent().getExtras().getString("mobileNo").toString(),
                            getIntent().getExtras().getString("Operator").toString(),
                            getIntent().getExtras().getString("ConnectionType").toString(),
                            getIntent().getExtras().getString("Region").toString(),
                            "0",
                            LoadWalletValue.WalletAmount);
                }

/*
                Integer randomNum = ServiceUtility.randInt(0, 9999999);

                Constants.ORDER_ID=randomNum.toString();
                //Mandatory parameters. Other parameters can be added if required.
                String vAccessCode = ServiceUtility.chkNull(Constants.ACCESS_CODE).toString().trim();
                String vMerchantId = ServiceUtility.chkNull(Constants.MERCHANT_ID).toString().trim();
                String vCurrency = ServiceUtility.chkNull(Constants.CURRENCY).toString().trim();
                String vAmount = ServiceUtility.chkNull(Constants.AMOUNT).toString().trim();
                if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
                    Intent intent = new Intent(PaymentOption.this,WebViewActivity.class);
                    intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(Constants.ACCESS_CODE).toString().trim());
                    intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(Constants.MERCHANT_ID).toString().trim());
                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(Constants.ORDER_ID).toString().trim());
                    intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(Constants.CURRENCY).toString().trim());
                    intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(getIntent().getExtras().getString("amount")).toString().trim());

                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(Constants.REDIRECT_URL).toString().trim());
                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(Constants.CANCEL_URL).toString().trim());
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(Constants.GETRSA_KEY_URL).toString().trim());

                    startActivity(intent);
                }else{
                    showToast("All parameters are mandatory.");
                }
*/
            }
        });
    }
    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }
    public class WalletRecharge extends AsyncTask {
        public String WalletAmount, RegionKey;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("id", String.valueOf(args[0])));
            params.add(new BasicNameValuePair("connection", String.valueOf(args[1])));
            params.add(new BasicNameValuePair("region_name", String.valueOf(args[2])));
            params.add(new BasicNameValuePair("operator_name", String.valueOf(args[3])));
            params.add(new BasicNameValuePair("mobile_no", String.valueOf(args[4])));
            params.add(new BasicNameValuePair("amount", String.valueOf(args[5])));
            JSONObject json = jsonParser.makeHttpRequest(Config.WALLET_RECHARGE_URL, "POST", params);
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
                    if (jresponse.getString("error").equals("1")) {
                        Toast.makeText(getBaseContext(), " " + jresponse.getJSONObject("error_msg"), Toast.LENGTH_LONG).show();
                        return;
                    } else if (jresponse.getString("success").equals("1")) {
                        try {
                            WalletAmount = jresponse.getString("wallet_amount");
                            Toast.makeText(getBaseContext(), jresponse.getString("wallet_amount"), Toast.LENGTH_LONG).show();
                            Log.d("Wallet_Recharge", "onPostExecute: " + Login.userID);
                            Log.d("Wallet_Recharge", "onPostExecute: " + getIntent().getExtras().getString("ConnectionType"));
                            Log.d("Wallet_Recharge", "onPostExecute: " + getIntent().getExtras().getString("Region"));
                            Log.d("Wallet_Recharge", "onPostExecute: " + getIntent().getExtras().getString("Operator"));
                            Log.d("Wallet_Recharge", "onPostExecute: " + getIntent().getExtras().getString("mobileNo"));
                            Log.d("Wallet_Recharge", "onPostExecute: " + getIntent().getExtras().getString("amount"));
                            double restamount = Double.parseDouble(getIntent().getExtras().getString("amount")) - Double.parseDouble(HomeScreenFragment.CouponAmount);
                            Log.d("restamount", "restamount: " + restamount);
                            WalletPAymentProcess walletPAymentProcess = new WalletPAymentProcess();
                            walletPAymentProcess.execute(Login.userID, Double.toString(restamount),
                                    getIntent().getExtras().getString("amount"),
                                    getIntent().getExtras().getString("Operator"),
                                    getIntent().getExtras().getString("Region"),
                                    getIntent().getExtras().getString("mobileNo"),
                                    getIntent().getExtras().getString("ConnectionType"),
                                    "192.168.1.1", "android", "1");
                            PaymentOption.tv_walletValue.setText("\u20B9 " + WalletAmount);
                            Log.d("jobject", "onPostExecute: " + jresponse);
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
    public class WalletPAymentProcess extends AsyncTask {
        public String WalletAmount, RegionKey;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("user_id", String.valueOf(args[0])));
            params.add(new BasicNameValuePair("rest_amount", String.valueOf(args[1])));
            params.add(new BasicNameValuePair("rech_amount", String.valueOf(args[2])));
            params.add(new BasicNameValuePair("operator_name", String.valueOf(args[3])));
            params.add(new BasicNameValuePair("region_name", String.valueOf(args[4])));
            params.add(new BasicNameValuePair("phone", String.valueOf(args[5])));
            params.add(new BasicNameValuePair("connection_type", String.valueOf(args[6])));
            params.add(new BasicNameValuePair("ip_address", String.valueOf(args[7])));
            params.add(new BasicNameValuePair("os_type", String.valueOf(args[8])));
            params.add(new BasicNameValuePair("payment_process_type", String.valueOf(args[9])));
            JSONObject json = jsonParser.makeHttpRequest(Config.WALLET_PAYMENT_PROCES_URL, "POST", params);
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
                    if (jresponse.getString("error").equals("1")) {
                        Toast.makeText(getBaseContext(), " " + jresponse.getJSONObject("error_msg"), Toast.LENGTH_LONG).show();
                        return;
                    } else if (jresponse.getString("success").equals("1")) {
                        try {
                            Toast.makeText(getBaseContext(), " " + jresponse.getJSONObject("msg").getString("message"), Toast.LENGTH_LONG).show();
                            PaymentOption.tv_walletValue.setText("\u20B9 " + (Double.parseDouble(LoadWalletValue.WalletAmount) - Double.parseDouble(getIntent().getExtras().getString("amount"))));
                            Log.d("jobject", "payment Process==>: " + jresponse);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class PaymentThroughCCAvanue extends AsyncTask {
        JSONParser jsonParser = new JSONParser();
        private Context context;
        public String conType, rechargeAmount, mobileNO, CouponCode, operator_name, location_name;
        public PaymentThroughCCAvanue(Context c) {
            context = c;
        }
        // double restAmount=Double.parseDouble(Copon_Amount)-Double.parseDouble(rechargeAmount);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            openloadingDialog();
           // openloadingDialog();
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
            params.add(new BasicNameValuePair("coupon_amt", String.valueOf(args[7])));// after reducing coupon amount
            params.add(new BasicNameValuePair("wallet_amt", String.valueOf(args[8]))); // after reducing wallet amount
//            params.add(new BasicNameValuePair("balance_ca_amt", String.valueOf(args[9])));// Amount for CC Avenue
//            params.add(new BasicNameValuePair("amount", String.valueOf(args[10])));
         //   Uri.Builder builder = new Uri.Builder();
          //  http:
//katkada.com/test/Payment_api/pay_now/
//            builder.scheme("http")
//                    .authority("katkada.com")
//                    .appendPath("test")
//                    .appendPath("Payment_api")
//                    .appendPath("pay_now")
//                    .appendQueryParameter("rechargeType", type)
//                    .appendQueryParameter("user_id", Login.userID)
//                    .appendQueryParameter("coupon_code", String.valueOf(args[1]))
//                    .appendQueryParameter("merchant_param1", String.valueOf(args[2]))
//                    .appendQueryParameter("merchant_param4", String.valueOf(args[3]))
//                    .appendQueryParameter("merchant_param3", String.valueOf(args[4]))
//                    .appendQueryParameter("merchant_param2", String.valueOf(args[5]))
//                    .appendQueryParameter("balance_camt", String.valueOf(args[6]))
//                    .appendQueryParameter("balance_wamt", String.valueOf(args[7]))
//                    .appendQueryParameter("balance_ca_amt", String.valueOf(args[8]));
//            String myUrl = builder.build().toString();
            Log.d("Login.userID: ", Login.userID);
            Log.d("conType: ", String.valueOf(args[3]));
            Log.d("location_name: ", String.valueOf(args[4]));
            Log.d("operator_name: ", String.valueOf(args[5]));
            //   Log.d("Login.userID: ",Login.userID);
            Log.d("mobileNO: ", String.valueOf(args[2]));
            //  Log.d("restAmount: ",""+restAmount);
            Log.v("Prakash","Recharge parameters-->>"+params);
            JSONObject json1 = jsonParser.makeHttpRequest(Config.PAYMENT_THROUGH_COUPON, "POST", params);
            return json1;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jsonObject = null;
            super.onPostExecute(o);

              closeloadingDialog();
            try {
                if (o != null) {

                    jsonObject = new JSONObject(String.valueOf(o));
                    Log.v("Prakash","Recharge response-->> "+jsonObject);
                    if (jsonObject.has("success") && jsonObject.getString("success").equals("1")) {
                        if (jsonObject.getString("success").equals("1")) {
                            if (jsonObject.has("data")) {
                                String URL = String.valueOf(jsonObject.getJSONObject("data").getString("url"));
                                //  Toast.makeText(context, "URL:\n\t"+URL, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, WebVIew_CCAvanue.class);
                                i.putExtra("url", URL);
                                startActivity(i);
                                //  WebVIew_CCAvanue.mywebview.loadUrl(URL);
                                //mywebview.loadUrl(url);
                            }
                        }
                    } else if (jsonObject.getString("error").equals("1")) {
                        if (jsonObject.getJSONObject("error_msg").has("amount")) {
                            Toast.makeText(context, "" + jsonObject.getJSONObject("error_msg").getString("amount"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "" + jsonObject.getJSONObject("error_msg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    Log.d("jsonObject", jsonObject.toString());
                    // Toast.makeText(context, ""+jsonObject, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }
    public void openloadingDialog() {
//        if(loadingDialog!=null)
//        {
//            loadingDialog.dismiss();
//        }
        loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setContentView(R.layout.loading_layout);


        // loadingDialog.dismiss();
        loadingDialog.show();
    }

    public void closeloadingDialog() {


        if(loadingDialog.isShowing())
        {
            loadingDialog.dismiss();
        }
    }
}

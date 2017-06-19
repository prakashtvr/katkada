package com.katkada.Activity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
//import com.bankerskey.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.katkada.Other.Config;
import com.katkada.R;
public class Payment_Page extends AppCompatActivity {
    public static final String MyPREFERENCES = "Login";
    SharedPreferences sharedpreferences;
    Editor editor;
    private String USER_ID;
    private ArrayList<String> selected_product_id = new ArrayList<String>();
    private ArrayList<String> list_url = new ArrayList<String>();
    private String MERCHANT_KEY = "DTBXdebd";
    private String SALT = "tbP1OEOXxO";
    private String PAYU_BASE_URL = "https://test.payu.in";
    // private String MERCHANT_KEY = "DTBXdebd";
    // private String SALT = "tbP1OEOXxO";
    // private String PAYU_BASE_URL = "https://test.payu.in";
    private String amount;
    private String firstname;
    private String email;
    private String phone;
    private String productinfo = "Bankerskey Tests";
    private String surl;
    private String furl;
    private String service_provider = "payu_paisa";
    private String mTXNId;
    private double mAmount;
    private String mHash;
    private String mAction = "";
    WebView webView;
    Handler mHandler = new Handler();
    Config cf = new Config();
    private String str_exam_name, str_cat_id, str_subcat_id, str_test_id,
            str_status;
    String from, str_exam_id;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__page);
        webView = (WebView) findViewById(R.id.web_view);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        USER_ID = sharedpreferences.getString("USER_ID", null);
        selected_product_id.clear();
        list_url.clear();
        surl = "https://www.bankerskey.com/webservice/order-summary.php?msg=1&ret=suc&uid="
                + USER_ID;
        furl = "https://www.bankerskey.com/webservice/order-summary.php?ret=fail&uid="
                + USER_ID;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            from = extras.getString("From");
            str_exam_id = extras.getString("Exam_Id");
            amount = extras.getString("Amount");
            firstname = extras.getString("Name");
            email = extras.getString("Email");
            phone = extras.getString("Phone");
            str_exam_name = extras.getString("Exam_Name");
            str_cat_id = extras.getString("Category_Id");
            str_subcat_id = extras.getString("Sub_Category_Id");
            str_test_id = extras.getString("Test_Id");
            str_status = extras.getString("Status");
            selected_product_id = extras.getStringArrayList("Selected_Product");
            Random rand = new Random();
            String randomString = Integer.toString(rand.nextInt())
                    + (System.currentTimeMillis() / 1000L);
            mTXNId = hashCal("SHA-256", randomString).substring(0, 20);
            mAmount = Double.parseDouble(amount);
            mAmount = new BigDecimal(mAmount).setScale(0, RoundingMode.UP)
                    .intValue();
            mHash = hashCal("SHA-512", MERCHANT_KEY + "|" + mTXNId + "|"
                    + mAmount + "|" + productinfo + "|" + firstname + "|"
                    + email + "|||||||||||" + SALT);
            mAction = PAYU_BASE_URL.concat("/_payment");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description,
                            failingUrl);
                    Toast.makeText(Payment_Page.this, "Oh no! " + failingUrl,
                            Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onReceivedSslError(WebView view,
                                               final SslErrorHandler handler, SslError error) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            Payment_Page.this);
                    builder.setMessage("Error\nInvalid SSL Certificate");
                    builder.setPositiveButton("Continue",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    handler.proceed();
                                }
                            });
                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    handler.cancel();
                                }
                            });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    // Toast.makeText(Payment_Page.this, "SSL Error! " +
                    // error,
                    // Toast.LENGTH_SHORT).show();
                    // handler.proceed();
                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    return super.shouldOverrideUrlLoading(view, url);
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    list_url.add(url.trim());
                    if (list_url.size() > 0) {
                        String final_url = list_url.get(list_url.size() - 1);
                        if (final_url.equals(surl.trim())) {
                            new UpdateTestInfo().execute();
                        } else if (final_url.equals(furl.trim())) {
                            Intent i = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Intent i = new Intent(getApplicationContext(),
                            // MockTestCourse.class);
                            // i.putStringArrayListExtra("Mock_Details",
                            // intent_data);
                            // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // finish();
                            // startActivity(i);
                            //
                            // Toast.makeText(getApplicationContext(), "Failed",
                            // Toast.LENGTH_SHORT).show();
                        }
                        // if (list_url.contains(surl.trim())) {
                        //
                        // new UpdateTestInfo().execute();
                        //
                        // } else if (list_url.contains(furl.trim())) {
                        //
                        // Intent i = new Intent(getApplicationContext(),
                        // MockTestCourse.class);
                        // finish();
                        // startActivity(i);
                        //
                        // Toast.makeText(getApplicationContext(), "Failed",
                        // Toast.LENGTH_SHORT).show();
                        //
                        // } else {
                        //
                        // Log.v("Failed", "Failed");
                        //
                        // }
                    }
                    super.onPageFinished(view, url);
                }
            });
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setCacheMode(2);
            webView.getSettings().setDomStorageEnabled(true);
            webView.clearHistory();
            webView.clearCache(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setUseWideViewPort(false);
            webView.getSettings().setLoadWithOverviewMode(false);
            webView.addJavascriptInterface(new PayUJavaScriptInterface(
                    Payment_Page.this), "PayUMoney");
            Map<String, String> mapParams = new HashMap<>();
            mapParams.put("key", MERCHANT_KEY);
            mapParams.put("txnid", mTXNId);
            mapParams.put("amount", String.valueOf(mAmount));
            mapParams.put("productinfo", productinfo);
            mapParams.put("firstname", firstname);
            mapParams.put("email", email);
            mapParams.put("phone", phone);
            mapParams.put("surl", surl);
            mapParams.put("furl", furl);
            mapParams.put("hash", mHash);
            mapParams.put("service_provider", service_provider);
            webViewClientPost(webView, mAction, mapParams.entrySet());
        }
    }
    public void webViewClientPost(WebView webView, String url,
                                  Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>",
                url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format(
                    "<input name='%s' type='hidden' value='%s' />",
                    item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        // Log.d("TAG", "webViewClientPost called: " + sb.toString());
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }
    public String hashCal(String type, String str) {
        byte[] hashSequence = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSequence);
            byte messageDigest[] = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException NSAE) {
        }
        return hexString.toString();
    }
    public class UpdateTestInfo extends AsyncTask<Void, String, String> {
        ProgressDialog dialog;
        String res = null;
        String str;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Payment_Page.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.LOGIN_URL + "buynow");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
                nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                for (int i = 0; i < selected_product_id.size(); i++) {
                    String val = selected_product_id.get(i);
                    nameValuePairs.add(new BasicNameValuePair("channelcost[]",
                            val));
                }
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                str = cf.inputStreamToString(response.getEntity().getContent())
                        .toString();
                // Log.v("response", str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                try {
                    JSONObject jobject = new JSONObject(result);
                    JSONObject jobj = jobject.getJSONObject("buy_now");
                    String str_status = jobj.getString("status");
                    // Log.v("BUY_STATUS", str_status);
                    if (str_status.equals("Success")) {
                        Intent i = new Intent(getApplicationContext(),
                                AboutUsActivity.class);
                        i.putExtra("Exam_Name", str_exam_name);
                        i.putExtra("Category_Id", str_cat_id);
                        i.putExtra("Sub_Category_Id", str_subcat_id);
                        i.putExtra("Test_Id", str_test_id);
                        i.putExtra("Status", str_status);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Success",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (from.equals("Alert_MockTest")) {
                            Intent i = new Intent(getApplicationContext(),
                                    AboutUsActivity.class);
                            i.putExtra("Category_Id", str_cat_id);
                            i.putExtra("Sub_Category_Id", str_subcat_id);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(i);
                        }
                    }
                } catch (Exception e) {
                    // startToast("Failed");
                }
            }
        }
    }
    public class PayUJavaScriptInterface {
        Context mContext;
        /**
         * Instantiate the interface and set the context
         */
        PayUJavaScriptInterface(Context c) {
            mContext = c;
        }
        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;
                    Toast.makeText(Payment_Page.this,
                            "Payment Successfully.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Payment_Page.this);
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Do you want to cancel this transaction?");
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (from.equals("Alert_MockTest")) {
                            Intent i = new Intent(getApplicationContext(),
                                    AboutUsActivity.class);
                            i.putExtra("Exam_Id", str_exam_id);
                            i.putExtra("Category_Id", str_cat_id);
                            i.putExtra("Sub_Category_Id", str_subcat_id);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(i);
                        }
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

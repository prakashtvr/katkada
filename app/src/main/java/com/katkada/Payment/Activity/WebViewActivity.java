package com.katkada.Payment.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.katkada.Activity.Login;
import com.katkada.Payment.Utility.AvenuesParams;
import com.katkada.Payment.Utility.Constants;
import com.katkada.Payment.Utility.RSAUtility;
import com.katkada.Payment.Utility.ServiceHandler;
import com.katkada.Payment.Utility.ServiceUtility;
import com.katkada.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
public class WebViewActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    Intent mainIntent;
    String html, encVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view2);
        mainIntent = getIntent();
        // Calling async task to get display content
        new RenderView().execute();
    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(WebViewActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
            String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
            System.out.println("Response: " + vResponse);
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_COUNTRY, "India"));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY, "India"));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (dialog.isShowing())
                dialog.dismiss();
            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    // process the html as needed by the app
                    String status = null;
                    if (html.indexOf("Failure") != -1) {
                        status = "Transaction Declined!";
                    } else if (html.indexOf("Success") != -1) {
                        status = "Transaction Successful!";
                    } else if (html.indexOf("Aborted") != -1) {
                        status = "Transaction Cancelled!";
                    } else {
                        status = "Status Not Known!";
                    }
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    intent.putExtra("transStatus", status);
                    startActivity(intent);
                }
            }
            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    if (url.indexOf("/ccavResponseHandler.jsp") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });

			/* An instance of this class will be registered as a JavaScript interface */
            StringBuffer params = new StringBuffer();
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME, Login.ProgileUserName));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_Address, "chennai"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY, "chennai"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE, "Tamilnadu"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP, "610103"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY, "india"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL, Login.phoneNumber));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL, Login.UserEmail));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_NAME, Login.ProgileUserName));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ADDRESS, "chennai,chennai"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY, "Chennai"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_STATE, "Tamilnadu"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ZIP, "610010"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_COUNTRY, "india"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_TEL, Login.phoneNumber));
            //   params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY, "prakash"));
            //   params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY, "prakash"));
            String vPostParams = params.substring(0, params.length() - 1);
            try {
                webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
                showToast("Exception occured while opening webview.");
            }
        }
    }
    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }
}
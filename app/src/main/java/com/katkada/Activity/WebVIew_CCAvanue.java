package com.katkada.Activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.katkada.Payment.Activity.*;
import com.katkada.R;
public class WebVIew_CCAvanue extends Activity {
    public static WebView mywebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view__ccavanue);
        mywebview = (WebView) findViewById(R.id.webview1);
        final Bundle extras = getIntent().getExtras();
        String url = extras.get("url").toString();
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        if (extras.containsKey("url"))
            mywebview.loadUrl(extras.get("url").toString());
        mywebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(mywebview, url);
              //  Toast.makeText(getApplicationContext(), "Oh no! " + url, Toast.LENGTH_SHORT).show();
                //  mywebview.loadUrl(extras.get("url").toString());
                //  mywebview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
//Toast.makeText(getApplicationContext(),extras.get("url").toString(),Toast.LENGTH_SHORT).show();
        //  String url=" https://secure.ccavenue.com/transaction/transaction.do?command=initiateTransaction&encRequest=6431a8bed669656926d8ccc6c291da89ed1e3418f4adcadd1fb8d7b382fa3a8b8e6dd810424db473287d0a4d9fe86a0b3cc2ff4e43b575eb000ccfbd23a253d8e42454cbe80d10392607ec6a86b5ac5176b051863e233d0f12cf482d18db4f2eeb44bcf310891a850591beeb5c5c4c91c0e9636175e80d5ea17e8e6209fc5f7c511b97bf6f465aedee7dad79e0ca213d174d7cf8cfa9875d0acf1c197afffa255be37061943d10c27dc9e75250a8d682b3ea024206a43806ca5c8ba03d68aa3989e2d65a8f58b8e9a763a1e86378db97d56fb781f6ffe40ea69f7f0c694839b9133cb10a5e296a260277dacf151722711934fea67117e083c0588849bbfcc0b52eab5e9675008eeb2358d9ad5b420fd633122adc4505e060d3fcd1c5de2e96c2c561554bd2e9b8011afb44964937e3e8811834dd7bb0e7da608c2e517ef6db9679535e2780df9ee6ec32a88519581b83b95cb7c5063417722e3ad54fc32f0110e6255e14537d2c0600e7be7c57669e3aeb607a1a4c61726f6e119b194eb26c1e78b774c97993a4054546d53d8bdbc943ebf78d5544353d8c2ec031ac4eb5e5a530298ba43e7822265d253a574504c606afa84c10a3cff2e1d883c4cb2642d04ec43b3539f50b22552c12c0689404a5709490897d0714fd33aaf547c871fd722f65da0dccbb94a291ac6240c23a97f2262530870194effe7d6260b913aca46554d38ef8d6500b0bcf80087e17af6074f2809ba3633585b35df9e33e8368ca3c382ee2c0d8745c41f5a589fa20b48f3d79&access_code=AVHL67DI04BP79LHPB";
    }
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
            AlertDialog alertDialog = new AlertDialog.Builder(
                    WebVIew_CCAvanue.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("TRANSACTION STATUS");

            // Setting Dialog Message
            alertDialog.setMessage(status+"");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.ic_pay_ok);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    WebVIew_CCAvanue.this.finish();
                    // Write your code here to execute after dialog closed
                  //  Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });

            // Showing Alert Message
            alertDialog.show();
//            Intent intent = new Intent(getApplicationContext(), com.katkada.Payment.Activity.StatusActivity.class);
//            intent.putExtra("transStatus", status);
//            startActivity(intent);
        }
    }
}

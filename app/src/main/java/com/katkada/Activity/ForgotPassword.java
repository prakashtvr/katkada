package com.katkada.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.Other.Config;
import com.katkada.Other.JSONParser;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class ForgotPassword extends AppCompatActivity {
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink, LoginLink, forgotPasswordLink;
    private static final int REQUEST_SIGNUP = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailText = (EditText) findViewById(R.id.input_email);
        //passwordText =(EditText)findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_next);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailText.getText().toString().trim().isEmpty()) {
                    if (Config.getConnectivityStatus(getBaseContext())) {
                        if (Config.isOnline()) {
                            RetrivePassword retrivePassword = new RetrivePassword();
                            retrivePassword.execute(emailText.getText().toString().trim());
                            // finish();
                        } else {
                            Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showalert();
                    }
                } else {
                    emailText.setError("Enter Email or Mobile no");
                }
            }
        });
        signupLink = (TextView) findViewById(R.id.link_signup);
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
            }
        });
        LoginLink = (TextView) findViewById(R.id.link_login);
        LoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
            }
        });
    }
    public void showalert() {
        new AlertDialog.Builder(getBaseContext())
                .setMessage("Internet Not Available")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.finish();
                    }
                }).show();
    }
    JSONParser jsonParser = new JSONParser();
    public class RetrivePassword extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] args) {
            String identity = String.valueOf(args[0]);
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("identity", identity));
            JSONObject json = jsonParser.makeHttpRequest(Config.FORGOT_PASSWORD_URL, "POST", params);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            String resultString = null;
            if (o != null) {
                JSONObject jobject = null;
                JSONObject jresponse = (JSONObject) o;
                try {
                    if (jresponse.getString("error").equals("1")) {
                        if (jresponse.has("message")) {
                            Toast.makeText(getBaseContext(), " " + jresponse.getString("message"), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), " " + jresponse.getJSONObject("message").getString("identity"), Toast.LENGTH_LONG).show();
                        }
                        return;
                    } else if (jresponse.getString("success").equals("1")) {
                        Toast.makeText(getBaseContext(), " " + jresponse.getString("message"), Toast.LENGTH_LONG).show();
                        finish();
                        // return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(o);
        }
    }
}

package com.katkada.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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

import butterknife.internal.Utils;

import com.android.volley.RequestQueue;
public class Register extends AppCompatActivity {
    TextView tv_Name, tv_Email, tv_Password, tv_MobileNo, Link_Login,tv_promo;
    Button btn_Signup;
    private Utils util;
    public String url;
    String URL = "http://katkada.com/index.php/api/register";
    String confirm_URL = "http://katkada.com/index.php/api/confirm";
    String ipAddress, mobileno;
    JSONParser jsonParser = new JSONParser();
    private AppCompatButton buttonConfirm, buttonResendOTP;
    private EditText editTextConfirmOtp;
    int i = 0;
    private static final int REQUEST_SIGNUP = 0;
    private RequestQueue requestQueue;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tv_Name = (TextView) findViewById(R.id.input_name);
        tv_Email = (TextView) findViewById(R.id.input_email);
        tv_Password = (TextView) findViewById(R.id.input_password);
        tv_MobileNo = (TextView) findViewById(R.id.input_MobileNo);
        Link_Login = (TextView) findViewById(R.id.link_login);
        btn_Signup = (Button) findViewById(R.id.btn_signup);
        tv_promo=(TextView)findViewById(R.id.input_promocode) ;
        url = "http://katkada.com/index.php/api/register";
        Link_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(intent);
                if (Config.getConnectivityStatus(getBaseContext())) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                } else {
                    Config.ShowToast(getBaseContext(), "Internet Not Available");
                }
            }
        });
        // url = "http://katkada.com/index.php/api/onfirm";
        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileno = tv_MobileNo.getText().toString();
//Validation for Blank Field
                if (tv_Email.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Email cannot be Blank", Toast.LENGTH_LONG).show();
                    tv_Email.setError("Email cannot be Blank");
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(tv_Email.getText().toString()).matches()) {
                    //Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                    tv_Email.setError("Invalid Email");
                    return;
                }
                //  else
                //{
                //    Toast.makeText(getApplicationContext(), "Validated Succesfully", Toast.LENGTH_LONG).show();
                //}
                if (tv_Password.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Password cannot be Blank", Toast.LENGTH_LONG).show();
                    tv_Password.setError("Password cannot be Blank");
                    return;
                }
                if (tv_MobileNo.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Mobile No cannot be Blank", Toast.LENGTH_LONG).show();
                    tv_MobileNo.setError("Mobile No cannot be Blank");
                    return;
                }
                RegisterUser attemptLogin = new RegisterUser();
                attemptLogin.execute(tv_Name.getText().toString(), tv_Email.getText().toString(), tv_Password.getText().toString(), tv_MobileNo.getText().toString(),tv_promo.getText().toString());
                // Toast.makeText(getBaseContext(), "Registration Success..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class RegisterUser extends AsyncTask {
        String URL_String;
        ProgressDialog dialog;
        String str = null;
        @Override
        protected JSONObject doInBackground(Object[] args) {
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            Log.d("IPAddress", ipAddress);
            String Name = String.valueOf(args[0]);
            String email = String.valueOf(args[1]);
            String password = String.valueOf(args[2]);
            String MobileNo = String.valueOf(args[3]);
            String promocode = String.valueOf(args[4]);
            //String ipAddress= args[4];
            // String email = args[2];
            //String password = args[1];
            //  String name= args[0];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("username", Name));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("phone", MobileNo));
            params.add(new BasicNameValuePair("ip_address", ipAddress));
            params.add(new BasicNameValuePair("referral_code", promocode));
            params.add(new BasicNameValuePair("device_type", "ANDROID"));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONObject json = jsonParser.makeHttpRequest(Config.REGISTER_URL, "POST", params);
            return json;
        }
        //http://katkada.com/index.php/api/register
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Register.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Object o) {
            dialog.dismiss();
            JSONObject jresponse = null;
            String resultString = null;
            if (o != null) {
                try {
                    jresponse = new JSONObject(String.valueOf(o));
                    Log.v("Register","RegisterUser:-->> "+jresponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    resultString = jresponse.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (jresponse.getString("error").equals("1")) {
                        jresponse = new JSONObject(jresponse.getString("errors"));
                        //     Toast.makeText(getBaseContext(),"errrrrr",Toast.LENGTH_LONG).show();
                        if (jresponse.has("password")) {
                            tv_Password.setError(jresponse.getString("password"));
                            Toast.makeText(getBaseContext(), jresponse.getString("password"), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (jresponse.has("phone")) {
                            tv_MobileNo.setError(jresponse.getString("phone"));
                            Toast.makeText(getBaseContext(), jresponse.getString("phone"), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (jresponse.has("email")) {
                            tv_Email.setError(jresponse.getString("email"));
                            Toast.makeText(getBaseContext(), jresponse.getString("email"), Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        // Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
                        tv_Email.setText("");
                        tv_MobileNo.setText("");
                        tv_Name.setText("");
                        tv_Password.setText("");
                        Config.showSimpleProgressDialog(Register.this,null,"Please wait while we detecting your OTP",true);
                        confirmOtp();

                      //  confirmOtp(tv_MobileNo.getText().toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), resultString.toString(), Toast.LENGTH_LONG).show();
            }
            //o.toString();
            super.onPostExecute(o);
        }
    }
    public class ActivateUser extends AsyncTask {
        String URL_String;
        ProgressDialog dialog;
        String str = null;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Register.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
            //super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            String otp = String.valueOf(args[1]);
            String MobileNo = String.valueOf(args[0]);
            String device_type = String.valueOf(args[2]);
            String ip = String.valueOf(args[3]);
            //String ipAddress= args[4];
            // String email = args[2];
            //String password = args[1];
            //  String name= args[0];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("phone", MobileNo));
            params.add(new BasicNameValuePair("otp", otp));
            params.add(new BasicNameValuePair("device_type", device_type));
            params.add(new BasicNameValuePair("ip_address", ip));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONObject json = jsonParser.makeHttpRequest(Config.USER_ACTIVATION_URL, "POST", params);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            dialog.dismiss();
            JSONObject jresponse = null;
            String resultString = null;
            try {
                jresponse = new JSONObject(String.valueOf(o));
                Log.v("Register","ActivateUser:-->> "+jresponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                resultString = jresponse.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (jresponse.getString("error").equals("1")) {
                    editTextConfirmOtp.setError("OTP Does Not Match");
                } else {
                    editTextConfirmOtp.setText("");
                    // Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
                    //  tv_Email.setText("");
                    //  tv_MobileNo.setText("");
                    //  tv_Name.setText("");
                    //   tv_Password.setText("");
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    Register.this.finish();
                    startActivity(new Intent(getBaseContext(), Login.class));
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    Toast.makeText(getBaseContext(), resultString.toString(), Toast.LENGTH_LONG).show();
                    //  confirmOtp();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), resultString.toString(), Toast.LENGTH_LONG).show();
            //o.toString();
            super.onPostExecute(o);
        }
    }
    private void confirmOtp() throws JSONException {
        //String nme=tv_Name.
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.otpverifition, null);
        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);
        buttonResendOTP = (AppCompatButton) confirmDialog.findViewById(R.id.buttonresendOTP);
        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final ProgressDialog loading;
        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);
        //Creating an alert dialog
        alertDialog = alert.create();
        //Displaying the alert dialog
        alertDialog.show();
        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                //  alertDialog.dismiss();
                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(Register.this, "Authenticating", "Please wait while we check the entered code", false, false);
                //Getting the user entered otp from edittext
                // final String otp = editTextConfirmOtp.getText().toString().trim();
                //  final String Mobile = tv_MobileNo.getText().toString().trim();
                if (Config.getConnectivityStatus(getBaseContext())) {
                    if (Config.isOnline()) {
                        ActivateUser activateUser = new ActivateUser();
                        activateUser.execute(mobileno, editTextConfirmOtp.getText().toString(),"ANDROID",ipAddress);
                    } else {
                        Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showalert();
                }
                loading.dismiss();
            }
        });
        //Resend OTP
        buttonResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.getConnectivityStatus(getBaseContext())) {
                    if (Config.isOnline()) {
                        ResendOTP resendOTP = new ResendOTP();
                        resendOTP.execute(mobileno);
                    } else {
                        Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showalert();
                }
                //
            }
        });
    }
    public class ResendOTP extends AsyncTask {
        @Override
        protected JSONObject doInBackground(Object[] args) {
            String MobileNo = String.valueOf(args[0]);
            //String ipAddress= args[4];
            // String email = args[2];
            //String password = args[1];
            //  String name= args[0];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("phone", MobileNo));
            //  params.add(new BasicNameValuePair("otp", otp));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONObject json = jsonParser.makeHttpRequest(Config.RESEND_OTP_URL, "POST", params);
            return json;
        }
        protected void onPostExecute(Object o) {
            JSONObject jresponse = null;
            String resultString = null;
            try {
                jresponse = new JSONObject(String.valueOf(o));
                Log.v("Register","ResendOTP:-->> "+jresponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                resultString = jresponse.getString("message");
                if (resultString.equals("Account activated")) {
                    alertDialog.dismiss();
                    // startActivity(new Intent(MainActivity.this, ProfileDetail.class));
                    startActivity(new Intent(getBaseContext(), RechargeNow.ProfileDetail.class));
                } else {
                   // editTextConfirmOtp.setError(resultString);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (jresponse.getString("error").equals("1")) {
                    // editTextConfirmOtp.setError("OTP Does Not Match");
                } else {
                    editTextConfirmOtp.setText("");
                    // Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
                    //  tv_Email.setText("");
                    //  tv_MobileNo.setText("");
                    //  tv_Name.setText("");
                    //   tv_Password.setText("");
                    //  confirmOtp();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), resultString.toString(), Toast.LENGTH_LONG).show();
            //o.toString();
            super.onPostExecute(o);
        }
    }
    public void showalert() {
        new android.app.AlertDialog.Builder(getBaseContext())
                .setMessage("Internet Not Available")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.finish();
                    }
                }).show();
    }
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                Log.v("BroadcastReceiver","message:-->> "+message);
                Config.removeSimpleProgressDialog();
                editTextConfirmOtp.setText(message);
                //  Toast.makeText(Login.this,"otp received:  --->>> "+message,Toast.LENGTH_SHORT).show();
                //Do whatever you want with the code here
            }
        }
    };
}

package com.katkada.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.format.Formatter;
import android.util.Log;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.Other.AndyUtils;
import com.katkada.Other.Config;
import com.katkada.Other.JSONParser;
import com.katkada.Other.LoadWalletValue;
import com.katkada.Other.PreferenceHelper;
import com.katkada.Other.SessionManager;
import com.katkada.R;


import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static String userID = "1", UserEmail;
    String URL = "http://katkada.com/index.php/api/login";
    static boolean valid = true;
    JSONParser jsonParser = new JSONParser();
    SessionManager manager;
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink, forgotPasswordLink;
    public static String ProgileUserName, phoneNumber, ConnectionType, OptName, RgName;
    android.support.v7.app.AlertDialog alertDialog;
    private AppCompatButton buttonConfirm, buttonResendOTP;
    private EditText editTextConfirmOtp;
    TextView tvotp;
    String ipAddress;
    PreferenceHelper preferenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferenceHelper= new PreferenceHelper(Login.this);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        emailText.setHintTextColor(getResources().getColor(R.color.Logintextcolor));
        passwordText.setHintTextColor(getResources().getColor(R.color.Logintextcolor));
        manager = new SessionManager();
        loginButton = (Button) findViewById(R.id.btn_login);
        // final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Config.getConnectivityStatus(getBaseContext())) {
                    if (Config.isOnline()) {
                       // Config.showCustomProgressDialog(getBaseContext(), "Login", true);
                        //   rippleBackground.startRippleAnimation();
                        //loginButton.setVisibility(View.GONE);
                        // if(loginButton.getVisibility()==View.GONE)
                        // {
                        //    loginButton.setVisibility(View.VISIBLE);
                        // }
                        if (isValidMobile(emailText.getText().toString().trim())) {
                            validate();
                            return;
                        } else if (isValidEmail(emailText.getText().toString().trim())) {
                            validate();
                            return;
                        } else {
                            emailText.setError("Invalid  Mobile No or email");
                        }
                        //  rippleBackground.stopRippleAnimation();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new AlertDialog.Builder(Login.this)
                            .setMessage("Internet Not Available")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //MainActivity.this.finish();
                                }
                            }).show();
                }
                // isInternetOn();
                // validate();
            }
        });
        signupLink = (TextView) findViewById(R.id.link_signup);
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        forgotPasswordLink = (TextView) findViewById(R.id.link_forgot_password);
        forgotPasswordLink.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }
    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
        manager.setPreferences(Login.this, "status", "1");
        String status = manager.getPreferences(Login.this, "status");
        Log.d("status_Login", status);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        Log.d("1", "Mains Started");
    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }
    public void validate() {
        valid = false;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            emailText.setError("enter a valid email address or mobile no");
            valid = false;
        } else if (password.isEmpty()) {
            passwordText.setError("password can not be blank");
        } else {
            LoginUser loginUser = new LoginUser();
            loginUser.execute(emailText.getText().toString(), passwordText.getText().toString());
        }
        // return valid;
    }
    public class LoginUser extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] args) {
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            //  Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_SHORT).show();
            String identity = String.valueOf(args[0]);
            String password = String.valueOf(args[1]);
            //  String password = String.valueOf(args[2]);
            // String MobileNo = String.valueOf(args[3]);
            //String ipAddress= args[4];
            // String email = args[2];
            //String password = args[1];
            //  String name= args[0];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("identity", identity));
            // params.add(new BasicNameValuePair("password", email));
            params.add(new BasicNameValuePair("password", password));
            // params.add(new BasicNameValuePair("phone", MobileNo));
            params.add(new BasicNameValuePair("ip_address", ipAddress));
            params.add(new BasicNameValuePair("device_type", "ANDROID"));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONObject json = jsonParser.makeHttpRequest(Config.LOGIN_URL, "POST", params);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jresponse = null;
            String resultString = null;
            if(o!=null)
            {
                try {
                    jresponse = new JSONObject(String.valueOf(o));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(jresponse.getInt("success")==1)
                    {
                        resultString = jresponse.getString("message");
                        userID = jresponse.getString("user_id");

                        preferenceHelper.putUserId(userID);


                        UserEmail = jresponse.getString("email_id");
                        manager.setPreferences(Login.this, "Shared_userID", userID);
                        manager.setPreferences(Login.this, "Shared_UserEmail", UserEmail);
                    }

                    //  GetProfileDetails getProfileDetails = new GetProfileDetails();
                    // getProfileDetails.execute(Login.userID.toString());
                    // Toast.makeText(getBaseContext(),ProgileUserName,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (jresponse.getString("error").equals("1")) {//Toast.makeText(getBaseContext(),"normal: "+String.valueOf(valid),Toast.LENGTH_LONG).show();
                        Login.valid = false;
                        onLoginFailed();
                        return;
                    } else  if(jresponse.getString("success").equals("1")) {
                        if(jresponse.getInt("active")==0)
                        {
                            confirmOtp(emailText.getText().toString());
                            return;
                        }
                        resultString = jresponse.getString("message");
                        userID = jresponse.getString("user_id");


                        preferenceHelper.putUserId(userID);

                        if(jresponse.getInt("referral_active")==1)
                        {
                            preferenceHelper.putRefferalCode(jresponse.getString("referral_code"));
                           // return;
                        }
                        if(jresponse.getInt("membership_type")==1)
                        {
                            preferenceHelper.putRefferalAmount(jresponse.getString("normal_referral_amount"));
                            // return;
                        }else {
                            preferenceHelper.putRefferalAmount(jresponse.getString("prime_referral_amount"));
                        }

                        UserEmail = jresponse.getString("email_id");
                        manager.setPreferences(Login.this, "Shared_userID", userID);
                        manager.setPreferences(Login.this, "Shared_UserEmail", UserEmail);

                        loginButton.setEnabled(false);
//                    final ProgressDialog progressDialog = new ProgressDialog(Login.this);
//                    progressDialog.setIndeterminate(true);
//                    progressDialog.setMessage("Authenticating...");
//                    progressDialog.show();
                        AndyUtils.openloadingDialog(Login.this);
                        String email = emailText.getText().toString();
                        String password = passwordText.getText().toString();
                        // TODO: Implement your own authentication logic here.
                        final String finalResultString = resultString;
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        onLoginSuccess();
                                        // onLoginFailed();
                                        // progressDialog.dismiss();
                                        AndyUtils.closeloadingDialog();
                                        Toast.makeText(getBaseContext(), finalResultString.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }, 3000);

                        new LoadWalletValue(Login.this).execute();
                    }
                    // Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            // return valid;
            super.onPostExecute(o);
        }
    }
    public final void isInternetOn() {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            Toast.makeText(this, "Internet Available", Toast.LENGTH_LONG).show();
            // validate();
        } else {
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_LONG).show();
        }
    }
    public class GetProfileDetails extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] args) {
            // WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            //  String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            //  Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_SHORT).show();
            String UserId = String.valueOf(args[0]);
            //String UserURL=
            // String password = String.valueOf(args[1]);
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("identity", UserId));
            // params.add(new BasicNameValuePair("password", email));
            //  params.add(new BasicNameValuePair("password", password));
            // params.add(new BasicNameValuePair("phone", MobileNo));
            //  params.add(new BasicNameValuePair("ip_address", ipAddress));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONObject json = jsonParser.makeHttpRequest(Config.GET_USER_DETAILS_URL + UserId, "GET", params);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jresponse = null;
            JSONObject jsonObject = null;
            String resultString = null;
            JSONArray jsonArray;
            try {
                jresponse = new JSONObject(String.valueOf(o));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                resultString = String.valueOf(jresponse.getJSONArray("result"));
                jsonArray = jresponse.getJSONArray("result");
                jsonObject = jsonArray.getJSONObject(0);
                // Username.setText(String.valueOf(jsonObject.getString("first_name")));
                ProgileUserName = String.valueOf(jsonObject.getString("first_name"));
                ConnectionType = String.valueOf(jsonObject.getString("conn_type"));
                phoneNumber = String.valueOf(jsonObject.getString("phone"));
                OptName = String.valueOf(jsonObject.getString("operator_name"));
                RgName = String.valueOf(jsonObject.getString("state"));
                manager.setPreferences(Login.this, "Shared_ProgileUserName", ProgileUserName);
                manager.setPreferences(Login.this, "Shared_ConnectionType", ConnectionType);
                manager.setPreferences(Login.this, "Shared_phoneNumber", phoneNumber);
                manager.setPreferences(Login.this, "Shared_OptName", "5");
                manager.setPreferences(Login.this, "Shared_RgName", "1");
                manager.setPreferences(Login.this, "Shared_TotalMinutes", "100");
                manager.setPreferences(Login.this, "Shared_TotalData", "1000");
                manager.setPreferences(Login.this, "Shared_TotalSMS", "100");
                //   PhoneNo=jsonObject.getString("phone");
                //  MobileNo.setText(String.valueOf(PhoneNo));
                //   DOB.setText(jsonObject.getString("dob"));
                // Gender.setText(jsonObject.getString("gender"));
                //  ConnectionType.setText(jsonObject.getString("conn_type"));
                //Operator.setText(jsonObject.getString("operator_name"));
                //  Region.setText(jsonObject.getString("state"));
                //   Address =jsonObject.getString("address_line_1");
                //   State =jsonObject.getString("zipcode");
                //   Ocupation =jsonObject.getString("occupation");
                //Address =jsonObject.getString("zipcode");
                //  res=PhoneNo;
                //  Toast.makeText(getBaseContext(),"j resultString "+resultString.toString(),Toast.LENGTH_SHORT).show();
                // Toast.makeText(getBaseContext(),"PhoneNo "+PhoneNo,Toast.LENGTH_SHORT).show();
                //  Log.d("result_Json",res);
                //   Toast.makeText(getBaseContext(),"PhoneNo "+PhoneNo,Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (jresponse.getString("error").equals("1")) {//Toast.makeText(getBaseContext(),"normal: "+String.valueOf(valid),Toast.LENGTH_LONG).show();
                    //  Login.valid = false;
                    // onLoginFailed();
                    return;
                } else {
                    //  Toast.makeText(getBaseContext(),"normal: ",Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            //   Toast.makeText(getBaseContext(),resultString.toString(),Toast.LENGTH_LONG).show();
            // return valid;
            //  super.onPostExecute(o);
        }
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    private boolean isValidMobile(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }
    private void confirmOtp(final String identity) throws JSONException {
        //String nme=tv_Name.
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.otpverifition, null);
        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);
        tvotp=(TextView)confirmDialog.findViewById(R.id.tv_Otp);
        buttonConfirm.setVisibility(View.GONE);
        buttonResendOTP = (AppCompatButton) confirmDialog.findViewById(R.id.buttonresendOTP);
        buttonResendOTP.setText("SEND OTP");
        tvotp.setText("Please complete OTP verification to activate your account");
        //Creating an alertdialog builder
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
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
                final ProgressDialog loading = ProgressDialog.show(Login.this, "Authenticating", "Please wait while we check the entered code", false, false);
                //Getting the user entered otp from edittext
                // final String otp = editTextConfirmOtp.getText().toString().trim();
                //  final String Mobile = tv_MobileNo.getText().toString().trim();
                if (Config.getConnectivityStatus(getBaseContext())) {
                    if (Config.isOnline()) {
                       ActivateUser activateUser = new ActivateUser();
                        activateUser.execute(identity, editTextConfirmOtp.getText().toString(),"ANDROID",ipAddress);
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
                        buttonConfirm.setVisibility(View.VISIBLE);
                        buttonResendOTP.setText("RESEND OTP");
                       ResendOTP resendOTP = new ResendOTP();
                        resendOTP.execute(emailText.getText().toString());
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
              //  e.printStackTrace();
            }
            try {
                resultString = jresponse.getString("message");
                if (resultString.equals("Account activated")) {
                   // alertDialog.dismiss();
                    // startActivity(new Intent(MainActivity.this, ProfileDetail.class));
                    startActivity(new Intent(getBaseContext(), RechargeNow.ProfileDetail.class));
                } else {
                 //   editTextConfirmOtp.setError("");
                }
            } catch (JSONException e) {
             //   e.printStackTrace();
            }
            try {
                if (jresponse.getString("error").equals("1")) {
                    // editTextConfirmOtp.setError("OTP Does Not Match");
                } else  {
                    editTextConfirmOtp.setText("");
                    // Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
                    //  tv_Email.setText("");
                    //  tv_MobileNo.setText("");
                    //  tv_Name.setText("");
                    //   tv_Password.setText("");
                    //  confirmOtp();
                   Config.showSimpleProgressDialog(Login.this,null,"Please wait while we detecting your OTP",true);
                }
            } catch (JSONException e) {
              //  e.printStackTrace();
            }
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), resultString.toString(), Toast.LENGTH_LONG).show();
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
            dialog = new ProgressDialog(Login.this);
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
                    Login.this.finish();
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

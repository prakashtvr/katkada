package com.katkada.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.Other.Config;
import com.katkada.Other.JSONParser;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class PostpaidBillForm extends AppCompatActivity {
    Button loginButton, btn_clear;
    TextView tv_Validity, tv_PRice, tv_name;
    EditText et_name, et_mobile, et_alt_mobile, et_email, et_address, et_pin;
    CheckBox chkTerms;
    String MNP_STR = "mnp";
    private RadioGroup radioGroup1;
    int selectedtypeId;
    private RadioButton radiotypeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpaid_bill_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        loginButton = (Button) findViewById(R.id.btn_login);
        btn_clear = (Button) findViewById(R.id.btn_ClearAll);
        tv_name = (TextView) findViewById(R.id.textView74);
        tv_Validity = (TextView) findViewById(R.id.textView75);
        tv_PRice = (TextView) findViewById(R.id.textView76);
        et_name = (EditText) findViewById(R.id.input_name);
        et_mobile = (EditText) findViewById(R.id.input_mobileno);
        et_alt_mobile = (EditText) findViewById(R.id.input_alter_mobile);
        et_email = (EditText) findViewById(R.id.input_email);
        et_address = (EditText) findViewById(R.id.input_address);
        et_pin = (EditText) findViewById(R.id.input_pincode);
        chkTerms = (CheckBox) findViewById(R.id.checkBox2);
        radioGroup1 = (RadioGroup) findViewById(R.id.radiogender1);
        Bundle extras = getIntent().getExtras();
        tv_name.setText(extras.getString("Selected_Name"));
        tv_Validity.setText(extras.getString("Selected_Validity"));
        tv_PRice.setText(extras.getString("Selected_Value"));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Name Required");
                    et_name.setFocusable(true);
                    return;
                }
                if (et_mobile.getText().toString().isEmpty()) {
                    et_mobile.setError("Mobile no Required");
                    et_mobile.setFocusable(true);
                    return;
                } else if (!Patterns.PHONE.matcher(et_mobile.getText().toString()).matches()) {
                    //Validation for Invalid Email Address
                    // Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                    et_mobile.setError("Invalid Mobile No");
                    et_mobile.setFocusable(true);
                    return;
                }
                if (et_alt_mobile.getText().toString().isEmpty()) {
                    et_alt_mobile.setFocusable(true);
                    et_alt_mobile.setError("Alternate Number Required");
                    return;
                } else if (!Patterns.PHONE.matcher(et_alt_mobile.getText().toString()).matches()) {
                    //Validation for Invalid Email Address
                    // Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                    et_alt_mobile.setError("Invalid Mobile No");
                    et_alt_mobile.setFocusable(true);
                    return;
                }
                if (et_email.getText().toString().isEmpty()) {
                    et_email.setError("Email Required");
                    et_email.setFocusable(true);
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
                    //Validation for Invalid Email Address
                    // Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                    et_email.setError("Invalid Email");
                    et_email.setFocusable(true);
                    return;
                }
                if (et_address.getText().toString().isEmpty()) {
                    et_address.setError("Address Required");
                    et_address.setFocusable(true);
                    return;
                }
                if (et_pin.getText().toString().isEmpty()) {
                    et_pin.setError("Pincode Required");
                    et_pin.setFocusable(true);
                    return;
                }
                selectedtypeId = radioGroup1.getCheckedRadioButtonId();
                radiotypeButton = (RadioButton) findViewById(selectedtypeId);
                //MNP_STR = radiotypeButton.getText().toString();
                if (radiotypeButton.getText().toString().equals("New Connection")) {
                    MNP_STR = "new_connection";
                } else {
                    MNP_STR = "mnp";
                }
                if (!chkTerms.isChecked()) {
                    chkTerms.setFocusable(true);
                    chkTerms.setTextColor(getResources().getColor(R.color.holo_red_dark));
                    chkTerms.setError(getString(R.string.must_be_checked));
                    return;
                }
                new PostpaidFormSubmit().execute(et_name.getText().toString(),
                        et_email.getText().toString(),
                        "",
                        et_mobile.getText().toString(),
                        et_alt_mobile.getText().toString(),
                        "127.0.0.1", et_address.getText().toString(), MNP_STR,
                        et_pin.getText().toString(),
                        "1",
                        Login.userID
                );
                //  Toast.makeText(getBaseContext(),MNP_STR,Toast.LENGTH_SHORT).show();
                // PostpaidBillForm.this.finish();
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_name.setText("");
                et_address.setText("");
                et_alt_mobile.setText("");
                et_email.setText("");
                et_mobile.setText("");
                et_pin.setText("");
            }
        });
    }
    JSONParser jsonParser = new JSONParser();
    public class PostpaidFormSubmit extends AsyncTask<Object, Object, JSONObject> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PostpaidBillForm.this);
            dialog.setMessage("please wait...");
            dialog.show();
        }
        @Override
        protected JSONObject doInBackground(Object... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("name", String.valueOf(args[0])));
            params.add(new BasicNameValuePair("email", String.valueOf(args[1])));
            params.add(new BasicNameValuePair("plan_details", String.valueOf(args[2])));
            params.add(new BasicNameValuePair("mobile_no", String.valueOf(args[3])));
            params.add(new BasicNameValuePair("alt_mobile_no", String.valueOf(args[4])));
            params.add(new BasicNameValuePair("ip_address", String.valueOf(args[5])));
            params.add(new BasicNameValuePair("address", String.valueOf(args[6])));
            params.add(new BasicNameValuePair("connection", String.valueOf(args[7])));
            params.add(new BasicNameValuePair("pincode", String.valueOf(args[8])));
            params.add(new BasicNameValuePair("term_condition", String.valueOf(args[9])));
            params.add(new BasicNameValuePair("user_id", String.valueOf(args[10])));
            JSONObject json = jsonParser.makeHttpRequest(Config.SAVE_POSTPAID_FORM, "POST", params);
            return json;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(JSONObject o) {
            dialog.dismiss();
            if (o != null) {
                JSONObject response = null;
                JSONObject jobject = null;
                try {
                    response = new JSONObject(String.valueOf(o));
                    if (response.getString("error").equals("1")) {
                        jobject = response.getJSONObject("error_msg");
                        if (jobject.has("mobile_no")) {
                            Toast.makeText(getBaseContext(), jobject.get("mobile_no").toString(), Toast.LENGTH_SHORT).show();
                        }
                        if (jobject.has("alt_mobile_no")) {
                            Toast.makeText(getBaseContext(), jobject.get("alt_mobile_no").toString(), Toast.LENGTH_SHORT).show();
                        }
                        if (jobject.has("pincode")) {
                            Toast.makeText(getBaseContext(), jobject.get("pincode").toString(), Toast.LENGTH_SHORT).show();
                        }
                        if (jobject.has("email")) {
                            Toast.makeText(getBaseContext(), jobject.get("email").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.getString("success").equals("1")) {
                        Toast.makeText(getBaseContext(), response.getString("error_msg").toString(), Toast.LENGTH_SHORT).show();
                        PostpaidBillForm.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(o);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}

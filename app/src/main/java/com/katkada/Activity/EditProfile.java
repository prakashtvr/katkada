package com.katkada.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.Fragment.HomeScreenFragment;
import com.katkada.Other.Config;
import com.katkada.Other.GPSTracker;
import com.katkada.Other.GetLocation;
import com.katkada.Other.JSONParser;
import com.katkada.Other.SessionManager;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
public class EditProfile extends AppCompatActivity {
    ImageView img_editProfile, img_editAccount;
    private EditText inputName, inputDOB, inputGender, inputOperator, inputRegion, inputMobileNo, inputConnectionType, inputOccupation, inputAddress, inputZipcode, inputState;
    private TextInputLayout inputLayoutName, inputLayoutGender, inputLayoutDOB;
    String name, ISUpdated;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private RadioGroup radiotypeGroup;
    ProgressDialog progressDialog;
    TextView tv_getAddress;
    private SimpleDateFormat dateFormatter;
    String gender;
    private DatePickerDialog fromDatePickerDialog;
    int selectedId;
    Fragment fragment = null;
    String screenName, DataConnectionType = "3g", TotalSTD_Loc_Calls = "100", TotalSTD_Loc_SMS = "100", Total_Data = "100";
    RelativeLayout rl_calls, rl_sms, rl_data, rl_roaming, rl_recommendation, rl_browse_packs, rl_recharge_now;
    TextView tv2g, tv3g, tv4g, floating_text_start, tv_coupon;
    TextInputLayout textInputLayout_Coupon;
    Spinner Search_Operators, Search_Region;
    public SeekBar bar, bar_data, bar_sms;
    public TextView textV, textV_Data, textV_sms;
    SessionManager manager;
    int selectedtypeId;
    public static String OperatorType = "prepaid", OperatorIdNo = "1", RegionIdNo = "1";
    //SegmentedRadioGroup segmentText;
    Toast mToast;
    public static String OperatorId = "1", CouponCode;
    private RadioGroup radioGroup1, radioGroup_SP;
    private RadioButton radiotypeButton_SP;
    private RadioButton radiotypeButton;
    RelativeLayout rl;
    EditText et_MobileNo, et_value, et_Couponcode;
    Spinner sp, sp_region;
    // List<String> list;
    InputStream is = null;
    String result = null;
    String line = null;
    ImageView loadContact;
    Button btn_Rechrge, btn_searchPlans;
    TextView Browse_Packs, ProfileName;
    private static final String TAG = Register.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    private static final int PICK_CONTACT = 1234;
    //JSONParser jsonParser = new JSONParser();
    AlertDialog dialog;
    static CountDownTimer timer = null;
    Toast toast;
    public static String OperatorID = "ACC", RegionID = "TN";
    public static List<String> list, Regions, Operators;
    public String[] spinnerArray_Oprators;
    public final HashMap<String, String> spinnerMap_Opearators = new HashMap<String, String>();
    public final HashMap<String, String> spinnerMap_Opearator2 = new HashMap<String, String>();
    //  TalkTimeFragment.DisplayPlans displayPlans;
    JSONParser jsonParser = new JSONParser();
    public static String[] spinnerArray_Region;
    public final static HashMap<String, String> spinnerMap_Region = new HashMap<String, String>();
    public final static HashMap<String, String> spinnerMap_Region1 = new HashMap<String, String>();
    // GPSTracker class
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        setDateTimeField();
        radioSexGroup = (RadioGroup) findViewById(R.id.radiogender1);
        radiotypeGroup = (RadioGroup) findViewById(R.id.radiotype);
        sp = (Spinner) findViewById(R.id.spinner_operator);
        sp_region = (Spinner) findViewById(R.id.spinner_Region);
        inputName = (EditText) findViewById(R.id.input_User_name);
        // inputGender = (EditText) findViewById(R.id.input_gender);
        inputDOB = (EditText) findViewById(R.id.input_dob);
        inputDOB.setInputType(InputType.TYPE_NULL);
        inputMobileNo = (EditText) findViewById(R.id.input_mobileno);
        inputOperator = (EditText) findViewById(R.id.input_operator);
        //   inputConnectionType = (EditText) findViewById(R.id.input_connectiontype);
        inputZipcode = (EditText) findViewById(R.id.input_zipcode);
        inputAddress = (EditText) findViewById(R.id.input_address);
        inputOccupation = (EditText) findViewById(R.id.input_occupation);
        inputState = (EditText) findViewById(R.id.input_state);
        tv_getAddress = (TextView) findViewById(R.id.getaddress);
        radioGroup_SP = (RadioGroup) findViewById(R.id.radiotype);
        img_editProfile = (ImageView) findViewById(R.id.imageView_updatePersonal);
        manager = new SessionManager();
        ISUpdated = getIntent().getStringExtra("IS_UPDATED");
        if (ISUpdated.equals("0")) {
            Toast.makeText(getApplicationContext(), "Complete your Registration ", Toast.LENGTH_LONG).show();
            new GetProfileDetails().execute(Login.userID.toString());
            //  GetProfileDetails getProfileDetails = new  GetProfileDetails();
            // getProfileDetails.execute(Login.userID.toString());
        } else {
            getUserDetails();
            LoadUserDetails();
        }
        getLat_Long();
        inputAddress.setText(GetLocation.location_string);
        radioGroup_SP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_prepaid:
                        OperatorType = "prepaid";
                        //  manager.setPreferences(getContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        if (Config.getConnectivityStatus(getBaseContext())) {
                            if (Config.isOnline()) {
                                LoadOperators loadOperators = new LoadOperators();
                                loadOperators.execute(OperatorType);
                            } else {
                                Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showalert();
                        }
                        break;
                    case R.id.radio_postpaid:
                        OperatorType = "postpaid";
                        //  manager.setPreferences(getContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        if (Config.getConnectivityStatus(getBaseContext())) {
                            if (Config.isOnline()) {
                                LoadOperators loadOperators1 = new LoadOperators();
                                loadOperators1.execute(OperatorType);
                            } else {
                                Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showalert();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        img_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.getConnectivityStatus(getBaseContext())) {
                    if (Config.isOnline()) {
                        getUserDetails();
                        selectedId = radioSexGroup.getCheckedRadioButtonId();
                        selectedtypeId = radiotypeGroup.getCheckedRadioButtonId();
                        radioSexButton = (RadioButton) findViewById(selectedId);
                        radiotypeButton = (RadioButton) findViewById(selectedtypeId);
                        gender = radioSexButton.getText().toString().toLowerCase();
                        OperatorType = radiotypeButton.getText().toString().toLowerCase();
                        screenName = inputName.getText().toString();
                        UpdatePersonalDetails();
                    } else {
                        Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new AlertDialog.Builder(EditProfile.this).setTitle("Attention!!!..")
                            .setMessage("Internet Not Available")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //MainActivity.this.finish();
                                }
                            })
                            .show();
                    //  Config.ShowToast(getBaseContext(),"Internet Not Available");
                    //Toast.makeText(getBaseContext(), "Internet Not available", Toast.LENGTH_LONG).show();
                }
                //LoadUserDetails();
            }
        });
        inputDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        tv_getAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLat_Long();
                // getlocation();
            }
        });
    }
    public void getUserDetails() {
        ISUpdated = getIntent().getStringExtra("IS_UPDATED");
        if (ISUpdated.equals("0")) {
            OperatorType = "prepaid";
        } else if (!RechargeNow.ProfileDetail.ConnectionType.getText().toString().equals("")) {
            OperatorType = RechargeNow.ProfileDetail.ConnectionType.getText().toString();
        } else {
            OperatorType = "prepaid";
        }
    }
    public void LoadUserDetails() {
        LoadRegion loadRegion = new LoadRegion();
        loadRegion.execute("regin");
        LoadOperators loadOperators = new LoadOperators();
        loadOperators.execute(OperatorType);
        gender = RechargeNow.ProfileDetail.Gender.getText().toString();
        // Toast.makeText(getApplicationContext(),gender,Toast.LENGTH_SHORT).show();
        if (!gender.equals("male")) {
            radioSexGroup.check(R.id.radio_female);
        } else {
            radioSexGroup.check(R.id.radio_male);
        }
        // OperatorType = ProfileDetail.ConnectionType.getText().toString();
        if (!RechargeNow.ProfileDetail.ConnectionType.getText().toString().equals("")) {
            OperatorType = RechargeNow.ProfileDetail.ConnectionType.getText().toString();
        } else {
            OperatorType = "prepaid";
        }
        //Toast.makeText(getApplicationContext(),OperatorType,Toast.LENGTH_SHORT).show();
        if (!OperatorType.equals("prepaid")) {
            radiotypeGroup.check(R.id.radio_postpaid);
        } else {
            radiotypeGroup.check(R.id.radio_prepaid);
        }
        if (!RechargeNow.ProfileDetail.Username.getText().toString().equals("null")) {
            inputName.setText(RechargeNow.ProfileDetail.Username.getText().toString());
        } else {
            inputName.setHint("Enter Name");
        }
        if (!RechargeNow.ProfileDetail.State.toString().equals("0")) {
            inputZipcode.setText(String.valueOf(RechargeNow.ProfileDetail.State));
        } else {
            inputZipcode.setHint("Enter Zip Code");
        }
        inputDOB.setText(String.valueOf(RechargeNow.ProfileDetail.DOB.getText()));
        inputMobileNo.setText(String.valueOf(RechargeNow.ProfileDetail.MobileNo.getText()));
        OperatorID = String.valueOf(RechargeNow.ProfileDetail.Operator.getText().toString());
        inputOperator.setText(OperatorID);
        inputAddress.setText(String.valueOf(RechargeNow.ProfileDetail.Address));
        RegionID = String.valueOf(RechargeNow.ProfileDetail.Region.getText().toString());
        inputState.setText(RegionID);
        inputOccupation.setText(String.valueOf(RechargeNow.ProfileDetail.Ocupation));
    }
    public void UpdatePersonalDetails() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputName.setError("Name Can not be blank");
            inputName.setFocusableInTouchMode(true);
            inputName.requestFocus();
            return;
        }
        if (inputDOB.getText().toString().trim().isEmpty()) {
            inputDOB.setError("Date of Birth Can not be blank");
            return;
        }
        if (inputMobileNo.getText().toString().trim().isEmpty()) {
            inputMobileNo.setError("Mobile Number Can not be blank");
            inputMobileNo.setFocusableInTouchMode(true);
            inputMobileNo.requestFocus();
            return;
        }
        if (inputState.getText().toString().trim().isEmpty()) {// inputState.setError("State Can not be blank");
            inputState.setText("1");
            //inputState.setFocusableInTouchMode(true);
            //inputState.requestFocus();
            // return;
        }
        if (inputAddress.getText().toString().trim().isEmpty()) {
            inputAddress.setError("Address Can not be blank");
            inputAddress.setFocusableInTouchMode(true);
            inputAddress.requestFocus();
            return;
        }
        if (inputZipcode.getText().toString().trim().isEmpty()) {
            inputZipcode.setError("Zip Code Can not be blank");
            inputZipcode.setFocusableInTouchMode(true);
            inputZipcode.requestFocus();
            return;
        }
        if (inputOperator.getText().toString().trim().isEmpty()) {//inputOperator.setError("Operator Can not be blank");
            inputOperator.setText("1");
            // inputOperator.setFocusableInTouchMode(true);
            // inputOperator.requestFocus();
            //return;
        }
        UpdateProfileDetails updateProfileDetails = new UpdateProfileDetails();
        updateProfileDetails.execute(Login.userID.toString(), screenName, gender, inputDOB.getText().toString(), inputMobileNo.getText().toString(), RechargeNow.ProfileDetail.Operator_id_no, OperatorType, inputOccupation.getText().toString(),
                inputAddress.getText().toString(), inputZipcode.getText().toString(), inputState.getText().toString());
    }
    public class UpdateProfileDetails extends AsyncTask {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(EditProfile.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating...");
            progressDialog.show();
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
            params.add(new BasicNameValuePair("user_id", UserId));
            params.add(new BasicNameValuePair("fname", String.valueOf(args[1])));
            params.add(new BasicNameValuePair("gender", String.valueOf(args[2])));
            params.add(new BasicNameValuePair("dob", String.valueOf(args[3])));
            params.add(new BasicNameValuePair("mobile_no", String.valueOf(args[4])));
            params.add(new BasicNameValuePair("operator_name", String.valueOf(args[5])));
            params.add(new BasicNameValuePair("number_type", String.valueOf(args[6])));
            params.add(new BasicNameValuePair("occupation", String.valueOf(args[7])));
            params.add(new BasicNameValuePair("address_line_1", String.valueOf(args[8])));
            params.add(new BasicNameValuePair("zipcode", String.valueOf(args[9])));
            params.add(new BasicNameValuePair("state", String.valueOf(args[10])));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONObject json = jsonParser.makeHttpRequest(Config.GET_USER_UPDATE_URL, "POST", params);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jresponse = null;
            JSONObject jsonObject = (JSONObject) o;
            String resultString = null;
            JSONArray jsonArray;
            progressDialog.dismiss();
            try {
                jresponse = new JSONObject(String.valueOf(o));
                Toast.makeText(getBaseContext(), String.valueOf(jsonObject.getString("result")), Toast.LENGTH_LONG).show();
                if (String.valueOf(jsonObject.getString("result")).equals("Successfully Updated")) {
                    EditProfile.this.finish();
                    manager.setPreferences(EditProfile.this, "Shared_ProgileUserName", screenName);
                    Login.ProgileUserName = screenName;
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                resultString = String.valueOf(jresponse.getJSONArray("result"));
//                jsonArray=jresponse.getJSONArray("result");
//                 jsonObject=jsonArray.getJSONObject(0);
//            inputMobileNo.setText(jsonObject.getString("phone").toString());
//              inputName.setText(jsonObject.getString("first_name").toString());
//               inputDOB.setText(jsonObject.getString("dob").toString());
//               inputOccupation.setText(jsonObject.getString("occupation").toString());
//                inputOperator.setText(jsonObject.getString("operator_name").toString());
//                inputState.setText(jsonObject.getString("state").toString());
//                inputAddress.setText(jsonObject.getString("address_line_1").toString());
//                MobileNo.setText(String.valueOf(PhoneNo));
//                DOB.setText(jsonObject.getString("dob"));
//                Gender.setText(jsonObject.getString("gender"));
//                ConnectionType.setText(jsonObject.getString("conn_type"));
//                Operator.setText(jsonObject.getString("operator_name"));
//                Region.setText(jsonObject.getString("zipcode"));
//                res=PhoneNo;
                //Toast.makeText(getBaseContext()," resultString "+resultString.toString(),Toast.LENGTH_SHORT).show();
                // Toast.makeText(getBaseContext(),"PhoneNo "+PhoneNo,Toast.LENGTH_SHORT).show();
                //  Log.d("result_Json",res);
                //   Toast.makeText(getBaseContext(),"PhoneNo "+PhoneNo,Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Toast.makeText(getBaseContext(), String.valueOf(jresponse.getString("message")), Toast.LENGTH_LONG).show();
                if (jresponse.getString("error").equals("1")) {
                    Toast.makeText(getBaseContext(), String.valueOf(jresponse.getJSONArray("message")), Toast.LENGTH_LONG).show();
                    //  Login.valid = false;
                    // onLoginFailed();
                    Toast.makeText(getBaseContext(), "Unable to update  ", Toast.LENGTH_SHORT).show();
                    return;
                } else if (jresponse.getString("success").equals("1")) {
                    Toast.makeText(getBaseContext(), " successfully Updated...", Toast.LENGTH_SHORT).show();
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
    //    private boolean validateName() {
//        if (inputName.getText().toString().trim().isEmpty()) {
//            inputLayoutName.setError(" error");
//             requestFocus(inputName);
//            return false;
//        } else {
//            inputLayoutName.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//    private class MyTextWatcher implements TextWatcher {
//
//        private View view;
//
//        private MyTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//                case R.id.input_name:
//                  //  validateName();
//                    break;
//                case R.id.input_gender:
//                  //  validateEmail();
//                    break;
//                case R.id.input_dob:
//                   // validatePassword();
//                    break;
//            }
//        }
//    }
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                inputDOB.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        finish();
        // startActivity(new Intent(getBaseContext(), MainActivity.class));
    }
    public void getLat_Long() {
        // create class object
        gps = new GPSTracker(EditProfile.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            String latitude = String.valueOf(gps.getLatitude());
            String longitude = String.valueOf(gps.getLongitude());
            GetLocation getLocation = new GetLocation();
            getLocation.execute("true", latitude, longitude);
            inputAddress.setText(GetLocation.location_string);
            // \n is for new line
            //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    public class LoadOperators extends AsyncTask {
        @Override
        protected void onPreExecute() {
            Showprogressdialog();
            //  super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            String Connection_Type = String.valueOf(args[0]);
            //  String password = String.valueOf(args[1]);
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Connection_Type", Connection_Type));
            // params.add(new BasicNameValuePair("password", email));
            //    params.add(new BasicNameValuePair("password", password));
            // params.add(new BasicNameValuePair("phone", MobileNo));
            /////////////////// params.add(new BasicNameValuePair("ip_address", ipAddress));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONObject json = jsonParser.makeHttpRequest(Config.LOADOPERATORS_URL + Connection_Type, "GET", params);
            // Log.d("jobject", "Do in Background: "+json);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jresponse = (JSONObject) o;
            JSONArray jsonArray = null;
            String resultString = null;
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (o != null) {
                JSONObject jobject = null;
                try {
                    if (jresponse.getString("error").equals("1")) {//Toast.makeText(getBaseContext(),"normal: "+String.valueOf(valid),Toast.LENGTH_LONG).show();
                        return;
                    } else if (jresponse.getString("success").equals("1")) {
                        try {
                            jobject = new JSONObject(String.valueOf(o));
                            //  Log.d("jobject", "onPostExecute: "+jobject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject jobj = null;
                        try {
                            jsonArray = jobject.getJSONArray("result");
                            // Toast.makeText(getContext(),"jsonArray: "+jsonArray,Toast.LENGTH_SHORT).show();
                            //  Toast.makeText(getContext(),"jobject: "+jobject,Toast.LENGTH_SHORT).show();
                            spinnerArray_Oprators = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jobj = jsonArray.getJSONObject(i);
                                spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
                                spinnerMap_Opearator2.put(jobj.getString("key"), jobj.getString("value"));
                                spinnerArray_Oprators[i] = jobj.getString("value");
                                Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);
                                Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                            }
                            ArrayAdapter<String> adp_Operators1 = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                            // adp_Regions.notifyDataSetChanged();
                            sp.setAdapter(adp_Operators1);
                            OperatorID = spinnerMap_Opearators.get(sp.getSelectedItem().toString());
                            // Toast.makeText(getBaseContext(),"Region KEy Value: "+  sp.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                            //     int spinnerPosition = adp_Operators.getPosition(spinnerMap_Opearator2.get(getRegionKEY(inputOperator.getText().toString())));
                            int spinnerPosition = adp_Operators1.getPosition(spinnerMap_Opearator2.get(getOperatorKEY(inputOperator.getText().toString(), OperatorType)));
                            Log.d(TAG, "spinnerPosition: " + spinnerPosition);
                            sp.setSelection(spinnerPosition);
                            // Toast.makeText(getBaseContext(),"Region ID Value: "+ OperatorID,Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "OperatorID: " + OperatorID);
                            Log.d(TAG, "inputOperator: " + inputOperator.getText().toString());
                            Log.d(TAG, "OperatorType: " + OperatorType);
                            Log.d(TAG, "getOperatorKEY: " + getOperatorKEY(inputOperator.getText().toString(), OperatorType));
                            Log.d(TAG, "getOperatorKEY: " + getOperatorKEY(inputOperator.getText().toString(), OperatorType));
//                            int spinnerPosition = adp_Operators.getPosition(spinnerMap_Opearators1.get(getOperatorKEY(inputOperator.getText().toString(),OperatorType)));
                            Log.d(TAG, "spinnerPosition: " + spinnerPosition);
                            Log.d(TAG, "spinnerArray_Oprators[spinnerPosition]: " + spinnerArray_Oprators);
                            //   sp.setSelection(8);
                            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    OperatorID = spinnerMap_Opearators.get(sp.getSelectedItem().toString());
                                    //   Log.d("Spinner", "onPostExecute: "+sp.getSelectedItem());
                                    OperatorIdNo = HomeScreenFragment.getOperatorID(OperatorID, OperatorType);
                                    //  manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                                    // Toast.makeText(getBaseContext(),"Region ID Value: "+ OperatorID,Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            //   Toast.makeText(getContext(),jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
            super.onPostExecute(o);
        }
    }
    public class LoadRegion extends AsyncTask {
        //  ProgressDialog loading3;
        @Override
        protected void onPreExecute() {
            //  loading3 = ProgressDialog.show(getBaseContext(), "Fetching Data","Please Wait...",true,true);
            // super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            String identity = String.valueOf(args[0]);
            //  String password = String.valueOf(args[1]);
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("identity", identity));
            JSONObject json = jsonParser.makeHttpRequest(Config.LOADREGION_URL, "GET", params);
            // Log.d("jobject", "Do in Background: "+json);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jresponse = (JSONObject) o;
            JSONArray jsonArray = null;
            String resultString = null;
            // loading3.dismiss();
            if (o != null) {
                JSONObject jobject = null;
                try {
                    if (jresponse.getString("error").equals("1")) {
                        return;
                    } else if (jresponse.getString("success").equals("1")) {
                        JSONObject jobj = null;
                        try {
                            jsonArray = jresponse.getJSONArray("result");
                            spinnerArray_Region = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jobj = jsonArray.getJSONObject(i);
                                spinnerMap_Region.put(jobj.getString("value"), jobj.getString("key"));
                                spinnerArray_Region[i] = jobj.getString("value");
                                Log.d("JsonObject", "onPostExecute: " + spinnerArray_Region[i]);
                            }
                            ArrayAdapter<String> adp_Regions = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            sp_region.setAdapter(adp_Regions);
                            RegionID = spinnerMap_Region.get(sp_region.getSelectedItem().toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jobj = jsonArray.getJSONObject(i);
                                spinnerMap_Region1.put(jobj.getString("key"), jobj.getString("value"));
                            }
                            //    Toast.makeText(getBaseContext(),"Region KEy Value: "+ sp_region.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                            //  int spinnerPosition = adp_Regions.getPosition(spinnerMap_Region1.get(getRegionKEY(inputState.getText().toString())));
                            //   sp_region.setSelection(spinnerPosition);
                            sp_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(sp_region.getSelectedItem().toString());
                                    //     Toast.makeText(getBaseContext(),"Region ID: "+RegionID,Toast.LENGTH_SHORT).show();
                                    RegionIdNo = HomeScreenFragment.getRegionID(RegionID);
                                    inputState.setText(RegionIdNo);
                                    //  manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            // String Region_name = sp_state.getSelectedItem().toString();
                            //  RegionID= spinnerMap.get(Region_name).toString();
                            //    Log.d("jsonArray Length", "onPostExecute: "+jsonArray.length());
                            //  Log.d("jsonArray", "onPostExecute: "+jsonArray);
                            //  jobj = jobject.getJSONObject("login");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            //   Toast.makeText(getContext(),jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
            super.onPostExecute(o);
        }
    }
    public static String getRegionKEY(String Key) {
        String region_id = "1";
        switch (Key) {
            case "1":
                region_id = "TN";
                break;
            case "2":
                region_id = "AP";
                break;
            case "3":
                region_id = "AS";
                break;
            case "4":
                region_id = "BR";
                break;
            case "5":
                region_id = "DL";
                break;
            case "6":
                region_id = "GJ";
                break;
            case "7":
                region_id = "HR";
                break;
            case "8":
                region_id = "HP";
                break;
            case "9":
                region_id = "JK";
                break;
            case "10":
                region_id = "KA";
                break;
            case "11":
                region_id = "KL";
                break;
            case "12":
                region_id = "KO";
                break;
            case "13":
                region_id = "MH";
                break;
            case "14":
                region_id = "MP";
                break;
            case "15":
                region_id = "MU";
                break;
            case "16":
                region_id = "NE";
                break;
            case "17":
                region_id = "OR";
                break;
            case "18":
                region_id = "PB";
                break;
            case "19":
                region_id = "RJ";
                break;
            case "20":
                region_id = "UE";
                break;
            case "21":
                region_id = "UW";
                break;
            case "22":
                region_id = "WB";
                break;
            case "23":
                region_id = "CH";
                break;
            default:
                throw new IllegalArgumentException("Invalid Key: " + region_id);
        }
        return region_id;
    }
    public static String getOperatorKEY(String id, String Type) {
        String operator_id = "";
        switch (Type) {
            case "prepaid":
                switch (id) {
                    case "0":
                        operator_id = "VSP";
                        break;
                    case "1":
                        operator_id = "ATP";
                        break;
                    case "2":
                        operator_id = "MMP";
                        break;
                    case "3":
                        operator_id = "RGP";
                        break;
                    case "4":
                        operator_id = "VFP";
                        break;
                    case "5":
                        operator_id = "ACP";
                        break;
                    case "6":
                        operator_id = "BGP";
                        break;
                    case "7":
                        operator_id = "IDP";
                        break;
                    case "10":
                        operator_id = "MTP";
                        break;
                    case "12":
                        operator_id = "TCP";
                        break;
                    case "13":
                        operator_id = "TGP";
                        break;
                    case "15":
                        operator_id = "VGP";
                        break;
                    case "28":
                        operator_id = "VGP";
                        break;
                }
                break;
            case "postpaid":
                switch (id) {
                    case "1":
                        operator_id = "ATC";
                        break;
                    case "3":
                        operator_id = "RGC";
                        break;
                    case "4":
                        operator_id = "VFC";
                        break;
                    case "5":
                        operator_id = "ACC";
                        break;
                    case "7":
                        operator_id = "IDC";
                        break;
                    case "6":
                        operator_id = "BGC";
                        break;
                    case "13":
                        operator_id = "TDC";
                        break;
                    case "10":
                        operator_id = "MTC";
                        break;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid Key: " + operator_id);
        }
        return operator_id;
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
    public void Showprogressdialog() {
        progressDialog = new ProgressDialog(EditProfile.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("pls wait...");
        progressDialog.show();
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
                inputName.setText(String.valueOf(jsonObject.getString("first_name")));
                String PhoneNo = jsonObject.getString("phone");
                inputMobileNo.setText(String.valueOf(PhoneNo));
                inputDOB.setText(jsonObject.getString("dob"));
                // Gender.setText(jsonObject.getString("gender"));
                if (jsonObject.getString("conn_type").equals("")) {
                    //  ConnectionType.setText("prepaid");
                } else {
                    // ConnectionType.setText(jsonObject.getString("conn_type"));
                }
                if (jsonObject.getString("operator_name").equals("0")) {
                    RechargeNow.ProfileDetail.Operator_id_no = "1";
                } else {
                    RechargeNow.ProfileDetail.Operator_id_no = jsonObject.getString("operator_name");
                }
                // String  Operator_name_string=Config.getOperatorName(EditProfile.getOperatorKEY(Operator_id_no,ConnectionType.getText().toString()),ConnectionType.getText().toString());
                //  inputOperator.setText(Operator_name_string);
                // Region.setText(jsonObject.getString("state"));
                String Address = jsonObject.getString("address_line_1");
                String State = jsonObject.getString("zipcode");
                String Ocupation = jsonObject.getString("occupation");
                //Address =jsonObject.getString("zipcode");
                String res = PhoneNo;
                //  Toast.makeText(getBaseContext(),"j resultString "+resultString.toString(),Toast.LENGTH_SHORT).show();
                // Toast.makeText(getBaseContext(),"PhoneNo "+PhoneNo,Toast.LENGTH_SHORT).show();
                Log.d("result_Json", res);
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
}

package com.katkada.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.Fragment.HomeScreenFragment;
import com.katkada.Other.AndyConstants;
import com.katkada.Other.AndyUtils;
import com.katkada.Other.Config;
import com.katkada.Other.CouponValidationGlobal;
import com.katkada.Other.Get_Region_Operators_Browse_Plan;
import com.katkada.Other.Get_Region_Operators_Plan;
import com.katkada.Other.JSONParser;
import com.katkada.Other.LoadWalletValue;
import com.katkada.Other.MyFontTextView;
import com.katkada.Other.PaymentThroughCoupon;
import com.katkada.Other.PreferenceHelper;
import com.katkada.Other.Printlog;
import com.katkada.Other.SessionManager;
import com.katkada.R;
import com.katkada.parse.AsyncTaskCompleteListener;
import com.katkada.parse.HttpRequester;
import com.katkada.parse.ParseContent;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
public class RechargeNow extends AppCompatActivity implements AsyncTaskCompleteListener {
    Fragment fragment = null;
    String screenName, DataConnectionType = "3g", TotalSTD_Loc_Calls = "100", TotalSTD_Loc_SMS = "100", Total_Data = "100";
    TextView tv_coupon, tv_Applycoupon;
    TextInputLayout textInputLayout_Coupon;
    public SeekBar bar;
    SessionManager manager;
    ProgressDialog progressDialog;
    int selectedtypeId;
    String Rgnid_converter, optid_converter;
    public static String OperatorType = "prepaid", OperatorIdNo = "1", RegionIdNo = "1";
    public String OperatorId = "1", CouponCode;
    private RadioGroup radioGroup1;
    public static boolean isCoupon = false;
    private RadioButton radiotypeButton;
    EditText et_MobileNo;
    static EditText et_value;
    EditText et_Couponcode;
    Spinner sp, sp_region;
    InputStream is = null;
    String result = null;
    String line = null;
    Button btn_Rechrge;
    TextView Browse_Packs;
    private final String TAG = Register.class.getSimpleName();
    private final int REQUEST_CODE_PICK_CONTACTS = 1;
    AlertDialog dialog;
    Toast toast;
    public String OperatorID, RegionID;
    public List<String> list, Regions, Operators;
    public String[] spinnerArray_Oprators;
    public String[] spinnerArray_Oprators_Postpaid;
    public final HashMap<String, String> spinnerMap_Opearators = new HashMap<String, String>();
    public final HashMap<String, String> spinnerMap_Opearators_Postpaid = new HashMap<String, String>();
    public final HashMap<String, String> spinnerMap_Opearators1 = new HashMap<String, String>();
    //  TalkTimeFragment.DisplayPlans displayPlans;
    ArrayAdapter<String> adp_Regions;
    JSONParser jsonParser = new JSONParser();
    Intent i = getIntent();
    public String[] spinnerArray_Region;
    ArrayAdapter<String> adp_Operators;
    public final HashMap<String, String> spinnerMap_Region = new HashMap<String, String>();
    public final HashMap<String, String> spinnerMap_Region1 = new HashMap<String, String>();
    String selectedAmount = "";
     public Dialog loadingDialog;
    public ParseContent parseContent;
    private PreferenceHelper preferenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recharge);
        sp = (Spinner) findViewById(R.id.spinner_operator);
        sp_region = (Spinner) findViewById(R.id.spinner_Region);
        manager = new SessionManager();
        preferenceHelper = new PreferenceHelper(RechargeNow.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedAmount = extras.getString("Selected_Value");
        } else {
            selectedAmount = "";
        }
        if (extras.containsKey("SelectedOperator")) {
            OperatorType = extras.getString("SelectedOperator");
        }
        if (Config.getConnectivityStatus(RechargeNow.this)) {
            if (Config.isOnline()) {
                new Get_Region_Operators_Browse_Plan().execute("");

            } else {
                Toast.makeText(RechargeNow.this, "No Internet Detected", Toast.LENGTH_SHORT).show();
            }
        } else {
            showalert();
        }
        if (!Config.isNetworkAvailable(RechargeNow.this)) {
            showalert();
            return;
        } else {
            if(Get_Region_Operators_Browse_Plan.spinnerArray_Oprators==null)
            {
                new loadRegion_and_operatior().execute();
            }
        /*    int i=Get_Region_Operators_Browse_Plan.spinnerArray_Oprators.length;
         if(i < 1){
           new  loadRegion_and_operatior().execute();
         } */  else {
                adp_Operators = new ArrayAdapter<String>(RechargeNow.this, R.layout.custom_textview_to_spinner, Get_Region_Operators_Browse_Plan.spinnerArray_Oprators);
                // adp_Regions.notifyDataSetChanged();
                sp.setAdapter(adp_Operators);
                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        OperatorID = Get_Region_Operators_Browse_Plan.spinnerMap_Opearators.get(sp.getSelectedItem().toString());
                        preferenceHelper.putOperatorId(OperatorID);
                        // Config.OperatorIdno = OperatorID;
                        // operatorID_BrowsePlans = OperatorID;
                        Toast.makeText(RechargeNow.this,"id new = "+OperatorID,Toast.LENGTH_SHORT).show();
                        //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                        // manager.setPreferences(RechargeNow.this, "Shared_OptName", OperatorIdNo);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                adp_Regions = new ArrayAdapter<String>(RechargeNow.this, R.layout.custom_textview_to_spinner, Get_Region_Operators_Browse_Plan.spinnerArray_Region);
                // adp_Regions.notifyDataSetChanged();
                sp_region.setAdapter(adp_Regions);
                sp_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        RegionID = Get_Region_Operators_Browse_Plan.spinnerMap_Region.get(sp_region.getSelectedItem().toString());
                        preferenceHelper.putRegionId(RegionID);
                        //  Config.Regionidno = RegionID;
                        // regionID_BrowsePlans = RegionID;
                        Toast.makeText(RechargeNow.this,"Region id new "+RegionID,Toast.LENGTH_SHORT).show();
                        //  manager.setPreferences(RechargeNow.this, "RegionIdNo = ", RegionIdNo);
                        // RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                        // manager.setPreferences(RechargeNow.this, "Shared_RgName", RegionIdNo);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

         }

            //new Get_Region_Operators_Browse_Plan().execute("");


            // LoadRegion1 loadRegion = new LoadRegion1();
            //loadRegion.execute("regin");
            // LoadOperators1 loadOperators = new LoadOperators1();
            // loadOperators.execute(OperatorType);
        }
        LoadWalletValue loadWalletValue1 = new LoadWalletValue(getBaseContext());
        loadWalletValue1.execute(preferenceHelper.getUserId());
        recharge(selectedAmount);
    }
    public void recharge(String selectedAmount2) {
        radioGroup1 = (RadioGroup) findViewById(R.id.radiotype1);
        // TextView Datacard = (TextView)rootView.findViewById(R.id.tv_Datacard);
        Browse_Packs = (TextView) findViewById(R.id.tv_Browse_Packs);
        btn_Rechrge = (Button) findViewById(R.id.button_rechrge);
        et_MobileNo = (EditText) findViewById(R.id.editText_MobileNo);
        et_value = (EditText) findViewById(R.id.editValue);
        tv_coupon = (TextView) findViewById(R.id.textViewcoupon);
        tv_Applycoupon = (TextView) findViewById(R.id.textViewApplycoupon);
        textInputLayout_Coupon = (TextInputLayout) findViewById(R.id.input_layout_Coupon);
        et_Couponcode = (EditText) findViewById(R.id.editText_CouponCode);

        radioGroup1.requestFocus();

        et_MobileNo.addTextChangedListener(mTextEditorWatcher);
        et_value.setText(selectedAmount2);
        // et_MobileNo.setText(Login.phoneNumber);
        if (!OperatorType.equals("prepaid")) {
            radioGroup1.check(R.id.radio_postpaid);
        } else {
            radioGroup1.check(R.id.radio_prepaid);
        }
        selectedtypeId = radioGroup1.getCheckedRadioButtonId();
        radiotypeButton = (RadioButton) findViewById(selectedtypeId);
        OperatorType = radiotypeButton.getText().toString().toLowerCase();

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_prepaid:
                        Browse_Packs.setVisibility(View.VISIBLE);
                        btn_Rechrge.setText("Recharge");
                        OperatorType = "prepaid";
                    //    new LoadOperators().execute(OperatorType);
                        if (et_MobileNo.getText().toString().length() == 10) {
                            if (Patterns.PHONE.matcher(et_MobileNo.getText().toString()).matches()) {
                              //  GetRegionandOperator(et_MobileNo.getText().toString().toString());
                            } else {
                                Toast.makeText(getBaseContext(), "Invalid Mobile", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case R.id.radio_postpaid:
                        Browse_Packs.setVisibility(View.GONE);
                        btn_Rechrge.setText("Pay Bill");
                        OperatorType = "postpaid";

                        if (et_MobileNo.getText().toString().length() == 10) {
                            if (Patterns.PHONE.matcher(et_MobileNo.getText().toString()).matches()) {

                            } else {
                                Toast.makeText(getBaseContext(), "Invalid Mobile", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        textInputLayout_Coupon.setVisibility(View.GONE);
        tv_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputLayout_Coupon.getVisibility() == View.GONE) {
                    textInputLayout_Coupon.setVisibility(View.VISIBLE);
                } else {
                    textInputLayout_Coupon.setVisibility(View.GONE);
                }
            }
        });
        tv_Applycoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputLayout_Coupon.getVisibility() == View.VISIBLE) {
                    CouponCode = et_Couponcode.getText().toString();
                    try {
                        new CouponValidationGlobal(RechargeNow.this).execute(OperatorType, RegionID, OperatorID, CouponCode, et_value.getText().toString(), et_MobileNo.getText().toString());
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), "Exception on Coupon Validation " + ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btn_Rechrge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     if (et_MobileNo.getText().toString().isEmpty() || et_value.getText().toString().isEmpty()) {
                        if (et_MobileNo.getText().toString().isEmpty()) {
                            et_MobileNo.setError("Enter Mobile Number");
                            return;
                        } else {
                            et_value.setError("Enter Amount");
                            return;
                        }
                    } else {
                      ///  LoadWalletValue loadWalletValue1 = new LoadWalletValue(getBaseContext());
                       /// loadWalletValue1.execute(preferenceHelper.getUserId());
                        if (textInputLayout_Coupon.getVisibility() == View.VISIBLE) {
                            CouponCode = et_Couponcode.getText().toString();
                            try {
                                isCoupon = true;
                                //   new CouponValidationGlobal(RechargeNow.this).execute(OperatorType, RegionID, OperatorID, CouponCode, et_value.getText().toString(),et_MobileNo.getText().toString());
                                if (Double.parseDouble(CouponValidationGlobal.Copon_Amount) >= Double.parseDouble(et_value.getText().toString())) {
                                    //  paymentThroughCoupon.execute("1",CouponCode,et_MobileNo.getText().toString(),OperatorType,RegionID,OperatorID,"0","0","0",et_value.getText().toString());
                                    PaymentThroughCoupon paymentThroughCoupon = new PaymentThroughCoupon(RechargeNow.this);
                                    paymentThroughCoupon.execute("1",
                                            CouponCode,
                                            et_value.getText().toString(),
                                            et_MobileNo.getText().toString(),
                                            OperatorID,
                                            OperatorType, // connection_type
                                            RegionID,
                                            CouponValidationGlobal.Copon_Amount,
                                            LoadWalletValue.WalletAmount);
                                } else if (Double.parseDouble(CouponValidationGlobal.Copon_Amount) < Double.parseDouble(et_value.getText().toString())) {
                                    double remain_BAL_from_Coupon = (Double.parseDouble(et_value.getText().toString()) - Double.parseDouble(CouponValidationGlobal.Copon_Amount));
                                   // LoadWalletValue loadWalletValue = new LoadWalletValue(RechargeNow.this);
                                   // loadWalletValue.execute(Login.userID);
                                    Log.d("remain_BAL_from_Coupon", "onClick: RegionID: " + RegionID);
                                     Intent i = new Intent(getBaseContext(), PaymentOption.class);
                                    i.putExtra("mobileNo", et_MobileNo.getText().toString());
                                    i.putExtra("amount", et_value.getText().toString());
                                    i.putExtra("coupounCode", CouponCode);
                                    i.putExtra("remain_BAL_from_Coupon", String.valueOf(remain_BAL_from_Coupon));
                                    i.putExtra("Coupoun", CouponValidationGlobal.Copon_Amount);
                                    i.putExtra("Region", RegionID);
                                    i.putExtra("Operator", OperatorID);
                                    i.putExtra("ConnectionType", OperatorType);
                                    Log.d("RegionID", "onClick: RegionID: " + RegionID);
                                    Log.d("OperatorID", "onClick: OperatorID: " + OperatorID);
                                    Log.d("OperatorType", "onClick: OperatorType: " + OperatorType);
                                    startActivity(i);
                                }
                            } catch (Exception ex) {
                                Toast.makeText(getBaseContext(), "Exception on Coupon Validation " + ex.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            isCoupon = false;
                           /// LoadWalletValue loadWalletValue = new LoadWalletValue(getBaseContext());
                          ///  loadWalletValue.execute(preferenceHelper.getUserId());
                            Intent i = new Intent(getBaseContext(), PaymentOption.class);
                            i.putExtra("mobileNo", et_MobileNo.getText().toString());
                            i.putExtra("amount", et_value.getText().toString());
                            i.putExtra("remain_BAL_from_Coupon", et_value.getText().toString());
                            i.putExtra("Coupoun", "0");
                            i.putExtra("Region", RegionID);
                            i.putExtra("Operator", OperatorID);
                            i.putExtra("ConnectionType", OperatorType);
                            Log.d("RegionID", "onClick: RegionID: " + RegionID);
                            Log.d("OperatorID", "onClick: OperatorID: " + OperatorID);
                            Log.d("OperatorType", "onClick: OperatorType: " + OperatorType);
                            startActivity(i);
                        }
                    }



            }
        });

        Browse_Packs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(getBaseContext(), "Retriving...", Toast.LENGTH_SHORT).show();
                if (Config.isNetworkAvailable(RechargeNow.this)) {
//                    Thread t = new Thread() {
               Config.is_rechare_screen_open="true";
//                        public void run() {
                    //sleep(200);
                    preferenceHelper.putOperation_method("2");
                    Log.d("BrowseButton", "run: " + "OperatorType: " + OperatorType + "\nRegionIdNo: " + RegionIdNo + "\nOperatorIdNo: " + OperatorIdNo + "\nDataConnectionType " + DataConnectionType + "\nTotalSTD_Loc_Calls " + TotalSTD_Loc_Calls + "\nTotal_Data " + Total_Data + "\nTotalSTD_Loc_SMS " + TotalSTD_Loc_SMS);
                    Intent i = new Intent(getBaseContext(), PrepaidMobilePacks.class);
                    startActivity(i);
                    //                        }
//                    };
//                    t.start();
                } else {
                    Toast.makeText(getBaseContext(), "Internet not Available", Toast.LENGTH_SHORT).show();
                    return;
                }







            }
        });
    }
    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {
            case AndyConstants.ServiceCode.LOADOPERATORS_URL:
                Printlog.Log(TAG, "checkrequeststatus Response :" + response);


                    ///////////////////////////////

                    if (response != null) {

                        if (parseContent.isSuccess(response)) {
                            try {





                                JSONArray jsonArray=(new JSONObject(response).getJSONArray("result"));
                                spinnerArray_Oprators = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj = jsonArray.getJSONObject(i);
                                    spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
                                    spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
                                    spinnerArray_Oprators[i] = jobj.getString("value");
                                    Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);
                                    Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                                }
                                adp_Operators = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                                // adp_Regions.notifyDataSetChanged();
                                sp.setAdapter(adp_Operators);


//
//                                JSONObject jresponse = (JSONObject) o;
//                                JSONObject jobject = null;
//                                try {
//                                    if (jresponse.getString("error").equals("1")) {//Toast.makeText(getBaseContext(),"normal: "+String.valueOf(valid),Toast.LENGTH_LONG).show();
//                                        return;
//                                    } else if (jresponse.getString("success").equals("1")) {
//                                        try {
//                                            jobject = new JSONObject(String.valueOf(o));
//                                            //  Log.d("jobject", "onPostExecute: "+jobject);
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                        JSONObject jobj = null;
//                                        try {
//                                            jsonArray = jobject.getJSONArray("result");
//                                            spinnerArray_Oprators = new String[jsonArray.length()];
//                                            for (int i = 0; i < jsonArray.length(); i++) {
//                                                jobj = jsonArray.getJSONObject(i);
//                                                spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
//                                                spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
//                                                spinnerArray_Oprators[i] = jobj.getString("value");
//                                                Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);
//                                                Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
//                                            }
//                                            adp_Operators = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
//                                            // adp_Regions.notifyDataSetChanged();
//                                            sp.setAdapter(adp_Operators);
//                                            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                                @Override
//                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                                    OperatorID = spinnerMap_Opearators.get(sp.getSelectedItem().toString());
//                                                    //   Log.d("Spinner", "onPostExecute: "+sp.getSelectedItem());
//                                                    OperatorIdNo = HomeScreenFragment.getOperatorID(OperatorID, OperatorType);
//                                                    manager.setPreferences(RechargeNow.this, "OperatorIdNo", OperatorIdNo);
//                                                    Config.OperatorIdno = OperatorIdNo;
//                                                    HomeScreenFragment.operatorID_BrowsePlans = OperatorIdNo;
//                                                    //  Toast.makeText(getBaseContext(),""+Config.OperatorIdno+"\n"+HomeScreenFragment.operatorID_BrowsePlans,Toast.LENGTH_SHORT).show();
//                                                    //  manager.setPreferences(RechargeNow.this, "Shared_OptName", OperatorIdNo);
//                                                }
//                                                @Override
//                                                public void onNothingSelected(AdapterView<?> parent) {
//                                                }
//                                            });
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//






                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }



                    //////////////////////////


                break;

            default:
                break;
        }



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            // retrieveContactName();
            //   retrieveContactNumber();
            // retrieveContactPhoto();
        } else if (requestCode == 2) {//double d=Double.parseDouble(data.getStringExtra("AMOUNT"));
            // int value= (int) d;
            String amount = data.getStringExtra("AMOUNT");
            et_value.setText(amount);
            //sp.setSelection(Integer.parseInt(data.getStringExtra("OPERATOR")));
        } else if (requestCode == 3) {//double d=Double.parseDouble(data.getStringExtra("AMOUNT"));
            //  int value= (int) d;
            String amount = data.getStringExtra("AMOUNT1");
            et_value.setText(amount);
            //sp.setSelection(Integer.parseInt(data.getStringExtra("OPERATOR")));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(i);
        finish();
        // overridePendingTransition(R.anim.push_out_right, R.anim.pull_in_left);
    }
    public void showProgress() {
        progressDialog = new ProgressDialog(getBaseContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wailt...");
        progressDialog.show();
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
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        public void afterTextChanged(Editable s) {
            if (s.length() == 10) {
                if (Patterns.PHONE.matcher(et_MobileNo.getText().toString()).matches()) {
                    GetRegionandOperator(s.toString());
                } else {
                    Toast.makeText(getBaseContext(), "Invalid Mobile", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    public void GetRegionandOperator(String Number) {
        LoadRegionandOperator loadRegionandOperator = new LoadRegionandOperator();
        loadRegionandOperator.execute(Number);

    }
    public class LoadRegionandOperator extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            String identity = String.valueOf(args[0]);
            //  String password = String.valueOf(args[1]);
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("securehash", "7a4a870320e6dc86847fdc356416e063943d430edf18f1f34fbf77d5e31e500a177c6c24b6ed4556f43fef788ec7669ae16ce3f59040287e53de3f3a23c43280"));
            params.add(new BasicNameValuePair("txnid", "abc"));
            params.add(new BasicNameValuePair("api_user_id", "EPj3bYVYYsA5NdLNjYCEnQCxMpL7gxV2YYRWxeebW4A3WWcr"));
            params.add(new BasicNameValuePair("search_type", "mobile"));
            JSONObject json = jsonParser.makeHttpRequest(Config.NUMBER_TO_OPT_RGN_URL + identity, "POST", params);
            // Log.d("jobject", "Do in Background: "+json);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jresponse = (JSONObject) o;
            JSONArray jsonArray = null;
            String resultString = null;
            if (o != null) {
                //  JSONObject jobject = null;
                //  Log.d("jresponse", "onPostExecute: " + jresponse);
                if (jresponse.has("o")) {
                    int spinnerPosition = 0;
                    try {
                        Rgnid_converter = jresponse.getString("r");
                        optid_converter = jresponse.getString("o");
                        int spinnerPosition1 = adp_Regions.getPosition(spinnerMap_Region1.get(EditProfile.getRegionKEY(Rgnid_converter)));
                        sp_region.setSelection(spinnerPosition1);
                        int spinnerPosition2 = adp_Operators.getPosition(spinnerMap_Opearators1.get(EditProfile.getOperatorKEY(optid_converter, OperatorType)));
                        sp.setSelection(spinnerPosition2);
                        //   Toast.makeText(getBaseContext(),"Operator "+EditProfile.getOperatorKEY(optid_converter,OperatorType),Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(getBaseContext(),"Operator1 "+adp_Operators.getPosition("IDP"),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            //   Toast.makeText(RechargeNow.this,jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
            super.onPostExecute(o);
        }
    }
    /**
     * Created by prakash on 28-12-2016.
     */
    public static class Recharge {
    }
    public static class ProfileDetail extends AppCompatActivity {
        LocationManager location_manager;
        JSONParser jsonParser = new JSONParser();
        ImageView img_edit_personal;
        public static TextView Username, Gender, DOB, MobileNo, Operator, ConnectionType, Region;
        public static String Address, State, Ocupation, Operator_id_no, Operator_name_string, Region_id_no, Region_id_name;
        String res, PhoneNo;
        public String ProgileUserName;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile_detail);
            Username = (TextView) findViewById(R.id.tv_name);
            MobileNo = (TextView) findViewById(R.id.tv_mobileno);
            DOB = (TextView) findViewById(R.id.tv_dob);
            Gender = (TextView) findViewById(R.id.tv_gender);
            Operator = (TextView) findViewById(R.id.tv_operator);
            ConnectionType = (TextView) findViewById(R.id.tv_connection_type);
            Region = (TextView) findViewById(R.id.tv_region);
            img_edit_personal = (ImageView) findViewById(R.id.imageView_editpersonal);
            GetProfileDetails getProfileDetails = new GetProfileDetails();
            getProfileDetails.execute(Login.userID.toString());
            //  Toast.makeText(getBaseContext(),res.toString(),Toast.LENGTH_SHORT).show();
            // new GetProfileDetails().execute(Login.user_id);
            img_edit_personal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //  LocationListener listner = new MyLocationListner();
                    Intent intent = new Intent(getBaseContext(), EditProfile.class);
                    intent.putExtra("IS_UPDATED", "1");
                    startActivity(intent);
                    //startActivity(new Intent(ProfileDetail.this, EditProfile.class));
                    finish();
                }
            });
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
                    Username.setText(String.valueOf(jsonObject.getString("first_name")));
                    ProgileUserName = String.valueOf(jsonObject.getString("first_name"));
                    PhoneNo = jsonObject.getString("phone");
                    MobileNo.setText(String.valueOf(PhoneNo));
                    DOB.setText(jsonObject.getString("dob"));
                    Gender.setText(jsonObject.getString("gender"));
                    if (jsonObject.getString("conn_type").equals("")) {
                        ConnectionType.setText("prepaid");
                    } else {
                        ConnectionType.setText(jsonObject.getString("conn_type"));
                    }
                    if (jsonObject.getString("operator_name").equals("0")) {
                        Operator_id_no = "1";
                    } else {
                        Operator_id_no = jsonObject.getString("operator_name");
                    }
                    Operator_name_string = Config.getOperatorName(EditProfile.getOperatorKEY(Operator_id_no, ConnectionType.getText().toString()), ConnectionType.getText().toString());
                    Operator.setText(Operator_name_string);
                    Region.setText(jsonObject.getString("state"));
                    Address = jsonObject.getString("address_line_1");
                    State = jsonObject.getString("zipcode");
                    Ocupation = jsonObject.getString("occupation");
                    //Address =jsonObject.getString("zipcode");
                    res = PhoneNo;
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
        @Override
        public void onBackPressed() {
            //  super.onBackPressed();
            finish();
            startActivity(new Intent(getBaseContext(), MainActivity.class));
        }
    }
    public static void setRechargeAmount(String amount)
    {
        et_value.setText(amount);
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

    public class loadRegion_and_operatior extends AsyncTask {
        ProgressDialog progressDialogRegion;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AndyUtils.openloadingDialog(RechargeNow.this);
//            progressDialogRegion = new ProgressDialog(PrepaidMobilePacks.this,
//                    R.style.AppTheme_Dark_Dialog);
//            progressDialogRegion.setIndeterminate(true);
//            progressDialogRegion.setMessage("Please wait...");
//            progressDialogRegion.show();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("getdate", ""));
            JSONObject json = jsonParser.makeHttpRequest(Config.GET_OPERATOR_REGION_BROWSE_PLAN, "POST", params);
            // Log.d("jobject", "Do in Background: "+json);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try {
                AndyUtils.closeloadingDialog();
                if(o!=null) {
                    JSONObject jsonObject = new JSONObject(String.valueOf(o));
                    JSONObject jsonRegion,jsonOperators;
                    JSONArray jsonArray_operators= new JSONArray();
                    JSONArray jsonArray_operators_postpaid= new JSONArray();
                    JSONArray jsonArray_region= new JSONArray();
                    if(jsonObject.getInt("success")==1)
                    {
                        if(jsonObject.has("data"))
                        {
                            // jsonObject.getJSONObject("data").getJSONObject("operators");
                            jsonArray_operators=   jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("prepaid_operator");
                            jsonArray_operators_postpaid=   jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("postpaid_operator");
                            //jsonArray
                            JSONObject jobj;

                            jsonArray_region= jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("location_list");


                            spinnerArray_Oprators = new String[jsonArray_operators.length()];
                            for (int i = 0; i < jsonArray_operators.length(); i++) {
                                jobj = jsonArray_operators.getJSONObject(i);
                                spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
                                //   spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
                                spinnerArray_Oprators[i] = jobj.getString("value");
                                Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);
                                Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                            }
                            spinnerArray_Oprators_Postpaid = new String[jsonArray_operators_postpaid.length()];
                            for (int i = 0; i < jsonArray_operators_postpaid.length(); i++) {
                                jobj = jsonArray_operators_postpaid.getJSONObject(i);
                                spinnerMap_Opearators_Postpaid.put(jobj.getString("value"), jobj.getString("key"));
                                //   spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
                                spinnerArray_Oprators_Postpaid[i] = jobj.getString("value");
                                Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators_Postpaid[i]);
                                Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                            }


                            spinnerArray_Region = new String[jsonArray_region.length()];
                            for (int i = 0; i < jsonArray_region.length(); i++) {
                                jobj = jsonArray_region.getJSONObject(i);
                                spinnerMap_Region.put(jobj.getString("value"), jobj.getString("key"));
                                //   spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
                                spinnerArray_Region[i] = jobj.getString("value");
                                Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Region[i]);
                                Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                            }

                            adp_Operators = new ArrayAdapter<String>(RechargeNow.this, R.layout.custom_textview_to_spinner, Get_Region_Operators_Browse_Plan.spinnerArray_Oprators);
                            // adp_Regions.notifyDataSetChanged();
                            sp.setAdapter(adp_Operators);
                            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    OperatorID = Get_Region_Operators_Browse_Plan.spinnerMap_Opearators.get(sp.getSelectedItem().toString());
                                    preferenceHelper.putOperatorId(OperatorID);
                                    // Config.OperatorIdno = OperatorID;
                                    // operatorID_BrowsePlans = OperatorID;
                                    Toast.makeText(RechargeNow.this,"id new xc= "+OperatorID,Toast.LENGTH_SHORT).show();
                                    //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                                    // manager.setPreferences(RechargeNow.this, "Shared_OptName", OperatorIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });


                            adp_Regions = new ArrayAdapter<String>(RechargeNow.this, R.layout.custom_textview_to_spinner, Get_Region_Operators_Browse_Plan.spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            sp_region.setAdapter(adp_Regions);
                            sp_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = Get_Region_Operators_Browse_Plan.spinnerMap_Region.get(sp_region.getSelectedItem().toString());
                                    preferenceHelper.putRegionId(RegionID);
                                    //  Config.Regionidno = RegionID;
                                    // regionID_BrowsePlans = RegionID;
                                    Toast.makeText(RechargeNow.this,"Region id new "+RegionID,Toast.LENGTH_SHORT).show();
                                    //  manager.setPreferences(RechargeNow.this, "RegionIdNo = ", RegionIdNo);
                                    // RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                                    // manager.setPreferences(RechargeNow.this, "Shared_RgName", RegionIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });


       /* jsonArray = jobject.getJSONArray("plan_operator_key");
        // Toast.makeText(getApplicationContext(),"jsonArray: "+jsonArray,Toast.LENGTH_SHORT).show();
        //  Toast.makeText(getApplicationContext(),"jobject: "+jobject,Toast.LENGTH_SHORT).show();
        spinnerArray_Oprators = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            jobj = jsonArray.getJSONObject(i);
            spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
            //   spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
            spinnerArray_Oprators[i] = jobj.getString("value");
            Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);
            Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
        }*/
                            Log.v("jsonArray_operators",""+jsonArray_operators.length());


                        }

                    }





                }






            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
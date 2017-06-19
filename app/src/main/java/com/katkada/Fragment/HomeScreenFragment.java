package com.katkada.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.katkada.Activity.CallDetails;
import com.katkada.Activity.DataDetails;
import com.katkada.Activity.EditProfile;
import com.katkada.Activity.Login;
import com.katkada.Activity.PaymentOption;
import com.katkada.Activity.PlanDetailsActivity;
import com.katkada.Activity.PostPaidMobilePacks;
import com.katkada.Activity.PrepaidMobilePacks;
import com.katkada.Activity.RechargeNow;
import com.katkada.Activity.Register;
import com.katkada.Activity.SMSDetails;
import com.katkada.Other.Config;
import com.katkada.Other.Get_Region_Operators_Plan;
import com.katkada.Other.JSONParser;
import com.katkada.Other.LoadWalletValue;
import com.katkada.Other.PreferenceHelper;
import com.katkada.Other.SessionManager;
import com.katkada.R;
import com.michael.easydialog.EasyDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
public class HomeScreenFragment extends Fragment {
    Fragment fragment = null;
    String screenName, DataConnectionType = "3G", TotalSTD_Loc_Calls = "100", TotalSTD_Loc_SMS = "100", Total_Data = "100", Oper;
    RelativeLayout rl_calls, rl_sms, rl_data, rl_roaming, rl_recommendation, rl_browse_packs, rl_recharge_now;
    TextView tv2g, tv3g, tv4g, floating_text_start, tv_coupon;
    TextInputLayout textInputLayout_Coupon;
    Spinner Search_Operators, Search_Region;
    public SeekBar bar, bar_data, bar_sms;
    public TextView textV, textV_Data, textV_sms;
    SessionManager manager;
    public static String CouponAmount = "0",storage_value="MB";
    int selectedtypeId;
    ProgressDialog progressDialog;

    public static String OperatorType = "prepaid", OperatorIdNo = "1", RegionIdNo = "1";
    //SegmentedRadioGroup segmentText;
    Toast mToast;
    ArrayAdapter<String> adp_Regions;
    ArrayAdapter<String> adp_Operators;
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

    private PreferenceHelper preferenceHelper;
    AlertDialog dialog;

    Toast toast;
    public static String OperatorID, RegionID, regionID_BrowsePlans = "CH", operatorID_BrowsePlans = "5";
    public static List<String> list, Regions, Operators;
    public String[] spinnerArray_Oprators;
    public final HashMap<String, String> spinnerMap_Opearators = new HashMap<String, String>();
    public final HashMap<String, String> spinnerMap_Opearators1 = new HashMap<String, String>();
    //  TalkTimeFragment.DisplayPlans displayPlans;
    JSONParser jsonParser = new JSONParser();
    public static String[] spinnerArray_Region;
    public final static HashMap<String, String> spinnerMap_Region = new HashMap<String, String>();
    public final static HashMap<String, String> spinnerMap_Region1 = new HashMap<String, String>();
   View actionB;
  //   FloatingActionsMenu menuMultipleActions;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3,floatingActionButton4;
    OkHttpClient client;
    MediaType JSON;
    public HomeScreenFragment() {
        // Required empty public constructor
    }
    public static HomeScreenFragment newInstance(String param1, String param2) {
        HomeScreenFragment fragment = new HomeScreenFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelper = new PreferenceHelper(getContext());
        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        manager = new SessionManager();
        final View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        rl_calls = (RelativeLayout) rootView.findViewById(R.id.layout_calls);
        rl_roaming = (RelativeLayout) rootView.findViewById(R.id.layout_Roaming);
        rl_sms = (RelativeLayout) rootView.findViewById(R.id.layout_sms);
        rl_data = (RelativeLayout) rootView.findViewById(R.id.layout_data);
        rl_browse_packs = (RelativeLayout) rootView.findViewById(R.id.layout_browse_packs);
        rl_recommendation = (RelativeLayout) rootView.findViewById(R.id.layout_recommded_plans);
        ProfileName = (TextView) rootView.findViewById(R.id.textView21);
        rl_recharge_now = (RelativeLayout) rootView.findViewById(R.id.layout_recharge_now);
    ImageView imageView=(ImageView)rootView.findViewById(R.id.imageView5);
      /*  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL("http://www.katkada.com/assets/public/images/calc_operator/operator_3.png");
            imageView.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }*/



        ProfileName.setText(Login.ProgileUserName);
        if (!Login.ProgileUserName.equals("null")) {
            ProfileName.setText(Login.ProgileUserName);
        } else {
            ProfileName.setText("Katkada");
        }
        rl_recharge_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Call_recharge();
            }
        });
        rl_calls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.getConnectivityStatus(getContext())) {
                    if (Config.isOnline()) {
                        startActivity(new Intent(getContext(), CallDetails.class));
                        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    } else {
                        Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showalert();
                }
            }
        });
        rl_roaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(getContext()," roaming ",Toast.LENGTH_SHORT).show();
            }
        });
        rl_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SMSDetails.class));
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        rl_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DataDetails.class));
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        rl_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Call_Remomendation();
                //  startActivity(new Intent(getContext(), PrepaidMobilePacks.class));
            }
        });
        rl_browse_packs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.getConnectivityStatus(getContext())) {
                    if (Config.isOnline()) {
                        Browseplans();
                    } else {
                        Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showalert();
                }

            }
        });
        materialDesignFAM = (FloatingActionMenu) rootView.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton)rootView. findViewById(R.id.fab_wallet);
        floatingActionButton1.setImageBitmap(textAsBitmap(preferenceHelper.getWallet(), 20, Color.GREEN));
        floatingActionButton2 = (FloatingActionButton)rootView. findViewById(R.id.fab_recharge);
        floatingActionButton3 = (FloatingActionButton)rootView. findViewById(R.id.fab_recommendation);
        floatingActionButton4 = (FloatingActionButton)rootView. findViewById(R.id.fab_search_plans);

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                // floatingActionButton3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_discount));
              //  Toast.makeText(getContext(),"test",Toast.LENGTH_SHORT).show();
                Call_recharge();
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                //floatingActionButton3.setImageResource(android.R.drawable.ic_media_pause);
Call_Remomendation();
            }
        });
        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
               // floatingActionButton2.setImageBitmap(textAsBitmap("OK", 40, Color.WHITE));
                if (Config.getConnectivityStatus(getContext())) {
                    if (Config.isOnline()) {
                        Browseplans();
                    } else {
                        Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showalert();
                }
            }
        });

        return rootView;
    }
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
    @SuppressLint("WrongViewCast")
    public void SearchPlans() {
        screenName = "SearchPlans";
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.search_plans, null, false);
        tv2g = (TextView) formElementsView.findViewById(R.id.tv_2g);
        tv3g = (TextView) formElementsView.findViewById(R.id.tv_3g);
        tv4g = (TextView) formElementsView.findViewById(R.id.tv_4g);
        bar = (SeekBar) formElementsView.findViewById(R.id.seekBar_std_local);
        bar_sms = (SeekBar) formElementsView.findViewById(R.id.seekBar_total_SMS);
        bar_data = (SeekBar) formElementsView.findViewById(R.id.seekBar_total_data);
        Search_Operators = (Spinner) formElementsView.findViewById(R.id.spinner3);
        Search_Region = (Spinner) formElementsView.findViewById(R.id.spinner);
        radioGroup_SP = (RadioGroup) formElementsView.findViewById(R.id.radioConnectiontype);
        textV = (TextView) formElementsView.findViewById(R.id.tv_progressvalue);
        textV_Data = (TextView) formElementsView.findViewById(R.id.textView48);
        textV_sms = (TextView) formElementsView.findViewById(R.id.tv_progressvalue1);
        btn_searchPlans = (Button) formElementsView.findViewById(R.id.button_searchplan);
        floating_text_start = (TextView) formElementsView.findViewById(R.id.textView35);
        alertDialog.setView(formElementsView);
        bar.setMax(4000);
        bar.setProgress(0);
        bar_sms.setMax(3100);
        bar_sms.setProgress(0);
        bar_data.setMax(10000);
        bar_data.setProgress(0);
        tv3g.setBackgroundResource(R.drawable.circle_blue_border);
        selectedtypeId = radioGroup_SP.getCheckedRadioButtonId();
        radiotypeButton_SP = (RadioButton) formElementsView.findViewById(selectedtypeId);
        OperatorType = radiotypeButton_SP.getText().toString().toLowerCase();
        //   bar.setThumb(getActivity().getResources().getDrawable(
        //       R.drawable.thump));
        tv2g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2g.setBackgroundResource(R.drawable.circle_blue_border);
                tv3g.setBackgroundResource(R.drawable.circle);
                tv4g.setBackgroundResource(R.drawable.circle);
                DataConnectionType = "2G";
                //   Toast.makeText(getContext(), "2g", Toast.LENGTH_SHORT).show();
                // alertDialog.dismiss();
            }
        });
        tv3g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2g.setBackgroundResource(R.drawable.circle);
                tv3g.setBackgroundResource(R.drawable.circle_blue_border);
                tv4g.setBackgroundResource(R.drawable.circle);
                DataConnectionType = "3G";
                //  Toast.makeText(getContext(), "3g", Toast.LENGTH_SHORT).show();
                // alertDialog.dismiss();
            }
        });
        tv4g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2g.setBackgroundResource(R.drawable.circle);
                tv3g.setBackgroundResource(R.drawable.circle);
                tv4g.setBackgroundResource(R.drawable.circle_blue_border);
                DataConnectionType = "4G";
                //  Toast.makeText(getContext(), "4g", Toast.LENGTH_SHORT).show();
                // alertDialog.dismiss();
            }
        });
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                TotalSTD_Loc_Calls = String.valueOf(progress);
                setText();
                textV.setText(String.valueOf(progress) + " Mins.");
                //   manager.setPreferences(getContext(), "Shared_TotalMinutes", String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        bar_data.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setText();

                if(progress>1024)

                {storage_value="GB";
                    double gb= Double.parseDouble(String.valueOf(progress))/1024;

                    Total_Data = String.valueOf( String.format("%.02f", gb));
                    textV_Data.setText(String.valueOf(  String.format("%.02f", gb))  + " GB.");
                } else {
                    storage_value="MB";
                    Total_Data = String.valueOf(progress);
                    textV_Data.setText(String.valueOf(progress) + " MB.");
                }

                // manager.setPreferences(getContext(), "Shared_TotalData", String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        bar_sms.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setText();
                textV_sms.setText(String.valueOf(progress) + " SMS.");
                TotalSTD_Loc_SMS = String.valueOf(progress);
                // manager.setPreferences(getContext(), "Shared_TotalSMS", String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        btn_searchPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.OperatorIdno = OperatorID;
                Config.Regionidno = RegionID;
                Config.OperatorType = OperatorType;
                // Toast.makeText(getContext(),"OperatorType: "+Config.OperatorType+"\nRegionIdNo: "+Config.Regionidno+"\nOperatorIdNo: "+Config.OperatorIdno+"\nDataConnectionType "+DataConnectionType+"\nTotalSTD_Loc_Calls "+TotalSTD_Loc_Calls+"\nTotal_Data "+Total_Data+"\nTotalSTD_Loc_SMS "+TotalSTD_Loc_SMS,Toast.LENGTH_SHORT).show();
                if (Config.getConnectivityStatus(getContext())) {
                    if (Config.isOnline()) {
                        Intent plandetail = new Intent(getContext(), PlanDetailsActivity.class);
                        plandetail.putExtra("connection", preferenceHelper.getOperatorType());
                        plandetail.putExtra("region", preferenceHelper.getRegionId());
                        plandetail.putExtra("operator", preferenceHelper.getOperatorId());

                        plandetail.putExtra("totalCall", TotalSTD_Loc_Calls);
                        double local = Double.parseDouble(TotalSTD_Loc_Calls)*0.81;
                        double std = Double.parseDouble(TotalSTD_Loc_Calls)*0.19;

                        plandetail.putExtra("totalSms", TotalSTD_Loc_SMS);

                        double localsms = Double.parseDouble(TotalSTD_Loc_SMS)*0.81;
                        double stdsms = Double.parseDouble(TotalSTD_Loc_SMS)*0.19;
                        plandetail.putExtra("dataType", DataConnectionType);
                        plandetail.putExtra("totalData", Total_Data);
                        plandetail.putExtra("storage_value", "");
                        plandetail.putExtra("totalLocalCall", String.valueOf(String.format("%.0f", local)));
                        plandetail.putExtra("totalStdCall", String.valueOf(String.format("%.0f", std)));
                        plandetail.putExtra("totalLocalSms", String.valueOf(String.format("%.0f", localsms)));
                        plandetail.putExtra("totalNatSms", String.valueOf(String.format("%.0f", stdsms)));
                        plandetail.putExtra("storage_value",storage_value);

                        startActivity(plandetail);
                        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        //startActivityForResult(plandetail, 2);

                    } else {
                        Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                    //  startActivity(new Intent(getContext(), PlanDetailsActivity.class));
                } else {
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                            getContext());
                    // set title
                    alertDialogBuilder.setTitle("Warning !");
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("pls Check Internet Connection!")
                            .setCancelable(false)
                            .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    getContext().startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            }
        });
        alertDialog.show();
        if (Config.getConnectivityStatus(getContext())) {
            if (Config.isOnline()) {
                new Get_Region_Operators_Plan().execute("");
                if(Get_Region_Operators_Plan.spinnerArray_Oprators==null)
                {
                    new  loadRegion_and_operatior().execute();
                }
        /*    int i=Get_Region_Operators_Plan.spinnerArray_Oprators.length;
         if(i < 1){
           new  loadRegion_and_operatior().execute();
         } */  else { adp_Operators = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, Get_Region_Operators_Plan.spinnerArray_Oprators);
                    // adp_Regions.notifyDataSetChanged();
                    Search_Operators.setAdapter(adp_Operators);
                    Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            OperatorID = Get_Region_Operators_Plan.spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                            preferenceHelper.putOperatorId(OperatorID);
                            // Config.OperatorIdno = OperatorID;
                            // operatorID_BrowsePlans = OperatorID;
                            Toast.makeText(getContext(),"id new = "+OperatorID,Toast.LENGTH_SHORT).show();
                            //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                            // manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


                    adp_Regions = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, Get_Region_Operators_Plan.spinnerArray_Region);
                    // adp_Regions.notifyDataSetChanged();
                    Search_Region.setAdapter(adp_Regions);
                    Search_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            RegionID = Get_Region_Operators_Plan.spinnerMap_Region.get(Search_Region.getSelectedItem().toString());
                            preferenceHelper.putRegionId(RegionID);
                            //  Config.Regionidno = RegionID;
                            // regionID_BrowsePlans = RegionID;
                            Toast.makeText(getContext(),"Region id new "+RegionID,Toast.LENGTH_SHORT).show();
                            //  manager.setPreferences(getContext(), "RegionIdNo = ", RegionIdNo);
                            // RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                            // manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                //LoadRegion1 loadRegion = new LoadRegion1();
               // loadRegion.execute("regin");
               // LoadOperators1 loadOperators = new LoadOperators1();
               // loadOperators.execute(OperatorType);
                //  LoadOperators2 loadOperators=new LoadOperators2();
                //loadOperators.execute(OperatorType);
            }
                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
            }
        } else {
            showalert();
        }
        radioGroup_SP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_prepaid:
                        OperatorType = "prepaid";
                        Config.OperatorType = OperatorType;
                        //  manager.setPreferences(getContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        if (Config.getConnectivityStatus(getContext())) {
                            if (Config.isOnline()) {
                               // LoadOperators1 loadOperators = new LoadOperators1();
                               // loadOperators.execute(OperatorType);
                            } else {
                                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showalert();
                        }
                        break;
                    case R.id.radio_postpaid:
                        OperatorType = "postpaid";
                        Config.OperatorType = OperatorType;
                        //  manager.setPreferences(getContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        if (Config.getConnectivityStatus(getContext())) {
                            if (Config.isOnline()) {
                             //   LoadOperators1 loadOperators1 = new LoadOperators1();
                               // loadOperators1.execute(OperatorType);
                            } else {
                                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
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
    }
    private void setText() {
        int progress = bar.getProgress();
        int max = bar.getMax();
        int offset = bar.getThumbOffset();
        float percent = ((float) progress) / (float) max;
        int width = bar.getWidth() - 2 * offset;
        int answer = ((int) (width * percent + offset + floating_text_start.getWidth() + 5));
        // textV.setX(answer);
    }
    public void Call_recharge()
    {

        if (Config.getConnectivityStatus(getContext())) {
            if (Config.isNetworkAvailable(getContext())) {
                Intent i = new Intent(getContext(), RechargeNow.class);
                i.putExtra("Selected_Value", "");
                ((Activity) getContext()).finish();
                startActivity(i);
                //  getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            } else {
                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
            }
//                    if(Config.isOnline()){
//                     Intent i= new Intent(getContext(), RechargeNow.class);
//                        i.putExtra("Selected_Value","");
//                        ((Activity)getContext()).finish();
//                        startActivity(i);
//                      //  Recharge();
//
//                    } else {   Toast.makeText(getContext(),"No Internet Detected",Toast.LENGTH_SHORT).show();}
        } else {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                    getContext());
            // set title
            alertDialogBuilder.setTitle("Warning !");
            // set dialog message
            alertDialogBuilder
                    .setMessage("pls Check Internet Connection!")
                    .setCancelable(false)
                    .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            getContext().startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
    }
    public void Call_Remomendation()
    {
        if (Config.getConnectivityStatus(getContext())) {
            if (Config.isOnline()) {
                SearchPlans();
                // startActivity(new Intent(getContext(), PlanDetailsActivity.class));
                // getActivity().overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
            } else {
                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
            }
        } else {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                    getContext());
            // set title
            alertDialogBuilder.setTitle("Warning !");
            // set dialog message
            alertDialogBuilder
                    .setMessage("pls Check Internet Connection!")
                    .setCancelable(false)
                    .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            getContext().startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
    }
    public void Recharge() {
        screenName = "Recharge";
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.fragment_recharge, null, false);
        radioGroup1 = (RadioGroup) rootView.findViewById(R.id.radiotype);
        // TextView Datacard = (TextView)rootView.findViewById(R.id.tv_Datacard);
        Browse_Packs = (TextView) rootView.findViewById(R.id.tv_Browse_Packs);
        btn_Rechrge = (Button) rootView.findViewById(R.id.button_rechrge);
        //loadContact = (ImageView)rootView.findViewById(R.id.image_contact);
        et_MobileNo = (EditText) rootView.findViewById(R.id.editText_MobileNo);
        et_value = (EditText) rootView.findViewById(R.id.editValue);
        tv_coupon = (TextView) rootView.findViewById(R.id.textViewcoupon);
        textInputLayout_Coupon = (TextInputLayout) rootView.findViewById(R.id.input_layout_Coupon);
        et_Couponcode = (EditText) rootView.findViewById(R.id.editText_CouponCode);
        sp = (Spinner) rootView.findViewById(R.id.spinner_operator);
        sp_region = (Spinner) rootView.findViewById(R.id.spinner_Region);
        alertDialog.setView(rootView);
        OperatorType = "prepaid";
        et_MobileNo.setText(Login.phoneNumber);
        selectedtypeId = radioGroup1.getCheckedRadioButtonId();
        radiotypeButton = (RadioButton) rootView.findViewById(selectedtypeId);
        OperatorType = radiotypeButton.getText().toString().toLowerCase();
//        LoadRegion loadRegion=new LoadRegion();
//        loadRegion.execute("regin");
//        OperatorType="prepaid";
//        LoadOperators loadOperators3=new LoadOperators();
//        loadOperators3.execute(OperatorType);
        alertDialog.show();
        if (Config.getConnectivityStatus(getContext())) {
            if (Config.isOnline()) {
                LoadOperators loadOperators3 = new LoadOperators();
                loadOperators3.execute(OperatorType);
                LoadRegion loadRegion = new LoadRegion();
                loadRegion.execute("regin");
                GetRegionandOperator(Login.phoneNumber);
            } else {
                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
            }
        } else {
            showalert();
        }
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_prepaid:
                        Browse_Packs.setVisibility(View.VISIBLE);
                        btn_Rechrge.setText("Recharge");
                        OperatorType = "prepaid";
                        //   Toast.makeText(getContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        if (Config.getConnectivityStatus(getContext())) {
                            if (Config.isOnline()) {
                                LoadOperators loadOperators = new LoadOperators();
                                loadOperators.execute(OperatorType);
                            } else {
                                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showalert();
                        }
                        break;
                    case R.id.radio_postpaid:
                        Browse_Packs.setVisibility(View.GONE);
                        btn_Rechrge.setText("Pay Bill");
                        OperatorType = "postpaid";
                        //   Toast.makeText(getContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        if (Config.getConnectivityStatus(getContext())) {
                            if (Config.isOnline()) {
                                LoadOperators loadOperators1 = new LoadOperators();
                                loadOperators1.execute(OperatorType);
                            } else {
                                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
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
        btn_Rechrge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_MobileNo.getText().toString().toString().isEmpty()) {
                    et_MobileNo.setError("Enter Mobile No");
                } else if (et_value.getText().toString().toString().isEmpty()) {
                    et_value.setError("Enter Recharge Amount");
                } else {
                    if (Config.getConnectivityStatus(getContext())) {
                        if (Config.isOnline()) {
                            if (textInputLayout_Coupon.getVisibility() == View.VISIBLE) {
                                CouponCode = et_Couponcode.getText().toString();
                                CouponValidation couponValidation = new CouponValidation();
                                couponValidation.execute(OperatorType, RegionID, OperatorID, CouponCode, et_value.getText().toString());
//                    LoadWalletValue loadWalletValue =new LoadWalletValue();
//                    loadWalletValue.execute(Login.userID);
//
//                    Intent i=new Intent(getContext(),PaymentOption.class);
//                    i.putExtra("mobileNo",et_MobileNo.getText().toString());
//                    i.putExtra("amount",et_value.getText().toString());
//                    startActivity(i);
                            }
                            LoadWalletValue loadWalletValue = new LoadWalletValue(getContext());
                            loadWalletValue.execute(Login.userID);
                            Intent i = new Intent(getContext(), PaymentOption.class);
                            i.putExtra("mobileNo", et_MobileNo.getText().toString());
                            i.putExtra("amount", et_value.getText().toString());
                            i.putExtra("Region", RegionID);
                            i.putExtra("Operator", OperatorID);
                            i.putExtra("ConnectionType", OperatorType);
                            Log.d("RegionID", "onClick: RegionID: " + RegionID);
                            Log.d("OperatorID", "onClick: OperatorID: " + OperatorID);
                            Log.d("OperatorType", "onClick: OperatorType: " + OperatorType);
                            startActivity(i);
//                final CharSequence PaymentMethod[] = new CharSequence[] {"Wallet", "CC Avenue"};
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Choose Payment Method");
//                builder.setItems(PaymentMethod, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // the user clicked on colors[which]
//                        switch(which)
//                        {  case 0:
//                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                            builder.setTitle("Wallet Value");
//                            builder.setMessage("Your Have \u20B9 0 in your Wallet" );
//                            builder.setNegativeButton("OK", null);
//                            AlertDialog dialog1 = builder.create();
//                            dialog1.show();
//
//                            break;
//                            case 1:
//                                  Intent i=new Intent(getContext(),InitialScreenActivity.class);
//                                   startActivity(i);
//                                break;
//                        }
//
//
//                    }
//                });
//                builder.show();
/*
                String Currency="INR";
                String merchant_id="123";
                String Access_COde="123";

                String redirectUrl="http://122.182.6.216/merchant/ccavResponseHandler.jsp";
                String cancelUrl="http://122.182.6.216/merchant/ccavResponseHandler.jsp";
                String rsaKeyUrl="http://122.182.6.216/merchant/GetRSA.jsp";
                String orderId="123";

             //   Intent i=new Intent(getContext(), InitialScreenActivity.class);
                //startActivity(i);
                //Mandatory parameters. Other parameters can be added if required.
                String vAccessCode = ServiceUtility.chkNull(Access_COde).toString().trim();
                String vMerchantId = ServiceUtility.chkNull(merchant_id).toString().trim();
                String vCurrency = ServiceUtility.chkNull(Currency).toString().trim();
                String vAmount = ServiceUtility.chkNull(et_value.getText()).toString().trim();
                if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
                    Intent intent = new Intent(getContext(),WebViewActivity.class);
                    intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(Access_COde).toString().trim());
                    intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchant_id).toString().trim());
                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId).toString().trim());
                    intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(Currency).toString().trim());
                    intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(et_value.getText()).toString().trim());

                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl).toString().trim());
                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl).toString().trim());
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl).toString().trim());

                    startActivity(intent);
                }else{
                    showToast("All parameters are mandatory.");
                }
                */
                            //  Intent i=new Intent(getContext(),InitialScreenActivity.class);
                            //  startActivity(i);
                        } else {
                            Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showalert();
                    }
                }
            }
            public void showToast(String msg) {
                //   Toast.makeText(getContext(), "Toast: " + msg, Toast.LENGTH_LONG).show();
            }
        });
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://127.0.0.1/spinner.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("Pass 1", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            // Toast.makeText(getContext(), "Invalid IP Address",
            //    Toast.LENGTH_LONG).show();
        }
        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("Pass 2", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 2", e.toString());
        }
        try {
            JSONArray JA = new JSONArray(result);
            JSONObject json = null;
            final String[] str1 = new String[JA.length()];
            final String[] str2 = new String[JA.length()];
            for (int i = 0; i < JA.length(); i++) {
                json = JA.getJSONObject(i);
                str1[i] = json.getString("name");
                str2[i] = json.getString("mobileno");
            }
            List<String> list1 = new ArrayList<String>();
            for (int i = 0; i < str2.length; i++) {
                list1.add(str2[i]);
            }
            Collections.sort(list1);
            ArrayAdapter<String> adp2 = new ArrayAdapter<String>
                    (getContext(), R.layout.custom_textview_to_spinner, list1);
            sp.setAdapter(adp2);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                    // TODO Auto-generated method stub
                    String item = sp.getSelectedItem().toString();
                    // Toast.makeText(getContext(), item,
                    //  Toast.LENGTH_LONG).show();
                    Log.e("Item", item);
                }
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
//        loadContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//// using native contacts selection
//                // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
//                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
//            }
//        });
        Browse_Packs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Retriving...", Toast.LENGTH_SHORT).show();
                Thread t = new Thread() {
                    public void run() {
                        try {
                            sleep(300);
                            Log.d("BrowseButton", "run: " + "OperatorType: " + OperatorType + "\nRegionIdNo: " + RegionIdNo + "\nOperatorIdNo: " + OperatorIdNo + "\nDataConnectionType " + DataConnectionType + "\nTotalSTD_Loc_Calls " + TotalSTD_Loc_Calls + "\nTotal_Data " + Total_Data + "\nTotalSTD_Loc_SMS " + TotalSTD_Loc_SMS);
                            // Toast.makeText(getContext(),"OperatorType: "+OperatorType+"\nRegionIdNo: "+RegionIdNo+"\nOperatorIdNo: "+OperatorIdNo+"\nDataConnectionType "+DataConnectionType+"\nTotalSTD_Loc_Calls "+TotalSTD_Loc_Calls+"\nTotal_Data "+Total_Data+"\nTotalSTD_Loc_SMS "+TotalSTD_Loc_SMS,Toast.LENGTH_SHORT).show();
                            //   Toast.makeText(getContext(),"\nRegionIdNo\nRegionIdNo",Toast.LENGTH_LONG).show();
                            //   Toast.makeText(getContext(), OperatorType ,Toast.LENGTH_LONG).show();
                            if (Config.getConnectivityStatus(getContext())) {
                                if (Config.isOnline()) {
                                    Intent i = new Intent(getContext(), PrepaidMobilePacks.class);
                                    startActivityForResult(i, 2);
                                    getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                } else {
                                    Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                showalert();
                            }
                            //finish();
                            //Intent intent=new Intent(getContext(),SecondActivity.class);
                            // startActivityForResult(intent, 2);/
                            // getActivity().overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                            //  getActivity().  overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();





                /*



                final StringBuilder sp=new StringBuilder(".");

                new CountDownTimer(5000, 1000) { //5 seconds
                    public void onTick(long millisUntilFinished) {
                        sp.append(".");
                       // Browse_Packs.setText("seconds remaining" + millisUntilFinished / 1000);
                        Toast.makeText(getContext(),"Retriving."+sp,Toast.LENGTH_SHORT).show();


                    }

                    public void onFinish() {
                        Intent i=new Intent(getContext(), PrepaidMobilePacks.class);
                        startActivity(i);
                      //  this.onFinish();
                    }

                }.start();
             */
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
           // uriContact = data.getData();
            // retrieveContactName();
            //   retrieveContactNumber();
            // retrieveContactPhoto();
        } else if (requestCode == 2) {
            String amount = data.getStringExtra("AMOUNT");
            et_value.setText(amount);
            //sp.setSelection(Integer.parseInt(data.getStringExtra("OPERATOR")));
        }
    }
//    private void retrieveContactPhoto() {
//
//        Bitmap photo = null;
//
//        try {
//            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
//                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
//
//            if (inputStream != null) {
//                photo = BitmapFactory.decodeStream(inputStream);
//                // ImageView imageView = (ImageView)  findViewById(R.id.img_contact);
//                loadContact.setImageBitmap(photo);
//            }
//
//            assert inputStream != null;
//            inputStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void retrieveContactNumber() {
//
//        String contactNumber = null;
//
//        // getting contacts ID
//        Cursor cursorID = getActivity().getContentResolver().query(uriContact,
//                new String[]{ContactsContract.Contacts._ID},
//                null, null, null);
//
//        if (cursorID.moveToFirst()) {
//
//            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
//        }
//
//        cursorID.close();
//
//        Log.d(TAG, "Contact ID: " + contactID);
//
//        // Using the contact ID now we will get contact phone number
//        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
//
//                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
//                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
//                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
//
//                new String[]{contactID},
//                null);
//
//        if (cursorPhone.moveToFirst()) {
//            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//        }
//
//        cursorPhone.close();
//
//        Log.d(TAG, "Contact Phone Number: " + contactNumber);
//        et_MobileNo.setText(contactNumber);
//    }
//
//    private void retrieveContactName() {
//
//        String contactName = null;
//
//        // querying contact data store
//        Cursor cursor = getActivity().getContentResolver().query(uriContact, null, null, null, null);
//
//        if (cursor.moveToFirst()) {
//
//            // DISPLAY_NAME = The display name for the contact.
//            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
//
//            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//        }
//
//        cursor.close();
//
//        Log.d(TAG, "Contact Name: " + contactName);
//
//    }
//
    public class LoadOperators extends AsyncTask {
        @Override
        protected void onPreExecute() {
            showProgress();
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
                                spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
                                spinnerArray_Oprators[i] = jobj.getString("value");
                                Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);
                                Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                            }
                            if (screenName.equals("Recharge")) {
                                ArrayAdapter<String> adp_Operators = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                                // adp_Regions.notifyDataSetChanged();
                                sp.setAdapter(adp_Operators);
                                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        OperatorID = spinnerMap_Opearators.get(sp.getSelectedItem().toString());
                                        //   Log.d("Spinner", "onPostExecute: "+sp.getSelectedItem());
                                        OperatorIdNo = getOperatorID(OperatorID, OperatorType);
                                        manager.setPreferences(getContext(), "OperatorIdNo", OperatorIdNo);
                                        //  manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            } else {
                                adp_Operators = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                                // adp_Regions.notifyDataSetChanged();
                                Search_Operators.setAdapter(adp_Operators);
                                Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        OperatorID = spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                                        OperatorIdNo = getOperatorID(OperatorID, OperatorType);
                                        // manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            }
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
        ProgressDialog progressDialogRegion;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogRegion = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialogRegion.setIndeterminate(true);
            progressDialogRegion.setMessage("Please wait...");
            progressDialogRegion.show();
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
            super.onPostExecute(o);
            JSONObject jresponse = (JSONObject) o;
            JSONArray jsonArray = null;
            String resultString = null;
            if (o != null) {
                JSONObject jobject = null;
                try {
                    if (jresponse.getString("error").equals("1")) {
                        return;
                    } else if (jresponse.getString("success").equals("1")) {
                        JSONObject jobj = null;
                        jsonArray = jresponse.getJSONArray("result");
                        spinnerArray_Region = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jobj = jsonArray.getJSONObject(i);
                            spinnerMap_Region.put(jobj.getString("value"), jobj.getString("key"));
                            spinnerMap_Region1.put(jobj.getString("key"), jobj.getString("value"));
                            spinnerArray_Region[i] = jobj.getString("value");
                            Log.d("JsonObject", "onPostExecute: " + spinnerArray_Region[i]);
                        }
                        if (screenName.equals("Recharge")) {
                            ArrayAdapter<String> adp_Regions = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            sp_region.setAdapter(adp_Regions);
                            sp_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(sp_region.getSelectedItem().toString());
                                    RegionIdNo = getRegionID(RegionID);
                                    manager.setPreferences(getContext(), "RegionIdNo", RegionIdNo);
                                    //  manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            adp_Regions = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            Search_Region.setAdapter(adp_Regions);
                            Search_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(Search_Region.getSelectedItem().toString());
                                    RegionIdNo = getRegionID(RegionID);
                                    // manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                        // String Region_name = sp_state.getSelectedItem().toString();
                        //  RegionID= spinnerMap.get(Region_name).toString();
                        //    Log.d("jsonArray Length", "onPostExecute: "+jsonArray.length());
                        //  Log.d("jsonArray", "onPostExecute: "+jsonArray);
                        //  jobj = jobject.getJSONObject("login");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (progressDialogRegion.isShowing()) {
                        progressDialogRegion.dismiss();
                    }
                }
            }
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            //   Toast.makeText(getContext(),jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
            super.onPostExecute(o);
        }
    }
    public class CouponValidation extends AsyncTask {
        // ProgressDialog loading1;
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("connection_type", String.valueOf(args[0])));
            params.add(new BasicNameValuePair("location_name", String.valueOf(args[1])));
            params.add(new BasicNameValuePair("operator_name", String.valueOf(args[2])));
            params.add(new BasicNameValuePair("coupon_code", String.valueOf(args[3])));
            params.add(new BasicNameValuePair("amount", String.valueOf(args[4])));
            JSONObject json = jsonParser.makeHttpRequest(Config.GET_USER_COUPON_URL, "POST", params);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jresponse = (JSONObject) o;
            // loading1.dismiss();
            JSONArray jsonArray = null;
            String resultString = null;
            if (o != null) {
                JSONObject jobject = null;
                try {
                    if (jresponse.getString("error").equals("1")) {
                        Toast.makeText(getContext(), "Unable to Process Your Coupon", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (jresponse.getString("success").equals("1")) {
                        jobject = new JSONObject(String.valueOf(o));
                        if (jobject.getJSONObject("result").has("amt")) {
                            CouponAmount = jobject.getJSONObject("result").getString("amt");
                            PaymentOption.tv_discount.setText("Discount Rs. " + HomeScreenFragment.CouponAmount);
                            Toast.makeText(getContext(), "Coupon Amount: " + jobject.getJSONObject("result").getString("amt"), Toast.LENGTH_SHORT).show();
                        } else if (jobject.getJSONObject("result").has("msg")) {
                            CouponAmount = "0";
                            PaymentOption.tv_discount.setText("Discount Rs. " + HomeScreenFragment.CouponAmount);
                            Toast.makeText(getContext(), " " + jobject.getJSONObject("result").getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            CouponAmount = "0";
                            PaymentOption.tv_discount.setText("Discount Rs. " + HomeScreenFragment.CouponAmount);
                            Toast.makeText(getContext(), "Unable to Process Your Coupon", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Unable to process Coupon", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(o);
        }
    }
    public static String getOperatorID(String Key, String Type) {
        String operator_id = "";
        switch (Type) {
            case "prepaid":
                switch (Key) {
                    case "UGP":
                    case "USP":
                    case "VSP":
                        operator_id = "0";
                        break;
                    case "ATP":
                        operator_id = "1";
                        break;
                    case "MSP":
                    case "MMP":
                        operator_id = "2";
                        break;
                    case "RGP":
                        operator_id = "3";
                        break;
                    case "VFP":
                        operator_id = "4";
                        break;
                    case "ACP":
                        operator_id = "5";
                        break;
                    case "BVP":
                    case "BGP":
                        operator_id = "6";
                        break;
                    case "IDP":
                        operator_id = "7";
                        break;
                    case "MTP":
                        operator_id = "10";
                        break;
                    case "TCP":
                        operator_id = "12";
                        break;
                    case "TSP":
                    case "TGP":
                        operator_id = "13";
                        break;
                    case "VGP":
                        operator_id = "15";
                        break;
                    case "TMP":
                    case "TVP":
                        operator_id = "28";
                        break;
                }
                break;
            case "postpaid":
                switch (Key) {
                    case "ATC":
                        operator_id = "1";
                        break;
                    case "RGC":
                        operator_id = "3";
                        break;
                    case "VFC":
                        operator_id = "4";
                        break;
                    case "ACC":
                        operator_id = "5";
                        break;
                    case "IDC":
                        operator_id = "7";
                        break;
                    case "BGC":
                        operator_id = "6";
                        break;
                    case "TDC":
                        operator_id = "13";
                        break;
                    case "MTC":
                        operator_id = "10";
                        break;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid Key: " + operator_id);
        }
        return operator_id;
    }
    public static String getRegionID(String Key) {
        String region_id = "1";
        switch (Key) {
            case "TN":
                region_id = "1";
                break;
            case "AP":
                region_id = "2";
                break;
            case "AS":
                region_id = "3";
                break;
            case "BR":
                region_id = "4";
                break;
            case "DL":
                region_id = "5";
                break;
            case "GJ":
                region_id = "6";
                break;
            case "HR":
                region_id = "7";
                break;
            case "HP":
                region_id = "8";
                break;
            case "JK":
                region_id = "9";
                break;
            case "KA":
                region_id = "10";
                break;
            case "KL":
                region_id = "11";
                break;
            case "KO":
                region_id = "12";
                break;
            case "MH":
                region_id = "13";
                break;
            case "MP":
                region_id = "14";
                break;
            case "MU":
                region_id = "15";
                break;
            case "NE":
                region_id = "16";
                break;
            case "OR":
                region_id = "17";
                break;
            case "PB":
                region_id = "18";
                break;
            case "RJ":
                region_id = "19";
                break;
            case "UE":
                region_id = "20";
                break;
            case "UW":
                region_id = "21";
                break;
            case "WB":
                region_id = "22";
                break;
            case "CH":
                region_id = "23";
                break;
            default:
                throw new IllegalArgumentException("Invalid Key: " + region_id);
        }
        return region_id;
    }
    public void showalert() {
        new AlertDialog.Builder(getContext())
                .setMessage("Internet Not Available")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.finish();
                    }
                }).show();
    }
    public void showProgress() {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wailt...");
        progressDialog.show();
    }
    public void GetRegionandOperator(String Number) {
        LoadRegionandOperator loadRegionandOperator = new LoadRegionandOperator();
        loadRegionandOperator.execute(Number);
        //Toast.makeText(getBaseContext(), "GetRegionandOperator", Toast.LENGTH_SHORT).show();
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
                        String Rgnid_converter = jresponse.getString("r");
                        String optid_converter = jresponse.getString("o");
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
            //   Toast.makeText(getContext(),jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
            super.onPostExecute(o);
        }
    }
    public void Browseplans() {
        screenName = "SearchPlans";
        View formElementsView = getActivity().getLayoutInflater().inflate(R.layout.browsepacks_filter, null);
        final EasyDialog easyDialog = new EasyDialog(getContext());
        Search_Operators = (Spinner) formElementsView.findViewById(R.id.spinner3);
        Search_Region = (Spinner) formElementsView.findViewById(R.id.spinner);
        radioGroup_SP = (RadioGroup) formElementsView.findViewById(R.id.radioConnectiontype);
        btn_searchPlans = (Button) formElementsView.findViewById(R.id.button_searchplan);
        selectedtypeId = radioGroup_SP.getCheckedRadioButtonId();
        radiotypeButton_SP = (RadioButton) formElementsView.findViewById(selectedtypeId);

        OperatorType = radiotypeButton_SP.getText().toString().toLowerCase();
preferenceHelper.putOperation_method("1");
        if(OperatorType.equals("prepaid"))
        {
            preferenceHelper.putOperatorType("1");
        } else {
            preferenceHelper.putOperatorType("2");
        }


        btn_searchPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.is_rechare_screen_open="false";
                easyDialog.dismiss();
               // Config.OperatorIdno = OperatorID;
              ///  Config.Regionidno = RegionID;
              //  Config.OperatorType = OperatorType;
                // Toast.makeText(getContext(),"OperatorType: "+OperatorType+"\nRegionIdNo: "+Config.Regionidno+"\nOperatorIdNo: "+Config.OperatorIdno+"\nDataConnectionType "+DataConnectionType+"\nTotalSTD_Loc_Calls "+TotalSTD_Loc_Calls+"\nTotal_Data "+Total_Data+"\nTotalSTD_Loc_SMS "+TotalSTD_Loc_SMS,Toast.LENGTH_SHORT).show();
//                if (!Config.isNetworkAvailable(getContext())) {
//                    showalert();
//                    return;
//                }
//                else {
//                    if(OperatorType.equals("prepaid"))
//                    {
//                        Intent plandetail=new Intent(getContext(),PrepaidMobilePacks.class);
//                        // plandetail.putExtra("unique_provider_id",OperatorIdNo);
//                        // plandetail.putExtra("region_id",RegionIdNo);
//                        //  plandetail.putExtra("type",OperatorType);
//
//                        //   PrepaidMobilePacks.this.finish();
//                        startActivity(plandetail);
//                    } else {
//                        Intent plandetail1=new Intent(getContext(),PostPaidMobilePacks.class);
//
//                        startActivity(plandetail1);
//                    }
//                }
                if (Config.isNetworkAvailable(getContext())) {
                    if (OperatorType.equals("prepaid")) {
                        Intent plandetail = new Intent(getContext(), PrepaidMobilePacks.class);
                        // plandetail.putExtra("unique_provider_id",OperatorIdNo);
                        // plandetail.putExtra("region_id",RegionIdNo);
                        //  plandetail.putExtra("type",OperatorType);
                        //   PrepaidMobilePacks.this.finish();
                        startActivity(plandetail);
                       getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    } else {
                        Intent plandetail1 = new Intent(getContext(), PostPaidMobilePacks.class);
                        startActivity(plandetail1);
                        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }
                } else {
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                            getContext());
                    // set title
                    alertDialogBuilder.setTitle("Warning !");
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("pls Check Internet Connection!")
                            .setCancelable(false)
                            .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            }
        });
        // alertDialog.show();
        if (!Config.isNetworkAvailable(getContext())) {
            showalert();
            return;
        } else {
            if(Get_Region_Operators_Plan.spinnerArray_Oprators==null)
            {
                new  loadRegion_and_operatior().execute();
            }
        /*    int i=Get_Region_Operators_Plan.spinnerArray_Oprators.length;
         if(i < 1){
           new  loadRegion_and_operatior().execute();
         } */  else { adp_Operators = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, Get_Region_Operators_Plan.spinnerArray_Oprators);
             // adp_Regions.notifyDataSetChanged();
             Search_Operators.setAdapter(adp_Operators);
             Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     OperatorID = Get_Region_Operators_Plan.spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                     preferenceHelper.putOperatorId(OperatorID);
                    // Config.OperatorIdno = OperatorID;
                    // operatorID_BrowsePlans = OperatorID;

                     Toast.makeText(getContext(),"id new = "+OperatorID,Toast.LENGTH_SHORT).show();
                     //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                     // manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                 }
                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {
                 }
             });


             adp_Regions = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, Get_Region_Operators_Plan.spinnerArray_Region);
             // adp_Regions.notifyDataSetChanged();
             Search_Region.setAdapter(adp_Regions);
             Search_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     RegionID = Get_Region_Operators_Plan.spinnerMap_Region.get(Search_Region.getSelectedItem().toString());
                     preferenceHelper.putRegionId(RegionID);
                     PostTask task = new PostTask();
                     task.execute(RegionID);
                   //  Config.Regionidno = RegionID;
                    // regionID_BrowsePlans = RegionID;
                      Toast.makeText(getContext(),"Region id new "+RegionID,Toast.LENGTH_SHORT).show();
                   //  manager.setPreferences(getContext(), "RegionIdNo = ", RegionIdNo);
                     // RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                     // manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
                 }
                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {
                 }
             });}

          //new Get_Region_Operators_Plan().execute("");


           // LoadRegion1 loadRegion = new LoadRegion1();
            //loadRegion.execute("regin");
           // LoadOperators1 loadOperators = new LoadOperators1();
           // loadOperators.execute(OperatorType);
        }
        radioGroup_SP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_prepaid:
                        OperatorType = "prepaid";
                        Config.OperatorType = OperatorType;
                        //  manager.setPreferences(getContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        if (Config.getConnectivityStatus(getContext())) {
                            if (Config.isOnline()) {
                              //  LoadOperators1 loadOperators = new LoadOperators1();
                               // loadOperators.execute(OperatorType);
                            } else {
                                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {   //showalert();
                        }
                        break;
                    case R.id.radio_postpaid:
                        OperatorType = "postpaid";
                        Config.OperatorType = OperatorType;
                        //  manager.setPreferences(getContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        if (Config.getConnectivityStatus(getContext())) {
                            if (Config.isOnline()) {
                              //  LoadOperators1 loadOperators1 = new LoadOperators1();
                               // loadOperators1.execute(OperatorType);
                            } else {
                                Toast.makeText(getContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {  // showalert();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
                 easyDialog
                // .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
                .setLayout(formElementsView)
                .setBackgroundColor(getContext().getResources().getColor(R.color.accent))
                // .setLocation(new location[])//point in screen
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(true)
                .setMarginLeftAndRight(24, 24)
                .setOutsideColor(getContext().getResources().getColor(R.color.background_color_black))
                .show();
    }
/*
    public class LoadOperators1 extends AsyncTask {
        @Override
        protected void onPreExecute() {
            //showProgress();
            super.onPreExecute();
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
            JSONObject json = jsonParser.makeHttpRequest(Config.PLAN_OPERATOR_KEY, "POST", params);
            // Log.d("jobject", "Do in Background: "+json);
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
                    if (jresponse.getString("error").equals("1")) {//Toast.makeText(getBaseContext(),"normal: "+String.valueOf(valid),Toast.LENGTH_LONG).show();
                        return;
                    } else if (jresponse.getString("success").equals("1")) {
                        try {
                            jobject = jresponse.getJSONObject("result");
                            //  Log.d("jobject", "onPostExecute: "+jobject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject jobj = null;
                        try {
                            jsonArray = jobject.getJSONArray("plan_operator_key");
                            // Toast.makeText(getContext(),"jsonArray: "+jsonArray,Toast.LENGTH_SHORT).show();
                            //  Toast.makeText(getContext(),"jobject: "+jobject,Toast.LENGTH_SHORT).show();
                            spinnerArray_Oprators = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jobj = jsonArray.getJSONObject(i);
                                spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
                                //   spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
                                spinnerArray_Oprators[i] = jobj.getString("value");
                                Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);
                                Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                            }
                            if (screenName.equals("Recharge")) {
                                ArrayAdapter<String> adp_Operators = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                                // adp_Regions.notifyDataSetChanged();
                                sp.setAdapter(adp_Operators);
                                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        OperatorID = spinnerMap_Opearators.get(sp.getSelectedItem().toString());
                                        //   Log.d("Spinner", "onPostExecute: "+sp.getSelectedItem());
                                        Config.OperatorIdno = OperatorID;
                                        //  Toast.makeText(getContext(),"OperatorID "+OperatorID,Toast.LENGTH_SHORT).show();
                                        // OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                                        //  manager.setPreferences(getContext(), "OperatorIdNo", OperatorIdNo);
                                        //  manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            } else {
                                adp_Operators = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                                // adp_Regions.notifyDataSetChanged();
                                Search_Operators.setAdapter(adp_Operators);
                                Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        OperatorID = spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                                        Config.OperatorIdno = OperatorID;
                                        operatorID_BrowsePlans = OperatorID;
                                        //  Toast.makeText(getContext(),"id new = "+Config.OperatorIdno,Toast.LENGTH_SHORT).show();
                                        //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                                        // manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            }
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
*/
  /*  public class LoadRegion1 extends AsyncTask {
        ProgressDialog progressDialogRegion;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialogRegion = new ProgressDialog(PrepaidMobilePacks.this,
//                    R.style.AppTheme_Dark_Dialog);
//            progressDialogRegion.setIndeterminate(true);
//            progressDialogRegion.setMessage("Please wait...");
//            progressDialogRegion.show();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            String identity = String.valueOf(args[0]);
            //  String password = String.valueOf(args[1]);
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("identity", identity));
            JSONObject json = jsonParser.makeHttpRequest(Config.GET_REGION_NAME, "POST", params);
            // Log.d("jobject", "Do in Background: "+json);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            JSONArray jsonArray = null;
            String resultString = null;
            if (o != null) {
                JSONObject jobject = null;
                JSONObject jresponse = (JSONObject) o;
                try {
                    if (jresponse.getString("error").equals("1")) {
                        return;
                    } else if (jresponse.getString("success").equals("1")) {
                        JSONObject jobj = null;
                        jsonArray = jresponse.getJSONObject("result").getJSONArray("location");
                        spinnerArray_Region = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jobj = jsonArray.getJSONObject(i);
                            spinnerMap_Region.put(jobj.getString("value"), jobj.getString("key"));
                            // spinnerMap_Region1.put(jobj.getString("key"), jobj.getString("value"));
                            spinnerArray_Region[i] = jobj.getString("value");
                            Log.d("JsonObject", "onPostExecute: " + spinnerArray_Region[i]);
                        }
                        if (screenName.equals("Recharge")) {
                            ArrayAdapter<String> adp_Regions = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            sp_region.setAdapter(adp_Regions);
                            sp_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(sp_region.getSelectedItem().toString());
                                    //  Toast.makeText(getContext(),"recharge screen= "+RegionID,Toast.LENGTH_SHORT).show();
                                    Config.Regionidno = RegionID;
                                    //  RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                                    manager.setPreferences(getContext(), "RegionIdNo = ", RegionIdNo);
                                    manager.setPreferences(getContext(), "RegionID = ", RegionID);
                                    //  manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            adp_Regions = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            Search_Region.setAdapter(adp_Regions);
                            Search_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(Search_Region.getSelectedItem().toString());
                                    Config.Regionidno = RegionID;
                                    regionID_BrowsePlans = RegionID;
                                    // Toast.makeText(getContext(),"Region id new "+RegionID,Toast.LENGTH_SHORT).show();
                                    manager.setPreferences(getContext(), "RegionIdNo = ", RegionIdNo);
                                    // RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                                    // manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                        // String Region_name = sp_state.getSelectedItem().toString();
                        //  RegionID= spinnerMap.get(Region_name).toString();
                        //    Log.d("jsonArray Length", "onPostExecute: "+jsonArray.length());
                        //  Log.d("jsonArray", "onPostExecute: "+jsonArray);
                        //  jobj = jobject.getJSONObject("login");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // Toast.makeText(getBaseContext(),ipAddress,Toast.LENGTH_LONG).show();
            //   Toast.makeText(getContext(),jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
        }
    }*/


    public class loadRegion_and_operatior extends AsyncTask<String, Void, String>  {
        ProgressDialog progressDialogRegion;
        private Exception exception;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialogRegion = new ProgressDialog(PrepaidMobilePacks.this,
//                    R.style.AppTheme_Dark_Dialog);
//            progressDialogRegion.setIndeterminate(true);
//            progressDialogRegion.setMessage("Please wait...");
//            progressDialogRegion.show();
        }
        protected String doInBackground(String... urls) {

            try {
                String getResponse = get(Config.GET_OPERATOR_REGION);
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
        public String get(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        protected void onPostExecute(String getResponse) {
            System.out.println(getResponse);

            try {
                if(getResponse!=null) {
                    JSONObject jsonObject = new JSONObject(String.valueOf(getResponse));
                    JSONObject jsonRegion,jsonOperators;
                    JSONArray jsonArray_operators= new JSONArray();
                    JSONArray jsonArray_region= new JSONArray();
                    if(jsonObject.getInt("success")==1)
                    {
                        if(jsonObject.has("data"))
                        {
                            // jsonObject.getJSONObject("data").getJSONObject("operators");
                            jsonArray_operators=   jsonObject.getJSONObject("data").getJSONArray("operators");
                            //jsonArray
                            JSONObject jobj,jobj1;

                            jsonArray_region= jsonObject.getJSONObject("data").getJSONArray("locations");


                            spinnerArray_Oprators = new String[jsonArray_operators.length()];
                            Get_Region_Operators_Plan.spinnerArray_Oprators=new String[jsonArray_operators.length()];
                            for (int i = 0; i < jsonArray_operators.length(); i++) {
                                jobj = jsonArray_operators.getJSONObject(i);
                                spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
                                spinnerArray_Oprators[i] = jobj.getString("value");
                                Get_Region_Operators_Plan.spinnerArray_Oprators[i] = jobj.getString("value");


                            }

                            spinnerArray_Region = new String[jsonArray_region.length()];
                            for (int i = 0; i < jsonArray_region.length(); i++) {
                                jobj = jsonArray_region.getJSONObject(i);
                                spinnerMap_Region.put(jobj.getString("value"), jobj.getString("key"));
                                spinnerArray_Region[i] = jobj.getString("value");

                            }

                            adp_Operators = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                            // adp_Regions.notifyDataSetChanged();
                            Search_Operators.setAdapter(adp_Operators);
                            Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    OperatorID = spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                                    preferenceHelper.putOperatorId(OperatorID);

                                    // Config.OperatorIdno = OperatorID;
                                    //  operatorID_BrowsePlans = OperatorID;
                                    //  Toast.makeText(getContext(),"id new = "+Config.OperatorIdno,Toast.LENGTH_SHORT).show();
                                    //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                                    // manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });


                            adp_Regions = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            Search_Region.setAdapter(adp_Regions);
                            Search_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(Search_Region.getSelectedItem().toString());
                                    preferenceHelper.putRegionId(RegionID);
                                    PostTask task = new PostTask();
                                    task.execute(RegionID);
                                    // Config.Regionidno = RegionID;
                                    // regionID_BrowsePlans = RegionID;
                                    // Toast.makeText(getContext(),"Region id new "+RegionID,Toast.LENGTH_SHORT).show();
                                    //manager.setPreferences(getContext(), "RegionIdNo = ", RegionIdNo);
                                    // RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                                    // manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
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
                            Log.v("jsonArray_operators","new:   "+jsonArray_operators.length());
                            Log.v("jsonArray_operators","new:   "+spinnerArray_Oprators.length);



                        }

                    }





                }






            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
       /* @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }*/

    }

   /* public void makePostRequest(View v) throws IOException {
        PostTask task = new PostTask();
        task.execute();
    }*/
    public class PostTask extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {

            try {
                String getResponse = post("http://www.katkada.com/api/plan/get_operator_by_region", urls[0]);
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {

            try {
                if(getResponse!=null) {
                    JSONObject jsonObject = new JSONObject(String.valueOf(getResponse));
                    JSONObject jsonRegion,jsonOperators;
                    JSONArray jsonArray_operators= new JSONArray();
                    JSONArray jsonArray_region= new JSONArray();
                    if(jsonObject.getInt("success")==1)
                    {
                        if(jsonObject.getInt("operator_data")==1)
                        {
                            // jsonObject.getJSONObject("data").getJSONObject("operators");
                            jsonArray_operators=   jsonObject.getJSONArray("operators");
                            //jsonArray
                            JSONObject jobj,jobj1;

                            spinnerArray_Oprators = new String[jsonArray_operators.length()];
                            Get_Region_Operators_Plan.spinnerArray_Oprators=new String[jsonArray_operators.length()];
                            for (int i = 0; i < jsonArray_operators.length(); i++) {
                                jobj = jsonArray_operators.getJSONObject(i);
                                spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
                                spinnerArray_Oprators[i] = jobj.getString("value");
                                Get_Region_Operators_Plan.spinnerArray_Oprators[i] = jobj.getString("value");

                            }



                            adp_Operators = new ArrayAdapter<String>(getContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                            // adp_Regions.notifyDataSetChanged();
                            Search_Operators.setAdapter(adp_Operators);
                            Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    OperatorID = spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                                    preferenceHelper.putOperatorId(OperatorID);
                                    // Config.OperatorIdno = OperatorID;
                                    //  operatorID_BrowsePlans = OperatorID;
                                    //  Toast.makeText(getContext(),"id new = "+Config.OperatorIdno,Toast.LENGTH_SHORT).show();
                                    //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                                    // manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                         Log.v("jsonArray_operators","new:   "+jsonArray_operators.length());
                            Log.v("jsonArray_operators","new:   "+spinnerArray_Oprators.length);

                        }

                    }

                }
        } catch (JSONException e) {
                e.printStackTrace();
            }


            Toast.makeText(getContext(),""+getResponse,Toast.LENGTH_LONG).show();
        }

        private String post(String url, String json) throws IOException {
            //   RequestBody body = RequestBody.create(JSON, json);
            RequestBody formBody = new FormBody.Builder()
                    .add("region", json)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
/*public  void floatingMenu(View v)
{


    FloatingActionButton actionC = new FloatingActionButton(getContext());
    actionC.setTitle("Hide/Show Action above");
    actionC.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    });

    menuMultipleActions.addButton(actionC);


}*/
}

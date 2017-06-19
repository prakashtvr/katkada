package com.katkada.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.Fragment.Baseplan;
import com.katkada.Fragment.BlackberryFragment;
import com.katkada.Fragment.CUGFragment;
import com.katkada.Fragment.DataFragment;
import com.katkada.Fragment.Data_2G;
import com.katkada.Fragment.Data_3G_4G_Fragment;
import com.katkada.Fragment.FultalktimeFragment;
import com.katkada.Fragment.GPRSFragment;
import com.katkada.Fragment.HomeScreenFragment;
import com.katkada.Fragment.ISDFragment;
import com.katkada.Fragment.International_Roaming_Data;
import com.katkada.Fragment.NightPacksFragment;
import com.katkada.Fragment.RoamingFragment;
import com.katkada.Fragment.SMSFragment;
import com.katkada.Fragment.STD_Calls;
import com.katkada.Fragment.TalkTimeFragment;
import com.katkada.Fragment.Topups;
import com.katkada.Fragment.VAS;
import com.katkada.Fragment.VoiceCallFragment;
import com.katkada.Fragment.VoiceCalls;
import com.katkada.Other.Config;
import com.katkada.Other.Get_Region_Operators_Plan;
import com.katkada.Other.JSONParser;
import com.katkada.Other.ListViewAdaptorPlanDetails;
import com.katkada.Other.PreferenceHelper;
import com.katkada.Other.SessionManager;
import com.katkada.R;
import com.michael.easydialog.EasyDialog;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class PrepaidMobilePacks extends AppCompatActivity {
    public static Spinner sp_state, sp_Operators;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayAdapter<String> adp_Regions;
    ArrayAdapter<String> adp_Operators;
    //  TalkTimeFragment.DisplayPlans displayPlans;
    JSONParser jsonParser = new JSONParser();
    Context context = PrepaidMobilePacks.this;
    ListView lview;
    // SessionManager manager;
    ListViewAdaptorPlanDetails lviewAdapter;
    /// JSONParser jsonParser = new JSONParser();
    Fragment fragment = null;
    String screenName, DataConnectionType = "3g", TotalSTD_Loc_Calls = "100", TotalSTD_Loc_SMS = "100", Total_Data = "100";
    RelativeLayout rl_calls, rl_sms, rl_data, rl_roaming, rl_recommendation, rl_browse_packs, rl_recharge_now;
    TextView tv2g, tv3g, tv4g, floating_text_start, tv_coupon;
    TextInputLayout textInputLayout_Coupon;
    Spinner Search_Operators, Search_Region;
    public SeekBar bar, bar_data, bar_sms;
    public TextView textV, textV_Data, textV_sms;
    SessionManager manager;
    public static String CouponAmount = "0";
    int selectedtypeId;
    ProgressDialog progressDialog;
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
    android.app.AlertDialog dialog;
    static CountDownTimer timer = null;
    Toast toast;
    public static String OperatorID, RegionID;
    public static List<String> list, Regions, Operators;
    public String[] spinnerArray_Oprators;
    public final HashMap<String, String> spinnerMap_Opearators = new HashMap<String, String>();
    //  TalkTimeFragment.DisplayPlans displayPlans;
    //JSONParser jsonParser = new JSONParser();
    public static String[] spinnerArray_Region;
    public final static HashMap<String, String> spinnerMap_Region = new HashMap<String, String>();
    PreferenceHelper preferenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        // getActionBar().setHomeButtonEnabled(true);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_prepaid_mobile_packs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        preferenceHelper=new PreferenceHelper(PrepaidMobilePacks.this);
        // stopButton.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
       // new loadRegion_and_operatior().execute("");
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TalkTimeFragment(), "LOCAL CALLS");//1
        adapter.addFragment(new STD_Calls(), "STD CALLS");//1
        adapter.addFragment(new SMSFragment(), "SMS");//3
        adapter.addFragment(new RoamingFragment(), "ROAMING CALLS");//4
        adapter.addFragment(new VoiceCallFragment(), "VIDEO CALL");//5
        adapter.addFragment(new DataFragment(), "DATA");//6
        adapter.addFragment(new NightPacksFragment(), "NIGHT& WEEKENDS PACKS");//7
     adapter.addFragment(new Baseplan(), "BASE PLAN");//7
        adapter.addFragment(new BlackberryFragment(), "BLACKBERRY");//9
        adapter.addFragment(new CUGFragment(), "CUG");//10
        adapter.addFragment(new Topups(), "TOPUPS");//10//11Topups
        adapter.addFragment(new ISDFragment(), "ISD");//12
        adapter.addFragment(new Data_2G(), "2G Data");//12//132G Data
        adapter.addFragment(new Data_3G_4G_Fragment(), "4G DATA");//14
        adapter.addFragment(new FultalktimeFragment(), "FULL & EXTARA TALKTIME");//15
        adapter.addFragment(new VAS(), "VAS");//16
        adapter.addFragment(new GPRSFragment(), "INTERNATIONAL ROAMING CALLS");//17
        adapter.addFragment(new VoiceCalls(), "VOICE CALS");//18Voice Calls
        adapter.addFragment(new International_Roaming_Data(), "INTERNATIONAL ROAMIND DATA");//19International Roaming Data

        //  adapter.addFragment(new PopularFragment(), "POPULAR");










        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(0);
        // PrepaidMobilePacks.ViewPagerAdapter adapter = new PrepaidMobilePacks.ViewPagerAdapter( getSupportFragmentManager());
        // adapter.addFragment(new RecomendationFragment(), "RECOMENDATION");
        // adapter.addFragment(new Recharge(), "RECHARGE");
        // adapter.addFragment(new RecomendationFragment(), "THREE");
        // viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
//    @Override
//    public void onBackPressed() {
//       // super.onBackPressed();
//        this.finish();
//      //  return
//                ;
//        // startActivity(new Intent(getBaseContext(), MainActivity.class));
//
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//
//                Intent i = new Intent(getApplicationContext(), RechargeNow.class);
//                finish();
//                startActivity(i);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(Config.is_rechare_screen_open.equals("false"))
        {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        finish();
        //Intent i = new Intent(getApplicationContext(), RechargeNow.class);
       // finish();
        //startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menufilter, menu);
        if(  preferenceHelper.getOperation_method()!="2")
        {   menu.findItem(R.id.action_Share).setVisible(true);
        } else {
            menu.findItem(R.id.action_Share).setVisible(false);
        }


        //   getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                if(Config.is_rechare_screen_open.equals("false"))
                {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }

                finish();
                // app icon in action bar clicked; goto parent activity.
                // this.finish();
                return true;
            default:
        }
        if (id == R.id.action_Share) {
            //    if(!tipWindow.isTooltipShown()) {
            //     tipWindow.showToolTip(v);
            //  }
            Browseplans();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //filter browse plans
    public void Browseplans() {
        screenName = "SearchPlans";
        View formElementsView = PrepaidMobilePacks.this.getLayoutInflater().inflate(R.layout.browsepacks_filter, null);
        final EasyDialog easyDialog = new EasyDialog(PrepaidMobilePacks.this);
        Search_Operators = (Spinner) formElementsView.findViewById(R.id.spinner3);
        Search_Region = (Spinner) formElementsView.findViewById(R.id.spinner);
        radioGroup_SP = (RadioGroup) formElementsView.findViewById(R.id.radioConnectiontype);
        btn_searchPlans = (Button) formElementsView.findViewById(R.id.button_searchplan);

        selectedtypeId = radioGroup_SP.getCheckedRadioButtonId();
        radiotypeButton_SP = (RadioButton) formElementsView.findViewById(selectedtypeId);
        OperatorType = radiotypeButton_SP.getText().toString().toLowerCase();
        if(OperatorType.equals("prepaid"))
        {
            preferenceHelper.putOperatorType("1");
        } else {
            preferenceHelper.putOperatorType("2");
        }
       // preferenceHelper.putOperatorType(OperatorType);

        Config.OperatorType = OperatorType;
        btn_searchPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Config.OperatorIdno=OperatorIdNo;
              //  Config.Regionidno = RegionIdNo;
              //  Config.OperatorType = OperatorType;
                //   Toast.makeText(getBaseContext(),"Type: "+  Config.OperatorType+"\nOpt: "+Config.OperatorIdno+"\nRgn: "+  Config.Regionidno,Toast.LENGTH_SHORT).show();
                if (Config.getConnectivityStatus(getBaseContext())) {
                    if (Config.isOnline()) {
                        Intent plandetail = new Intent(getBaseContext(), PrepaidMobilePacks.class);
                        // plandetail.putExtra("unique_provider_id",OperatorIdNo);
                        // plandetail.putExtra("region_id",RegionIdNo);
                        //  plandetail.putExtra("type",OperatorType);
                        Log.d("SearchPlans", "Filter: " + "OperatorType: " + Config.OperatorType + "\nRegionIdNo: " + Config.Regionidno + "\nOperatorIdNo: " + Config.OperatorType + "\nDataConnectionType " + DataConnectionType + "\nTotalSTD_Loc_Calls " + TotalSTD_Loc_Calls + "\nTotal_Data " + Total_Data + "\nTotalSTD_Loc_SMS " + TotalSTD_Loc_SMS);
                        PrepaidMobilePacks.this.finish();
                        startActivity(plandetail);
                        // Toast.makeText(getApplicationContext(),"OperatorType Id="+OperatorType,Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(getApplicationContext(),"Region Id="+RegionIdNo,Toast.LENGTH_SHORT).show();
                        // ;
                    } else {
                        Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                    //  startActivity(new Intent(getApplicationContext(), PlanDetailsActivity.class));
                } else {
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                            getBaseContext());
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
        if (Config.getConnectivityStatus(getBaseContext())) {
            if (Config.isOnline()) {

                if(Get_Region_Operators_Plan.spinnerArray_Oprators==null)
                {
                    new loadRegion_and_operatior().execute();
                }
        /*    int i=Get_Region_Operators_Plan.spinnerArray_Oprators.length;
         if(i < 1){
           new  loadRegion_and_operatior().execute();
         } */  else { adp_Operators = new ArrayAdapter<String>(PrepaidMobilePacks.this, R.layout.custom_textview_to_spinner, Get_Region_Operators_Plan.spinnerArray_Oprators);
                    // adp_Regions.notifyDataSetChanged();
                    Search_Operators.setAdapter(adp_Operators);
                    Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            OperatorID = Get_Region_Operators_Plan.spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                            preferenceHelper.putOperatorId(OperatorID);
                            // Config.OperatorIdno = OperatorID;
                            // operatorID_BrowsePlans = OperatorID;
                            Toast.makeText(PrepaidMobilePacks.this,"id new = "+OperatorID,Toast.LENGTH_SHORT).show();
                            //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                            // manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


                    adp_Regions = new ArrayAdapter<String>(PrepaidMobilePacks.this, R.layout.custom_textview_to_spinner, Get_Region_Operators_Plan.spinnerArray_Region);
                    // adp_Regions.notifyDataSetChanged();
                    Search_Region.setAdapter(adp_Regions);
                    Search_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            RegionID = Get_Region_Operators_Plan.spinnerMap_Region.get(Search_Region.getSelectedItem().toString());
                            preferenceHelper.putRegionId(RegionID);
                            //  Config.Regionidno = RegionID;
                            // regionID_BrowsePlans = RegionID;
                            Toast.makeText(PrepaidMobilePacks.this,"Region id new "+RegionID,Toast.LENGTH_SHORT).show();
                            //  manager.setPreferences(getContext(), "RegionIdNo = ", RegionIdNo);
                            // RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                            // manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });}


            /*    LoadRegion1 loadRegion = new LoadRegion1();
                loadRegion.execute("regin");
                LoadOperators1 loadOperators = new LoadOperators1();
                loadOperators.execute(OperatorType);*/
            } else {
                Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
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

                            preferenceHelper.putOperatorType("1");


                        //  manager.setPreferences(getApplicationContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getApplicationContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                       /* if (Config.getConnectivityStatus(getBaseContext())) {
                            if (Config.isOnline()) {
                                LoadOperators1 loadOperators = new LoadOperators1();
                                loadOperators.execute(OperatorType);
                            } else {
                                Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {   //showalert();
                        }
                        break;*/
                    case R.id.radio_postpaid:
                        OperatorType = "postpaid";
                        preferenceHelper.putOperatorType("2");
                        Config.OperatorType = OperatorType;
                        //  manager.setPreferences(getApplicationContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getApplicationContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                       /* if (Config.getConnectivityStatus(getBaseContext())) {
                            if (Config.isOnline()) {
                                LoadOperators1 loadOperators1 = new LoadOperators1();
                                loadOperators1.execute(OperatorType);
                            } else {
                                Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {  // showalert();
                        }*/
                        break;
                    default:
                        break;
                }
            }
        });
        easyDialog
                // .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
                .setLayout(formElementsView)
                .setBackgroundColor(getBaseContext().getResources().getColor(R.color.accent))
                // .setLocation(new location[])//point in screen
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(true)
                .setMarginLeftAndRight(24, 24)
                .setOutsideColor(getBaseContext().getResources().getColor(R.color.background_color_black))
                .show();
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
    public class LoadOperators extends AsyncTask {
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
            JSONObject json = jsonParser.makeHttpRequest(Config.COMMON_URL, "GET", params);
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
                            jobject = new JSONObject(String.valueOf(o));
                            //  Log.d("jobject", "onPostExecute: "+jobject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject jobj = null;
                        try {
                            jsonArray = jobject.getJSONArray("result");
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
                            }
                            if (screenName.equals("Recharge")) {
                                ArrayAdapter<String> adp_Operators = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                                // adp_Regions.notifyDataSetChanged();
                                sp.setAdapter(adp_Operators);
                                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        OperatorID = spinnerMap_Opearators.get(sp.getSelectedItem().toString());
                                        //   Log.d("Spinner", "onPostExecute: "+sp.getSelectedItem());
                                        OperatorIdNo = HomeScreenFragment.getOperatorID(OperatorID, OperatorType);
                                        manager.setPreferences(getBaseContext(), "OperatorIdNo", OperatorIdNo);
                                        //  manager.setPreferences(getApplicationContext(), "Shared_OptName", OperatorIdNo);
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            } else {
                                adp_Operators = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                                // adp_Regions.notifyDataSetChanged();
                                Search_Operators.setAdapter(adp_Operators);
                                Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        OperatorID = spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                                        Config.OperatorIdno = OperatorID;
                                        //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                                        // manager.setPreferences(getApplicationContext(), "Shared_OptName", OperatorIdNo);
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
            //   Toast.makeText(getApplicationContext(),jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
            super.onPostExecute(o);
        }
    }
    public class LoadRegion extends AsyncTask {
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
            JSONObject json = jsonParser.makeHttpRequest(Config.COMMON_Region_URL, "GET", params);
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
                            // spinnerMap_Region1.put(jobj.getString("key"), jobj.getString("value"));
                            spinnerArray_Region[i] = jobj.getString("value");
                            Log.d("JsonObject", "onPostExecute: " + spinnerArray_Region[i]);
                        }
                        if (screenName.equals("Recharge")) {
                            ArrayAdapter<String> adp_Regions = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            sp_region.setAdapter(adp_Regions);
                            sp_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(sp_region.getSelectedItem().toString());
                                    // Toast.makeText(getBaseContext(),RegionID,Toast.LENGTH_SHORT).show();
                                    //  RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                                    manager.setPreferences(getBaseContext(), "RegionIdNo", RegionIdNo);
                                    //  manager.setPreferences(getApplicationContext(), "Shared_RgName", RegionIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            adp_Regions = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            Search_Region.setAdapter(adp_Regions);
                            Search_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(Search_Region.getSelectedItem().toString());
                                    Config.Regionidno = RegionID;
                                    // Toast.makeText(PrepaidMobilePacks.this,""+RegionID,Toast.LENGTH_SHORT).show();
                                    // RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                                    // manager.setPreferences(getApplicationContext(), "Shared_RgName", RegionIdNo);
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
            //   Toast.makeText(getApplicationContext(),jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
        }
    }
    public class LoadRegion1 extends AsyncTask {
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
                            ArrayAdapter<String> adp_Regions = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            sp_region.setAdapter(adp_Regions);
                            sp_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(sp_region.getSelectedItem().toString());
                                    //   Toast.makeText(getApplicationContext(),"recharge screen= "+RegionID,Toast.LENGTH_SHORT).show();
                                    Config.Regionidno = RegionID;
                                    //  RegionIdNo=HomeScreenFragment.getRegionID(RegionID);
                                    manager.setPreferences(getApplicationContext(), "RegionIdNo = ", RegionIdNo);
                                    manager.setPreferences(getApplicationContext(), "RegionID = ", RegionID);
                                    //  manager.setPreferences(getApplicationContext(), "Shared_RgName", RegionIdNo);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            adp_Regions = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_textview_to_spinner, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            Search_Region.setAdapter(adp_Regions);
                            Search_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    RegionID = spinnerMap_Region.get(Search_Region.getSelectedItem().toString());
                                    Config.Regionidno = RegionID;
                                    HomeScreenFragment.regionID_BrowsePlans = RegionID;
                                    // Toast.makeText(getBaseContext(),""+Config.Regionidno+"\n"+HomeScreenFragment.regionID_BrowsePlans,Toast.LENGTH_SHORT).show();
//
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
            //   Toast.makeText(getApplicationContext(),jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
        }
    }
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
                            }
                            if (screenName.equals("Recharge")) {
                                ArrayAdapter<String> adp_Operators = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                                // adp_Regions.notifyDataSetChanged();
                                sp.setAdapter(adp_Operators);
                                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        OperatorID = spinnerMap_Opearators.get(sp.getSelectedItem().toString());
                                        //   Log.d("Spinner", "onPostExecute: "+sp.getSelectedItem());
                                        Config.OperatorIdno = OperatorID;
                                        //    Toast.makeText(getApplicationContext(),"id=123 "+spinnerMap_Opearators.get(sp.getSelectedItem().toString()),Toast.LENGTH_SHORT).show();
                                        // OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                                        //  manager.setPreferences(getApplicationContext(), "OperatorIdNo", OperatorIdNo);
                                        //  manager.setPreferences(getApplicationContext(), "Shared_OptName", OperatorIdNo);
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            } else {
                                adp_Operators = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                                // adp_Regions.notifyDataSetChanged();
                                Search_Operators.setAdapter(adp_Operators);
                                Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        OperatorID = spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                                        Config.OperatorIdno = OperatorID;
                                        HomeScreenFragment.operatorID_BrowsePlans = OperatorID;
                                        //  Toast.makeText(getBaseContext(),""+Config.OperatorIdno+"\n"+HomeScreenFragment.operatorID_BrowsePlans,Toast.LENGTH_SHORT).show();
//
                                        //  Toast.makeText(getApplicationContext(),"id new = "+Config.OperatorIdno,Toast.LENGTH_SHORT).show();
                                        //OperatorIdNo=HomeScreenFragment.getOperatorID(OperatorID,OperatorType);
                                        // manager.setPreferences(getApplicationContext(), "Shared_OptName", OperatorIdNo);
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
            //   Toast.makeText(getApplicationContext(),jresponse.toString(),Toast.LENGTH_LONG).show();
            // return valid;
            super.onPostExecute(o);
        }
    }


    public class loadRegion_and_operatior extends AsyncTask {
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

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("getdate", ""));
            JSONObject json = jsonParser.makeHttpRequest(Config.GET_OPERATOR_REGION, "POST", params);
            // Log.d("jobject", "Do in Background: "+json);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if(o!=null) {
                JSONObject jsonObject = new JSONObject(String.valueOf(o));
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
JSONObject jobj;

        jsonArray_region= jsonObject.getJSONObject("data").getJSONArray("locations");


        spinnerArray_Oprators = new String[jsonArray_operators.length()];
        for (int i = 0; i < jsonArray_operators.length(); i++) {
            jobj = jsonArray_operators.getJSONObject(i);
            spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
            //   spinnerMap_Opearators1.put(jobj.getString("key"), jobj.getString("value"));
            spinnerArray_Oprators[i] = jobj.getString("value");
            Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);
            Log.d("JsonObject", "Key: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
        }



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
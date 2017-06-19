package com.katkada.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.katkada.Fragment.HomeScreenFragment;
import com.katkada.Other.Config;
import com.katkada.Other.JSONParser;
import com.katkada.Other.ListViewAdaptorPlanDetails;
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

public class BrowsePlansandPacks extends AppCompatActivity {


    Context context = BrowsePlansandPacks.this;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_plansand_packs);
        lview = (ListView) findViewById(R.id.listView2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        RecomentedPlans recomentedPlans = new RecomentedPlans();
        recomentedPlans.execute("");
        //   PlanDetails planDetails=new PlanDetails();
        //   planDetails.execute("");
    }
    // JSONPARSER_Recharge jsonParser = new JSONPARSER_Recharge();

    JSONParser jsonParserrecomendation = new JSONParser();
    public class RecomentedPlans extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] args) {
            String UserId = String.valueOf(args[0]);
            Bundle extras = getIntent().getExtras();
            String Optid = "1";
            String Rgnid = "1";
            String ConType = "prepaid";
            String TotalMins = "100";
            String TotalSTD_Loc_Calls = "1";
            String TotalData = "1";
            String TotalSMS = "1";
            String ConnectionType = "3G";
            if (extras == null) {
                Toast.makeText(getBaseContext(), "pls Try again..", Toast.LENGTH_SHORT).show();
            } else {

                if (extras.containsKey("OperatorIdNo")) {
                    Optid = extras.getString("OperatorIdNo");
                }
                if (extras.containsKey("DataConnectionType")) {
                    ConnectionType = extras.getString("DataConnectionType");
                }
                if (extras.containsKey("OperatorType")) {
                    ConType = extras.getString("OperatorType");
                }

                if (extras.containsKey("TotalSTD_Loc_Calls")) {
                    TotalSTD_Loc_Calls = extras.getString("TotalSTD_Loc_Calls");
                }

                if (extras.containsKey("Total_Data")) {
                    TotalData = extras.getString("Total_Data");
                }
                if (extras.containsKey("TotalSTD_Loc_SMS")) {
                    TotalSMS = extras.getString("TotalSTD_Loc_SMS");
                }
                if (extras.containsKey("totalminutes")) {
                    TotalMins = extras.getString("totalminutes");

                } else {
                    TotalMins = "100";
                }

                if (extras.containsKey("RegionIdNo")) {
                    Rgnid = extras.getString("RegionIdNo");
                } else {
                    Rgnid = "1";
                }
                //Optid=extras.getString("OperatorIdNo");
            }
//            String Optid=manager.getPreferences(PlanDetailsActivity.this,"Shared_OptName");
//            String Rgnid="5";
//            String ConType=manager.getPreferences(PlanDetailsActivity.this,"Shared_ConnectionType");
//            String TotMinutes=manager.getPreferences(PlanDetailsActivity.this,"Shared_TotalMinutes");
//            String TotalData=manager.getPreferences(PlanDetailsActivity.this,"Shared_TotalData");
//            String TotalSMS=manager.getPreferences(PlanDetailsActivity.this,"Shared_TotalSMS");
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("securehash", "7a4a870320e6dc86847fdc356416e063943d430edf18f1f34fbf77d5e31e500a177c6c24b6ed4556f43fef788ec7669ae16ce3f59040287e53de3f3a23c43280"));
            params.add(new BasicNameValuePair("txnid", "abc"));
            params.add(new BasicNameValuePair("api_user_id", "EPj3bYVYYsA5NdLNjYCEnQCxMpL7gxV2YYRWxeebW4A3WWcr"));
            params.add(new BasicNameValuePair("type", ConType));
            params.add(new BasicNameValuePair("typeofplan", "mobile"));

            params.add(new BasicNameValuePair("region_id", Rgnid));
            params.add(new BasicNameValuePair("totalminutes", TotalMins));
            if (extras.containsKey("OperatorIdNo")) {
                params.add(new BasicNameValuePair("unique_provider_id", extras.getString("OperatorIdNo")));
            }
            if (extras.containsKey("Total_Data")) {
                params.add(new BasicNameValuePair("datatotalusage", extras.getString("Total_Data")));
            }
     if (extras.containsKey("totalsms")) {
                params.add(new BasicNameValuePair("totalsms", extras.getString("TotalSTD_Loc_SMS")));

            }
     //  params.add(new BasicNameValuePair("datatotalusage", TotalData));
            //   params.add(new BasicNameValuePair("totalsms", TotalSMS));
            JSONObject json = jsonParserrecomendation.makeHttpRequest(Config.GET_RECOMENDATION_URL, "POST", params);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jresponse = null;
            JSONObject jsonObject = null;
            String resultString = null;
            JSONArray jsonArray;
            if (o == null) {
                Toast.makeText(getBaseContext(), "Unable to get Value", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    jresponse = new JSONObject(String.valueOf(o));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (jresponse.getString("error").equals("false")) {
                    //   Toast.makeText(getBaseContext(), jresponse.getString("search_id"),Toast.LENGTH_SHORT).show();
                    RecomentedPlannsResult recomentedPlannsResult = new RecomentedPlannsResult();
                    recomentedPlannsResult.execute(jresponse.getString("search_id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class RecomentedPlannsResult extends AsyncTask {
        String EstimatedCost[], packPrice[], pricerecharge[],
                OperatorsName[],
                Operatorsid[],
                region_name[],
                region_id[], Price[],
                Talktime[], packTalktime[],
                Validity[], packValidity[],
                Name[], ImageURL[], packName[], packMonthlyCoast[],
                MonthlyCoast[];
        ProgressDialog loading1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading1 = ProgressDialog.show(BrowsePlansandPacks.this, "Fetching Data", "Please Wait...", true, true);
            // dialog = new ProgressDialog(getBaseContext());
            // dialog.setMessage("please wait...");
            // dialog.show();

            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] args) {


            String searchid = String.valueOf(args[0]);
            String api_user_id = "EPj3bYVYYsA5NdLNjYCEnQCxMpL7gxV2YYRWxeebW4A3WWcr";


            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("start", "1132142"));
            //    params.add(new BasicNameValuePair("number_of_packs", "10"));


            JSONObject json = jsonParserrecomendation.makeHttpRequest(Config.GET_REMD_RESULT_URL + api_user_id + "/" + searchid, "POST", params);


            return json;
        }


        @Override
        protected void onPostExecute(Object o) {
            loading1.dismiss();
            JSONObject jresponse = null;
            JSONObject jsonObject = null;
            String resultString = null;
            JSONArray jsonArray;
            JSONArray myListsAll = null;
            JSONArray myListstopup = null;
            JSONObject jsonobject = null;
            JSONObject jsonobject1 = null;
            String opt_id;
            String IMAGE_URL = "http://katkada.com/assets/public//images/operator/operator_5.png";
            if (o == null) {

                Toast.makeText(getBaseContext(), "Unable to get Value", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    jresponse = new JSONObject(String.valueOf(o));


                    int res = jresponse.getInt("error");
                    Log.v("test", String.valueOf(res));
                    Log.d("error", "onPostExecute: " + res);
                    if (jresponse.getString("error").equals("0")) {

                        jresponse = new JSONObject(String.valueOf(o));
                        if (jresponse.has("resultready")) {
                            if (jresponse.getString("resultready").equals("1")) {

                                if (jresponse.has("no_results")) {
                                    if (jresponse.getString("no_results").equals("false")) {

                                        myListsAll = jresponse.getJSONArray("results");
                                        EstimatedCost = new String[myListsAll.length()];
                                        OperatorsName = new String[myListsAll.length()];
                                        Operatorsid = new String[myListsAll.length()];
                                        region_name = new String[myListsAll.length()];
                                        region_id = new String[myListsAll.length()];

                                        packPrice = new String[myListsAll.length()];
                                        packTalktime = new String[myListsAll.length()];
                                        packName = new String[myListsAll.length()];
                                        packValidity = new String[myListsAll.length()];
                                        packMonthlyCoast = new String[myListsAll.length()];
                                        pricerecharge = new String[myListsAll.length()];
                                        ImageURL = new String[myListsAll.length()];
                                        for (int i = 0; i < myListsAll.length(); i++) {
//                        JSONObject jsonobject= null;
//                        JSONObject jsonobject1= null;
                                            // Toast.makeText(getBaseContext(),  jsonobject.optString("unique_operator_name"),Toast.LENGTH_SHORT).show();


                                            jsonobject = (JSONObject) myListsAll.get(i);
                                            EstimatedCost[i] = jsonobject.optString("estimatedcost");
                                            ImageURL[i] = jsonobject.optString("unique_operator_id");


                                            OperatorsName[i] = jsonobject.optString("unique_operator_name");
                                            Operatorsid[i] = jsonobject.optString("unique_operator_id");
                                            opt_id = jsonobject.optString("unique_operator_id");
                                            region_name[i] = jsonobject.optString("region_name");
                                            region_id[i] = jsonobject.optString("region_id");
                                            // loadImageLogoURL.execute(getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"));
                                            Log.d("NAme", "onPostExecute: " + jsonobject.optString("unique_operator_name"));
                                            myListstopup = jsonobject.getJSONArray("topups");
                                            Price = new String[myListstopup.length()];
                                            Talktime = new String[myListstopup.length()];
                                            Validity = new String[myListstopup.length()];
                                            Name = new String[myListstopup.length()];
                                            MonthlyCoast = new String[myListstopup.length()];

                                            for (int j = 0; j < myListstopup.length(); j++) {
                                                jsonobject1 = (JSONObject) myListstopup.get(j);
                                                //  Log.d("Name", "onPostExecute: "+jsonobject1.optString("name"));


                                                Price[j] = "\u20B9" + jsonobject1.optString("cost");
                                                packPrice[i] = Price[j];
                                                pricerecharge[i] = jsonobject1.optString("cost");
                                                Log.d("Name", "inner for loop: " + Price[j]);
                                                Talktime[j] = "Talktime: " + jsonobject1.optString("talktime");
                                                packTalktime[i] = Talktime[j];
                                                Validity[j] = "Validity: " + jsonobject1.optString("validity_string");
                                                packValidity[i] = Validity[j];
                                                Name[j] = jsonobject1.optString("name");
                                                packName[i] = Name[j];
                                                MonthlyCoast[j] = "Monthly Coast: " + jsonobject1.optString("monthlycost");
                                                packMonthlyCoast[i] = MonthlyCoast[j];

                                                // Toast.makeText(getBaseContext(), "operator_"+getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"),Toast.LENGTH_SHORT).show();
                                                //  Log.d("Name", " operator_"+getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"));
                                                // lviewAdapter = new ListViewAdaptorPlanDetails(BrowsePlansandPacks.this,Price,Name,  Validity, Price);
                                                // lviewAdapter = new ListViewAdaptorPlanDetails(BrowsePlansandPacks.this,packPrice,packPrice,  packPrice, packPrice);
                                                //   lviewAdapter.notifyDataSetChanged();
                                            }

                                        }
                                        lviewAdapter = new ListViewAdaptorPlanDetails(BrowsePlansandPacks.this, packPrice, packName, packValidity, pricerecharge, packMonthlyCoast, OperatorsName);

                                        //  System.out.println("adapter => "+lviewAdapter.getCount());
                                        lview.setAdapter(lviewAdapter);
                                    } else if (jresponse.has("error_code")) {

                                        Toast.makeText(getBaseContext(), "No Result Found", Toast.LENGTH_SHORT).show();

                                    }

                                } else {
                                    Toast.makeText(getBaseContext(), "" + jresponse, Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(getBaseContext(), "" + jresponse, Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(getBaseContext(), "" + jresponse, Toast.LENGTH_SHORT).show();
                        }


//                    LoadImageLogoURL loadImageLogoURL=new LoadImageLogoURL();
//no_resolt=false
                        //  Toast.makeText(getBaseContext(), jresponse.getString("no_results"),Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }


    }


//    public  class LoadImageLogoURL extends AsyncTask
//    {
//
//
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected Object doInBackground(Object[] args) {
//
//
//            String LogoName = "operator_"+String.valueOf(args[0]);
//
//
//
//            ArrayList params = new ArrayList();
//            params.add(new BasicNameValuePair("start", "100"));
//            params.add(new BasicNameValuePair("number_of_packs", "10"));
//
//
//
//
//
//            JSONObject json = jsonParserrecomendation.makeHttpRequest(Config.GET_LOGO_URL+LogoName, "GET",params);
//
//
//            return json;
//        }
//
//
//        @Override
//        protected void onPostExecute(Object o) {
//
//            JSONObject jresponse = null;
//            JSONObject jsonObject=null;
//            String resultString = null;
//            JSONArray jsonArray;
//            JSONArray myListsAll= null;
//            JSONArray myListsAll1= null;
//            JSONObject jsonobject= null;
//            JSONObject jsonobject1= null;
//            String opt_id;
//            if( o == null)
//            {
//
//                Toast.makeText(getBaseContext(),"Unable to get Value",Toast.LENGTH_SHORT).show();
//            }
//            else {
//                try {
//
//                    jresponse = new JSONObject(String.valueOf(o));
//                    Toast.makeText(getBaseContext(),"URL :"+jresponse,Toast.LENGTH_SHORT).show();
//                    IMAGE_URL=String.valueOf(o);
////                    myListsAll = jresponse.getJSONArray("results");
////                    EstimatedCost =new String[myListsAll.length()];
////                    OperatorsName =new String[myListsAll.length()];
////                    Operatorsid =new String[myListsAll.length()];
////                    region_name =new String[myListsAll.length()];
////                    region_id =new String[myListsAll.length()];
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
////            try {
////                if(jresponse.getString("error").equals("0"))
////                {
////
////                    Toast.makeText(getBaseContext(), jresponse.getString("no_results"),Toast.LENGTH_SHORT).show();
////                    for(int i=0;i<myListsAll.length();i++){
//////                        JSONObject jsonobject= null;
//////                        JSONObject jsonobject1= null;
////                        // Toast.makeText(getBaseContext(),  jsonobject.optString("unique_operator_name"),Toast.LENGTH_SHORT).show();
////
////                        try {
////                            jsonobject = (JSONObject) myListsAll.get(i);
////                            EstimatedCost[i]=jsonobject.optString("estimatedcost");
////                            OperatorsName[i]=jsonobject.optString("unique_operator_name");
////                            Operatorsid[i]=jsonobject.optString("unique_operator_id");
////                            opt_id=jsonobject.optString("unique_operator_id");
////                            region_name[i]=jsonobject.optString("region_name");
////                            region_id[i]=jsonobject.optString("region_id");
////
////                            Log.d("NAme", "onPostExecute: "+jsonobject.optString("unique_operator_name"));
////                            myListsAll1 = jsonobject.getJSONArray("topups");
////                            Price =new String[myListsAll1.length()];
////                            Talktime =new String[myListsAll1.length()];
////                            Validity =new String[myListsAll1.length()];
////                            Name =new String[myListsAll1.length()];
////                            MonthlyCoast =new String[myListsAll1.length()];
////                            ImageURL=new String[myListsAll1.length()];
////                            for(int j=0;j<myListsAll1.length();j++){
////                                jsonobject1 = (JSONObject) myListsAll1.get(j);
////                                //  Log.d("Name", "onPostExecute: "+jsonobject1.optString("name"));
////
////
////                                Price[j]="\u20B9"+jsonobject1.optString("cost");
////                                Talktime[j]="Talktime: "+jsonobject1.optString("talktime");
////                                Validity[j]="Validity: "+jsonobject1.optString("validity_string");
////                                Name[j]=jsonobject1.optString("name");
////                                MonthlyCoast[j]="Monthly Coast: "+jsonobject1.optString("monthlycost");
////                                ImageURL[j]=IMAGE_URL;
////                                // Toast.makeText(getBaseContext(), "operator_"+getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"),Toast.LENGTH_SHORT).show();
////                                //  Log.d("Name", " operator_"+getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"));
////
////
////                            }
////
////
////
////
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
//
////                    }
////
////                }
////
////
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//
//        }
//
//
//
//
//
//    }
//


    public String getOperatorID(String Key, String Type) {
        String operator_id = "1";
        switch (Type) {

            case "prepaid":

                switch (Key) {
                    case "VSP":
                    case "USP":
                    case "UGP":
                        operator_id = "0";
                        break;
                    case "ATP":
                        operator_id = "1";
                        break;
                    case "MMP":
                    case "MSP":
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
                    case "BGP":
                    case "BVP":
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

    public String getOperatorKEy(String id, String Type) {
        String operator_Key = "ACP";

        switch (Type) {
            case "prepaid":
                switch (id) {

                    case "0":
                        operator_Key = "UGP";
                        break;
                    case "1":
                        operator_Key = "ATP";
                        break;
                    case "2":
                        operator_Key = "MSP";
                        break;
                    case "3":
                        operator_Key = "RGP";
                        break;
                    case "4":
                        operator_Key = "VGP";
                        break;
                    case "5":
                        operator_Key = "ACP";
                        break;
                    case "6":
                        operator_Key = "BGP";
                        break;
                    case "7":
                        operator_Key = "IDP";
                        break;
                    case "8":
                        operator_Key = "1";
                        break;

                    case "10":
                        operator_Key = "MTP";
                        break;

                    case "12":
                        operator_Key = "TCP";
                        break;
                    case "13":
                        operator_Key = "TGP";
                        break;

                    case "15":
                        operator_Key = "VSP";
                        break;
                    case "16":
                        operator_Key = "1";
                        break;

                    case "24":
                        operator_Key = "TMP";
                        break;

                    default:
                        throw new IllegalArgumentException("Invalid Key: " + operator_Key);
                }

                break;
            case "postpaid":

                switch (id) {
                    case "1":
                        operator_Key = "ATC";
                        break;
                    case "3":
                        operator_Key = "RGC";
                        break;
                    case "4":
                        operator_Key = "VFC";
                        break;
                    case "5":
                        operator_Key = "ACC";
                        break;
                    case "7":
                        operator_Key = "IDC";
                        break;
                    case "6":
                        operator_Key = "BGC";
                        break;
                    case "13":
                        operator_Key = "TDC";
                        break;
                    case "10":
                        operator_Key = "MTC";
                        break;

                }

                break;
        }
        return operator_Key;
    }


    public String getLogoName(String Key) {
        String operator_Logo = "1";


        switch (Key) {
            case "VSP":
            case "USP":
            case "UGP":
                operator_Logo = "0";
                break;
            case "ATP":
                operator_Logo = "operator_1";
                break;
            case "MMP":
            case "MSP":
                operator_Logo = "2";
                break;
            case "RGP":
                operator_Logo = "3";
                break;
            case "VFP":
                operator_Logo = "4";
                break;
            case "ACP":
                operator_Logo = "operator_5";
                break;
            case "BGP":
            case "BVP":
                operator_Logo = "6";
                break;
            case "IDP":
                operator_Logo = "7";
                break;


            case "MTP":
                operator_Logo = "10";
                break;


            case "TCP":
                operator_Logo = "12";
                break;
            case "TSP":
            case "TGP":
                operator_Logo = "13";
                break;

            case "VGP":
                operator_Logo = "15";
                break;
            case "TMP":
            case "TVP":
                operator_Logo = "28";
                break;

            default:
                throw new IllegalArgumentException("Invalid Key: " + operator_Logo);


        }


        return operator_Logo;
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


        if (id == R.id.action_Share) {

            //    if(!tipWindow.isTooltipShown()) {
            //     tipWindow.showToolTip(v);
            //  }
            Browseplans();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Browseplans() {
        View formElementsView = BrowsePlansandPacks.this.getLayoutInflater().inflate(R.layout.browsepacks_filter, null);
        final EasyDialog easyDialog = new EasyDialog(BrowsePlansandPacks.this);

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
                DataConnectionType = "2g";
                //   Toast.makeText(getBaseContext(), "2g", Toast.LENGTH_SHORT).show();
                // alertDialog.dismiss();
            }
        });


        tv3g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2g.setBackgroundResource(R.drawable.circle);
                tv3g.setBackgroundResource(R.drawable.circle_blue_border);
                tv4g.setBackgroundResource(R.drawable.circle);
                DataConnectionType = "3g";
                //  Toast.makeText(getBaseContext(), "3g", Toast.LENGTH_SHORT).show();
                // alertDialog.dismiss();
            }
        });

        tv4g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2g.setBackgroundResource(R.drawable.circle);
                tv3g.setBackgroundResource(R.drawable.circle);

                tv4g.setBackgroundResource(R.drawable.circle_blue_border);
                DataConnectionType = "4g";
                //  Toast.makeText(getBaseContext(), "4g", Toast.LENGTH_SHORT).show();
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
                //   manager.setPreferences(getBaseContext(), "Shared_TotalMinutes", String.valueOf(progress));
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
                Total_Data = String.valueOf(progress);
                textV_Data.setText(String.valueOf(progress) + " MB.");

                // manager.setPreferences(getBaseContext(), "Shared_TotalData", String.valueOf(progress));
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

                // manager.setPreferences(getBaseContext(), "Shared_TotalSMS", String.valueOf(progress));
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


                //  Toast.makeText(getBaseContext(),"OperatorType: "+OperatorType+"\nRegionIdNo: "+RegionIdNo+"\nOperatorIdNo: "+OperatorIdNo+"\nDataConnectionType "+DataConnectionType+"\nTotalSTD_Loc_Calls "+TotalSTD_Loc_Calls+"\nTotal_Data "+Total_Data+"\nTotalSTD_Loc_SMS "+TotalSTD_Loc_SMS,Toast.LENGTH_SHORT).show();

                if (Config.getConnectivityStatus(getBaseContext())) {
                    if (Config.isOnline()) {
                        Intent plandetail = new Intent(getBaseContext(), BrowsePlansandPacks.class);
                        plandetail.putExtra("OperatorType", OperatorType);
                        plandetail.putExtra("RegionIdNo", RegionIdNo);
                        plandetail.putExtra("OperatorIdNo", OperatorIdNo);
                        plandetail.putExtra("totalminutes", TotalSTD_Loc_Calls);

                        plandetail.putExtra("DataConnectionType", DataConnectionType);

                        plandetail.putExtra("TotalSTD_Loc_Calls", TotalSTD_Loc_Calls);
                        plandetail.putExtra("Total_Data", Total_Data);
                        plandetail.putExtra("TotalSTD_Loc_SMS", TotalSTD_Loc_SMS);
                        BrowsePlansandPacks.this.finish();
                        startActivity(plandetail);


                        // easyDialog.dismiss();
                        //finish();
                        //startActivityForResult(plandetail, 2);
                        // overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);

                    } else {
                        Toast.makeText(getBaseContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                    //  startActivity(new Intent(getContext(), PlanDetailsActivity.class));

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

                LoadRegion loadRegion = new LoadRegion();
                loadRegion.execute("regin");


                LoadOperators loadOperators = new LoadOperators();
                loadOperators.execute(OperatorType);


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
                        //  manager.setPreferences(getBaseContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getBaseContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();

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
                        //  manager.setPreferences(getBaseContext(), "Shared_ConnectionType", OperatorType);
                        //   Toast.makeText(getBaseContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();

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


        easyDialog
                // .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
                .setLayout(formElementsView)
                .setBackgroundColor(BrowsePlansandPacks.this.getResources().getColor(R.color.accent))
                // .setLocation(new location[])//point in screen

                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(true)
                .setMarginLeftAndRight(24, 24)
                .setOutsideColor(BrowsePlansandPacks.this.getResources().getColor(R.color.background_color_black))
                .show();
    }


    public class LoadOperators extends AsyncTask {

        @Override
        protected void onPreExecute() {

            //    showProgress();

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

            JSONObject json = jsonParserrecomendation.makeHttpRequest(Config.PLAN_OPERATOR_KEY, "POST", params);

            // Log.d("jobject", "Do in Background: "+json);
            return json;
        }


        @Override
        protected void onPostExecute(Object o) {

            JSONArray jsonArray = null;
            String resultString = null;


            if (o != null) {
                JSONObject jresponse = (JSONObject) o;
                JSONObject jobject = null;

                try {
                    if (jresponse.getString("error").equals("1")) {//Toast.makeText(getBaseContext(),"normal: "+String.valueOf(valid),Toast.LENGTH_LONG).show();

                        return;


                    } else if (jresponse.getString("success").equals("1"))

                    {
                        try {
                            jobject = jresponse.getJSONObject("result");
                            // jobject = new JSONObject(String.valueOf(o));
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
                                spinnerMap_Opearators.put(jobj.getString("value").toUpperCase(), jobj.getString("key"));
                                spinnerArray_Oprators[i] = jobj.getString("value").toUpperCase();
                                Log.d("JsonObject", "onPostExecute123: " + spinnerArray_Oprators[i]);

                                Log.d("JsonObject", "Key NEW: " + jobj.getString("key") + " Value:" + jobj.getString("value"));
                            }


                            ArrayAdapter<String> adp_Operators = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_textview_to_spinner, spinnerArray_Oprators);
                            // adp_Regions.notifyDataSetChanged();

                            Search_Operators.setAdapter(adp_Operators);
                            Search_Operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    OperatorID = spinnerMap_Opearators.get(Search_Operators.getSelectedItem().toString());
                                    OperatorIdNo = getOperatorID(OperatorID, OperatorType);
                                    Toast.makeText(getBaseContext(), "operatorid new: " + OperatorID, Toast.LENGTH_SHORT).show();
                                    // manager.setPreferences(getContext(), "Shared_OptName", OperatorIdNo);
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


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected JSONObject doInBackground(Object[] args) {

            String identity = String.valueOf(args[0]);
            //  String password = String.valueOf(args[1]);


            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("identity", identity));


            JSONObject json = jsonParserrecomendation.makeHttpRequest(Config.LOADREGION_URL, "GET", params);

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


                    } else if (jresponse.getString("success").equals("1"))

                    {


                        JSONObject jobj = null;


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
                        Search_Region.setAdapter(adp_Regions);
                        Search_Region.setSelection(0);
                        Search_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                RegionID = spinnerMap_Region.get(Search_Region.getSelectedItem().toString());
                                RegionIdNo = HomeScreenFragment.getRegionID(RegionID);
                                // manager.setPreferences(getContext(), "Shared_RgName", RegionIdNo);
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

    private void setText() {
        int progress = bar.getProgress();
        int max = bar.getMax();
        int offset = bar.getThumbOffset();
        float percent = ((float) progress) / (float) max;
        int width = bar.getWidth() - 2 * offset;

        int answer = ((int) (width * percent + offset + floating_text_start.getWidth() + 5));

        // textV.setX(answer);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menufilter, menu);
        //   getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    public void showProgress() {

        progressDialog = new ProgressDialog(getBaseContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wailt...");
        progressDialog.show();
    }


}

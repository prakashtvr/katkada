package com.katkada.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.katkada.Other.Config;
import com.katkada.Other.JSONPARSER_Recharge;
import com.katkada.Other.JSONParser;
import com.katkada.Other.ListViewAdaptorPlanDetails;
import com.katkada.Other.PreferenceHelper;
import com.katkada.Other.SessionManager;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class PlanDetailsActivity extends AppCompatActivity {
    Context context = PlanDetailsActivity.this;
    ListView lview;
    SessionManager manager;
    ListViewAdaptorPlanDetails lviewAdapter;
    PreferenceHelper preferenceHelper;
    OkHttpClient client;
    MediaType JSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");

        lview = (ListView) findViewById(R.id.listView2);
        preferenceHelper= new PreferenceHelper(PlanDetailsActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        RecomentedPlannsResult recomentedPlans = new RecomentedPlannsResult();
        recomentedPlans.execute("");
        //   PlanDetails planDetails=new PlanDetails();
        //   planDetails.execute("");
    }
    JSONPARSER_Recharge jsonParser = new JSONPARSER_Recharge();
    //    public  class PlanDetails extends AsyncTask<Object, Object, JSONArray>
//    {
//        private ProgressDialog dialog;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//           // dialog = new ProgressDialog(getApplicationContext());
//           // dialog.setMessage("please wait...");
//           // dialog.show();
//
//        }
//
//        @Override
//        protected JSONArray doInBackground(Object... args) {
//            // String Name = String.valueOf(args[0]);
//            // String email = String.valueOf(args[1]);
//            // String password = String.valueOf(args[2]);
//            // String MobileNo = String.valueOf(args[3]);
//            //String ipAddress= args[4];
//
//            // String email = args[2];
//            //String password = args[1];
//            //  String name= args[0];
//            String Region_id="1";
//            String Oerator_id="3";
//
//            ArrayList params = new ArrayList();
//            params.add(new BasicNameValuePair("typeofplan", "mobile"));
//            params.add(new BasicNameValuePair("type", "prepaid"));
//            params.add(new BasicNameValuePair("txnid", "abc"));
//            params.add(new BasicNameValuePair("securehash", "7a4a870320e6dc86847fdc356416e063943d430edf18f1f34fbf77d5e31e500a177c6c24b6ed4556f43fef788ec7669ae16ce3f59040287e53de3f3a23c43280"));
//            params.add(new BasicNameValuePair("api_user_id", "EPj3bYVYYsA5NdLNjYCEnQCxMpL7gxV2YYRWxeebW4A3WWcr"));
//            params.add(new BasicNameValuePair("topuptype", "recharge+voucher"));
//            params.add(new BasicNameValuePair("region_id", Region_id));
//            params.add(new BasicNameValuePair("unique_provider_id",Oerator_id));
//            params.add(new BasicNameValuePair("amount", "10"));
//
//
//
//            //if(email.length()>0)
//            // params.add(new BasicNameValuePair("email",email));
//
//            JSONArray json = jsonParser.makeHttpRequest(Config.PLANS_URL_4g, "POST", params);
//
//
//            return json;
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        @Override
//        protected void onPostExecute(JSONArray o) {
//
//          //  dialog.dismiss();
//
//
//            JSONArray jsonArray=null;
//            JSONObject jresponse = null;
//            String resultString = null;
//            final String Price[],Talktime[],Validity[],Name[],Description[],Type[];
//            JSONArray myListsAll= null;
//            myListsAll = o;
//            Price=new String[myListsAll.length()];
//            Talktime=new String[myListsAll.length()];
//            Validity=new String[myListsAll.length()];
//            Name=new String[myListsAll.length()];
//            //    Description=new String[myListsAll.length()];
//
//
//            for(int i=0;i<myListsAll.length();i++){
//                JSONObject jsonobject= null;
//                try {
//                    jsonobject = (JSONObject) myListsAll.get(i);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Price[i]="\u20B9"+jsonobject.optString("price");
//                Talktime[i]="Talktime: "+jsonobject.optString("talktime");
//                Validity[i]="Validity: "+jsonobject.optString("validity_string");
//                Name[i]=jsonobject.optString("name");
//                // number[i]=jsonobject.optString("name");
//                // arrayListPrice.add(jsonobject.optString("price"));
//                String id=jsonobject.optString("name");
//                String value1=jsonobject.optString("id");
//                String value2=jsonobject.optString("price");
//
//
//                System.out.println("nid="+id);
//                System.out.println("value1="+value1);
//                System.out.println("value2="+value2);
//            }
//            lviewAdapter = new ListViewAdaptorPlanDetails(PlanDetailsActivity.this,Talktime,Name,  Validity, Price);
//
//            //  System.out.println("adapter => "+lviewAdapter.getCount());
//
//            lview.setAdapter(lviewAdapter);
////            lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                @Override
////                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//////                    Intent intent=new Intent();
//////                    intent.putExtra("AMOUNT",Price[position]);
//////                    getActivity().setResult(2,intent);
//////                    getActivity().finish();//finishing activity
////
////                    Toast.makeText(getApplicationContext(),"Title  ", Toast.LENGTH_SHORT).show();
////
////                }
////            });
//            /*
//            try {
//                Log.e("Json Response", o.toString());
//                jsonArray=new JSONArray(o);
//
//                JSONArray jArray = new JSONArray(String.valueOf(o));
//               // JSONArray jArray1 = new JSONArray(String.valueOf(o));
//
//                String name ,name2;//= jArray.getJSONObject(2).getString("name");
//
//                JSONObject json_obj = jArray.getJSONObject(2);
//                JSONObject json_obj1 = jsonArray.getJSONObject(3);
//                //get the 3rd item
//                name = json_obj.getString("name");
//                name2 = json_obj1.getString("name");
//
//
//
//                Toast.makeText(getContext(),name,Toast.LENGTH_LONG).show();
//                //txt_title.setText(name);
//               // jresponse = new JSONObject(String.valueOf(o));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                resultString = jresponse.getString("name");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                if(jresponse.getString("error").equals("1"))
//                {
//                    jresponse = new JSONObject(jresponse.getString("errors"));
//                    //     Toast.makeText(getBaseContext(),"errrrrr",Toast.LENGTH_LONG).show();
//                    // tv_Password.setError(jresponse.getString("password"));
//                    // tv_MobileNo.setError(jresponse.getString("phone"));
//
//                    Toast.makeText(getContext(),jresponse.getString("password"),Toast.LENGTH_LONG).show();
//
//
//                    Toast.makeText(getContext(),jresponse.getString("phone"),Toast.LENGTH_LONG).show();
//                } else {
//                    // Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
//                    //  tv_Email.setText("");
//                    //   tv_MobileNo.setText("");
//                    //  tv_Name.setText("");
//                    //  tv_Password.setText("");
//                    // confirmOtp();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//*/
//            //Toast.makeText(getContext(),ipAddress,Toast.LENGTH_LONG).show();
//            ////////////// Toast.makeText(getContext(),myListsAll.toString(),Toast.LENGTH_LONG).show();
//            super.onPostExecute(o);
//        }
//    }
    JSONParser jsonParserrecomendation = new JSONParser();

    public class RecomentedPlannsResult extends AsyncTask<String, Void, String> {
        private Exception exception;
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
            loading1 = ProgressDialog.show(PlanDetailsActivity.this, "Fetching Data", "Please Wait...", true, true);
            // dialog = new ProgressDialog(getBaseContext());
            // dialog.setMessage("please wait...");
            // dialog.show();
            super.onPreExecute();
        }
        protected String doInBackground(String... urls) {

            try {
             //   Bundle extras = getIntent().getExtras();

           /*    ArrayList params = new ArrayList();
                 params.add(new BasicNameValuePair("region", preferenceHelper.getRegionId()));
                params.add(new BasicNameValuePair("operator", preferenceHelper.getOperatorId()));
                params.add(new BasicNameValuePair("connection", preferenceHelper.getOperatorType()));
                params.add(new BasicNameValuePair("totalCall", extras.getString("totalCall") ));
                params.add(new BasicNameValuePair("totalSms",  extras.getString("totalSms")));

                params.add(new BasicNameValuePair("totalLocalSms", extras.getString("totalLocalSms")));
                params.add(new BasicNameValuePair("totalNatSms", extras.getString("totalNatSms")));

                params.add(new BasicNameValuePair("dataType",  extras.getString("dataType")));
                params.add(new BasicNameValuePair("totalData", extras.getString("totalData")));
                params.add(new BasicNameValuePair("storage_value", ""));
                params.add(new BasicNameValuePair("totalLocalCall", extras.getString("totalLocalCall")));
                params.add(new BasicNameValuePair("totalStdCall", extras.getString("totalStdCall")));
                //    params.add(new BasicNameValuePair("number_of_packs", "10"));
                Log.d("Recomendation", "onPostExecute: params" + params);*/
                String getResponse = post(Config.GET_RECOMENDATION_URL, urls[0]);
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
        protected void onPostExecute(String getResponse) {
            System.out.println(getResponse);
Log.v("OKHTTO RESPONSE",":->>"+getResponse);
            loading1.dismiss();
            JSONObject jresponse = null;
            JSONObject jsonObject = null;
            String resultString = null;
            JSONArray jsonArray;
            JSONArray myListsAll = null;
            JSONArray myListstopup = null;
            JSONObject jsonobject = null;
            JSONObject jsonobject1 = null;

            if (getResponse == null) {
                Toast.makeText(getBaseContext(), "Unable to get Value", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    jresponse = new JSONObject(String.valueOf(getResponse));
                    // int res = jresponse.getInt("error");
                    // Log.v("test", String.valueOf(res));
                    //  Log.d("error", "onPostExecute: " + res);
                    if (jresponse.getString("success").equals("1"))
                    {
                        jresponse = new JSONObject(String.valueOf(getResponse));
                        if (jresponse.has("data")) {
                            if (jresponse.has("data")) {
                                if (jresponse.has("data")) {
                                    if (jresponse.has("data")) {
                                        myListsAll = jresponse.getJSONObject("data").getJSONArray("plans");
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
                                        Price = new String[myListsAll.length()];
                                        Talktime = new String[myListsAll.length()];
                                        Validity = new String[myListsAll.length()];
                                        Name = new String[myListsAll.length()];
                                        MonthlyCoast = new String[myListsAll.length()];
                                        for (int k = 0; k < myListsAll.length(); k++) {
//                        JSONObject jsonobject= null;
//                        JSONObject jsonobject1= null;
                                            // Toast.makeText(getBaseContext(),  jsonobject.optString("unique_operator_name"),Toast.LENGTH_SHORT).show();
                                            jsonobject = (JSONObject) myListsAll.get(k);

                                            Name[k] = jsonobject.optString("name");
                                            packName[k] = Name[k];
                                            Validity[k] = "Validity: " + jsonobject.optString("validity_string");
                                            packValidity[k] = Validity[k];
                                            Price[k] = "\u20B9" + jsonobject.optString("cost");
                                            packPrice[k] = Price[k];

                                            MonthlyCoast[k] = "Monthly Coast: " + jsonobject.optString("estimated_cost");
                                            packMonthlyCoast[k] = MonthlyCoast[k];
                                            pricerecharge[k] = jsonobject.optString("estimated_cost");
                                            OperatorsName[k]=jsonobject.optString("operator_id");
                                            EstimatedCost[k] = jsonobject.optString("estimated_cost");


                                            // region_name[i] = jsonobject.optString("estimated_cost");
                                            //  region_id[i] = jsonobject.optString("estimated_cost");

                                            // loadImageLogoURL.execute(getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"));
                                            Log.d("", "onPostExecute: " + jsonobject.optString("estimated_cost"));
//                                            myListstopup = jsonobject.getJSONArray("addons");
//                                            Price = new String[myListstopup.length()];
//                                            Talktime = new String[myListstopup.length()];
//                                            Validity = new String[myListstopup.length()];
//                                            Name = new String[myListstopup.length()];
//                                            MonthlyCoast = new String[myListstopup.length()];
                                          /*  for (int j = 0; j < myListstopup.length(); j++) {
                                                jsonobject1 = (JSONObject) myListstopup.get(j);
                                                //  Log.d("Name", "onPostExecute: "+jsonobject1.optString("name"));
//                                                Price[j] = "\u20B9" + jsonobject1.optString("cost");
//                                                packPrice[i] = Price[j];
//                                                pricerecharge[i] = jsonobject1.optString("estimated_cost");
                                                Log.d("Name", "inner for loop: " + Price[j]);
                                                Talktime[j] = "Talktime: " + jsonobject1.optString("estimated_cost");
                                                packTalktime[i] = Talktime[j];
//                                                Validity[j] = "Validity: " + jsonobject1.optString("validity_string");
//                                                packValidity[i] = Validity[j];
//                                                Name[j] = jsonobject1.optString("name");
//                                                packName[i] = Name[j];
//                                                MonthlyCoast[j] = "Monthly Coast: " + jsonobject1.optString("total_cost");
//                                                packMonthlyCoast[i] = MonthlyCoast[j];
                                              //  ImageURL[i] = "http://katkada.com/assets/public//images/operator/operator_5.png";
                                                // Toast.makeText(getBaseContext(), "operator_"+getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"),Toast.LENGTH_SHORT).show();
                                                //  Log.d("Name", " operator_"+getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"));
                                                // lviewAdapter = new ListViewAdaptorPlanDetails(BrowsePlansandPacks.this,Price,Name,  Validity, Price);
                                                // lviewAdapter = new ListViewAdaptorPlanDetails(BrowsePlansandPacks.this,packPrice,packPrice,  packPrice, packPrice);
                                                //   lviewAdapter.notifyDataSetChanged();
                                            }*/
                                        }
                                        lviewAdapter = new ListViewAdaptorPlanDetails(PlanDetailsActivity.this, packPrice, packName, packValidity, pricerecharge, packMonthlyCoast, OperatorsName);
                                        //  System.out.println("adapter => "+lviewAdapter.getCount());
                                        lview.setAdapter(lviewAdapter);
                                    } else if (jresponse.has("error_code")) {
                                        Toast.makeText(getBaseContext(), "No Result Found 1", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getBaseContext(), "No Result Found 2", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getBaseContext(), "No Result Found 3", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "No Result Found 4", Toast.LENGTH_SHORT).show();
                        }
//                    LoadImageLogoURL loadImageLogoURL=new LoadImageLogoURL();
//no_resolt=false
                        //  Toast.makeText(getBaseContext(), jresponse.getString("no_results"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
           // Toast.makeText(getBaseContext(),""+getResponse,Toast.LENGTH_LONG).show();
        }
        private String post(String url, String json) throws IOException {
            Bundle extras = getIntent().getExtras();
            //   RequestBody body = RequestBody.create(JSON, json);
            RequestBody formBody = new FormBody.Builder()
                    .add("region", preferenceHelper.getRegionId())
                    .add("operator", preferenceHelper.getOperatorId())
                    .add("connection",  preferenceHelper.getOperatorType())
                    .add("totalCall", extras.getString("totalCall"))
                    .add("totalSms", extras.getString("totalSms"))
                    .add("totalLocalSms",  extras.getString("totalLocalSms"))
                    .add("totalNatSms", extras.getString("totalNatSms"))
                    .add("dataType",  extras.getString("dataType"))
                    .add("totalData",extras.getString("totalData"))
                    .add("storage_value", "")
                    .add("totalLocalCall", extras.getString("totalLocalCall"))
                    .add("totalStdCall", extras.getString("totalStdCall"))


                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

     /*   @Override
        protected Object doInBackground(Object[] args) {

            Bundle extras = getIntent().getExtras();

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("region", preferenceHelper.getRegionId()));
            params.add(new BasicNameValuePair("operator", preferenceHelper.getOperatorId()));
            params.add(new BasicNameValuePair("connection", preferenceHelper.getOperatorType()));
            params.add(new BasicNameValuePair("totalCall", extras.getString("totalCall") ));
            params.add(new BasicNameValuePair("totalSms",  extras.getString("totalSms")));

            params.add(new BasicNameValuePair("totalLocalSms", extras.getString("totalLocalSms")));
            params.add(new BasicNameValuePair("totalNatSms", extras.getString("totalNatSms")));

            params.add(new BasicNameValuePair("dataType",  extras.getString("dataType")));
            params.add(new BasicNameValuePair("totalData", extras.getString("totalData")));
            params.add(new BasicNameValuePair("storage_value", ""));
            params.add(new BasicNameValuePair("totalLocalCall", extras.getString("totalLocalCall")));
            params.add(new BasicNameValuePair("totalStdCall", extras.getString("totalStdCall")));
            //    params.add(new BasicNameValuePair("number_of_packs", "10"));
            Log.d("Recomendation", "onPostExecute: params" + params);
            JSONObject json = jsonParserrecomendation.makeHttpRequest(Config.GET_RECOMENDATION_URL, "POST", params);
            Log.d("Recomendation", "onPostExecute: json" + json);
            return json;
        }*/
       // @Override
//        protected void onPostExecute(Object o) {
//            /*loading1.dismiss();
//            JSONObject jresponse = null;
//            JSONObject jsonObject = null;
//            String resultString = null;
//            JSONArray jsonArray;
//            JSONArray myListsAll = null;
//            JSONArray myListstopup = null;
//            JSONObject jsonobject = null;
//            JSONObject jsonobject1 = null;
//
//            if (o == null) {
//                Toast.makeText(getBaseContext(), "Unable to get Value", Toast.LENGTH_SHORT).show();
//            } else {
//                try {
//                    jresponse = new JSONObject(String.valueOf(o));
//                   // int res = jresponse.getInt("error");
//                   // Log.v("test", String.valueOf(res));
//                  //  Log.d("error", "onPostExecute: " + res);
//                    if (jresponse.getString("success").equals("1"))
//                    {
//                       jresponse = new JSONObject(String.valueOf(o));
//                        if (jresponse.has("data")) {
//                            if (jresponse.has("data")) {
//                                if (jresponse.has("data")) {
//                                    if (jresponse.has("data")) {
//                                        myListsAll = jresponse.getJSONObject("data").getJSONArray("plans");
//                                        EstimatedCost = new String[myListsAll.length()];
//                                        OperatorsName = new String[myListsAll.length()];
//                                        Operatorsid = new String[myListsAll.length()];
//                                        region_name = new String[myListsAll.length()];
//                                        region_id = new String[myListsAll.length()];
//                                        packPrice = new String[myListsAll.length()];
//                                        packTalktime = new String[myListsAll.length()];
//                                        packName = new String[myListsAll.length()];
//                                        packValidity = new String[myListsAll.length()];
//                                        packMonthlyCoast = new String[myListsAll.length()];
//                                        pricerecharge = new String[myListsAll.length()];
//                                        ImageURL = new String[myListsAll.length()];
//                                        Price = new String[myListsAll.length()];
//                                        Talktime = new String[myListsAll.length()];
//                                        Validity = new String[myListsAll.length()];
//                                        Name = new String[myListsAll.length()];
//                                        MonthlyCoast = new String[myListsAll.length()];
//                                        for (int k = 0; k < myListsAll.length(); k++) {
////                        JSONObject jsonobject= null;
////                        JSONObject jsonobject1= null;
//                                            // Toast.makeText(getBaseContext(),  jsonobject.optString("unique_operator_name"),Toast.LENGTH_SHORT).show();
//                                            jsonobject = (JSONObject) myListsAll.get(k);
//
//                                            Name[k] = jsonobject.optString("name");
//                                               packName[k] = Name[k];
//                                            Validity[k] = "Validity: " + jsonobject.optString("validity_string");
//                                            packValidity[k] = Validity[k];
//                                            Price[k] = "\u20B9" + jsonobject.optString("cost");
//                                            packPrice[k] = Price[k];
//
//                                            MonthlyCoast[k] = "Monthly Coast: " + jsonobject.optString("estimated_cost");
//                                            packMonthlyCoast[k] = MonthlyCoast[k];
//                                            pricerecharge[k] = jsonobject.optString("estimated_cost");
//
//                                            EstimatedCost[k] = jsonobject.optString("estimated_cost");
//
//
//
//
//
//
//
//                                           // region_name[i] = jsonobject.optString("estimated_cost");
//                                          //  region_id[i] = jsonobject.optString("estimated_cost");
//
//                                            // loadImageLogoURL.execute(getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"));
//                                            Log.d("", "onPostExecute: " + jsonobject.optString("estimated_cost"));
////                                            myListstopup = jsonobject.getJSONArray("addons");
////                                            Price = new String[myListstopup.length()];
////                                            Talktime = new String[myListstopup.length()];
////                                            Validity = new String[myListstopup.length()];
////                                            Name = new String[myListstopup.length()];
////                                            MonthlyCoast = new String[myListstopup.length()];
//                                          *//*  for (int j = 0; j < myListstopup.length(); j++) {
//                                                jsonobject1 = (JSONObject) myListstopup.get(j);
//                                                //  Log.d("Name", "onPostExecute: "+jsonobject1.optString("name"));
////                                                Price[j] = "\u20B9" + jsonobject1.optString("cost");
////                                                packPrice[i] = Price[j];
////                                                pricerecharge[i] = jsonobject1.optString("estimated_cost");
//                                                Log.d("Name", "inner for loop: " + Price[j]);
//                                                Talktime[j] = "Talktime: " + jsonobject1.optString("estimated_cost");
//                                                packTalktime[i] = Talktime[j];
////                                                Validity[j] = "Validity: " + jsonobject1.optString("validity_string");
////                                                packValidity[i] = Validity[j];
////                                                Name[j] = jsonobject1.optString("name");
////                                                packName[i] = Name[j];
////                                                MonthlyCoast[j] = "Monthly Coast: " + jsonobject1.optString("total_cost");
////                                                packMonthlyCoast[i] = MonthlyCoast[j];
//                                              //  ImageURL[i] = "http://katkada.com/assets/public//images/operator/operator_5.png";
//                                                // Toast.makeText(getBaseContext(), "operator_"+getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"),Toast.LENGTH_SHORT).show();
//                                                //  Log.d("Name", " operator_"+getOperatorID( getOperatorKEy(opt_id,"prepaid"),"prepaid"));
//                                                // lviewAdapter = new ListViewAdaptorPlanDetails(BrowsePlansandPacks.this,Price,Name,  Validity, Price);
//                                                // lviewAdapter = new ListViewAdaptorPlanDetails(BrowsePlansandPacks.this,packPrice,packPrice,  packPrice, packPrice);
//                                                //   lviewAdapter.notifyDataSetChanged();
//                                            }*//*
//                                        }
//                                        lviewAdapter = new ListViewAdaptorPlanDetails(PlanDetailsActivity.this, packPrice, packName, packValidity, pricerecharge, packMonthlyCoast, OperatorsName);
//                                        //  System.out.println("adapter => "+lviewAdapter.getCount());
//                                        lview.setAdapter(lviewAdapter);
//                                    } else if (jresponse.has("error_code")) {
//                                        Toast.makeText(getBaseContext(), "No Result Found 1", Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    Toast.makeText(getBaseContext(), "No Result Found 2", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(getBaseContext(), "No Result Found 3", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(getBaseContext(), "No Result Found 4", Toast.LENGTH_SHORT).show();
//                        }
////                    LoadImageLogoURL loadImageLogoURL=new LoadImageLogoURL();
////no_resolt=false
//                        //  Toast.makeText(getBaseContext(), jresponse.getString("no_results"),Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }*/
//        }
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
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

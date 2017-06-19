package com.katkada.Fragment;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.AdapterView.OnItemSelectedListener;

import com.katkada.Activity.PrepaidMobilePacks;
import com.katkada.Activity.Register;
import com.katkada.Other.Config;
import com.katkada.Other.JSONParser;
import com.katkada.R;

import android.widget.RadioGroup.OnCheckedChangeListener;

import static android.app.Activity.RESULT_OK;
public class Recharge extends Fragment {
    //SegmentedRadioGroup segmentText;
    Toast mToast;
    public Recharge() {
        // Required empty public constructor
    }
    public static String OperatorId = "1";
    private RadioGroup radioGroup1;
    RelativeLayout rl;
    EditText et_MobileNo, et_value;
    Spinner sp, sp_region;
    // List<String> list;
    InputStream is = null;
    String result = null;
    String line = null;
    ImageView loadContact;
    Button btn_Rechrge;
    TextView Browse_Packs;
    private static final String TAG = Register.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    private static final int PICK_CONTACT = 1234;
    //JSONParser jsonParser = new JSONParser();
    AlertDialog dialog;
    static CountDownTimer timer = null;
    Toast toast;
    public static String OperatorID, RegionID;
    public static List<String> list, Regions, Operators;
    public String[] spinnerArray_Oprators;
    public final HashMap<String, String> spinnerMap_Opearators = new HashMap<String, String>();
    //  TalkTimeFragment.DisplayPlans displayPlans;
    JSONParser jsonParser = new JSONParser();
    public static String[] spinnerArray_Region;
    public final static HashMap<String, String> spinnerMap_Region = new HashMap<String, String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // (getActivity()).getActionBar().setDisplayHomeAsUpEnabled(true);
        // getActivity(). getActionBar().setHomeButtonEnabled(true);
        ContentResolver contactResolver;
        contactResolver = getActivity().getContentResolver();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recharge, container, false);
        //   TextView view = (TextView)rootView.findViewById(R.id.ripple_layout_2);
        //TextView mobile = (TextView)rootView.findViewById(R.id.tv_mobile);
        radioGroup1 = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
        // TextView Datacard = (TextView)rootView.findViewById(R.id.tv_Datacard);
        Browse_Packs = (TextView) rootView.findViewById(R.id.tv_Browse_Packs);
        btn_Rechrge = (Button) rootView.findViewById(R.id.button_rechrge);
        et_MobileNo = (EditText) rootView.findViewById(R.id.editText_MobileNo);
        et_value = (EditText) rootView.findViewById(R.id.editValue);
        sp = (Spinner) rootView.findViewById(R.id.spinner_operator);
        sp_region = (Spinner) rootView.findViewById(R.id.spinner_Region);
        LoadRegion loadRegion = new LoadRegion();
        loadRegion.execute("regin");
        LoadOperators loadOperators = new LoadOperators();
        loadOperators.execute("prepaid");
        list = new ArrayList<String>();
        list.add("Airtel");
        list.add("MTNL");
        list.add("Reliance GSM");
        list.add("Vodafone");
        list.add("Aircel");
        list.add("BSNL");
        list.add("Idea");
        list.add("Loop Mobile");
        list.add("MTS");
        list.add("Tata Docomo on CDMA");
        list.add("Tata Docomo");
        list.add("Uninor");
        list.add("Videocon");
        list.add("S Tel");
        list.add("Reliance CDMA");
        list.add("VIrgin Mobile GSM");
        list.add("VIrgin Mobile CDMA");
        //  ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, list);
        // sp.setAdapter(adp);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(getContext(), list.get(arg2).toString(),
                        Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        //  MaterialRippleLayout.on(mobile)
        //      .rippleColor(Color.parseColor("#FF0000"))
        //      .rippleAlpha(0.2f)
        //       .rippleHover(true)
        //      .create();
        //   MaterialRippleLayout.on(view)
        ////           .rippleColor(Color.parseColor("#FF0000"))
        //         .rippleAlpha(0.2f)
        //         .rippleHover(true)
        //        .create();
        radioGroup1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioAndroid:
                        Browse_Packs.setVisibility(View.VISIBLE);
                        btn_Rechrge.setText("Recharge");
                        //   Toast.makeText(getContext(), "prepaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioiPhone:
                        Browse_Packs.setVisibility(View.GONE);
                        btn_Rechrge.setText("Pay Bill");
                        // Browse_Packs.setVisibility(View.INVISIBLE);
                        // Intent i=new Intent(getContext(), ListviewcheckboxExample.class);
                        // startActivity(i);
                        //  Toast.makeText(getContext(), "Postpaid RadioButton checked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        btn_Rechrge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final CharSequence PaymentMethod[] = new CharSequence[] {"Wallet", "CC_Avenue"};
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Choose Payment Method");
//                builder.setItems(PaymentMethod, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // the user clicked on colors[which]
//                        if(PaymentMethod[1].equals("Wallet"))
//                        {
//
//                            Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            Toast.makeText(getContext(),"2",Toast.LENGTH_SHORT).show();}
//
//                    }
//                });
//                builder.show();
//
///*
//                String Currency="INR";
//                String merchant_id="123";
//                String Access_COde="123";
//
//                String redirectUrl="http://122.182.6.216/merchant/ccavResponseHandler.jsp";
//                String cancelUrl="http://122.182.6.216/merchant/ccavResponseHandler.jsp";
//                String rsaKeyUrl="http://122.182.6.216/merchant/GetRSA.jsp";
//                String orderId="123";
//
//             //   Intent i=new Intent(getContext(), InitialScreenActivity.class);
//                //startActivity(i);
//                //Mandatory parameters. Other parameters can be added if required.
//                String vAccessCode = ServiceUtility.chkNull(Access_COde).toString().trim();
//                String vMerchantId = ServiceUtility.chkNull(merchant_id).toString().trim();
//                String vCurrency = ServiceUtility.chkNull(Currency).toString().trim();
//                String vAmount = ServiceUtility.chkNull(et_value.getText()).toString().trim();
//                if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
//                    Intent intent = new Intent(getContext(),WebViewActivity.class);
//                    intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(Access_COde).toString().trim());
//                    intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchant_id).toString().trim());
//                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId).toString().trim());
//                    intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(Currency).toString().trim());
//                    intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(et_value.getText()).toString().trim());
//
//                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl).toString().trim());
//                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl).toString().trim());
//                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl).toString().trim());
//
//                    startActivity(intent);
//                }else{
//                    showToast("All parameters are mandatory.");
//                }
//                */
//
//
//               // Intent i=new Intent(getContext(),InitialScreenActivity.class);
//             //   startActivity(i);
            }
            public void showToast(String msg) {
                Toast.makeText(getContext(), "Toast: " + msg, Toast.LENGTH_LONG).show();
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
                    (getContext(), android.R.layout.simple_dropdown_item_1line, list1);
            sp.setAdapter(adp2);
            sp.setOnItemSelectedListener(new OnItemSelectedListener() {
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
//
//        loadContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//// using native contacts selection
//                // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
//                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
//          }
//        });
        Browse_Packs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Retriving...", Toast.LENGTH_SHORT).show();
                Thread t = new Thread() {
                    public void run() {
                        try {
                            sleep(300);
                            //finish();
                            //Intent intent=new Intent(getContext(),SecondActivity.class);
                            // startActivityForResult(intent, 2);/
                            Intent i = new Intent(getContext(), PrepaidMobilePacks.class);
                            // startActivity(i);
                            startActivityForResult(i, 2);
                            getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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
        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();
            retrieveContactName();
            retrieveContactNumber();
            // retrieveContactPhoto();
        } else if (requestCode == 2) {
            String amount = data.getStringExtra("AMOUNT");
            et_value.setText(amount);
            //sp.setSelection(Integer.parseInt(data.getStringExtra("OPERATOR")));
        }
    }
    private void retrieveContactPhoto() {
        Bitmap photo = null;
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                // ImageView imageView = (ImageView)  findViewById(R.id.img_contact);
                loadContact.setImageBitmap(photo);
            }
            assert inputStream != null;
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void retrieveContactNumber() {
        String contactNumber = null;
        // getting contacts ID
        Cursor cursorID = getActivity().getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        Log.d(TAG, "Contact ID: " + contactID);
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID},
                null);
        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        Log.d(TAG, "Contact Phone Number: " + contactNumber);
        et_MobileNo.setText(contactNumber);
    }
    private void retrieveContactName() {
        String contactName = null;
        // querying contact data store
        Cursor cursor = getActivity().getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        Log.d(TAG, "Contact Name: " + contactName);
    }
    public class DisplayPlans extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            // String Name = String.valueOf(args[0]);
            // String email = String.valueOf(args[1]);
            // String password = String.valueOf(args[2]);
            // String MobileNo = String.valueOf(args[3]);
            //String ipAddress= args[4];
            // String email = args[2];
            //String password = args[1];
            //  String name= args[0];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("typeofplan", "mobile"));
            params.add(new BasicNameValuePair("type", "prepaid"));
            params.add(new BasicNameValuePair("txnid", "abc"));
            params.add(new BasicNameValuePair("securehash", "7a4a870320e6dc86847fdc356416e063943d430edf18f1f34fbf77d5e31e500a177c6c24b6ed4556f43fef788ec7669ae16ce3f59040287e53de3f3a23c43280"));
            params.add(new BasicNameValuePair("api_user_id", "EPj3bYVYYsA5NdLNjYCEnQCxMpL7gxV2YYRWxeebW4A3WWcr"));
            params.add(new BasicNameValuePair("topuptype", "recharge+voucher"));
            params.add(new BasicNameValuePair("region_id", "1"));
            params.add(new BasicNameValuePair("unique_provider_id", "1"));
            params.add(new BasicNameValuePair("amount", "10"));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONObject json = jsonParser.makeHttpRequest(Config.PLANS_URL, "POST", params);
            return json;
        }
        @Override
        protected void onPostExecute(Object o) {
            JSONObject jresponse = null;
            String resultString = null;
            try {
                jresponse = new JSONObject(String.valueOf(o));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                resultString = jresponse.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (jresponse.getString("error").equals("1")) {
                    jresponse = new JSONObject(jresponse.getString("errors"));
                    //     Toast.makeText(getBaseContext(),"errrrrr",Toast.LENGTH_LONG).show();
                    // tv_Password.setError(jresponse.getString("password"));
                    // tv_MobileNo.setError(jresponse.getString("phone"));
                    Toast.makeText(getContext(), jresponse.getString("password"), Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), jresponse.getString("phone"), Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
                    //  tv_Email.setText("");
                    //   tv_MobileNo.setText("");
                    //  tv_Name.setText("");
                    //  tv_Password.setText("");
                    // confirmOtp();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getContext(),ipAddress,Toast.LENGTH_LONG).show();
            //   Toast.makeText(getContext(),resultString.toString(),Toast.LENGTH_LONG).show();
            super.onPostExecute(o);
        }
    }
    public class LoadOperators extends AsyncTask {
        @Override
        protected void onPreExecute() {
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
            JSONObject json = jsonParser.makeHttpRequest(Config.LOADOPERATORS_URL, "GET", params);
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
                            spinnerArray_Oprators = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jobj = jsonArray.getJSONObject(i);
                                spinnerMap_Opearators.put(jobj.getString("value"), jobj.getString("key"));
                                spinnerArray_Oprators[i] = jobj.getString("value");
                                Log.d("JsonObject", "onPostExecute: " + spinnerArray_Oprators[i]);
                                Log.d("JsonObject", "test: " + spinnerMap_Opearators.get(jobj.getString("key")));
                            }
                            ArrayAdapter<String> adp_Operators = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, spinnerArray_Oprators);
                            // adp_Regions.notifyDataSetChanged();
                            sp.setAdapter(adp_Operators);
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
    public class LoadRegion extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Object[] args) {
            String identity = String.valueOf(args[0]);
            //  String password = String.valueOf(args[1]);
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("identity", identity));
            // params.add(new BasicNameValuePair("password", email));
            //    params.add(new BasicNameValuePair("password", password));
            // params.add(new BasicNameValuePair("phone", MobileNo));
            /////////////////// params.add(new BasicNameValuePair("ip_address", ipAddress));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONObject json = jsonParser.makeHttpRequest(Config.LOADREGION_URL, "GET", params);
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
                            spinnerArray_Region = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jobj = jsonArray.getJSONObject(i);
                                spinnerMap_Region.put(jobj.getString("value"), jobj.getString("key"));
                                spinnerArray_Region[i] = jobj.getString("value");
                                Log.d("JsonObject", "onPostExecute: " + spinnerArray_Region[i]);
                            }
                            ArrayAdapter<String> adp_Regions = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, spinnerArray_Region);
                            // adp_Regions.notifyDataSetChanged();
                            sp_region.setAdapter(adp_Regions);
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
}

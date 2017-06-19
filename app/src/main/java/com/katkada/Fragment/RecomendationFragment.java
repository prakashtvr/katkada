package com.katkada.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.katkada.Other.Config;
import com.katkada.Other.JSONPARSER_Recharge;
import com.katkada.Other.ListViewAdapter;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class RecomendationFragment extends Fragment {
    ListView lview;
    ListViewAdapter lviewAdapter;
    private RadioGroup radioGroup1;
    public RecomendationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recomendation, container, false);
        // Get System TELEPHONY service reference
        TelephonyManager tManager = (TelephonyManager) getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
// Get carrier name (Network Operator Name)
        String carrierName = tManager.getNetworkOperatorName();
// Get Phone model and manufacturer name
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        ///  Toast.makeText(getContext(), "carrierName   "+carrierName, Toast.LENGTH_SHORT).show();
        ///   Toast.makeText(getContext(), "manufacturer  "+manufacturer, Toast.LENGTH_SHORT).show();
        /// Toast.makeText(getContext(), "carrmodelierName  "+model, Toast.LENGTH_SHORT).show();
        // RecomendationFragment.DisplayPlans displayPlans=new RecomendationFragment.DisplayPlans();
        //  displayPlans.execute();
        return rootView;
    }
    JSONPARSER_Recharge jsonParser = new JSONPARSER_Recharge();
    public class DisplayPlans extends AsyncTask<Object, Object, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONArray doInBackground(Object... args) {
            // String Name = String.valueOf(args[0]);
            // String email = String.valueOf(args[1]);
            // String password = String.valueOf(args[2]);
            // String MobileNo = String.valueOf(args[3]);
            //String ipAddress= args[4];
            // String email = args[2];
            //String password = args[1];
            //  String name= args[0];
            String Region_id = "1";
            String Oerator_id = "3";
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("typeofplan", "mobile"));
            params.add(new BasicNameValuePair("type", "prepaid"));
            params.add(new BasicNameValuePair("txnid", "abc"));
            params.add(new BasicNameValuePair("securehash", "7a4a870320e6dc86847fdc356416e063943d430edf18f1f34fbf77d5e31e500a177c6c24b6ed4556f43fef788ec7669ae16ce3f59040287e53de3f3a23c43280"));
            params.add(new BasicNameValuePair("api_user_id", "EPj3bYVYYsA5NdLNjYCEnQCxMpL7gxV2YYRWxeebW4A3WWcr"));
            params.add(new BasicNameValuePair("topuptype", "recharge+voucher"));
            params.add(new BasicNameValuePair("region_id", Region_id));
            params.add(new BasicNameValuePair("unique_provider_id", Oerator_id));
            params.add(new BasicNameValuePair("amount", "10"));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONArray json = jsonParser.makeHttpRequest(Config.PLANS_URL, "POST", params);
            return json;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(JSONArray o) {
            if (o != null) {
                JSONArray jsonArray = null;
                JSONObject jresponse = null;
                String resultString = null;
                final String Price[], Talktime[], Validity[], Name[], Description[], Type[];
                JSONArray myListsAll = null;
                myListsAll = o;
                Price = new String[myListsAll.length()];
                Talktime = new String[myListsAll.length()];
                Validity = new String[myListsAll.length()];
                Name = new String[myListsAll.length()];
                //    Description=new String[myListsAll.length()];
                for (int i = 0; i < myListsAll.length(); i++) {
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = (JSONObject) myListsAll.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Price[i] = "\u20B9" + jsonobject.optString("price");
                    Talktime[i] = "Talktime: " + jsonobject.optString("talktime");
                    Validity[i] = "Validity: " + jsonobject.optString("validity_string");
                    Name[i] = jsonobject.optString("name");
                    // number[i]=jsonobject.optString("name");
                    // arrayListPrice.add(jsonobject.optString("price"));
                    String id = jsonobject.optString("name");
                    String value1 = jsonobject.optString("id");
                    String value2 = jsonobject.optString("price");
                    String value3 = jsonobject.optString("talktime");
                    String value4 = jsonobject.optString("validity_string");
                    String value5 = jsonobject.optString("description");
                    String value6 = jsonobject.optString("recharge_type");
                    String value7 = jsonobject.optString("recharge_type");
                    System.out.println("nid=" + id);
                    System.out.println("value1=" + value1);
                    System.out.println("value2=" + value2);
                }
                lviewAdapter = new ListViewAdapter((Activity) getContext(), Price, Talktime, Name, Validity, "prepaid");
                //  System.out.println("adapter => "+lviewAdapter.getCount());
                lview.setAdapter(lviewAdapter);
                lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("AMOUNT", Price[position]);
                        getActivity().setResult(2, intent);
                        getActivity().finish();//finishing activity
                        //  Toast.makeText(getContext(),"Title => "+month[position]+"=> n setOnItemClickListener:"+number[position], Toast.LENGTH_SHORT).show();
                    }
                });
            /*
            try {
                Log.e("Json Response", o.toString());
                jsonArray=new JSONArray(o);

                JSONArray jArray = new JSONArray(String.valueOf(o));
               // JSONArray jArray1 = new JSONArray(String.valueOf(o));

                String name ,name2;//= jArray.getJSONObject(2).getString("name");

                JSONObject json_obj = jArray.getJSONObject(2);
                JSONObject json_obj1 = jsonArray.getJSONObject(3);
                //get the 3rd item
                name = json_obj.getString("name");
                name2 = json_obj1.getString("name");



                Toast.makeText(getContext(),name,Toast.LENGTH_LONG).show();
                //txt_title.setText(name);
               // jresponse = new JSONObject(String.valueOf(o));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                resultString = jresponse.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if(jresponse.getString("error").equals("1"))
                {
                    jresponse = new JSONObject(jresponse.getString("errors"));
                    //     Toast.makeText(getBaseContext(),"errrrrr",Toast.LENGTH_LONG).show();
                    // tv_Password.setError(jresponse.getString("password"));
                    // tv_MobileNo.setError(jresponse.getString("phone"));

                    Toast.makeText(getContext(),jresponse.getString("password"),Toast.LENGTH_LONG).show();


                    Toast.makeText(getContext(),jresponse.getString("phone"),Toast.LENGTH_LONG).show();
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
*/
            } else {
                Toast.makeText(getContext(), "Unable To Load Value", Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(getContext(),ipAddress,Toast.LENGTH_LONG).show();
            ////////////// Toast.makeText(getContext(),myListsAll.toString(),Toast.LENGTH_LONG).show();
            super.onPostExecute(o);
        }
    }
}

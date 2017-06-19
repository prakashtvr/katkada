package com.katkada.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.katkada.Activity.RechargeNow;
import com.katkada.Other.Config;
import com.katkada.Other.JSONPARSER_Recharge;
import com.katkada.Other.ListViewAdapter;
import com.katkada.Other.SessionManager;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class PopularFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public PopularFragment() {
        // Required empty public constructor
    }
    ListView lview;
    ListViewAdapter lviewAdapter;
    private final static String month[] = {"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10", "11", "12"};
    private final static String number[] = {"LIFE TIME", "LIFE TIME", "LIFE TIME",
            "LIFE TIME", "LIFE TIME", "LIFE TIME",
            "LIFE TIME", "LIFE TIME", "LIFE TIME",
            "LIFE TIME", "LIFE TIME", "LIFE TIME"};
    //  ListView lview3;
    //ListViewCustomAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
        lview = (ListView) rootView.findViewById(R.id.listView2);
        // lviewAdapter = new ListViewAdapter((Activity) getContext(), month, number,month, number);
        // System.out.println("adapter => "+lviewAdapter.getCount());
        //lview.setAdapter(lviewAdapter);
        lview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Title => " + month[position] + "=> n setOnItemSelectedListener" + number[position], Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        PopularFragment.DisplayPlans displayPlans = new PopularFragment.DisplayPlans();
        displayPlans.execute();
        // lview.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        return rootView;
    }
    JSONPARSER_Recharge jsonParser = new JSONPARSER_Recharge();
    public class DisplayPlans extends AsyncTask<Object, Object, JSONArray> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("please wait...");
            dialog.show();
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
          //  String Region_id = new SessionManager().getPreferences(getContext(), "RegionIdNo");
          //  String Oerator_id = new SessionManager().getPreferences(getContext(), "OperatorIdNo");
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("typeofplan", "mobile"));
            params.add(new BasicNameValuePair("type", Config.OperatorType));
            params.add(new BasicNameValuePair("txnid", "abc"));
            params.add(new BasicNameValuePair("securehash", "7a4a870320e6dc86847fdc356416e063943d430edf18f1f34fbf77d5e31e500a177c6c24b6ed4556f43fef788ec7669ae16ce3f59040287e53de3f3a23c43280"));
            params.add(new BasicNameValuePair("api_user_id", "EPj3bYVYYsA5NdLNjYCEnQCxMpL7gxV2YYRWxeebW4A3WWcr"));
            params.add(new BasicNameValuePair("topuptype", "recharge+voucher"));
            params.add(new BasicNameValuePair("unique_provider_id", Config.OperatorIdno));
            params.add(new BasicNameValuePair("region_id", Config.Regionidno));
            params.add(new BasicNameValuePair("amount", "10"));
            //if(email.length()>0)
            // params.add(new BasicNameValuePair("email",email));
            JSONArray json = jsonParser.makeHttpRequest(Config.PLANS_URL_popular_recharges, "POST", params);
            return json;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(JSONArray o) {
            dialog.dismiss();
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
                    Price[i] = jsonobject.optString("price");
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
                        Intent i = new Intent(getActivity(), RechargeNow.class);
                        i.putExtra("Selected_Value", Price[position]);
                        getActivity().finish();
                        startActivity(i);
                        //    Toast.makeText(getContext(),"Title => "+month[position]+"=> n setOnItemClickListener:"+number[position], Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Unable To Load Value", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(o);
        }
    }
}

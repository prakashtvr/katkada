package com.katkada.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.katkada.Activity.RechargeNow;
import com.katkada.Other.AndyUtils;
import com.katkada.Other.Config;
import com.katkada.Other.JSONParser;
import com.katkada.Other.ListViewAdapter;
import com.katkada.Other.PreferenceHelper;
import com.katkada.Other.SessionManager;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class FultalktimeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SessionManager manager;
    public FultalktimeFragment() {
        // Required empty public constructor
    }
    ListView lview;
    ListViewAdapter lviewAdapter;

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
        View rootView = inflater.inflate(R.layout.fragment_fultalktime, container, false);
        lview = (ListView) rootView.findViewById(R.id.listView2);
        manager = new SessionManager();
        lview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //     Toast.makeText(getContext(),"Title => "+month[position]+"=> n setOnItemSelectedListener"+number[position], Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        FultalktimeFragment.DisplayPlans displayPlans = new FultalktimeFragment.DisplayPlans();
        displayPlans.execute();
        // lview.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        return rootView;
    }
    JSONParser jsonParser = new JSONParser();
    public class DisplayPlans extends AsyncTask<Object, Object, JSONObject> {
        PreferenceHelper preferenceHelper=new PreferenceHelper(getContext());
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(getContext());
//            dialog.setMessage("please wait...");
//            dialog.show();
           // AndyUtils.openloadingDialog(getContext());
        }
        @Override
        protected JSONObject doInBackground(Object... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("region", preferenceHelper.getRegionId()));
            params.add(new BasicNameValuePair("operator", preferenceHelper.getOperatorId()));
            params.add(new BasicNameValuePair("connection_type", preferenceHelper.getOperatorType()));
            params.add(new BasicNameValuePair("operation", preferenceHelper.getOperation_method()));
            params.add(new BasicNameValuePair("plan_type", "15"));
            JSONObject json = jsonParser.makeHttpRequest(Config.PLAN_SEARCH, "POST", params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject o) {
          //  AndyUtils.closeloadingDialog();
            if (o != null) {
                JSONArray jsonArray = null;
                JSONObject jresponse = o;
                String resultString = null;
                final String Price[], Talktime[], Validity[], Name[], Description[], Type[];
                JSONArray myListsAll = null;

                try {
                    if(jresponse.getInt("plan_data")==0)
                    {
                       // Toast.makeText(getContext(), jresponse.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    myListsAll = jresponse.getJSONArray("plans");

                    if (myListsAll == null || myListsAll.equals("null") || myListsAll.equals("")) {
                     //   Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Price = new String[myListsAll.length()];
                Talktime = new String[myListsAll.length()];
                Validity = new String[myListsAll.length()];
                Name = new String[myListsAll.length()];
                for (int i = 0; i < myListsAll.length(); i++) {
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = (JSONObject) myListsAll.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Price[i] = jsonobject.optString("plan_cost");
                    Talktime[i] = "Talktime: " + jsonobject.optString("plan_cost");
                    Validity[i] = "Validity: " + jsonobject.optString("validity_string");
                    Name[i] = jsonobject.optString("plan_name");
                }
                lviewAdapter = new ListViewAdapter((Activity) getContext(), Price, Talktime, Name, Validity, "prepaid");
                lview.setAdapter(lviewAdapter);
                lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(Config.is_rechare_screen_open.equals("true"))
                        {
                            getActivity().finish();
                            RechargeNow.setRechargeAmount(Price[position]);
                        }else {
                            Intent i = new Intent(getActivity(), RechargeNow.class);
                            i.putExtra("Selected_Value", Price[position]);
                            getActivity().finish();
                            startActivity(i);
                        }

                    }
                });
                super.onPostExecute(o);
            }
        }
    }
}

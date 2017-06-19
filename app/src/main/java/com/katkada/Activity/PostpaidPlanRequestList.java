package com.katkada.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.katkada.Other.Config;
import com.katkada.Other.JSONParser;
import com.katkada.Other.ListViewAdapterPlanrequest;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class PostpaidPlanRequestList extends AppCompatActivity {
    public static ListView lview;
    JSONParser jsonParser = new JSONParser();
    public static ListViewAdapterPlanrequest lviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpaid_plan_request_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        lview = (ListView) findViewById(R.id.listViewPostpaidrequest);
        if (Config.getConnectivityStatus(getApplicationContext())) {
            if (Config.isOnline()) {
                //Toast.makeText(getApplicationContext(),"Region: "+HomeScreenFragment.regionID_BrowsePlans+"\n"+
                //"Operator: "+HomeScreenFragment.operatorID_BrowsePlans+"\n"+
                // "Type: "+Config.OperatorType+"\n",Toast.LENGTH_SHORT).show();
                DisplayPlans displayPlans = new DisplayPlans();
                displayPlans.execute();
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
        }
    }
    public class DisplayPlans extends AsyncTask<Object, Object, JSONObject> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(getApplicationContext());
//            dialog.setMessage("please wait...");
//            dialog.show();
        }
        @Override
        protected JSONObject doInBackground(Object... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("user_id", Login.userID));
            JSONObject json = jsonParser.makeHttpRequest(Config.PLAN_REQUEST_LIST, "POST", params);
            return json;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(JSONObject o) {
            // dialog.dismiss();
            if (o != null) {
                JSONArray jsonArray = null;
                JSONObject jresponse = o;
                String resultString = null;
                final String Name[], Email[], mobileno[], ConType[], ReqDate[], Status[], Type[], plantyprID[];
                JSONArray myListsAll = null;
                if (jresponse.has("result")) {
                    try {
                        if (jresponse.getString("result").equals("false")) {
                            Toast.makeText(getApplicationContext(), "No Request Found  ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    myListsAll = jresponse.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Name = new String[myListsAll.length()];
                Email = new String[myListsAll.length()];
                mobileno = new String[myListsAll.length()];
                ConType = new String[myListsAll.length()];
                ReqDate = new String[myListsAll.length()];
                Status = new String[myListsAll.length()];
                plantyprID = new String[myListsAll.length()];
                //    Description=new String[myListsAll.length()];
                for (int i = 0; i < myListsAll.length(); i++) {
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = (JSONObject) myListsAll.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Name[i] = jsonobject.optString("name");
                    Email[i] = jsonobject.optString("email");
                    mobileno[i] = jsonobject.optString("mobile_no");
                    ConType[i] = jsonobject.optString("connection");
                    ReqDate[i] = jsonobject.optString("created_date");
                    Status[i] = jsonobject.optString("cancel_status");
                    plantyprID[i] = jsonobject.optString("id");
                    // number[i]=jsonobject.optString("name");
                    // arrayListPrice.add(jsonobject.optString("price"));
                }
                lviewAdapter = new ListViewAdapterPlanrequest(PostpaidPlanRequestList.this, Name, Email, mobileno, ConType, ReqDate, Status, plantyprID);
                //  System.out.println("adapter => "+lviewAdapter.getCount());
                lview.setAdapter(lviewAdapter);
//                lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getApplicationContext(),plantyprID[position].toString(),Toast.LENGTH_SHORT).show();
//                        Log.d("price", "onItemClick: " + Price[position]);
//                        Intent i = new Intent(getApplicationContext(), PostpaidBillForm.class);
//                        i.putExtra("Selected_Value", Price[position]);
//                        i.putExtra("Selected_Name", Name[position]);
//                        i.putExtra("Selected_Validity", Validity1[position]);
//                        i.putExtra("Selected_planID", plantyprID[position]);
//
//                        PostpaidPlanRequestList.this.finish();
//                        startActivity(i);
//
//
//                    }
//                });
                super.onPostExecute(o);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                //      overridePendingTransition(R.anim.push_out_left,R.anim.pull_in_right);
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}

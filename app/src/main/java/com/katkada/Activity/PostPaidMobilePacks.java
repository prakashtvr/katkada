package com.katkada.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.katkada.Fragment.HomeScreenFragment;
import com.katkada.Other.Config;
import com.katkada.Other.JSONParser;
import com.katkada.Other.ListViewAdapterPostpaidForm;
import com.katkada.Other.SessionManager;
import com.katkada.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class PostPaidMobilePacks extends AppCompatActivity {
    public static ListView lview;
    JSONParser jsonParser = new JSONParser();
    public static ListViewAdapterPostpaidForm lviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_paid_mobile_packs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        lview = (ListView) findViewById(R.id.listViewPostpaid);
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
            showalert();
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
            // String Name = String.valueOf(args[0]);
            // String email = String.valueOf(args[1]);
            // String password = String.valueOf(args[2]);
            // String MobileNo = String.valueOf(args[3]);
            //String ipAddress= args[4];
            // String email = args[2];
            //String password = args[1];
            //  String name= args[0];
            //   String Region_id= new SessionManager().getPreferences(getApplicationContext(),"RegionIdNo");
            //   String Oerator_id=  new SessionManager().getPreferences(getApplicationContext(),"OperatorIdNo");
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("region", HomeScreenFragment.regionID_BrowsePlans));
            params.add(new BasicNameValuePair("operator", HomeScreenFragment.operatorID_BrowsePlans));
            params.add(new BasicNameValuePair("conntype", Config.OperatorType));
            // params.add(new BasicNameValuePair("plantype","13"));
            // params.add(new BasicNameValuePair("txnid", "abc"));
            //  params.add(new BasicNameValuePair("securehash", "7a4a870320e6dc86847fdc356416e063943d430edf18f1f34fbf77d5e31e500a177c6c24b6ed4556f43fef788ec7669ae16ce3f59040287e53de3f3a23c43280"));
            //  params.add(new BasicNameValuePair("api_user_id", "EPj3bYVYYsA5NdLNjYCEnQCxMpL7gxV2YYRWxeebW4A3WWcr"));
            // params.add(new BasicNameValuePair("topuptype", "recharge+voucher"));
            // params.add(new BasicNameValuePair("amount", "10"));
            JSONObject json = jsonParser.makeHttpRequest(Config.PLAN_SEARCH, "POST", params);
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
                final String Price[], Talktime[], Validity[], Name[], Validity1[], Description[], Type[], plantyprID[];
                JSONArray myListsAll = null;
                try {
                    if (jresponse.has("error_msg"))
                        myListsAll = jresponse.getJSONArray("error_msg");
                    if (jresponse.has("result")) {
                        Intent i = new Intent(getApplicationContext(), PostpaidBillForm.class);
                        i.putExtra("Selected_Value", "");
                        i.putExtra("Selected_Name", "");
                        i.putExtra("Selected_Validity", "");
                        i.putExtra("Selected_planID", "");
//                        i.putExtra("Selected_Value", HomeScreenFragment.OperatorType);
//                        i.putExtra("SelectedOperator", HomeScreenFragment.OperatorType);
                        PostPaidMobilePacks.this.finish();
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (myListsAll != null) {
                    Price = new String[myListsAll.length()];
                    Talktime = new String[myListsAll.length()];
                    Validity = new String[myListsAll.length()];
                    Validity1 = new String[myListsAll.length()];
                    Name = new String[myListsAll.length()];
                    plantyprID = new String[myListsAll.length()];
                    //    Description=new String[myListsAll.length()];
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
                        Validity1[i] = jsonobject.optString("validity_string");
                        Name[i] = jsonobject.optString("plan_name");
                        plantyprID[i] = jsonobject.optString("id");
                        // number[i]=jsonobject.optString("name");
                        // arrayListPrice.add(jsonobject.optString("price"));
                    }
                    lviewAdapter = new ListViewAdapterPostpaidForm(PostPaidMobilePacks.this, Price, Talktime, Name, Validity, "postpaid", plantyprID);
                    //  System.out.println("adapter => "+lviewAdapter.getCount());
                    lview.setAdapter(lviewAdapter);
                    lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getApplicationContext(), plantyprID[position].toString(), Toast.LENGTH_SHORT).show();
                            Log.d("price", "onItemClick: " + Price[position]);
                            Intent i = new Intent(getApplicationContext(), PostpaidBillForm.class);
                            i.putExtra("Selected_Value", Price[position]);
                            i.putExtra("Selected_Name", Name[position]);
                            i.putExtra("Selected_Validity", Validity1[position]);
                            i.putExtra("Selected_planID", plantyprID[position]);
//                        i.putExtra("Selected_Value", HomeScreenFragment.OperatorType);
//                        i.putExtra("SelectedOperator", HomeScreenFragment.OperatorType);
                            PostPaidMobilePacks.this.finish();
                            startActivity(i);

                    /*Intent intent=new Intent();
                    intent.putExtra("AMOUNT",Price[position]);
                    getActivity().setResult(2,intent);
                    getActivity().finish();//finishing activity*/
                            //Toast.makeText(getContext(), "Title => " + month[position] + "=> n setOnItemClickListener:" + number[position], Toast.LENGTH_SHORT).show();
                        }
                    });
                    super.onPostExecute(o);
                } else {
                    Intent i = new Intent(getApplicationContext(), PostpaidBillForm.class);
                    i.putExtra("Selected_Value", "");
                    i.putExtra("Selected_Name", "");
                    i.putExtra("Selected_Validity", "");
                    i.putExtra("Selected_planID", "");
//                        i.putExtra("Selected_Value", HomeScreenFragment.OperatorType);
//                        i.putExtra("SelectedOperator", HomeScreenFragment.OperatorType);
                    PostPaidMobilePacks.this.finish();
                    startActivity(i);
                }
            }
        }
    }
    public void showalert() {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage("Internet Not Available")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.finish();
                    }
                }).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                // startActivity(new Intent(getBaseContext(),RechargeNow.class));
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}

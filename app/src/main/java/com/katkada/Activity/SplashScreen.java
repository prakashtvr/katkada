package com.katkada.Activity;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;


import com.katkada.Other.Config;
import com.katkada.Other.Get_Region_Operators_Browse_Plan;
import com.katkada.Other.Get_Region_Operators_Plan;
import com.katkada.Other.PreferenceHelper;
import com.katkada.Other.SessionManager;
import com.katkada.R;


public class SplashScreen extends AppCompatActivity {
    SessionManager manager;

//    private NetworkChangeReceiver receiver;
    private static final String LOG_TAG = "CheckNetworkStatus";
    private boolean isConnected = false;
    private PreferenceHelper preferenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        manager = new SessionManager();
       // preferenceHelper = new PreferenceHelper(SplashScreen.this);

        manager.setPreferences(SplashScreen.this, "OperatorIdNo", "5");
        manager.setPreferences(SplashScreen.this, "RegionIdNo", "1");
        Login.userID = manager.getPreferences(SplashScreen.this, "Shared_userID");
        Login.ProgileUserName = manager.getPreferences(SplashScreen.this, "Shared_ProgileUserName");
        Login.UserEmail = manager.getPreferences(SplashScreen.this, "Shared_UserEmail");
        Login.ConnectionType = manager.getPreferences(SplashScreen.this, "Shared_ConnectionType");
        Login.phoneNumber = manager.getPreferences(SplashScreen.this, "Shared_phoneNumber");
        Login.OptName = manager.getPreferences(SplashScreen.this, "Shared_OptName");
        Login.RgName = manager.getPreferences(SplashScreen.this, "Shared_RgName");
        manager.setPreferences(SplashScreen.this, "Shared_TotalMinutes", "100");
        manager.setPreferences(SplashScreen.this, "Shared_TotalData", "1200");
        manager.setPreferences(SplashScreen.this, "Shared_TotalSMS", "100");
        Config.OperatorIdno = "1";
        Config.Regionidno = "1";
        Config.OperatorType = "prepaid";
        Log.d("OptName", Login.OptName);
        Log.d("RgName", Login.RgName);
        Log.d("phoneNumber", Login.phoneNumber);
        Log.d("ConnectionType", Login.ConnectionType);
        Login.valid = true;

      //  new Get_Region_Operators_Plan().execute();
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 1 seconds
                    sleep(1 * 1000);
                    // After 5 seconds redirect to another intent
                    String status = manager.getPreferences(SplashScreen.this, "status");
                    Log.d("status_splash", status);
                    if (status.equals("1")) {
//                        Intent i=new Intent(SplashScreen.this,MainActivity.class);
//                        startActivity(i);
                        new Get_Region_Operators_Plan().execute();
                        new Get_Region_Operators_Browse_Plan().execute();
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        // SplashScreen.this.finish();
                        SplashScreen.this.startActivity(i);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    } else {
//                        Intent i=new Intent(SplashScreen.this,Login.class);
//                        startActivity(i);
                        Intent i = new Intent(SplashScreen.this, Login.class);
                        //SplashScreen.this.finish();
                        SplashScreen.this.startActivity(i);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }
                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

    }

//    public class NetworkChangeReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(final Context context, final Intent intent) {
//            Log.v(LOG_TAG, "Receieved notification about network status");
//            isNetworkAvailable(context);
//        }
//        private boolean isNetworkAvailable(Context context) {
//            ConnectivityManager connectivity = (ConnectivityManager)
//                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (connectivity != null) {
//                NetworkInfo[] info = connectivity.getAllNetworkInfo();
//                if (info != null) {
//                    for (int i = 0; i < info.length; i++) {
//                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                            if (!isConnected) {
//                                Log.v(LOG_TAG, "Now you are connected to Internet!");
//                                // networkStatus.setText("Now you are connected to Internet!");
//                                isConnected = true;
//                                //do your processing here ---
//                                //if you need to post any data to the server or get status
//                                //update from the server
//                            }
//                            return true;
//                        }
//                    }
//                }
//            }
//            Log.v(LOG_TAG, "You are not connected to Internet!");
//            //  networkStatus.setText("You are not connected to Internet!");
//            isConnected = false;
//            return false;
//        }
//    }
//    public List<Sms> getAllSms(String folderName) {
//        List<Sms> lstSms = new ArrayList<Sms>();
//        Sms objSms = new Sms();
//        Uri allMessage = Uri.parse("content://sms/");
//        ContentResolver cr1 = getContentResolver();
//        Cursor c1 = cr1.query(allMessage, null, null, null, null);
//        Log.d("totalSMS", "getAllSms: " + c1.getCount());
//        while (c1.moveToNext()) {
//            String row = c1.getString(1);
//        }
//        Uri message = Uri.parse("content://sms/" + folderName);
//        ContentResolver cr = getContentResolver();
//        Cursor c = cr.query(message, null, null, null, null);
//        startManagingCursor(c);
//        int totalSMS = c.getCount();
//        Log.d("totalSMS", "getAllSms: " + totalSMS);
//        if (c.moveToFirst()) {
//            for (int i = 0; i < totalSMS; i++) {
//                objSms = new Sms();
//                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
//                objSms.setAddress(c.getString(c
//                        .getColumnIndexOrThrow("address")));
//                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
//                objSms.setReadState(c.getString(c.getColumnIndex("read")));
//                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
//                lstSms.add(objSms);
//                c.moveToNext();
//            }
//        }
//        // else {
//        // throw new RuntimeException("You have no SMS in " + folderName);
//        // }
//        c.close();
//        return lstSms;
//    }
//    public class contentObserver extends ContentObserver {
//        public contentObserver(Handler handler) {
//            super(handler);
//            // you can use a handler if you want or directly do everythinh onChange();
//        }
//        @Override
//        public void onChange(boolean selfChange) {
//            // Do your stuff here
//            super.onChange(selfChange);
//        }
//    }
}

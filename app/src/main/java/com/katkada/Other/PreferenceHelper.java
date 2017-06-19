package com.katkada.Other;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by prakash on 23-05-2017.
 */
public class PreferenceHelper {

    private final String USER_ID = "user_id";
    private final String WALLET_AMOUNT = "Wallet";

    private final String EMAIL = "email";
    private final String PASSWORD = "password";
    private final String REFFERAL_CODE = "RefferalCode";
    private final String REFFERAL_AMOUNT = "RefferalAmount";
    private final String DEVICE_TOKEN = "device_token";
    private final String IS_COMPLETED = "Is_completed";
    private final String IS_FIRST_TIME = "Is_first_time";
    private final String SESSION_TOKEN = "session_token";
    private final String REQUEST_ID = "request_id";
    private final String REQUEST_TIME = "request_time";
    private final String REQUEST_LATITUDE = "request_latitude";
    private final String REQUEST_LONGITUDE = "request_longitude";
    private final String LOGIN_BY = "login_by";
    private final String SOCIAL_ID = "social_id";
    private final String PAYMENT_MODE = "payment_mode";
    private final String DEFAULT_CARD = "default_card";
    private final String DEFAULT_CARD_NO = "default_card_no";
    private final String DEFAULT_CARD_TYPE = "default_card_type";
    private final String BASE_PRICE = "base_cost";
    private final String OPERATOR_ID_PLAN = "OperatorId_Plan";
    private final String OPERATOR_ID_BROWSE_PLAN = "OperatorId_BrowsePlan";
    private final String OPERATION_MODE = "Operation_mode";
    private final String REGION_ID_BROWSE_PLAN = "RegionId_BrowsePlan";
    private final String REGION_ID_PLAN = "RegionId_Plan";
    private final String OPERATOR_TYPE = "Operator_Type";


    private final String DISTANCE_PRICE = "distance_cost";
    private final String TIME_PRICE = "time_cost";
    private final String IS_TRIP_STARTED = "is_trip_started";
    private final String HOME_ADDRESS = "home_address";
    private final String WORK_ADDRESS = "work_address";
    private final String DEST_LAT = "dest_lat";
    private final String DEST_LNG = "dest_lng";
    private final String REFEREE = "is_referee";
    private final String PROMO_CODE = "promo_code";
    //	private Context context;
    private final String TIME_ZONE = "time_zone";
    private final String START_TIME = "start_time";
    private final String CURRENT_TRIP_TIME = "time";
    private SharedPreferences app_prefs;

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences(Config.PREF_NAME,
                Context.MODE_PRIVATE);
//		this.context = context;
    }

    public void putUserId(String userId) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(USER_ID, userId);
        edit.commit();
    }

    public String getUserId() {
        return app_prefs.getString(USER_ID, "");

    }

    public void putWallet(String Wallet) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(WALLET_AMOUNT, Wallet);
        edit.commit();
    }

    public String getWallet() {
        return app_prefs.getString(WALLET_AMOUNT, "");

    }


    public void putOperatorId(String OperatorId) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(OPERATOR_ID_PLAN, OperatorId);
        edit.commit();
    }

    public String getOperatorId_browsePlan() {
        return app_prefs.getString(OPERATOR_ID_BROWSE_PLAN, "1");

    }
    public void putOperatorId_browsePlan(String OperatorId_browsePlan) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(OPERATOR_ID_BROWSE_PLAN, OperatorId_browsePlan);
        edit.commit();
    }

    public String getOperation_method() {
        return app_prefs.getString(OPERATION_MODE, "1");

    }
    public void putOperation_method(String Operation_method) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(OPERATION_MODE, Operation_method);
        edit.commit();
    }

    public String getRegionId_browsePlan() {
        return app_prefs.getString(REGION_ID_BROWSE_PLAN, "1");

    }
    public void putRegionId_browsePlan(String RegionId_browsePlan) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(REGION_ID_BROWSE_PLAN, RegionId_browsePlan);
        edit.commit();
    }

    public String getOperatorId() {
        return app_prefs.getString(OPERATOR_ID_PLAN, "1");

    }


    public void putRegionId(String RegionId) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(REGION_ID_PLAN, RegionId);
        edit.commit();
    }

    public String getRegionId() {
        return app_prefs.getString(REGION_ID_PLAN, "1");
    }


    public void putOperatorType(String OperatorType) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(OPERATOR_TYPE, OperatorType);
        edit.commit();
    }

    public String getOperatorType() {
        return app_prefs.getString(OPERATOR_TYPE, "1");
    }
    public void putPassword(String password) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(PASSWORD, password);
        edit.commit();
    }
    public String getRefferalCode() {
        return app_prefs.getString(REFFERAL_CODE, "0");
    }
    public void putRefferalCode(String RefferalCode) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(REFFERAL_CODE, RefferalCode);
        edit.commit();
    }
    public String getRefferalAmount() {
        return app_prefs.getString(REFFERAL_AMOUNT, "0");
    }
    public void putRefferalAmount(String RefferalAmount) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(REFFERAL_AMOUNT, RefferalAmount);
        edit.commit();
    }
    public String getPassword() {
        return app_prefs.getString(PASSWORD, "0");
    }



    public void putDistancePrice(float price) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putFloat(DISTANCE_PRICE, price);
        edit.commit();
    }

    public float getDistancePrice() {
        return app_prefs.getFloat(DISTANCE_PRICE, 0f);
    }

    public void putTimePrice(float price) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putFloat(TIME_PRICE, price);
        edit.commit();
    }

    public float getTimePrice() {
        return app_prefs.getFloat(TIME_PRICE, 0f);
    }




    public void putDeviceToken(String deviceToken) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(DEVICE_TOKEN, deviceToken);
        edit.commit();
    }

    public String getDeviceToken() {
        return app_prefs.getString(DEVICE_TOKEN, "123");

    }

    public void putISCompleted(String COMPLETED) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(IS_COMPLETED, COMPLETED);
        edit.commit();
    }

    public String getISCompleted() {
        return app_prefs.getString(IS_COMPLETED, "2");

    }
    public void putISFirsttime(String Firsttime) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(IS_FIRST_TIME, Firsttime);
        edit.commit();
    }

    public String getISFirsttime() {
        // String str=app_prefs.getString(IS_FIRST_TIME, null).toString();

       /* try
        {
            String str= app_prefs.getString(IS_FIRST_TIME, null);
        }catch (Exception e){
            Editor edit = app_prefs.edit();
            edit.putString(IS_FIRST_TIME, "0");
            edit.commit();
            //new PreferenceHelper(SplashActivity.this).putISFirsttime("0");
        }*/
        // if(app_prefs.getString(IS_FIRST_TIME, null)!=null){
        return app_prefs.getString(IS_FIRST_TIME, "0");
        // }
        // return "0";
    }

    public void putSessionToken(String sessionToken) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(SESSION_TOKEN, sessionToken);
        edit.commit();
    }

    public String getSessionToken() {
        return app_prefs.getString(SESSION_TOKEN, "0");

    }




    public void putLoginBy(String loginBy) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(LOGIN_BY, loginBy);
        edit.commit();
    }


    public void clearRequestData() {

        // new DBHelper(context).deleteAllLocations();
    }

   /* public void Logout() {
        clearRequestData();
        // new DBHelper(context).deleteUser();
        putUserId(null);
        putSessionToken(null);
        putSocialId(null);
        putClientDestination(null);
        putLoginBy(Const.MANUAL);
        app_prefs.edit().clear();
    }*/

    public void putTimeZone(String timeZone) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(TIME_ZONE, timeZone);
        edit.commit();
    }

    public String getTimeZone() {
        return app_prefs.getString(TIME_ZONE, "");
    }

    public void putStartTime(String startTime) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(START_TIME, startTime);
        edit.commit();
    }

    public String getStartTime() {
        return app_prefs.getString(START_TIME, "");
    }

    public void putRequestCreatedTime(String time) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(CURRENT_TRIP_TIME, time);
        edit.commit();
    }

    public String getRequestCreatedTime() {
        return app_prefs.getString(CURRENT_TRIP_TIME, "");
    }
}

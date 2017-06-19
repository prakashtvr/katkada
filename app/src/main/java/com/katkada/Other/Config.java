package com.katkada.Other;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Created by admin on 10/11/2016.
 */
public class Config {
    Context cn;

    private static ImageView imageView;
    private static Animation anim;
    public static String Regionidno = "1", OperatorIdno = "1", OperatorType = "pre";
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    private static ProgressDialog mProgressDialog;
    public static Dialog mDialog;
    public static String is_rechare_screen_open="";
    //URLs to register.php and confirm.php file
    public static String PREF_NAME = "Katkada";


    public static final String PLANS_URL_blackberry = "https://api.komparify.com/api/v2/topuptype/blackberry.json";
    public static final String PLANS_URL_cug = "https://api.komparify.com/api/v2/topuptype/cug.json";
    public static final String PLANS_URL_4g = "https://api.komparify.com/api/v2/topuptype/data.json";
    public static final String PLANS_URL_2g_gprs_data = "https://api.komparify.com/api/v2/topuptype/2g+gprs+data.json";
    public static final String PLANS_URL_isd = "https://api.komparify.com/api/v2/topuptype/isd.json";
    public static final String PLANS_URL_nights_26_weekends = "https://api.komparify.com/api/v2/topuptype/nights+%26+weekends.json";
    public static final String PLANS_URL_popular_recharges = "https://api.komparify.com/api/v2/topuptype/popular+recharges.json";
    public static final String PLANS_URL_roaming_call = "https://api.komparify.com/api/v2/topuptype/roaming+call.json";
    public static final String PLANS_URL_sms = "https://api.komparify.com/api/v2/topuptype/sms.json";
    public static final String PLANS_URL_video_call = "https://api.komparify.com/api/v2/topuptype/video+call.json";
    public static final String PLANS_URL = "https://api.komparify.com/api/v2/topuptype/recharge+voucher.json";
    public static final String GET_USER_DETAILS_URL = "http://katkada.com/api/api/view_profile/";
    public static final String NUMBER_TO_OPT_RGN_URL = "https://api.komparify.com/api/v2/convertnumber/";
    public static final String COMMON_URL = "http://katkada.com/api/index.php/api/get_profile_operator";
    public static final String COMMON_Region_URL = "http://katkada.com/api/index.php/api/get_plans_region";
    public static final String GET_USER_UPDATE_URL = "http://katkada.com/api/api/update_profile";



    public static final String CCAVANUE_PAYMENT_PROCESS = "http://katkada.com/api/Payment_api/payment_now/";
    public static final String GET_REGION_KEY_URL = "http://katkada.com/api/Recharge_api/get_state_key";
    public static final String GET_OPERATOR_KEY_URL = "http://katkada.com/api/Recharge_api/get_operator_key";

    public static final String GET_REMD_RESULT_URL = "https://api.komparify.com/api/v1/check_get_results/";
    public static final String GET_LOGO_URL = "http://katkada.com/api/Recharge_api/operator_image_url/";
    public static final String WALLET_RECHARGE_URL = "http://katkada.com/api/Recharge_api/recharge_now";
    public static final String WALLET_PAYMENT_PROCES_URL = "http://katkada.com/api/Recharge_api/wallet_payment_process";
    public static final String FORGOT_PASSWORD_URL = "http://katkada.com/api/api/forget_password";


    public static final String USER_RECOMENTATION_URL = "https://api.komparify.com/api/v1/search/";
    public static final String LOADREGION_URL = "http://katkada.com/api/api/get_region";
    public static final String LOADOPERATORS_URL = "http://katkada.com/api/api/get_operators/";
    public static final String PLAN_OPERATOR_KEY = "http://katkada.com/api/Recharge_api/plan_operator_key";
    public static final String GET_REGION_NAME = "http://katkada.com/api/Recharge_api/get_region_name";
    public static final String SEARCH_PLAN = "http://katkada.com/Recharge_api/plan_search";

    public static final String SAVE_POSTPAID_FORM = "http://katkada.com/api/Buy_plan_api/save_form";
    public static final String PLAN_REQUEST_LIST = "http://katkada.com/api/Buy_plan_api/plan_request_list";


    public  static final  String HOST_URL="http://www.katkada.com/api/";
    private static final String BASE_URL = HOST_URL + "user/";
    public static final String REGISTER_URL = BASE_URL+"register";
    public static final String USER_ACTIVATION_URL = BASE_URL+"user_activation";
    public static final String RESEND_OTP_URL = BASE_URL+"resend_otp";
   // http://www.katkada.com/api/user/resend_otp
    public static final String LOGIN_URL = BASE_URL+"login";
    public static final String GET_OPERATOR_REGION = HOST_URL+"plan/search_plan_data";
    public static final String GET_OPERATOR_REGION_BROWSE_PLAN = HOST_URL+"recharge/get_recharge_data";

    public static final String PLAN_SEARCH = HOST_URL+"plan/search_plan";
    public static final String GET_USER_WALLET_URL = BASE_URL+"get_wallet_amount";
    public static final String GET_RECOMENDATION_URL = HOST_URL+"compare/get_result";
    public static final String GET_USER_COUPON_URL = HOST_URL+"Recharge/check_coupon_status";
    public static final String PAYMENT_THROUGH_COUPON = HOST_URL+"payment/pay_now";



    //Keys to send username, password, p
    // hone and otp
    public static final String KEY_USERNAME = "username";
    public static final String KEY_IDENTITY = "identity";
    public static final String KEY_MOBILENO = "mobileno";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";
    //JSON Tag from response from server
    public static final String TAG_RESPONSE = "ErrorMessage";
    public String inputStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static boolean connectedtoInternet = false;
    public static Boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                connectedtoInternet = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                connectedtoInternet = true;
            }
        } else {
            connectedtoInternet = false;
        }
        return connectedtoInternet;
    }
    public static void ShoDialog(Context context1, String Message) {
        new AlertDialog.Builder(context1)
                .setTitle("One Button")
                .setMessage("Thanks for visiting The Code of a Ninja - codeofaninja.com")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
    public static void ShowToast(Context context2, String message) {
        Toast.makeText(context2, message, Toast.LENGTH_SHORT).show();
    }
    public static void OperatorName(Context context2) {
        // Get System TELEPHONY service reference
        TelephonyManager tManager = (TelephonyManager) context2
                .getSystemService(Context.TELEPHONY_SERVICE);
        // Get carrier name (Network Operator Name)
        String carrierName = tManager.getNetworkOperatorName();
        String mobileno = tManager.getLine1Number();
        // Get Phone model and manufacturer name
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        // Toast.makeText(context2, "Operator Name: "+carrierName+"\n Sim Operator Name: "+mobileno, Toast.LENGTH_SHORT).show();
    }
    public class NetworkChangeReceiver extends BroadcastReceiver {
        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {
                                Log.v(LOG_TAG, "Now you are connected to Internet!");
                                //  networkStatus.setText("Now you are connected to Internet!");
                                isConnected = true;
                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server
                            }
                            return true;
                        }
                    }
                }
            }
            Log.v(LOG_TAG, "You are not connected to Internet!");
            // networkStatus.setText("You are not connected to Internet!");
            isConnected = false;
            return false;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);
        }
    }
    //    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity == null) {
//            return false;
//        } else {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null) {
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
    public static void showToast(String msg, Context ctx) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String getOperatorName(String Key, String Type) {
        String operator_id = "1";
        switch (Type) {
            case "prepaid":
                switch (Key) {
                    case "ACP":
                        operator_id = "Aircel";
                        break;
                    case "BGP":
                        operator_id = "BSNL - Talktime";
                        break;
                    case "IDP":
                        operator_id = "Idea";
                        break;
                    case "MSP":
                        operator_id = " MTNL - Special Tariff";
                        break;
                    case "MMP":
                        operator_id = "MTNL - Talktime ";
                        break;
                    case "MTP":
                        operator_id = "MTS ";
                        break;
                    case "RGP":
                        operator_id = " Reliance";
                        break;
                    case "TCP":
                        operator_id = "Tata Docomo CDMA ";
                        break;
                    case "TGP":
                        operator_id = "Tata Docomo GSM - Talktime";
                        break;
                    case "UGP":
                        operator_id = "Telenor - Talktime";
                        break;
                    case "VGP":
                        operator_id = "Videocon - Talktime";
                        break;
                    case "VFP":
                        operator_id = "Vodafone";
                        break;
                    case "BVP":
                        operator_id = "BSNL - Special Tariff";
                        break;
                    case "TSP":
                        operator_id = "Tata Docomo GSM - Special Tariff";
                        break;
                    case "USP":
                        operator_id = "Telenor - Special Tariff";
                        break;
                    case "VSP":
                        operator_id = "Videocon - Special Tariff";
                        break;
                    case "TMP":
                        operator_id = "T24 Mobile - Talktime";
                        break;
                    case "TVP":
                        operator_id = "T24 Mobile - Special Tariff";
                    case "RJP":
                        operator_id = "Reliance Jio";
                        break;
                }
                break;
            case "postpaid":
                switch (Key) {
                    case "ATC":
                        operator_id = "Airtel";
                        break;
                    case "RGC":
                        operator_id = "Reliance";
                        break;
                    case "VFC":
                        operator_id = "Vodafone";
                        break;
                    case "ACC":
                        operator_id = "Aircel";
                        break;
                    case "IDC":
                        operator_id = "Idea";
                        break;
                    case "BGC":
                        operator_id = "BSNL";
                        break;
                    case "TDC":
                        operator_id = "Tata Docomo";
                        break;
                    case "MTC":
                        operator_id = "MTS";
                        break;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid Key: " + operator_id);
        }
        return operator_id;
    }


    public static String rechargeID_to_PlansID(String Key, String Type) {
        String operator_id = "1";
        switch (Type) {
            case "prepaid":
                switch (Key) {
                    case "ACP":
                        operator_id = "1";
                        break;
                    case "BGP":
                        operator_id = "3";
                        break;
                    case "IDP":
                        operator_id = "4";
                        break;
                    case "MSP":
                        operator_id = "5";
                        break;
                    case "MMP":
                        operator_id = "5";
                        break;
                    case "MTP":
                        operator_id = "6";
                        break;
                    case "RGP":
                        operator_id = "14";
                        break;
                    case "TCP":
                        operator_id = "8";
                        break;
                    case "TGP":
                        operator_id = "9";
                        break;
                    case "UGP":
                        operator_id = "10";
                        break;
//                    case "VGP":
//                        operator_id = "Videocon - Talktime";
//                        break;
                    case "VFP":
                        operator_id = "12";
                        break;
//                    case "BVP":
//                        operator_id = "BSNL - Special Tariff";
//                        break;
//                    case "TSP":
//                        operator_id = "Tata Docomo GSM - Special Tariff";
//                        break;
//                    case "USP":
//                        operator_id = "Telenor - Special Tariff";
//                        break;
//                    case "VSP":
//                        operator_id = "Videocon - Special Tariff";
                       // break;
                    case "TMP":
                        operator_id = "13";
                        break;
//                    case "TVP":
//                        operator_id = "T24 Mobile - Special Tariff";
                    case "RJP":
                        operator_id = "15";
                        break;
                }
                break;
            case "postpaid":
                switch (Key) {
                    case "ATC":
                        operator_id = "2";
                        break;
                    case "RGC":
                        operator_id = "14";
                        break;
                    case "VFC":
                        operator_id = "12";
                        break;
                    case "ACC":
                        operator_id = "1";
                        break;
                    case "IDC":
                        operator_id = "4";
                        break;
                    case "BGC":
                        operator_id = "3";
                        break;
                    case "TDC":
                        operator_id = "9";
                        break;
                    case "MTC":
                        operator_id = "6";
                        break;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid Key: " + operator_id);
        }
        return operator_id;
    }

    public static void showCustomProgressDialog(Context context, String title,
                                                boolean iscancelable) {
        removeCustomProgressDialog();
        mDialog = new Dialog(context, R.style.MyDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_progress);
        imageView = (ImageView) mDialog.findViewById(R.id.iv_dialog_progress);
        ((TextView) mDialog.findViewById(R.id.tv_dialog_title)).setText(title);
        mDialog.setCancelable(iscancelable);
        anim = AnimationUtils.loadAnimation(context, R.anim.scale_updown);
        imageView.startAnimation(anim);
        mDialog.show();
    }
    public static void removeCustomProgressDialog() {
        try {
            if (mDialog != null && imageView != null) {
                imageView.clearAnimation();
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void showSimpleProgressDialog(Context context) {
        showSimpleProgressDialog(context, null, "Loading...", false);
    }
    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
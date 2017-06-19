package com.katkada.Other;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by prakash on 24-03-2017.
 */
public class AndyUtils {
    static float density = 1;
    private static ProgressDialog mProgressDialog;
    private static Dialog mDialog;
    private static ImageView imageView;
    private static Animation anim;
    private static ProgressDialog  loadingDialog;

    public static void openloadingDialog(Context context) {
//      if(loadingDialog == null)
//        {
//            loadingDialog.dismiss();
//        }
        loadingDialog = new ProgressDialog (context,R.style.MyAlertDialogStyle);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setMessage("Please wait ...");
        loadingDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        //loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       // loadingDialog.setContentView(R.layout.loading_layout);

        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
        // loadingDialog.dismiss();
        //loadingDialog.show();
    }

    public  static void closeloadingDialog() {

        try {
             if (loadingDialog != null) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                    loadingDialog = null;
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

    public static String comaReplaceWithDot(String value) {

        value = value.replace(',', '.');
        return value;
        // DecimalFormat decimalFormat=new DecimalFormat("#.##");
        // DecimalFormatSymbols symbol=new DecimalFormatSymbols();
        // symbol.setDecimalSeparator('.');
        // decimalFormat.setDecimalFormatSymbols(symbol);
        // decimalFormat.format(value);
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
    public static void showCustomProgressDialogSplash(Context context, String title,
                                                      boolean iscancelable) {
        removeCustomProgressDialog();
        mDialog = new Dialog(context, R.style.MyDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.activity_splash);
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
    public static void removeCustomProgressDialogSplash() {
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

    public static boolean eMailValidation(String emailstring) {
        if (null == emailstring || emailstring.length() == 0) {
            return false;
        }
        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(emailstring);
        return emailMatcher.matches();
    }

    public static void showToast(String msg, Context ctx) {

        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showErrorToast(int id, Context ctx) {
        String msg = "";
        String error_code = AndyConstants.ERROR_CODE_PREFIX + id;
        msg = ctx.getResources().getString(
                ctx.getResources().getIdentifier(error_code, "string",
                        ctx.getPackageName()));
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static double distance(double lat1, double lon1, double lat2,
                                  double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}

package com.katkada.parse;
import android.app.Activity;
import android.text.TextUtils;

import com.katkada.Other.AndyUtils;

import org.json.JSONObject;
/**
 * Created by prakash on 24-03-2017.
 */
public class ParseContent {
  //  private Activity activity;
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MESSAGES = "error_messages";
    static Activity activitynew;
    public static boolean isSuccess(String response) {
        if (TextUtils.isEmpty(response))
            return false;
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_SUCCESS).equals("1")) {
                return true;
            } else {
                // AndyUtils.showToast(jsonObject.getString(KEY_ERROR),
                // activity);


                AndyUtils.showErrorToast(
                        jsonObject.getJSONArray(KEY_ERROR_MESSAGES).getInt(0),
                        activitynew);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

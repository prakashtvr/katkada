package com.katkada.Other;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by prakash on 20-12-2016.
 */
public class SessionManager {
    public void setPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Androidwarriors", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
    public void clearPreferences(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Androidwarriors", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
    public String getPreferences(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("Androidwarriors", Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }
}
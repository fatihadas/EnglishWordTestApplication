package com.projects.ingilizcekelimecalis;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Utils {
    public static final String requestid = "Yds781227*";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String URL = "http://opencartexcel.hol.es/api/ydskelimecalis/index.php";


    public static void setSharedAyarla(Context context, String parametre, Boolean deger) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(parametre, deger);
        editor.commit();
    }

    public static Boolean getSharedAyarla(Context context, String parametre) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean deger = preferences.getBoolean(parametre, false);
        return deger;
    }

    private static Boolean firstTime = null;

    public static boolean isFirstTime(Context context) {
        if (firstTime == null) {
            SharedPreferences mPreferences = context.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                //Toast.makeText(context, "ilk", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
                Utils.setSharedAyarla(context, "kcotomatikdinle", true);
                Utils.setSharedAyarla(context, "ktotomatikdinle", true);
                Utils.setSharedAyarla(context, "ktotomatikses", true);
                Utils.setwaitTime(context, "ktwaittme", Long.parseLong("2000"));
            } else {
                //Toast.makeText(context, "degil", Toast.LENGTH_SHORT).show();
            }
        }
        return firstTime;
    }

    public static void setwaitTime(Context context, String parametre, Long deger) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(parametre, deger);
        editor.commit();
    }

    public static Long getwaitTime(Context context, String parametre) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Long deger = preferences.getLong(parametre, 2000);
        return deger;
    }

}

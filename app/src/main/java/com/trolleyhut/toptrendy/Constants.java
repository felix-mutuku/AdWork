package com.trolleyhut.toptrendy;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Constants {
    public static final String BASE_URL_LOGIC = "https://www.trolleyhut.com/apps/driversaid/api/";

    //saving a user's personal data in shared preferences
    public static final String SHARED_PREF_NAME = "Trolleyhut_pref";
    public static final String PREF_USERNAME = "trolleyhut_username";
    public static final String PREF_POINTS = "trolleyhut_points";
    public static final String PREF_POINTS_LIFETIME = "trolleyhut_points_lifetime";


    //display toast to user
    public static void showToast(String s, Activity a) {
        LayoutInflater inflater = a.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) a.findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);
        text.setText(s);

        Toast toast = new Toast(a.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 550);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}

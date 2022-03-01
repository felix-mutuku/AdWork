package com.trolleyhut.toptrendy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConsecutiveDayChecker {
    /**
     * Call this method when user login
     */
    public static void onUserLogin(Activity a) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String today = dateFormat.format(date);
        String lastLoginDay = getLastLoginDate(a);

        String yesterday = getYesterdayDate(dateFormat, date);

        if (lastLoginDay == null) {
            // user logged in for the first time
            updateLastLoginDate(today, a);
            incrementDays(a);
        } else {
            if (lastLoginDay.equals(today)) {
                // User logged in the same day , do nothing
            } else if (lastLoginDay.equals(yesterday)) {
                // User logged in consecutive days , add 1
                updateLastLoginDate(today, a);
                incrementDays(a);
            } else {
                // It's been more than a day user logged in, reset the counter to 1
                updateLastLoginDate(today, a);
                resetDays(a);
            }
        }
    }

    private static String getYesterdayDate(DateFormat simpleDateFormat, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Update last login date into the storage
     *
     * @param date
     */
    private static void updateLastLoginDate(String date, Activity a) {
        SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("last_login_day", date);
        editor.apply();
    }

    /**
     * Get last login date
     *
     * @return
     */
    private static String getLastLoginDate(Activity a) {
        String lastLogin = null;
        SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        lastLogin = sharedPref.getString("last_login_day", null);
        return lastLogin;
    }

    private static int getConsecutiveDays(Activity a) {
        int days = 0;
        SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        days = sharedPref.getInt("num_consecutive_days", 0);
        return days;
    }

    private static void incrementDays(Activity a) {
        int days = getConsecutiveDays(a) + 1;
        SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("num_consecutive_days", days);
        editor.apply();
    }

    private static void resetDays(Activity a) {
        int days = 1;
        SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.PREF_POINTS, String.valueOf(days));
        editor.putInt("num_consecutive_days", days);
        editor.apply();
    }

    public static int getStreak(Activity a) {
        return getConsecutiveDays(a);
    }
}

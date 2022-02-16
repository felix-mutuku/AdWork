package com.trolleyhut.toptrendy;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {
    public static final String BASE_URL_LOGIC = "";
    public static double coins = 0.05; //one point is $0.05 or Ksh5
    public static int warning_points = 380; //one point is $0.05 or Ksh5
    public static Dialog loadingDialog;

    //saving a user's personal data in shared preferences
    public static final String SHARED_PREF_NAME = "AdWork_pref";
    public static final String PREF_USERNAME = "adwork_username";
    public static final String PREF_EMAIL = "adwork_email";
    public static final String PREF_POINTS = "adwork_points";
    public static final String PREF_POINTS_LIFETIME = "adwork_points_lifetime";
    public static final String PREF_DATE_JOINED = "adwork_date_joined";
    public static final String PREF_POINTS_DEDUCTED = "adwork_points_deducted";

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

    public static void checkInternet(Activity a) {
        ConnectivityManager cManager;
        NetworkInfo nInfo;

        cManager = (ConnectivityManager) a.getSystemService(CONNECTIVITY_SERVICE);
        assert cManager != null;
        nInfo = cManager.getActiveNetworkInfo();

        if (nInfo != null && nInfo.isConnected()) {
            //everything is okay, no need for alarm
        } else {
            //show check internet connection activity
            Intent intent = new Intent(a, InternetActivity.class);
            a.startActivity(intent);
            a.finish();
        }
    }

    public static void deductAPoint(Activity a) {
        //removePoints
        SharedPreferences sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String Spoints = sharedPreferences.getString(PREF_POINTS, "0");
        String SpointsLifetime = sharedPreferences.getString(PREF_POINTS_LIFETIME, "0");
        String SpointsDeducted = sharedPreferences.getString(PREF_POINTS_DEDUCTED, "0");
        //change string points to integer for calculations
        int Ipoints = Integer.parseInt(Spoints);
        int IpointsLifetime = Integer.parseInt(SpointsLifetime);
        int IpointsDeducted = Integer.parseInt(SpointsDeducted);
        //remove one point as penalty
        if (Ipoints > 1) {
            //if user has more than one point, remove one point for luck of internet
            Ipoints = Ipoints - 1;
            //deduct points from lifetime points as well
            if (IpointsLifetime > 1) {
                IpointsLifetime = IpointsLifetime - 1;
            }
            //ad points to deducted points
            IpointsDeducted = IpointsDeducted + 1;

            //store points back to shared preference for future reference
            Spoints = String.valueOf(Ipoints);
            SpointsLifetime = String.valueOf(IpointsLifetime);
            SpointsDeducted = String.valueOf(IpointsDeducted);
            //add to shared preferences
            sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_POINTS, Spoints);
            editor.putString(PREF_POINTS_LIFETIME, SpointsLifetime);
            editor.putString(PREF_POINTS_DEDUCTED, SpointsDeducted);
            editor.apply();
        }
    }

    public static void addAPoint(Activity a) {
        //addPoints
        SharedPreferences sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String Spoints = sharedPreferences.getString(PREF_POINTS, "0");
        String SpointsLifetime = sharedPreferences.getString(PREF_POINTS_LIFETIME, "0");
        //change string points to integer for calculations
        int Ipoints = Integer.parseInt(Spoints);
        int IpointsLifetime = Integer.parseInt(SpointsLifetime);

        //add a point for user
        Ipoints = Ipoints + 1;
        IpointsLifetime = IpointsLifetime + 1;

        //is user has more than 380 points
        if (Ipoints > warning_points) {
            addPointsDataToFirebase(a);
        }

        //store points back to shared preference for future reference
        Spoints = String.valueOf(Ipoints);
        SpointsLifetime = String.valueOf(IpointsLifetime);
        //add to shared preferences
        sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_POINTS, Spoints);
        editor.putString(PREF_POINTS_LIFETIME, SpointsLifetime);
        editor.apply();
    }

    public static void addTwoPoints(Activity a) {
        //addPoints
        SharedPreferences sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String Spoints = sharedPreferences.getString(PREF_POINTS, "0");
        String SpointsLifetime = sharedPreferences.getString(PREF_POINTS_LIFETIME, "0");
        //change string points to integer for calculations
        int Ipoints = Integer.parseInt(Spoints);
        int IpointsLifetime = Integer.parseInt(SpointsLifetime);

        //add a point for user
        Ipoints = Ipoints + 2;
        IpointsLifetime = IpointsLifetime + 2;

        //is user has more than 380 points
        if (Ipoints > warning_points) {
            addPointsDataToFirebase(a);
        }

        //store points back to shared preference for future reference
        Spoints = String.valueOf(Ipoints);
        SpointsLifetime = String.valueOf(IpointsLifetime);
        //add to shared preferences
        sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_POINTS, Spoints);
        editor.putString(PREF_POINTS_LIFETIME, SpointsLifetime);
        editor.apply();
    }

    public static void showEstimateDialog(Activity a) {
        SharedPreferences sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String Susername = sharedPreferences.getString(PREF_USERNAME, "N/A");
        String Spoints = sharedPreferences.getString(PREF_POINTS, "0");

        //calculate money earned estimate
        int Ipoints = Integer.parseInt(Spoints);
        //calculate money from points
        double Docash = Ipoints * coins;
        //show only two decimal places
        Docash = Double.parseDouble(String.format("%.2f", Docash));

        //show next dialog for knowledge
        Dialog current_estimate_dialog = new Dialog(a);
        current_estimate_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        current_estimate_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        current_estimate_dialog.setCancelable(false);
        current_estimate_dialog.setContentView(R.layout.dialog_current_estimate);

        Button okay = current_estimate_dialog.findViewById(R.id.okay);
        TextView user_estimate = current_estimate_dialog.findViewById(R.id.user_estimate);

        //show user how much money they have earned so far
        user_estimate.setText("Dear, " + Susername + ",\n" +
                "Your current estimated earnings are $" + Docash + "\n\n" +
                "You can watch the Ads to earn more");

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exit dialogs to continue using app
                current_estimate_dialog.dismiss();
            }
        });

        current_estimate_dialog.show();
    }

    public static void addPointsDataToFirebase(Activity a) {
        SharedPreferences sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String Spoints = sharedPreferences.getString(PREF_POINTS, "0");
        String SpointsLifetime = sharedPreferences.getString(PREF_POINTS_LIFETIME, "0");
        String Susername = sharedPreferences.getString(PREF_USERNAME, "N/A");
        String Semail = sharedPreferences.getString(PREF_EMAIL, "N/A");
        //calculate current date and time
        String dateNow = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String date = dateNow + " - " + currentTime;

        // Variables for Firebase Database.
        FirebaseDatabase firebaseDatabase;
        // creating a variable for our Database
        // Reference for Firebase.
        DatabaseReference databaseReferencePoints;
        // creating a variable for
        // our object class
        UserPointsData userPointsData;

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        // for user ratings
        databaseReferencePoints = firebaseDatabase.getReference();
        databaseReferencePoints = databaseReferencePoints.child("UsersPointsUp380").push();

        // initializing our object
        // class variable.
        userPointsData = new UserPointsData();

//        UserPointsData.setUserName(Susername);
//        UserPointsData.setUserEmail(Semail);
//        UserPointsData.setUserPoints(Spoints);
//        UserPointsData.setUserPointsLifetime(SpointsLifetime);
//        UserPointsData.setUserDate(date);

        // we are use add value event listener method
        // which is called with database reference.
        DatabaseReference finalDatabaseReferencePoints = databaseReferencePoints;
        databaseReferencePoints.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                finalDatabaseReferencePoints.setValue(userPointsData);

                // after adding this data we are showing toast message.
                //Constants.showToast("Rating Submitted !", MainActivity.this);
                //Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();

                //make UI usable again
                //rating_dialog.dismiss();
                //loading.setVisibility(View.INVISIBLE);
                //linear_login.setVisibility(View.VISIBLE);
                //adError.setVisibility(View.INVISIBLE);
                //loading_dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                //Constants.showToast("Failed ! Please Try Again", a);
                Log.e("FIREBASE POINTS >>>", String.valueOf(error));
                //Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();

                //make UI usable
                //loading.setVisibility(View.INVISIBLE);
                //linear_login.setVisibility(View.VISIBLE);
                //adError.setVisibility(View.INVISIBLE);
                //loading_dialog.dismiss();
            }
        });

    }

    public static void addRegistrationDataToFirebase(Activity a, String userName, String userEmail,
                                                     String date) {
        //show user that you are loading
        showLoadingDialog(a);
        //initialize variables
        FirebaseDatabase firebaseDatabase;
        // creating a variable for our Database
        // Reference for Firebase.
        DatabaseReference databaseReference;

        // creating a variable for
        // our object class
        UserRegisterData userRegisterData;

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        //databaseReference = firebaseDatabase.getReference("RegisteredUsers");

        databaseReference = firebaseDatabase.getReference();
        databaseReference = databaseReference.child("AdWorkRegisteredUsers").push();

        // initializing our object
        // class variable.
        userRegisterData = new UserRegisterData();
        // below lines of code is used to set
        // data in our object class.
        userRegisterData.setUserName(userName);
        userRegisterData.setUserEmail(userEmail);
        userRegisterData.setUserDate(date);

        // we are use add value event listener method
        // which is called with database reference.
        DatabaseReference finalDatabaseReference = databaseReference;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                finalDatabaseReference.setValue(userRegisterData);

                // after adding this data we are showing toast message.
                Constants.showToast("Successfully Registered !", a);

                //make UI usable
                loadingDialog.dismiss();

                //set logged in cookie
                SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(a.getBaseContext());
                SharedPreferences.Editor e = getSharedPreferences.edit();
                e.putBoolean("AdworkisLoggedin", true);
                e.apply();

                //go to Main Activity
                Intent i = new Intent(a, MainActivity.class);
                a.startActivity(i);
                a.finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Constants.showToast("Failed ! Please Try Again", a);
                Log.e("FIREBASE ERROR >>>", String.valueOf(error));

                //make UI usable
                loadingDialog.dismiss();
            }
        });
    }

    public static void showLoadingDialog(Activity a) {
        //show dialog
        loadingDialog = new Dialog(a);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.dialog_please_wait2);
        //show user the dialog for loading
        loadingDialog.show();
    }

}

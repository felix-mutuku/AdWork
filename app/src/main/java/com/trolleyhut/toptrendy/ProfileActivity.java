package com.trolleyhut.toptrendy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    TextView userName, userEmail, userDateJoined, estEarnings, factsSeen, adsWatched,
            adsSkipped, totalStreaks, totalWithdrawals, deleteAccount;
    Button buttonWithdraw;
    String SuserName, SuserEmail, SuserDateJoined, SestEarnings, SfactsSeen, SadsWatched,
            SadsSkipped, StotalStreaks, StotalWithdrawals, Spoints, SpointsLifeTime, SpointsDeducted;
    Dialog withdraw_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialize mobile ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //show initialization status
                Log.e("ADS INITIALIZATION >>>", String.valueOf(initializationStatus));
            }
        });

        //show banner AD
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userDateJoined = findViewById(R.id.userDateJoined);
        estEarnings = findViewById(R.id.estEarnings);
        factsSeen = findViewById(R.id.factsSeen);
        adsWatched = findViewById(R.id.adsWatched);
        adsSkipped = findViewById(R.id.adsSkipped);
        totalStreaks = findViewById(R.id.totalStreaks);
        totalWithdrawals = findViewById(R.id.totalWithdrawals);
        deleteAccount = findViewById(R.id.deleteAccount);
        buttonWithdraw = findViewById(R.id.buttonWithdraw);

        buttonWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show withdrawal dialog
                showWithdrawDialog();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show delete account dialog

            }
        });

        //get information needed for calculations
        getUserInfoFromPreferences();

    }

    private void getUserInfoFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SuserName = sharedPreferences.getString(Constants.PREF_USERNAME, "N/A");
        SuserEmail = sharedPreferences.getString(Constants.PREF_EMAIL, "N/A");
        SuserDateJoined = sharedPreferences.getString(Constants.PREF_DATE_JOINED, "N/A");
        SestEarnings = sharedPreferences.getString(Constants.PREF_EST_EARNINGS, "0");
        SfactsSeen = sharedPreferences.getString(Constants.PREF_FACTS_SEEN, "0");
        SadsWatched = sharedPreferences.getString(Constants.PREF_ADS_WATCHED, "0");
        SadsSkipped = sharedPreferences.getString(Constants.PREF_ADS_SKIPPED, "0");
        StotalStreaks = sharedPreferences.getString(Constants.PREF_TOTAL_STREAKS, "0");
        StotalWithdrawals = sharedPreferences.getString(Constants.PREF_TOTAL_WITHDRAWALS, "0");
        Spoints = sharedPreferences.getString(Constants.PREF_POINTS, "0");
        SpointsLifeTime = sharedPreferences.getString(Constants.PREF_POINTS_LIFETIME, "0");
        SpointsDeducted = sharedPreferences.getString(Constants.PREF_POINTS_DEDUCTED, "0");


        //check if login is okay
        if (SuserName.equals("N/A")) {
            //set login status to false and login again
            SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor e = getSharedPreferences.edit();
            e.putBoolean(Constants.LOGIN_TOKEN, false);
            e.apply();

            //start app from beginning
            Intent intent = new Intent(ProfileActivity.this, SplashScreenActivity.class);
            ProfileActivity.this.startActivity(intent);
            finish();
        } else {
            //populate user info into view
            userName.setText(SuserName);
            userEmail.setText(SuserEmail);
            userDateJoined.setText(SuserDateJoined);
            estEarnings.setText("$" + SestEarnings);
            factsSeen.setText(SfactsSeen);
            adsWatched.setText(SadsWatched);
            adsSkipped.setText(SadsSkipped);
            totalStreaks.setText(StotalStreaks);
            totalWithdrawals.setText(StotalWithdrawals);

            //show critical buttons to user
            buttonWithdraw.setVisibility(View.VISIBLE);
            deleteAccount.setVisibility(View.VISIBLE);
        }
    }

    private void showWithdrawDialog() {
        //show withdraw dialog
        withdraw_dialog = new Dialog(ProfileActivity.this);
        withdraw_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        withdraw_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        withdraw_dialog.setCancelable(false);
        withdraw_dialog.setContentView(R.layout.dialog_withdraw);

        EditText paypalEmail = withdraw_dialog.findViewById(R.id.paypal_email);
        Button withdraw = withdraw_dialog.findViewById(R.id.buttonWithdraw);
        Button close = withdraw_dialog.findViewById(R.id.buttonClose);

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pemail = paypalEmail.getText().toString();
                //calculate time and date
                String dateNow = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String date = dateNow + " - " + currentTime;
                //send to firebase
                Constants.addWithdrawDataToFirebase(SuserName, Pemail, SuserDateJoined, SestEarnings,
                        SfactsSeen, SadsWatched, SadsSkipped, StotalStreaks, StotalWithdrawals, Spoints,
                        SpointsDeducted, SpointsLifeTime, date, ProfileActivity.this);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close dialog
                withdraw_dialog.dismiss();
            }
        });

        withdraw_dialog.show(); //don't forget to dismiss the dialog when done loading
    }
}
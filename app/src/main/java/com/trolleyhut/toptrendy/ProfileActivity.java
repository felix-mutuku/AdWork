package com.trolleyhut.toptrendy;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
            SadsSkipped, StotalWithdrawals, Spoints, SpointsLifeTime, SpointsDeducted, StotalStreaks;
    Dialog withdraw_dialog, delete_dialog, warning_dialog;
    ImageView back, share;

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

        share = findViewById(R.id.share);
        back = findViewById(R.id.back);
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
                int NumPoints = Integer.parseInt(Spoints);

                if (NumPoints >= Constants.max_points) {
                    //show withdrawal dialog
                    showWithdrawDialog();
                } else {
                    //show least required money dialog
                    showWithdrawWarningDialog();
                }
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show delete account dialog
                showDeleteDialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back
                onBackPressed();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share app with others
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "AdWork App");
                    String sAux = "\nThis App let's you earn money by rating facts True or False\n\n" +
                            "Download today and earn a living online!\n\n";
                    sAux = sAux + "http://play.google.com/store/apps/details?id=com.trolleyhut.toptrendy";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //show toast
                    Constants.showToast("Please Try Again Later :(", ProfileActivity.this);
                }
            }
        });

        //get information needed for calculations
        getUserInfoFromPreferences();

        //check streaks
        ConsecutiveDayChecker.onUserLogin(ProfileActivity.this);

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
        //StotalStreaks = sharedPreferences.getString(Constants.PREF_TOTAL_STREAKS, "0");
        StotalWithdrawals = sharedPreferences.getString(Constants.PREF_TOTAL_WITHDRAWALS, "0");
        Spoints = sharedPreferences.getString(Constants.PREF_POINTS, "0");
        SpointsLifeTime = sharedPreferences.getString(Constants.PREF_POINTS_LIFETIME, "0");
        SpointsDeducted = sharedPreferences.getString(Constants.PREF_POINTS_DEDUCTED, "0");

        StotalStreaks = String.valueOf(ConsecutiveDayChecker.getStreak(ProfileActivity.this));

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

    private void showWithdrawWarningDialog() {
        //show withdraw dialog
        warning_dialog = new Dialog(ProfileActivity.this);
        warning_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        warning_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        warning_dialog.setCancelable(false);
        warning_dialog.setContentView(R.layout.dialog_withdraw_warning);

        Button gotIt = warning_dialog.findViewById(R.id.buttonGotIt);

        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close dialog
                warning_dialog.dismiss();
            }
        });

        warning_dialog.show(); //don't forget to dismiss the dialog when done loading
    }

    private void showDeleteDialog() {
        //show withdraw dialog
        delete_dialog = new Dialog(ProfileActivity.this);
        delete_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delete_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        delete_dialog.setCancelable(false);
        delete_dialog.setContentView(R.layout.dialog_delete_profile);

        Button delete = delete_dialog.findViewById(R.id.buttonDelete);
        Button close = delete_dialog.findViewById(R.id.buttonClose);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete all user details in account
                deleteUserData();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close dialog
                delete_dialog.dismiss();
            }
        });

        delete_dialog.show(); //don't forget to dismiss the dialog when done loading
    }

    private void deleteUserData() {
        //delete all data from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREF_USERNAME, "N/A");
        editor.putString(Constants.PREF_EMAIL, "N/A");
        editor.putString(Constants.PREF_POINTS, "0");
        editor.putString(Constants.PREF_POINTS_LIFETIME, "0");
        editor.putString(Constants.PREF_DATE_JOINED, "N/A");
        editor.putString(Constants.PREF_POINTS_DEDUCTED, "0");
        editor.putString(Constants.PREF_EST_EARNINGS, "0");
        editor.putString(Constants.PREF_ADS_WATCHED, "0");
        editor.putString(Constants.PREF_ADS_SKIPPED, "0");
        editor.putString(Constants.PREF_FACTS_SEEN, "0");
        //editor.putString(Constants.PREF_TOTAL_STREAKS, "0");
        editor.putString(Constants.PREF_TOTAL_WITHDRAWALS, "0");
        editor.apply();

        //set logged in cookie
        SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = getSharedPreferences.edit();
        e.putBoolean(Constants.LOGIN_TOKEN, false);
        e.apply();

        //go to splash activity and re-start the app
        Intent i = new Intent(ProfileActivity.this, SplashScreenActivity.class);
        startActivity(i);
        finish();
    }
}
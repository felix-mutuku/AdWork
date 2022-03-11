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
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Constants {
    public static final String BASE_URL_LOGIC = "";
    public static double coins = 0.05; //one point is $0.05 or Ksh5
    public static int warning_points = 380; //one point is $0.05 or Ksh5
    public static int max_points = 400; //one point is $0.05 or Ksh5
    public static Dialog loadingDialog;
    public static Dialog current_estimate_dialog;
    public static RewardedAd mRewardedAd;
    public static InterstitialAd mInterstitialAd;

    //saving a user's personal data in shared preferences
    public static final String SHARED_PREF_NAME = "AdWork_pref";
    public static final String PREF_USERNAME = "adwork_username";
    public static final String PREF_EMAIL = "adwork_email";
    public static final String PREF_POINTS = "adwork_points";
    public static final String PREF_POINTS_LIFETIME = "adwork_points_lifetime";
    public static final String PREF_DATE_JOINED = "adwork_date_joined";
    public static final String PREF_POINTS_DEDUCTED = "adwork_points_deducted";
    public static final String PREF_EST_EARNINGS = "adwork_est_earnings";
    public static final String PREF_ADS_WATCHED = "adwork_ads_watched";
    public static final String PREF_ADS_SKIPPED = "adwork_ads_skipped";
    public static final String PREF_FACTS_SEEN = "adwork_facts_seen";
    //public static final String PREF_TOTAL_STREAKS = "adwork_total_streaks";
    public static final String PREF_TOTAL_WITHDRAWALS = "adwork_total_withdrawals";

    //app tokens
    public static final String LOGIN_TOKEN = "AdworkisLoggedin";
    public static final String INFO_TOKEN = "AdworkfirstTimeUseInfo";
    public static final String MAIN_TOKEN = "AdworkfirstTimeUseMain";
    public static final String MAIN_KNOWLEDGE_TOKEN = "AdworkfirstTimeUseMainKnowledge";
    public static final String SPLASH_TOKEN = "AdworkfirstStartUp";

    //Firebase database names
    public static final String FIREBASE_REGISTERED_USERS = "AdWorkRegisteredUsers";
    public static final String FIREBASE_WITHDRAWALS = "AdWorkWithdrawals";

    //Notification data
    public static final String NOTIFICATION_TITLE = "AdWork - make money online";
    public static final String NOTIFICATION_CONTENT = "Keep the streak going! Continue making money online by rating some more facts";
    public static final String NOTIFICATION_CHANNEL_NAME = "ADWORK_CHANNEL_NAME";

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
        String SfactSeen = sharedPreferences.getString(PREF_FACTS_SEEN, "0");
        //change string points to integer for calculations
        int Ipoints = Integer.parseInt(Spoints);
        int IpointsLifetime = Integer.parseInt(SpointsLifetime);
        int IfactSeen = Integer.parseInt(SfactSeen);

        //add a point for user
        Ipoints = Ipoints + 1;
        IpointsLifetime = IpointsLifetime + 1;
        IfactSeen = IfactSeen + 1;

        //is user has more than 380 points
//        if (Ipoints > warning_points) {
//            addPointsDataToFirebase(a);
//        }

        //store points back to shared preference for future reference
        Spoints = String.valueOf(Ipoints);
        SpointsLifetime = String.valueOf(IpointsLifetime);
        SfactSeen = String.valueOf(IfactSeen);
        //add to shared preferences
        sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_POINTS, Spoints);
        editor.putString(PREF_POINTS_LIFETIME, SpointsLifetime);
        editor.putString(PREF_FACTS_SEEN, SfactSeen);
        editor.apply();

        //show estimate dialog
        showEstimateDialog(a);

        //show reward ad to user
        try {
            showRewardAd(a);
        } catch (Exception e) {
            Log.e("ADS ERROR!", String.valueOf(e));
        }

    }

    public static void addTwoPoints(Activity a) {
        //close estimate dialog
        current_estimate_dialog.dismiss();

        //addPoints
        SharedPreferences sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String Spoints = sharedPreferences.getString(PREF_POINTS, "0");
        String SpointsLifetime = sharedPreferences.getString(PREF_POINTS_LIFETIME, "0");
        String SadWatched = sharedPreferences.getString(PREF_ADS_WATCHED, "0");
        //change string points to integer for calculations
        int Ipoints = Integer.parseInt(Spoints);
        int IpointsLifetime = Integer.parseInt(SpointsLifetime);
        int IadWatched = Integer.parseInt(SadWatched);

        //add a point for user
        Ipoints = Ipoints + 2;
        IpointsLifetime = IpointsLifetime + 2;
        IadWatched = IadWatched + 1;

        //is user has more than 380 points
//        if (Ipoints > warning_points) {
//            addPointsDataToFirebase(a);
//        }

        //store points back to shared preference for future reference
        Spoints = String.valueOf(Ipoints);
        SpointsLifetime = String.valueOf(IpointsLifetime);
        SadWatched = String.valueOf(IadWatched);
        //add to shared preferences
        sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_POINTS, Spoints);
        editor.putString(PREF_POINTS_LIFETIME, SpointsLifetime);
        editor.putString(PREF_ADS_WATCHED, SadWatched);
        editor.apply();

        //show estimate dialog again
        showEstimateDialog(a);
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

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_EST_EARNINGS, String.valueOf(Docash));
        editor.apply();

        //show next dialog for knowledge
        current_estimate_dialog = new Dialog(a);
        current_estimate_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        current_estimate_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        current_estimate_dialog.setCancelable(false);
        current_estimate_dialog.setContentView(R.layout.dialog_current_estimate);

        Button okay = current_estimate_dialog.findViewById(R.id.okay);
        TextView user_estimate = current_estimate_dialog.findViewById(R.id.user_estimate);
        TextView countdown_txt = current_estimate_dialog.findViewById(R.id.countdown_txt);
        LinearLayout countdown_linear = current_estimate_dialog.findViewById(R.id.countdown_linear);

        //show user how much money they have earned so far
        user_estimate.setText("Dear, " + Susername + ",\n" +
                "Your current estimated earnings are $" + Docash + "\n\n" +
                "You can watch the Ads to earn more");

        //Declare timer
        CountDownTimer cTimer = null;
        //start timer function
        cTimer = new CountDownTimer(6000, 1000) {
            public void onTick(long millisUntilFinished) {
                //show something maybe
                countdown_txt.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //hide countdown timer
                countdown_linear.setVisibility(View.GONE);
                //display button
                okay.setVisibility(View.VISIBLE);
            }
        };
        cTimer.start();

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exit dialogs to continue using app
                current_estimate_dialog.dismiss();
            }
        });

        current_estimate_dialog.show();
    }

    public static void adSkipped(Activity a) {
        SharedPreferences sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String SadSkipped = sharedPreferences.getString(PREF_ADS_SKIPPED, "0");
        //change string points to integer for calculations
        int IadSkipped = Integer.parseInt(SadSkipped);

        //add a point for user
        IadSkipped = IadSkipped + 1;

        //store points back to shared preference for future reference
        SadSkipped = String.valueOf(IadSkipped);
        //add to shared preferences
        sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_ADS_SKIPPED, SadSkipped);
        editor.apply();
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
        databaseReference = databaseReference.child(FIREBASE_REGISTERED_USERS).push();

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
                e.putBoolean(LOGIN_TOKEN, true);
                e.apply();

                //go to Main Activity
                Intent i = new Intent(a, MainActivity2.class);
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

    public static void addWithdrawDataToFirebase(String userName, String userEmail, String userDateJoined,
                                                 String estEarnings, String factsSeen, String adsWatched,
                                                 String adsSkipped, String totalStreaks, String totalWithdrawals,
                                                 String points, String pointsDeducted, String pointsLifeTime,
                                                 String date, Activity a) {

        //show user that you are loading
        showLoadingDialog(a);
        //initialize variables
        FirebaseDatabase firebaseDatabase;
        // creating a variable for our Database
        // Reference for Firebase.
        DatabaseReference databaseReference;

        // creating a variable for
        // our object class
        UserWithdrawData userWithdrawData;

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        //databaseReference = firebaseDatabase.getReference("RegisteredUsers");

        databaseReference = firebaseDatabase.getReference();
        databaseReference = databaseReference.child(FIREBASE_WITHDRAWALS).push();

        // initializing our object
        // class variable.
        userWithdrawData = new UserWithdrawData();
        // below lines of code is used to set
        // data in our object class.
        userWithdrawData.setUserName(userName);
        userWithdrawData.setUserEmail(userEmail);
        userWithdrawData.setUserDate(date);
        userWithdrawData.setUserDateJoined(userDateJoined);
        userWithdrawData.setEstEarnings(estEarnings);
        userWithdrawData.setFactsSeen(factsSeen);
        userWithdrawData.setAdsWatched(adsWatched);
        userWithdrawData.setAdsSkipped(adsSkipped);
        userWithdrawData.setTotalStreaks(totalStreaks);
        userWithdrawData.setTotalWithdrawals(totalWithdrawals);
        userWithdrawData.setPoints(points);
        userWithdrawData.setPointsDeducted(pointsDeducted);
        userWithdrawData.setPointsLifeTime(pointsLifeTime);

        // we are use add value event listener method
        // which is called with database reference.
        DatabaseReference finalDatabaseReference = databaseReference;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                finalDatabaseReference.setValue(userWithdrawData);

                // after adding this data we are showing toast message.
                Constants.showToast("Request Sent !", a);

                //make UI usable
                loadingDialog.dismiss();

                //go to Main Activity
                Intent i = new Intent(a, MainActivity2.class);
                a.startActivity(i);
                a.finish();

                //show success dialog
                showWithDrawSuccessDialog(a);
                //reset points and EST earnings
                resetPoints(a);
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

    private static void resetPoints(Activity a) {
        SharedPreferences sharedPreferences = a.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String StotalWithdrawals = sharedPreferences.getString(PREF_TOTAL_WITHDRAWALS, "0");

        //add one to withdrawals
        int NumWithdrawals = Integer.parseInt(StotalWithdrawals);
        NumWithdrawals = NumWithdrawals + 1;

        //reset points and est earnings
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_POINTS, "0");
        editor.putString(PREF_EST_EARNINGS, "0");
        editor.putString(PREF_TOTAL_WITHDRAWALS, String.valueOf(NumWithdrawals));
        editor.apply();
    }

    private static void showWithDrawSuccessDialog(Activity a) {
        //show next dialog for knowledge
        Dialog withdraw_success_dialog = new Dialog(a);
        withdraw_success_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        withdraw_success_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        withdraw_success_dialog.setCancelable(true);
        withdraw_success_dialog.setContentView(R.layout.dialog_withdraw_success);

        Button gotIt = withdraw_success_dialog.findViewById(R.id.buttonGotIt);

        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exit dialogs to continue using app
                withdraw_success_dialog.dismiss();
            }
        });

        withdraw_success_dialog.show();
    }

    public static void showRewardAd(Activity a) {
        //show full screen reward ad
        final String TAG = "RewardedAD";
        //Sample AD UNIT ID: ca-app-pub-3940256099942544/5224354917
        //REAL AD UNIT ID: ca-app-pub-5123885596101098/3933992145
        String AD_UNIT_ID_REWARD = "ca-app-pub-5123885596101098/3933992145";

        //mRewardedAd = null;

        //initialize mobile ads
        MobileAds.initialize(a, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //show initialization status
                Log.e("REWARDED INITIALIZE >>>", String.valueOf(initializationStatus));
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(a, AD_UNIT_ID_REWARD, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error.
                Log.e(TAG, loadAdError.getMessage());
                mRewardedAd = null;
                //show user full screen ad
                showFullScreenAd(a);
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                mRewardedAd = rewardedAd;
                Log.e(TAG, "Ad was loaded.");
            }

        });

        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                adSkipped(a);
                Log.d(TAG, "Ad was dismissed.");
                mRewardedAd = null;
            }
        });

        //show reward ad
        if (mRewardedAd != null) {
            Activity activityContext = a;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();

                    Log.e("rewardAmount>>", String.valueOf(rewardAmount));
                    Log.e("rewardType >>>", rewardType);

                    //add two points to the user
                    addTwoPoints(a);
                }
            });
        } else {
            //Constants.showToast("Please try again later", MainActivity.this);
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
            //show user full screen ad
            showFullScreenAd(a);
        }

    }

    public static void showFullScreenAd(Activity a) {
        //Sample AD UNIT ID: ca-app-pub-3940256099942544/1033173712
        //REAL AD UNIT ID: ca-app-pub-5123885596101098/7264792098
        String AD_UNIT_ID_FUll_SCREEN = "ca-app-pub-5123885596101098/7264792098";
        final String TAG = "FullScreenAD";

        //initialize mobile ads
        MobileAds.initialize(a, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //show initialization status
                Log.e("FULLSCRN INITIALIZE >>>", String.valueOf(initializationStatus));
            }
        });

        //show interstitial ad
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(a, AD_UNIT_ID_FUll_SCREEN, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoadedFullScreen");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                                adSkipped(a);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adErrorr) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");

                                //add two points
                                Constants.addTwoPoints(a);
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
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

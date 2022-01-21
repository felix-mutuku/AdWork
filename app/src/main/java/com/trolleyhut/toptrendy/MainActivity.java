package com.trolleyhut.toptrendy;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView username, points, adError;
    public boolean isFirstTimeUse;
    String Susername;
    String Spoints, SpointsLifetime;
    String Pemail;
    String SmoreDetails;
    int Ipoints;
    float numberOfStars;
    ImageView info, share;
    LinearLayout linear_login;
    AVLoadingIndicatorView loading;
    Button buttonWatch;
    Dialog withdraw_dialog, loading_dialog, rating_dialog;
    Dialog knowledge_one_dialog, knowledge_two_dialog, knowledge_three_dialog, withdraw_success_dialog;
    //loading ads
    private RewardedAd mRewardedAd;
    private InterstitialAd mInterstitialAd;
    private final String TAG = "MainActivity";
    //for calculating moneys
    double coins = 0.05; //one point is $0.05 or Ksh5
    int maxPoints = 4; //number of points allowed to make a withdrawal request

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReferenceRating;
    DatabaseReference databaseReferenceWithdraw;

    // creating a variable for
    // our object class
    UserRatingData userRatingData;
    UserWithdrawData userWithdrawData;
    //Sample AD UNIT ID: ca-app-pub-3940256099942544/5224354917
    //REAL AD UNIT ID: ca-app-pub-5123885596101098/3933992145
    String AD_UNIT_ID_REWARD = "ca-app-pub-5123885596101098/3933992145";
    //Sample AD UNIT ID: ca-app-pub-3940256099942544/1033173712
    //REAL AD UNIT ID: ca-app-pub-5123885596101098/7264792098
    String AD_UNIT_ID_FUll_SCREEN = "ca-app-pub-5123885596101098/7264792098";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

//        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
//                adRequest, new RewardedAdLoadCallback() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error.
//                        Log.e(TAG, loadAdError.getMessage());
//                        mRewardedAd = null;
//                    }
//
//                    @Override
//                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                        mRewardedAd = rewardedAd;
//                        Log.e(TAG, "Ad was loaded.");
//                    }
//
//                });

//        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//            @Override
//            public void onAdShowedFullScreenContent() {
//                // Called when ad is shown.
//                Log.e(TAG, "Ad was shown.");
//            }
//
//            @Override
//            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                // Called when ad fails to show.
//                Log.e(TAG, "Ad failed to show.");
//            }
//
//            @Override
//            public void onAdDismissedFullScreenContent() {
//                // Called when ad is dismissed.
//                // Set the ad reference to null so you don't show the ad a second time.
//                Log.d(TAG, "Ad was dismissed.");
//                mRewardedAd = null;
//            }
//        });


        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        // for user ratings
        databaseReferenceRating = firebaseDatabase.getReference();
        databaseReferenceRating = databaseReferenceRating.child("UsersRatings").push();
        //for user withdrawals done
        databaseReferenceWithdraw = firebaseDatabase.getReference();
        databaseReferenceWithdraw = databaseReferenceWithdraw.child("UsersWithdrawing").push();

        // initializing our object
        // class variable.
        userRatingData = new UserRatingData();
        userWithdrawData = new UserWithdrawData();

        //check if user is logged in
        checkKnowledgeStatus();

        ImageView main_pic = findViewById(R.id.main_pic);
        buttonWatch = findViewById(R.id.buttonWatch);
        loading = findViewById(R.id.loading);
        info = findViewById(R.id.info);
        share = findViewById(R.id.share);
        adError = findViewById(R.id.adError);

        RequestOptions options = new RequestOptions()
                //.centerCrop()
                //.placeholder(R.drawable.loading_gif)
                .skipMemoryCache(false)
                .error(R.drawable.logo)
                .priority(Priority.HIGH);

        Glide
                .with(MainActivity.this)
                .load(R.drawable.main_pic)
                .transition(withCrossFade())
                //.thumbnail(0.1f)
                .apply(options)
                .into(main_pic);

        username = findViewById(R.id.username);
        points = findViewById(R.id.points);
        linear_login = findViewById(R.id.linear_login);

        buttonWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something when sign in button clicked
                //make button disappear and show loading
                loading.setVisibility(View.VISIBLE);
                linear_login.setVisibility(View.INVISIBLE);
                adError.setVisibility(View.INVISIBLE);

                AdRequest adRequest = new AdRequest.Builder().build();

                RewardedAd.load(MainActivity.this, AD_UNIT_ID_REWARD, adRequest,
                        new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error.
                                Log.e(TAG, loadAdError.getMessage());
                                mRewardedAd = null;
                                tryFullScreenAd();
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                mRewardedAd = rewardedAd;
                                Log.e(TAG, "Ad was loaded.");
                            }

                        });

                //show reward ad
                if (mRewardedAd != null) {
                    Activity activityContext = MainActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();

                            Log.e("rewardAmount>>", String.valueOf(rewardAmount));
                            Log.e("rewardType >>>", rewardType);

                            //show UI again
                            loading.setVisibility(View.INVISIBLE);
                            linear_login.setVisibility(View.VISIBLE);
                            //rate ad to earn points
                            showRatingDialog();
                        }
                    });
                } else {
                    //Constants.showToast("Please try again later", MainActivity.this);
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                    tryFullScreenAd();
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show info activity
                Intent i = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(i);
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
                    String sAux = "\nAdWork App let's you earn money by watching and rating Videos and Ads\n\n" +
                            "Download AdWork today and earn a living online!\n\n";
                    sAux = sAux + "http://play.google.com/store/apps/details?id=com.trolleyhut.toptrendy";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //show toast
                    Constants.showToast("Please Try Again Later :(", MainActivity.this);
                }
            }
        });

        //get information needed for calculations
        getUserInfoFromPreferences();

    }

    private void tryFullScreenAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(MainActivity.this, AD_UNIT_ID_FUll_SCREEN, adRequest,
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
                                //make UI useable
                                loading.setVisibility(View.INVISIBLE);
                                linear_login.setVisibility(View.VISIBLE);
                                //show toast to user
                                adError.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adErrorr) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                                //make UI useable
                                loading.setVisibility(View.INVISIBLE);
                                linear_login.setVisibility(View.VISIBLE);
                                //show toast to user
                                adError.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                                //show rating dialog
                                //show UI again
                                loading.setVisibility(View.INVISIBLE);
                                linear_login.setVisibility(View.VISIBLE);
                                //show toast to user
                                adError.setVisibility(View.INVISIBLE);
                                //rate ad to earn points
                                showRatingDialog();
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                        //make UI useable
                        loading.setVisibility(View.INVISIBLE);
                        linear_login.setVisibility(View.VISIBLE);
                        //show toast to user
                        adError.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void showRatingDialog() {
        //dialog starts here
        rating_dialog = new Dialog(MainActivity.this);
        rating_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rating_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rating_dialog.setCancelable(false);
        rating_dialog.setContentView(R.layout.dialog_rating);

        RatingBar ratingBar = rating_dialog.findViewById(R.id.ratingBar);
        EditText moreDetails = rating_dialog.findViewById(R.id.moreDetails);
        TextView buttonSkip = rating_dialog.findViewById(R.id.buttonSkip);
        Button buttonSubmit = rating_dialog.findViewById(R.id.buttonSubmit);

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add one point for the user
                //int Rpoints = Integer.parseInt(Spoints);
                //int RpointsLifetime = Integer.parseInt(SpointsLifetime);
                //Rpoints = Rpoints + 1;
                //RpointsLifetime = RpointsLifetime + 1;
                //Spoints = String.valueOf(Rpoints);
                //SpointsLifetime = String.valueOf(RpointsLifetime);
                //show new adjusted points
                //computePointsDatabase();
                Constants.showToast("Please rate to earn points !", MainActivity.this);
                rating_dialog.dismiss();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfStars = ratingBar.getRating(); //get total number of stars of rating bar

                SmoreDetails = moreDetails.getText().toString();

                //check that user has provided a rating
                if (numberOfStars > 0) {
                    //Constants.showToast(String.valueOf(numberOfStars), MainActivity.this);
                    //add two points for the user
                    int Rpoints = Integer.parseInt(Spoints);
                    int RpointsLifetime = Integer.parseInt(SpointsLifetime);
                    Rpoints = Rpoints + 2;
                    RpointsLifetime = RpointsLifetime + 2;
                    Spoints = String.valueOf(Rpoints);
                    SpointsLifetime = String.valueOf(RpointsLifetime);

                    //show new adjusted points
                    computePointsDatabase();
                    //send data to firebase
                } else {
                    Constants.showToast("Provide a Star Rating!", MainActivity.this);
                }
            }
        });

        rating_dialog.show(); //don't forget to dismiss the dialog when done loading
    }

    private void getUserInfoFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Susername = sharedPreferences.getString(Constants.PREF_USERNAME, "N/A");
        Spoints = sharedPreferences.getString(Constants.PREF_POINTS, "0");
        SpointsLifetime = sharedPreferences.getString(Constants.PREF_POINTS_LIFETIME, "0");

        Log.e("WE REACH HERE >>>", "getUserInfoFromPreferences<<<<");

        //check if login is okay
        if (!Susername.equals("N/A")) {
            //get user information from preferences
            username.setText(Susername + ", earn money from watching and rating video ads.");
        } else {
            //set login status to false and login again
            SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor e = getSharedPreferences.edit();
            e.putBoolean("isLoggedin", false);
            e.apply();

            //start app from beginning
            Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
            MainActivity.this.startActivity(intent);
            finish();
        }

        //change points string to integer
        Ipoints = Integer.parseInt(Spoints);
        //check if points are more than 100
        if (Ipoints >= maxPoints) {
            //show withdraw dialog
            showWithdrawDialog();
        } else {
            //compute how much money they have from the points
            computePoints();
        }
    }

    private void computePoints() {
        try {
            //change points string to integer
            Ipoints = Integer.parseInt(Spoints);
            //calculate money from points
            double Dcash = Ipoints * coins;
            //show only two decimal places
            Dcash = Double.parseDouble(String.format("%.2f", Dcash));
            //show user how many points & money they have
            points.setText("Account: " + Ipoints + " points (EST $" + Dcash + " )");
            //update points in shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.PREF_POINTS, Spoints);
            editor.putString(Constants.PREF_POINTS_LIFETIME, SpointsLifetime);
            editor.apply();

            if (Ipoints >= maxPoints) {
                //show withdraw dialog
                showWithdrawDialog();
            } else {
                //compute how much money they have from the points
                //change points string to integer
                Ipoints = Integer.parseInt(Spoints);
                //calculate money from points
                double Docash = Ipoints * coins;
                //show only two decimal places
                Docash = Double.parseDouble(String.format("%.2f", Docash));
                //show user how many points & money they have
                points.setText("Account: " + Ipoints + " points (EST $" + Docash + " )");
            }
        } catch (Exception e) {
            Log.e("Compute Points Error>>", String.valueOf(e));
            Constants.showToast("Good Job!", MainActivity.this);
        }
    }

    private void computePointsDatabase() {
        try {
            //change points string to integer
            Ipoints = Integer.parseInt(Spoints);
            //calculate money from points
            double Dcash = Ipoints * coins;
            //show only two decimal places
            Dcash = Double.parseDouble(String.format("%.2f", Dcash));
            //show user how many points & money they have
            points.setText("Account: " + Ipoints + " points (EST $" + Dcash + " )");
            //update points in shared preferences
            addInfoToSharedPreference();
        } catch (Exception e) {
            Log.e("Compute Points Error>>", String.valueOf(e));
            Constants.showToast("Good Job!", MainActivity.this);
        }
    }

    private void addInfoToSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREF_POINTS, Spoints);
        editor.putString(Constants.PREF_POINTS_LIFETIME, SpointsLifetime);
        editor.apply();

        if (Ipoints >= maxPoints) {
            //show withdraw dialog
            showWithdrawDialog();
        } else {
            //compute how much money they have from the points
            //change points string to integer
            Ipoints = Integer.parseInt(Spoints);
            //calculate money from points
            double Dcash = Ipoints * coins;
            //show only two decimal places
            Dcash = Double.parseDouble(String.format("%.2f", Dcash));
            //show user how many points & money they have
            points.setText("Account: " + Ipoints + " points (EST $" + Dcash + " )");

            //add to database
            showLoadingDialog();
            //calculate current date and time
            String dateNow = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String date = dateNow + " - " + currentTime;
            //add data to firebase
            addRatingDatatoFirebase(Susername, Spoints, SpointsLifetime, SmoreDetails, numberOfStars, date);
        }
    }

    private void showWithdrawDialog() {
        //show withdraw dialog
        withdraw_dialog = new Dialog(MainActivity.this);
        withdraw_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        withdraw_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        withdraw_dialog.setCancelable(false);
        withdraw_dialog.setContentView(R.layout.dialog_withdraw);

        EditText paypalEmail = withdraw_dialog.findViewById(R.id.paypal_email);
        Button withdraw = withdraw_dialog.findViewById(R.id.buttonWithdraw);

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pemail = paypalEmail.getText().toString();
                //send data to firebase
                showLoadingDialog();
                //calculate time and date
                String dateNow = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String date = dateNow + " - " + currentTime;
                //send to firebase
                addWithdrawDatatoFirebase(Susername, Pemail, Spoints, SpointsLifetime, date);
            }
        });

        withdraw_dialog.show(); //don't forget to dismiss the dialog when done loading
    }

    private void showLoadingDialog() {
        loading_dialog = new Dialog(MainActivity.this);
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_dialog.setCancelable(false);
        loading_dialog.setContentView(R.layout.dialog_please_wait2);

        loading_dialog.show(); //don't forget to dismiss the dialog when done loading
        //loading_dialog.dismiss();
    }

    private void addRatingDatatoFirebase(String userName, String userPoints, String userPointsLifetime,
                                         String moreDetails, float numberOfStars, String date) {
        // below lines of code is used to set
        // data in our object class.
        String starsRated = String.valueOf(numberOfStars);

        userRatingData.setUserName(userName);
        userRatingData.setUserPoints(userPoints);
        userRatingData.setUserPointsLifetime(userPointsLifetime);
        userRatingData.setMoreDetails(moreDetails);
        userRatingData.setStarsRated(starsRated);
        userRatingData.setUserDate(date);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReferenceRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReferenceRating.setValue(userRatingData);

                // after adding this data we are showing toast message.
                Constants.showToast("Rating Submitted !", MainActivity.this);
                //Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();

                //make UI usable again
                rating_dialog.dismiss();
                loading.setVisibility(View.INVISIBLE);
                linear_login.setVisibility(View.VISIBLE);
                adError.setVisibility(View.INVISIBLE);
                loading_dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Constants.showToast("Failed ! Please Try Again", MainActivity.this);
                Log.e("FIREBASE ERROR >>>", String.valueOf(error));
                //Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();

                //make UI usable
                loading.setVisibility(View.INVISIBLE);
                linear_login.setVisibility(View.VISIBLE);
                adError.setVisibility(View.INVISIBLE);
                loading_dialog.dismiss();
            }
        });
    }

    private void addWithdrawDatatoFirebase(String userName, String userEmail, String userPoints,
                                           String userPointsLifetime, String date) {
        // below lines of code is used to set
        // data in our object class.
        userWithdrawData.setUserName(userName);
        userWithdrawData.setUserEmail(userEmail);
        userWithdrawData.setUserPoints(userPoints);
        userWithdrawData.setUserPointsLifetime(userPointsLifetime);
        userWithdrawData.setUserDate(date);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReferenceWithdraw.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReferenceWithdraw.setValue(userWithdrawData);

                String nilPoints = "0";
                //clear points to start earning again
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.PREF_POINTS, nilPoints);
                editor.apply();

                // after adding this data we are showing toast message.
                Constants.showToast("Withdrawal request submitted !", MainActivity.this);
                //Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();

                //make UI usable again
                loading.setVisibility(View.INVISIBLE);
                linear_login.setVisibility(View.VISIBLE);
                adError.setVisibility(View.INVISIBLE);
                loading_dialog.dismiss();
                withdraw_dialog.dismiss();
                //show user dialog
                showDialogSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Constants.showToast("Failed ! Please Try Again", MainActivity.this);
                Log.e("FIREBASE ERROR >>>", String.valueOf(error));
                //Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();

                //make UI usable
                loading.setVisibility(View.INVISIBLE);
                linear_login.setVisibility(View.VISIBLE);
                adError.setVisibility(View.INVISIBLE);
                loading_dialog.dismiss();
            }
        });
    }

    private void checkKnowledgeStatus() {
        SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        isFirstTimeUse = getSharedPreferences.getBoolean("firstTimeUse", true);

        if (isFirstTimeUse) {
            //show knowledge 1 for welcome
            knowledge_one_dialog = new Dialog(MainActivity.this);
            knowledge_one_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            knowledge_one_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            knowledge_one_dialog.setCancelable(false);
            knowledge_one_dialog.setContentView(R.layout.dialog_knowledge_one);

            Button gotIt = knowledge_one_dialog.findViewById(R.id.buttonGotIt);

            gotIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dismiss first dialog
                    knowledge_one_dialog.dismiss();
                    //show next dialog for knowledge
                    knowledge_two_dialog = new Dialog(MainActivity.this);
                    knowledge_two_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    knowledge_two_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    knowledge_two_dialog.setCancelable(false);
                    knowledge_two_dialog.setContentView(R.layout.dialog_knowledge_two);

                    Button gotIt = knowledge_two_dialog.findViewById(R.id.buttonGotIt);

                    gotIt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // dismiss first dialog
                            knowledge_two_dialog.dismiss();
                            //show next dialog for knowledge
                            knowledge_three_dialog = new Dialog(MainActivity.this);
                            knowledge_three_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            knowledge_three_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            knowledge_three_dialog.setCancelable(false);
                            knowledge_three_dialog.setContentView(R.layout.dialog_knowledge_three);

                            Button gotIt = knowledge_three_dialog.findViewById(R.id.buttonGotIt);

                            gotIt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //exit dialogs to continue using app
                                    //save knowledge session for user
                                    SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                    SharedPreferences.Editor e = getSharedPreferences.edit();
                                    e.putBoolean("firstTimeUse", false);
                                    e.apply();
                                    knowledge_three_dialog.dismiss();
                                }
                            });

                            knowledge_three_dialog.show(); //don't forget to dismiss the dialog when done loading
                        }
                    });

                    knowledge_two_dialog.show(); //don't forget to dismiss the dialog when done loading
                }
            });

            knowledge_one_dialog.show(); //don't forget to dismiss the dialog when done loading
        }
    }

    private void showDialogSuccess() {
        withdraw_dialog.dismiss();
        //show next dialog for knowledge
        withdraw_success_dialog = new Dialog(MainActivity.this);
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

}
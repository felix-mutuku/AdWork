package com.trolleyhut.toptrendy;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginSignUpActivity extends AppCompatActivity {
    public boolean isLoggedin;
    LinearLayout linear_login;
    AVLoadingIndicatorView loading;
    EditText username;
    Button buttonStart;
    String Susername = "N/A";

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    UserRegisterData userRegisterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        //initialize mobile ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //show initialization status
                Log.e("ADS INITIALIZATION >>>", String.valueOf(initializationStatus));
            }
        });

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        //databaseReference = firebaseDatabase.getReference("RegisteredUsers");

        databaseReference = firebaseDatabase.getReference();
        databaseReference = databaseReference.child("RegisteredUsers").push();

        // initializing our object
        // class variable.
        userRegisterData = new UserRegisterData();

        linear_login = findViewById(R.id.linear_login);
        loading = findViewById(R.id.loading);
        username = findViewById(R.id.username);
        buttonStart = findViewById(R.id.buttonStart);

        ImageView login_pic = findViewById(R.id.login_pic);

        RequestOptions options = new RequestOptions()
                //.centerCrop()
                //.placeholder(R.drawable.loading_gif)
                .skipMemoryCache(false)
                .error(R.drawable.logo)
                .priority(Priority.HIGH);

        Glide
                .with(LoginSignUpActivity.this)
                .load(R.drawable.login_background)
                .transition(withCrossFade())
                //.thumbnail(0.1f)
                .apply(options)
                .into(login_pic);

        //check if user is logged in
        checkLoginStatus();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something when sign in button clicked
                //make button disappear and show loading
                loading.setVisibility(View.VISIBLE);
                linear_login.setVisibility(View.INVISIBLE);
                //proceed to signing in
                signIn();
            }
        });

    }

    private void signIn() {
        Susername = username.getText().toString();
        //make sure username is not empty
        if (TextUtils.isEmpty(Susername)) {
            username.setError("Choose username!");
            loading.setVisibility(View.INVISIBLE);
            linear_login.setVisibility(View.VISIBLE);
            //Constants.showToast("Please choose a username!", LoginSignUpActivity.this);
        } else {
            //not empty, can proceed
            goToMain();
        }

    }

    private void checkLoginStatus() {
        SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        isLoggedin = getSharedPreferences.getBoolean("isLoggedin", false);

        if (isLoggedin) {
            //user has already logged in
            Intent i = new Intent(LoginSignUpActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void addInfoToSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREF_USERNAME, Susername);
        editor.apply();
    }

    private void goToMain() {
        //save login session for user
        SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = getSharedPreferences.edit();
        e.putBoolean("isLoggedin", true);
        e.apply();

        addInfoToSharedPreference();

        String date = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());

        //add username to firebase
        addDatatoFirebase(Susername, date);

        //go to main activity after user has finished successfully
        Intent i = new Intent(LoginSignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void addDatatoFirebase(String userName, String date) {
        // below lines of code is used to set
        // data in our object class.
        userRegisterData.setUserName(userName);
        userRegisterData.setUserDate(date);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.setValue(userRegisterData);

                // after adding this data we are showing toast message.
                Constants.showToast("Successfully Registered !", LoginSignUpActivity.this);
                //Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();

                //go to Main Activity
                goNow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Constants.showToast("Failed ! Please Try Again", LoginSignUpActivity.this);
                Log.e("FIREBASE ERROR >>>", String.valueOf(error));
                //Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();

                //make UI usable
                loading.setVisibility(View.INVISIBLE);
                linear_login.setVisibility(View.VISIBLE);
            }
        });
    }

    private void goNow() {
        //go to main activity after user has finished successfully
        Intent i = new Intent(LoginSignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}

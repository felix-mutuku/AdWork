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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginSignUpActivity extends AppCompatActivity {
    public boolean isLoggedin;
    LinearLayout linear_login;
    AVLoadingIndicatorView loading;
    EditText username, user_email;
    Button buttonStart;
    String Susername;
    String date_joined;
    String Semail;
    TextView terms;

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
        databaseReference = databaseReference.child("AdWorkRegisteredUsers").push();

        // initializing our object
        // class variable.
        userRegisterData = new UserRegisterData();

        linear_login = findViewById(R.id.linear_login);
        loading = findViewById(R.id.loading);
        username = findViewById(R.id.username);
        user_email = findViewById(R.id.user_email);
        buttonStart = findViewById(R.id.buttonStart);
        terms = findViewById(R.id.terms);

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
                //loading.setVisibility(View.VISIBLE);
                //linear_login.setVisibility(View.INVISIBLE);

                //check if internet connection is active
                Constants.checkInternet(LoginSignUpActivity.this);
                //proceed to signing in
                signIn();
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to info page
                Intent i = new Intent(LoginSignUpActivity.this, InfoActivity.class);
                startActivity(i);
            }
        });

    }

    private void signIn() {
        Susername = username.getText().toString();
        Semail = user_email.getText().toString();
        //check if email is of desired pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        //make sure username is not empty
        if (TextUtils.isEmpty(Susername)) {
            //username is empty,cancel operation
            username.setError("Choose username!");
            loading.setVisibility(View.INVISIBLE);
            linear_login.setVisibility(View.VISIBLE);
            //Constants.showToast("Please choose a username!", LoginSignUpActivity.this);
        } else if (!Semail.matches(emailPattern) || TextUtils.isEmpty(Semail)) {
            //username is okay, check if email is okay
            //email is either empty or wrongly typed, cancel  operations
            user_email.setError("Enter valid email address!");
            loading.setVisibility(View.INVISIBLE);
            linear_login.setVisibility(View.VISIBLE);
        } else {
            //calculate current date and time
            String dateNow = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String date = dateNow + " - " + currentTime;
            //add data to shared preferences
            addInfoToSharedPreference();
            //send data to firebase
            Constants.addRegistrationDataToFirebase(LoginSignUpActivity.this, Susername, Semail, date);
        }
    }

    private void checkLoginStatus() {
        SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        isLoggedin = getSharedPreferences.getBoolean("AdworkisLoggedin", false);

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
        editor.putString(Constants.PREF_EMAIL, Semail);
        editor.putString(Constants.PREF_DATE_JOINED, date_joined);
        editor.apply();
    }
}

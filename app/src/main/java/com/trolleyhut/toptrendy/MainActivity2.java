package com.trolleyhut.toptrendy;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    public boolean isFirstTimeUse;
    Dialog swipeMainDialog, knowledge_one_dialog, knowledge_two_dialog, knowledge_three_dialog;
    ImageView shuffle_cards;
    ImageView info, profile;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        shuffle_cards = findViewById(R.id.shuffle_cards);
        info = findViewById(R.id.info);
        mSwipeView = findViewById(R.id.swipeView);
        profile = findViewById(R.id.profile);
        mContext = getApplicationContext();

        //start alarm to show notifications to bring users back to the app
        myAlarm();

        int bottomMargin = Utils.dpToPx(120);
        Point windowSize = Utils.getDisplaySize(getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(5)
                .setIsUndoEnabled(true)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.CENTER_VERTICAL)
                        .setPaddingTop(50)
                        .setRelativeScale(0.01f)
                        .setSwipeMaxChangeAngle(2f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_out_msg_view));

        for (Questions questions : Utils.loadFacts(getApplicationContext())) {
            mSwipeView.addView(new QuestionsCard(mContext, questions, mSwipeView,
                    MainActivity2.this));
        }

        shuffle_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check internet
                Constants.checkInternet(MainActivity2.this);
                //shuffle cards
                for (Questions questions : Utils.loadFacts(getApplicationContext())) {
                    mSwipeView.addView(new QuestionsCard(mContext, questions, mSwipeView,
                            MainActivity2.this));
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show info activity
                Intent i = new Intent(MainActivity2.this, InfoActivity.class);
                startActivity(i);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open profile activity
                Intent i = new Intent(MainActivity2.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        //check if first time use
        checkKnowledgeStatus();

        //check Streaks
        ConsecutiveDayChecker.onUserLogin(MainActivity2.this);

    }

    private void showDialogSwipeMain() {
        //show dialog
        swipeMainDialog = new Dialog(MainActivity2.this);
        swipeMainDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        swipeMainDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        swipeMainDialog.setCancelable(false);
        swipeMainDialog.setContentView(R.layout.dialog_swipe_main);

        Button gotIt = swipeMainDialog.findViewById(R.id.buttonGotIt);

        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exit dialogs to continue using app
                SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor e = getSharedPreferences.edit();
                e.putBoolean(Constants.MAIN_TOKEN, false);
                e.apply();
                //dismiss dialog
                swipeMainDialog.dismiss();
            }
        });

        swipeMainDialog.show();
    }

    private void checkKnowledgeStatus() {
        SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        isFirstTimeUse = getSharedPreferences.getBoolean(Constants.MAIN_KNOWLEDGE_TOKEN, true);

        if (isFirstTimeUse) {
            //show knowledge 1 for welcome
            knowledge_one_dialog = new Dialog(MainActivity2.this);
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
                    knowledge_two_dialog = new Dialog(MainActivity2.this);
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
                            knowledge_three_dialog = new Dialog(MainActivity2.this);
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
                                    e.putBoolean(Constants.MAIN_KNOWLEDGE_TOKEN, false);
                                    e.apply();
                                    //close dialog
                                    knowledge_three_dialog.dismiss();

                                    //SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                    isFirstTimeUse = getSharedPreferences.getBoolean(Constants.MAIN_TOKEN, true);

                                    if (isFirstTimeUse) {
                                        showDialogSwipeMain();
                                    }
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

    public void myAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 05);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }

    }

}
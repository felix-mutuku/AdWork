package com.trolleyhut.toptrendy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
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

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

public class MainActivity2 extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    public boolean isFirstTimeUse;
    Dialog swipeMainDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mSwipeView = findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        int bottomMargin = Utils.dpToPx(120);
        Point windowSize = Utils.getDisplaySize(getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.CENTER_VERTICAL)
                        .setPaddingTop(50)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_out_msg_view));

        for (Questions questions : Utils.loadFacts(this.getApplicationContext())) {
            mSwipeView.addView(new QuestionsCard(mContext, questions, mSwipeView));
        }

        SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        isFirstTimeUse = getSharedPreferences.getBoolean(Constants.MAIN_TOKEN, true);

        if (isFirstTimeUse) {
            showDialogSwipeMain();
        }

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

}
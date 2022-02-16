package com.trolleyhut.toptrendy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class InfoActivity extends AppCompatActivity {
    ImageView back, share;
    public boolean isFirstTimeUse;
    Dialog swipeInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        // Find the view pager that will
        // allow the user to swipe
        // between fragments
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);

        // Create an adapter that
        // knows which fragment should
        // be shown on each page
        InfoFragmentPageAdapter adapter = new InfoFragmentPageAdapter(getSupportFragmentManager());

        // Set the adapter onto
        // the view pager
        viewPager.setAdapter(adapter);

        SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        isFirstTimeUse = getSharedPreferences.getBoolean("AdworkfirstTimeUseInfo", true);

        if (isFirstTimeUse) {
            showDialogSwipe();
        }

        share = findViewById(R.id.share);
        back = findViewById(R.id.back);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share app with others
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "AdWork App");
                    String sAux = "\nAdWork App let's you earn money by rating facts from around the world\n\n" +
                            "Download AdWork today and earn a living online!\n\n";
                    sAux = sAux + "http://play.google.com/store/apps/details?id=com.trolleyhut.toptrendy";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //show toast
                    Constants.showToast("Please Try Again Later :(", InfoActivity.this);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go backwards
                onBackPressed();
            }
        });
    }

    private void showDialogSwipe() {
        //show dialog
        swipeInfoDialog = new Dialog(InfoActivity.this);
        swipeInfoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        swipeInfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        swipeInfoDialog.setCancelable(false);
        swipeInfoDialog.setContentView(R.layout.dialog_swipe_info);

        Button gotIt = swipeInfoDialog.findViewById(R.id.buttonGotIt);

        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exit dialogs to continue using app
                SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor e = getSharedPreferences.edit();
                e.putBoolean("AdworkfirstTimeUseInfo", false);
                e.apply();
                //dismiss dialog
                swipeInfoDialog.dismiss();
            }
        });

        swipeInfoDialog.show();
    }
}
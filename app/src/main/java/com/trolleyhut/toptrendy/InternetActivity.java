package com.trolleyhut.toptrendy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InternetActivity extends AppCompatActivity {
    Dialog warning_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        Button buttonRetry = findViewById(R.id.buttonRetry);

        //show user warning about loosing money
        showWarningDialog();
        //deduct a point for not having internet
        Constants.deductAPoint(InternetActivity.this);

        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InternetActivity.this, SplashScreenActivity.class));
                InternetActivity.this.finish();
            }
        });
    }

    private void showWarningDialog() {
        //show withdraw dialog
        warning_dialog = new Dialog(InternetActivity.this);
        warning_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        warning_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        warning_dialog.setCancelable(false);
        warning_dialog.setContentView(R.layout.dialog_warning);

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
}

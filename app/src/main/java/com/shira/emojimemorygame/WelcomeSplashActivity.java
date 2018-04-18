package com.shira.emojimemorygame;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeSplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcomeIntent = new Intent(WelcomeSplashActivity.this, MainActivity.class);
                startActivity(welcomeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

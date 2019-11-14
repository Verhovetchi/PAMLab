package com.example.dumitru.lab5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

/*        EasySplashScreen easySplashScreen = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(WelcomeActivity.class)
                .withSplashTimeOut(3000)
                .withLogo(R.drawable.telemedicine_logo_image)
                .withBackgroundResource(R.drawable.default_background);

        View view = easySplashScreen.create();
        setContentView(view);*/

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        finish();
    }

}

package com.app.whatsapptools.MainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.app.whatsapptools.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startMainActivity();
    }

    private void startMainActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadActivity(DrawerActivity.class);
                finish();
            }
        }, 1000);
    }

    private void loadActivity(Class startClass) {
        Intent intent = new Intent(SplashActivity.this, startClass);
        startActivity(intent);
        finish();
    }
}

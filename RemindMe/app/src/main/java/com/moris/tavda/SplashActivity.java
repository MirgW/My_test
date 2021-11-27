package com.moris.tavda;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //        try {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
//            Thread.sleep(500);
            finishAffinity();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
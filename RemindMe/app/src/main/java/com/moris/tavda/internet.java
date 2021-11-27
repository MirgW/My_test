package com.moris.tavda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import static com.moris.tavda.MainActivity.isOnline;

public class internet extends AppCompatActivity {
    Intent intent;

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
    }

    public void povtor(View view) {
        if (isOnline(this)) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }
}
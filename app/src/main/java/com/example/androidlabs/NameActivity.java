package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        TextView welcomeText = findViewById(R.id.textView2);

        // Get user's name passed from the previous activity
        String userName = getIntent().getStringExtra("userName");
        if (userName != null && !userName.isEmpty()) {
            welcomeText.setText(getString(R.string.welcome_message, userName));
        }

        Button dontCallMeButton = findViewById(R.id.button1);
        dontCallMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });

        Button thankYouButton = findViewById(R.id.button2);
        thankYouButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
    }
}
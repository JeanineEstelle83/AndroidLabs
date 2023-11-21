package com.example.androidlabs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         editText = findViewById(R.id.editText);

        Button nextButton = findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = editText.getText().toString();

                Intent intent = new Intent(MainActivity.this, NameActivity.class);
                intent.putExtra("userName", userInput);
                startActivityForResult(intent, 1);
            }
        });

        // Load user's name from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedName = sharedPreferences.getString("userName", "");
        if (!savedName.isEmpty()) {
            editText.setText(savedName);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save current value in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", editText.getText().toString());
        editor.apply();
    }

    // Handle the result from NameActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 0) {
                // User wants to change their name
                // Handle this scenario
            } else if (resultCode == 1) {
                // User is happy, close the app
                finish();
            }
        }
    }
}
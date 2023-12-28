package com.example.androidlabs;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DadJoke extends AppCompatActivity {

    private TextView textViewDadJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dad_joke);

        // Find the TextView for displaying the dad joke
        textViewDadJoke = findViewById(R.id.textViewDadJoke);

        // Display your best dad joke in the TextView
        displayBestDadJoke();

        // Setup the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Set the toolbar title to "Lab 8"
        setTitle(" cst 2335 Lab 8");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon0);
        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon2 = findViewById(R.id.icon2);

        // Set click listeners for icon1 and icon2
        icon1.setOnClickListener(v -> {
            Toast.makeText(this, "Icon 1 clicked", Toast.LENGTH_SHORT).show();
        });

        icon2.setOnClickListener(v -> {
            Toast.makeText(this, "Icon 2 clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void displayBestDadJoke() {
        String bestDadJoke = "Why don't skeletons fight each other? They don't have the guts!";
        textViewDadJoke.setText(bestDadJoke);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back when the up button is clicked
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Other necessary methods like onOptionsItemSelected, etc.
}
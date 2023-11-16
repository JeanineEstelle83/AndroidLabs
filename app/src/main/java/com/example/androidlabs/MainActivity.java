package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_main_grid);


        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Show a Snackbar with the checkbox state and an "Undo" action
                String checkboxState = isChecked ? "on" : "off";
                String snackbarMessage = "The checkbox is now " + checkboxState;

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), snackbarMessage, Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Perform "Undo" action: set the checkbox back to its original state
                                checkBox.setChecked(!isChecked);
                            }
                        });

                // Show the Snackbar
                snackbar.show();
            }
        });
    }
    public void onClickBtn(View view) {
        EditText editText = findViewById(R.id.etLove);
        TextView textView = findViewById(R.id.tvLove);


        textView.setText(editText.getText().toString());

        String toastMessage = getResources().getString(R.string.toast_message);
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

}
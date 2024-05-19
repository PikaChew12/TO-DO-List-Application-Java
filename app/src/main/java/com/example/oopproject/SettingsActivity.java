package com.example.oopproject;

import android.os.Bundle; // Import for Bundle
import android.view.View; // Import for View
import android.widget.AdapterView; // Import for AdapterView
import android.widget.ArrayAdapter; // Import for ArrayAdapter
import android.widget.Spinner; // Import for Spinner
import android.widget.Toast; // Import for Toast
import androidx.appcompat.app.AppCompatActivity; // Import for AppCompatActivity
import android.content.Context;

public class SettingsActivity extends AppCompatActivity {

    // Array of theme names
    private static final String[] themes = {
            "Midnight Dusk", "Tidal Wave", "Teal and Turquoise", "Tako", "Strawberry Daiquiri", "Lavender"
    };

    private Spinner themeSpinner; // Spinner for theme selection
    private int currentThemeIndex; // Index of the currently selected theme

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get the currently selected theme index
        currentThemeIndex = ThemeUtil.getSelectedTheme(this);
        // Apply the currently selected theme
        ThemeUtil.applyTheme(this, currentThemeIndex);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // Set the content view to activity_settings layout

        // Set up the Spinner for theme selection
        themeSpinner = findViewById(R.id.theme_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, themes);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);
        themeSpinner.setSelection(currentThemeIndex);

        // Set the listener for Spinner item selection
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Check if the selected theme is different from the current theme
                if (position != currentThemeIndex) {
                    // Save the new theme selection
                    ThemeUtil.setSelectedTheme(SettingsActivity.this, position);
                    currentThemeIndex = position;
                    recreate();
                    // Notify the user to restart the app for changes to take effect
                    notifyUserToRestart();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if no theme is selected
            }
        });
    }

    // Show a Toast message to notify the user to restart the app
    private void notifyUserToRestart() {
        Toast.makeText(this, "Restart the app for the changes to occur.", Toast.LENGTH_LONG).show();
    }
}

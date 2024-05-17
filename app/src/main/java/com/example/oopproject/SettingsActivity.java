package com.example.oopproject;

import android.content.SharedPreferences; // Import SharedPreferences for saving user preferences
import android.os.Bundle; // Import Bundle for activity state saving
import androidx.appcompat.app.AppCompatActivity; // Import AppCompatActivity for activity base class
import androidx.appcompat.app.AppCompatDelegate; // Import AppCompatDelegate for night mode support
import com.google.android.material.switchmaterial.SwitchMaterial; // Import SwitchMaterial for switch component

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate method called when activity is created
        super.onCreate(savedInstanceState); // Call superclass onCreate method
        setContentView(R.layout.activity_settings); // Set the content view to activity_settings layout

        final SwitchMaterial themeSwitch = findViewById(R.id.theme_switch); // Find SwitchMaterial by ID
        final SharedPreferences sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE); // Get shared preferences
        boolean isDarkMode = sharedPreferences.getBoolean("DarkTheme", false); // Get saved theme mode from preferences
        themeSwitch.setChecked(isDarkMode); // Set switch state based on saved preference

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> { // Set listener for switch state change
            SharedPreferences.Editor editor = sharedPreferences.edit(); // Get SharedPreferences editor
            editor.putBoolean("DarkTheme", isChecked); // Save the new theme mode preference
            editor.apply(); // Apply the changes
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO); // Set the night mode based on switch state
        });
    }
}

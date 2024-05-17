package com.example.oopproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String[] themes = {
            "Midnight Dusk", "Tidal Wave", "Teal and Turquoise", "Tako", "Strawberry Daiquiri", "Lavender"
    };

    private Spinner themeSpinner;
    private int currentThemeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentThemeIndex = ThemeUtil.getSelectedTheme(this);
        ThemeUtil.applyTheme(this, currentThemeIndex);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        themeSpinner = findViewById(R.id.theme_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, themes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);
        themeSpinner.setSelection(currentThemeIndex);

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != currentThemeIndex) {
                    ThemeUtil.setSelectedTheme(SettingsActivity.this, position);
                    currentThemeIndex = position;
                    notifyUserToRestart();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void notifyUserToRestart() {
        Toast.makeText(this, "Restart the app for the changes to occur.", Toast.LENGTH_LONG).show();
    }
}

package com.example.oopproject;

import android.app.Activity; // Import for Activity
import android.content.Context; // Import for Context
import android.content.SharedPreferences; // Import for SharedPreferences
import androidx.appcompat.app.AppCompatDelegate; // Import for AppCompatDelegate

public class ThemeUtil {
    private static final String PREFS_NAME = "AppSettingsPrefs"; // SharedPreferences file name
    private static final String KEY_SELECTED_THEME = "SelectedTheme"; // Key for storing selected theme

    // Method to apply the currently selected theme
    public static void applyTheme(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int themeIndex = sharedPreferences.getInt(KEY_SELECTED_THEME, 0); // Get the selected theme index
        applyTheme(activity, themeIndex); // Apply the theme based on the index
    }

    // Method to apply the theme based on the provided index
    public static void applyTheme(Activity activity, int themeIndex) {
        switch (themeIndex) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable night mode
                activity.setTheme(R.style.midnight_dusk); // Apply midnight_dusk theme
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable night mode
                activity.setTheme(R.style.tidal_wave); // Apply tidal_wave theme
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable night mode
                activity.setTheme(R.style.teal_and_turquoise); // Apply teal_and_turquoise theme
                break;
            case 3:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable night mode
                activity.setTheme(R.style.tako); // Apply tako theme
                break;
            case 4:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable night mode
                activity.setTheme(R.style.strawberry_daiquiri); // Apply strawberry_daiquiri theme
                break;
            case 5:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable night mode
                activity.setTheme(R.style.lavender); // Apply lavender theme
                break;
            case 6:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable night mode
                activity.setTheme(R.style.green_apple); // Apply green apple theme
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable night mode
                activity.setTheme(R.style.AppTheme); // Apply default AppTheme
                break;
        }
    }

    // Method to save the selected theme index
    public static void setSelectedTheme(Context context, int themeIndex) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SELECTED_THEME, themeIndex); // Save the theme index
        editor.apply(); // Apply the changes
    }

    // Method to get the currently selected theme index
    public static int getSelectedTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_SELECTED_THEME, 0); // Return the saved theme index
    }
}

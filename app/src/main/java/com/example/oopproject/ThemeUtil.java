package com.example.oopproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtil {
    private static final String PREFS_NAME = "AppSettingsPrefs";
    private static final String KEY_SELECTED_THEME = "SelectedTheme";

    public static void applyTheme(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int themeIndex = sharedPreferences.getInt(KEY_SELECTED_THEME, 0);
        applyTheme(activity, themeIndex);
    }

    public static void applyTheme(Activity activity, int themeIndex) {
        switch (themeIndex) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                activity.setTheme(R.style.midnight_dusk);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                activity.setTheme(R.style.tidal_wave);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                activity.setTheme(R.style.teal_and_turquoise);
                break;
            case 3:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                activity.setTheme(R.style.tako);
                break;
            case 4:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                activity.setTheme(R.style.strawberry_daiquiri);
                break;
            case 5:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                activity.setTheme(R.style.lavender);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                activity.setTheme(R.style.AppTheme);
                break;
        }
    }

    public static void setSelectedTheme(Context context, int themeIndex) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SELECTED_THEME, themeIndex);
        editor.apply();
    }

    public static int getSelectedTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_SELECTED_THEME, 0);
    }
}

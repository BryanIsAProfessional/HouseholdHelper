package com.example.householdhelper.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.householdhelper.R;

/**
 * The activity for setting preferences.
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            loadSettings();
        }

        /**
         * load settings from sharedpreferences
         */
        private void loadSettings(){
            ListPreference listPreference = findPreference("Themes");
            SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
            String themeName = preferences.getString("theme", "Default");
            listPreference.setSummary(listPreference.getEntries()[listPreference.findIndexOfValue(themeName)]);

            listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String item = (String)newValue;
                if(preference.getKey().equals("Themes")){
                    SharedPreferences preferences1 = android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences1.edit();

                    editor.putString("theme", item);
                    editor.apply();

                    ListPreference temp = (ListPreference) preference;
                    temp.setSummary(temp.getEntries()[temp.findIndexOfValue(item)]);

                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }
                return true;
            });


        }

        /**
         * reloads settings on resume
         */
        @Override
        public void onResume() {
            super.onResume();
            loadSettings();
        }
    }

    /**
     * Chooses the theme based on the one selected in sharedpreferences, or default if none is selected
     * @return the selected theme
     */
    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();

        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String themeName = preferences.getString("theme", "Default");

        switch(themeName){
            case "Default":
                //editor.putString();
                theme.applyStyle(R.style.Theme_HouseholdHelper, true);
                break;
            case "Beach":
                theme.applyStyle(R.style.Theme_Beach, true);
                break;
            case "Dark":
                theme.applyStyle(R.style.Theme_Dark, true);
                break;
        }
        return theme;
    }
}
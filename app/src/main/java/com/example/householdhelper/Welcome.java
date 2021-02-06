package com.example.householdhelper;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.householdhelper.helpers.WelcomeGenerator;

/**
 * Greeting widget for homepage
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class Welcome extends Fragment {
    private TextView welcomeTextView;

    /**
     * empty constructor
     */
    public Welcome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * instantiates TextView from layout
     * @param view layout
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String name = preferences.getString("name", "");
        WelcomeGenerator welcomer = new WelcomeGenerator();

        welcomeTextView = view.findViewById(R.id.textViewWelcome);
        welcomeTextView.setText(welcomer.generateWelcome(name));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }
}
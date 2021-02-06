package com.example.householdhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Splash screen activity. Goes to main menu after a delay, and can also be clicked through.
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int DELAY = 4000;

    private LottieAnimationView anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        anim = findViewById(R.id.splashAnimation);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }, DELAY);

        anim.setOnClickListener(v->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            timer.cancel();
        });
    }

}
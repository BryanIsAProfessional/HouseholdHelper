package com.example.householdhelper;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Homepage widget for tracking and rewarding list completion
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class PointsCollector extends Fragment {

    private static final int DELAY = 6000;
    private static final int START_TIME = 500;

    private Button testButton;
    private LottieAnimationView anim;
    private MediaPlayer audio;

    /**
     * empty constructor
     */
    public PointsCollector() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * instantiate views in layout
     * @param view layout
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        anim = view.findViewById(R.id.collectAnimation);
        audio = MediaPlayer.create(getContext(), R.raw.redeem);
        testButton = view.findViewById(R.id.testButton);
        testButton.setOnClickListener(v->{
            if(!anim.isAnimating() && !audio.isPlaying()){
                anim.playAnimation();
                audio.seekTo(START_TIME);
                audio.start();
            }
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    audio.pause();
                }
            }, DELAY);

            //TODO: reactivate if points are ready to be collected
            //testButton.setOnClickListener(null);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_points_collector, container, false);
    }
}
package com.patrykkrawczyk.liveo.managers;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.patrykkrawczyk.liveo.R;
import com.skyfishjy.library.RippleBackground;

import java.util.Random;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class HeartRateManager {


    private TextView heartRateText;
    private RippleBackground heartRateRipple;
    private int colorPoint;
    private int colorGrid;

    public HeartRateManager(Activity activity, RippleBackground ripple, TextView text) {
        colorPoint = activity.getResources().getColor(R.color.colorAccent);
        colorGrid = activity.getResources().getColor(R.color.colorFont);

        this.heartRateRipple = ripple;
        this.heartRateText = text;

    }

    public void add(int value) {
        heartRateText.setText(String.valueOf(value));
        heartRateRipple.startRippleAnimation();
    }

}

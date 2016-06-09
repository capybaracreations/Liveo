package com.patrykkrawczyk.liveo.managers.heartrate;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import com.patrykkrawczyk.liveo.R;
import com.skyfishjy.library.RippleBackground;

import java.util.Random;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class HeartRateViewManager {

    private TextView heartRateText;
    private RippleBackground heartRateRipple;
    private boolean enabled = false;
    public static int lastValue = 0;

    public HeartRateViewManager(RippleBackground ripple, TextView text) {
        this.heartRateRipple = ripple;
        this.heartRateText = text;
    }

    public void setEnabled(boolean state) {
        enabled = state;

        if (!enabled) {
            heartRateText.setText("OFF");
            heartRateRipple.stopRippleAnimation();
        } else {
            heartRateRipple.startRippleAnimation();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void set(int value) {
        if (enabled) {
            if (value == 0) heartRateText.setText("Calibrating");
            else if (value == -3) heartRateText.setText("NO HR");
            else if (value == -10) heartRateText.setText("...");
            else {
                heartRateText.setText(String.valueOf(value));
                lastValue = value;
            }
        }
    }

}

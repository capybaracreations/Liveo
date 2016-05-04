package com.patrykkrawczyk.liveo.managers;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.patrykkrawczyk.liveo.R;
import com.skyfishjy.library.RippleBackground;
import com.txusballesteros.SnakeView;

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

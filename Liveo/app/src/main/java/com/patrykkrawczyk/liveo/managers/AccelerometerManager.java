package com.patrykkrawczyk.liveo.managers;

import android.app.Activity;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.patrykkrawczyk.liveo.R;

import java.util.ArrayList;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class AccelerometerManager {


    private static AccelerometerManager instance;
    private RadarChart radarChart;

    public static AccelerometerManager getInstance(Activity activity, RadarChart radarChart) {
        if (instance == null) instance = new AccelerometerManager(activity, radarChart);
        return instance;
    }

    public AccelerometerManager(Activity activity, RadarChart chart) {
        this.radarChart = chart;

        radarChart.setWebLineWidth(0f);
        radarChart.setWebLineWidthInner(0.75f);
        radarChart.setWebAlpha(100);
        radarChart.getLegend().setEnabled(false);
        radarChart.setRotationEnabled(false);
        radarChart.setWebColor(activity.getResources().getColor(R.color.colorFont));
        radarChart.setDescription("");
        radarChart.getYAxis().setEnabled(false);
        radarChart.setDrawMarkerViews(false);

        setData(activity);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setTextSize(9f);
        xAxis.setTextColor(activity.getResources().getColor(R.color.colorFont));

    }

    private final String[] xLabels = new String[]{
            "BRAKE", "", "RIGHT", "", "ACCELERATE", "", "LEFT", ""
    };

    public void setData(Activity activity) {
        ArrayList<String> xVals = new ArrayList<>();
        for (String xVal: xLabels) xVals.add(xVal);
        for (int i = 0; i < 20; i++) {
            xVals.add("");
        }

        ArrayList<Entry> yVals = new ArrayList<>();

        yVals.add(new Entry(1, 1));
        yVals.add(new Entry(2, 3));
        yVals.add(new Entry(3, 20));

        RadarDataSet set = new RadarDataSet(yVals, "");
        set.setColor(activity.getResources().getColor(R.color.colorAccent));
        set.setFillColor(activity.getResources().getColor(R.color.colorAccent));
        set.setLineWidth(1f);


        RadarData data = new RadarData(xVals, set);
        data.setValueTextSize(8f);
        data.setDrawValues(true);


        radarChart.setData(data);
        radarChart.invalidate();
    }
}

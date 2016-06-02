package com.patrykkrawczyk.liveo.managers.accelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.activities.CalibrateActivity;
import com.patrykkrawczyk.liveo.managers.StateManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class AccelerometerViewManager {

    private ScatterChart accelerometerChart;
    private int colorPoint;
    private int colorGrid;
    private int maxRange;
    private static float[] calibration;
    private static boolean isCalibrated = false;
    private boolean enabled = false;

    public AccelerometerViewManager(Activity activity, ScatterChart chart) {
        this.accelerometerChart = chart;
        accelerometerChart.setNoDataText(activity.getString(R.string.LIVEO_ACCELEROMETER_UNAVAILABLE));

        colorPoint = activity.getResources().getColor(R.color.newAccent);
        colorGrid = activity.getResources().getColor(R.color.newFont);

        SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensor == null) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }

        if (isEnabled()) {
            maxRange = (int) sensor.getMaximumRange();
            initializeChart();
        }
    }

    private void setEnabled(boolean state) {
        enabled = state;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public void setChartValue(float x, float y) {
        if (isEnabled()) {
            ArrayList<String> xVals = new ArrayList<String>();
            for (int i = 0; i <= maxRange * 2; i++) {
                xVals.add((i) + "");
            }

            ArrayList<Entry> yVals = new ArrayList<>();

            yVals.add(new Entry(maxRange + (int)(y - calibration[2]), maxRange + (int)(x - calibration[0])));
            ScatterDataSet set = new ScatterDataSet(yVals, "ACCELEROMETER");
            set.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            set.setScatterShapeSize(20f);
            set.setColor(colorPoint);

            ScatterData data = new ScatterData(xVals, set);
            data.setHighlightEnabled(false);
            data.setDrawValues(false);
            accelerometerChart.setData(data);
            accelerometerChart.invalidate();
        }
    }

    public static void setCalibration(SensorEvent sensorEvent) {
        if (sensorEvent != null)
            calibration = sensorEvent.values;
        isCalibrated = true;
    }

    public static boolean isCalibrated() {
        return isCalibrated;
    }

    private void initializeChart() {
        accelerometerChart.getLegend().setEnabled(false);
        accelerometerChart.setTouchEnabled(false);
        accelerometerChart.setDescription("");
        accelerometerChart.setDrawGridBackground(false);
        accelerometerChart.setDragEnabled(false);
        accelerometerChart.setScaleEnabled(false);
        accelerometerChart.setPinchZoom(false);
        accelerometerChart.setAutoScaleMinMaxEnabled(false);

        YAxis yAxisR = accelerometerChart.getAxisRight();
        yAxisR.setDrawAxisLine(true);
        yAxisR.setDrawGridLines(false);
        yAxisR.setDrawLabels(false);
        yAxisR.setGridColor(colorGrid);
        yAxisR.setAxisMinValue(0);
        yAxisR.setAxisMaxValue(maxRange*2);

        YAxis yAxis = accelerometerChart.getAxisLeft();
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setGridColor(colorGrid);
        yAxis.setAxisMinValue(0);
        yAxis.setAxisMaxValue(maxRange*2);

        XAxis xAxis = accelerometerChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setGridColor(colorGrid);
        xAxis.setAvoidFirstLastClipping(true);

        // limit lines
        LimitLine limitX = new LimitLine(maxRange);
        limitX.enableDashedLine(10, 10, 0);
        limitX.setLineColor(colorGrid);
        limitX.setLineWidth(0);

        LimitLine limitY = new LimitLine(maxRange);
        limitY.enableDashedLine(10, 10, 0);
        limitY.setLineColor(colorGrid);
        limitY.setLineWidth(0);

        xAxis.addLimitLine(limitX);
        xAxis.setDrawLimitLinesBehindData(true);

        yAxis.addLimitLine(limitY);
        yAxis.setDrawLimitLinesBehindData(true);
    }
}

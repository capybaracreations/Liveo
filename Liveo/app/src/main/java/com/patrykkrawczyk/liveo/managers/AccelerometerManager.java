package com.patrykkrawczyk.liveo.managers;

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

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class AccelerometerManager implements SensorEventListener{

    private final int UPDATE_THRESHOLD = 50;
    private long lastUpdateTime;
    private StateManager stateManager;
    private SensorManager sensorManager;
    private Sensor senAccelerometer;
    private ScatterChart accelerometerChart;
    private int colorPoint;
    private int colorGrid;
    private int maxRange;

    public AccelerometerManager(Activity activity, ScatterChart chart) {
        stateManager = StateManager.getInstance();

        colorPoint = activity.getResources().getColor(R.color.colorAccent);
        colorGrid = activity.getResources().getColor(R.color.colorFont);

        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        maxRange = (int) senAccelerometer.getMaximumRange();

        this.accelerometerChart = chart;
        initializeChart();
        setChartValue(maxRange, maxRange);
    }

    public void enable(boolean state) {
        if (state) {
            sensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            sensorManager.unregisterListener(this);
        }

        stateManager.setAccelerometerState(state);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();

            long diffTime = (currentTime - lastUpdateTime);
            if (diffTime > UPDATE_THRESHOLD) {
                lastUpdateTime = currentTime;

                float z = event.values[0];
                float x = event.values[2];
                setChartValue(maxRange + (int)x, maxRange + (int)z);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
        yAxisR.setDrawLabels(true);
        yAxisR.setGridColor(colorGrid);
        yAxisR.setAxisMinValue(0);
        yAxisR.setAxisMaxValue(maxRange*2);

        YAxis yAxis = accelerometerChart.getAxisLeft();
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(true);
        yAxis.setGridColor(colorGrid);
        yAxis.setAxisMinValue(0);
        yAxis.setAxisMaxValue(maxRange*2);

        XAxis xAxis = accelerometerChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
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

    public void setChartValue(int x, int y) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i <= maxRange*2; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<>();
        //for (int i = 0; i <= maxRange; i++) {
            //yVals.add(new Entry(i, i));
       // }
        yVals.add(new Entry(x, y));
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

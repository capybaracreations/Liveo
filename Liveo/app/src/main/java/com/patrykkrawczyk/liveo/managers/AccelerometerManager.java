package com.patrykkrawczyk.liveo.managers;

import android.app.Activity;

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
public class AccelerometerManager {


    private ScatterChart accelerometerChart;
    private int colorPoint;
    private int colorGrid;

    public AccelerometerManager(Activity activity, ScatterChart chart) {
        colorPoint = activity.getResources().getColor(R.color.colorAccent);
        colorGrid = activity.getResources().getColor(R.color.colorFont);

        this.accelerometerChart = chart;

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
        yAxisR.setAxisMaxValue(100);

        YAxis yAxis = accelerometerChart.getAxisLeft();
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setGridColor(colorGrid);
        yAxis.setAxisMinValue(0);
        yAxis.setAxisMaxValue(100);

        XAxis xAxis = accelerometerChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setGridColor(colorGrid);
        xAxis.setAvoidFirstLastClipping(true);

        // limit lines
        LimitLine limitX = new LimitLine(50);
        limitX.enableDashedLine(10, 10, 0);
        limitX.setLineColor(colorGrid);
        limitX.setLineWidth(0);

        LimitLine limitY = new LimitLine(50);
        limitY.enableDashedLine(10, 10, 0);
        limitY.setLineColor(colorGrid);
        limitY.setLineWidth(0);

        xAxis.addLimitLine(limitX);
        xAxis.setDrawLimitLinesBehindData(true);

        yAxis.addLimitLine(limitY);
        yAxis.setDrawLimitLinesBehindData(true);



        set(50, 50);
    }


    public void set(int x, int y) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i <= 100; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            //yVals.add(new Entry(i, i));
        }
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


    public void feedMultiple(final Activity activity) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i = 0; i < 500; i++) {

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            set(new Random().nextInt() % 100, new Random().nextInt() % 100);
                        }
                    });

                    try {
                        Thread.sleep(35);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

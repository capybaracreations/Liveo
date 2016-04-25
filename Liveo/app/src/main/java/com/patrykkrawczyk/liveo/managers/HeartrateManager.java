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
import com.txusballesteros.SnakeView;

import java.util.Random;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class HeartRateManager {


    private TextView heartRateText;
    private LineChart heartRateChart;
    private int colorPoint;
    private int colorGrid;

    public HeartRateManager(Activity activity, LineChart chart, TextView text) {
        colorPoint = activity.getResources().getColor(R.color.colorAccent);
        colorGrid = activity.getResources().getColor(R.color.colorFont);

        this.heartRateChart = chart;
        this.heartRateText = text;

        heartRateChart.getLegend().setEnabled(false);
        heartRateChart.setTouchEnabled(false);
        heartRateChart.setDescription("");
        heartRateChart.setDrawGridBackground(false);
        heartRateChart.setDragEnabled(false);
        heartRateChart.setScaleEnabled(false);
        heartRateChart.setPinchZoom(false);
        heartRateChart.setAutoScaleMinMaxEnabled(true);
        heartRateChart.getAxisRight().setEnabled(false);

        YAxis yAxis = heartRateChart.getAxisLeft();
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setAxisLineColor(colorGrid);
        yAxis.setGridColor(colorGrid);

        XAxis xAxis = heartRateChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setGridColor(colorGrid);
        xAxis.setAxisLineColor(colorGrid);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineData data = new LineData();
        data.setValueTextColor(colorPoint);
        heartRateChart.setData(data);
    }

    public void add(int value) {
        LineData data = heartRateChart.getData();
        ILineDataSet set = data.getDataSetByIndex(0);

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        data.addXValue("" + data.getXValCount());
        data.addEntry(new Entry(value, set.getEntryCount()), 0);

        if (set.getEntryCount() > 10) {
            float max = 0; float subValue = 0;
            for (int k = 0; k < 10; k++) {
                subValue = set.getEntryForXIndex(set.getEntryCount() - 1 - k).getVal();
                if (subValue > max) max = subValue;
            }
            heartRateChart.moveViewToY(max, YAxis.AxisDependency.LEFT);
        }

        heartRateChart.notifyDataSetChanged();
        heartRateChart.setVisibleXRangeMaximum(10);
        heartRateChart.moveViewToX(data.getXValCount() - 11);

        heartRateText.setText(String.valueOf(value));
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "HeartRate");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(colorPoint);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setDrawCubic(true);
        return set;
    }

    public void feedMultiple(final Activity activity) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i = 0; i < 500; i++) {

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            add(new Random().nextInt() % 100);
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

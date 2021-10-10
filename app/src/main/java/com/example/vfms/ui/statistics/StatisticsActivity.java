package com.example.vfms.ui.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vfms.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        TextView dateText = findViewById(R.id.textDate);
        Intent intent = getIntent();
        String date = intent.getStringExtra(String.valueOf(R.string.date_string));
        dateText.setText(date);

        lineChart = (LineChart) findViewById(R.id.line);

        ArrayList<Entry> peak = new ArrayList<>();
        ArrayList<Entry> constant = new ArrayList<>();

        constant.add(new Entry(0, 0));
        constant.add(new Entry(23, 0));
        peak.add(new Entry(10, 10));
        peak.add(new Entry(12, 15));
        peak.add(new Entry(13, 9));

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(peak, getString(R.string.peak_label));
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.BLUE);

        LineDataSet lineDataSet2 = new LineDataSet(constant, getString(R.string.constant_label));
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(Color.RED);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);

        lineChart.setData(new LineData(lineDataSets));

        lineChart.setVisibleXRangeMaximum(65f);

        Description des = new Description();
        des.setText(getString(R.string.chart_label));
        lineChart.setDescription(des);
    }
}

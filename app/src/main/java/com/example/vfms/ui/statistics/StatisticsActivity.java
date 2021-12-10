package com.example.vfms.ui.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vfms.BackgroundWorker;
import com.example.vfms.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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

        lineChart = findViewById(R.id.line);

        ArrayList<Entry> peak = new ArrayList<>();
        ArrayList<Entry> constant = new ArrayList<>();

        constant.add(new Entry(0, 0));
        constant.add(new Entry(23, 0));
        try {
            setPeak(peak, "1", date);
        } catch (ExecutionException | InterruptedException | ParseException e) {
            e.printStackTrace();
        }

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

    private void setPeak(ArrayList<Entry> peak, @SuppressWarnings("SameParameterValue") String id, String date) throws ExecutionException, InterruptedException, ParseException {
        String type = "chart";
        BackgroundWorker backgroundWorker = new BackgroundWorker();
        String output = backgroundWorker.execute(type, id, date).get();
        if (output == null) return;
        JSONParser jsonParser = new JSONParser();
        JSONObject outputJSON = (JSONObject) jsonParser.parse(output);
        @SuppressWarnings("ConstantConditions") long count = (long) outputJSON.get("count");
        JSONObject list = (JSONObject) outputJSON.get("list");
        JSONObject value = (JSONObject) outputJSON.get("value");
        for (int i = 0; i < count; i++) {
            assert list != null;
            assert value != null;
            //noinspection SingleStatementInBlock
            peak.add(new Entry(Float.parseFloat((String) Objects.requireNonNull(list.get(Integer.toString(i)))), Float.parseFloat((String) Objects.requireNonNull(value.get(list.get(Integer.toString(i)))))));
        }
    }
}

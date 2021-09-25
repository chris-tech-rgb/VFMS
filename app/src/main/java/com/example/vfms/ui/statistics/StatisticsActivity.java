package com.example.vfms.ui.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vfms.R;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        TextView dateText = findViewById(R.id.textDate);
        Intent intent = getIntent();
        dateText.setText(intent.getStringExtra(String.valueOf(R.string.date_string)));
    }
}

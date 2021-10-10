package com.example.vfms.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.vfms.R;
import com.example.vfms.ui.statistics.StatisticsActivity;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        CalendarView calendarView = (CalendarView) root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((calendarView1, i, i1, i2) -> {
            String date = i+ "/" + format(i1 + 1) + "/" + format(i2);
            Intent intent = new Intent(getActivity(), StatisticsActivity.class);
            intent.putExtra(String.valueOf(R.string.date_string), date);
            startActivity(intent);
        });
        return root;
    }

    String format(int i) {
        return String.valueOf(i).length() == 2 ? String.valueOf(i) : '0' + String.valueOf(i);
    }
}
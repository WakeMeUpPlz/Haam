package com.haam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haam.adapter.AlarmAdapter;
import com.haam.models.Alarm;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    private List<Alarm> alarmList;

    private TextView tvAlarmNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAlarmNo=findViewById(R.id.tvAlarmNo);
        recyclerView = findViewById(R.id.rvAlarmList);
        alarmList = new ArrayList<>();
        alarmList.add(new Alarm(1, "08:00", "Wake up", "default", "Helper 1", "Monday", true, false, "AM"));
        alarmList.add(new Alarm(2, "12:30", "Lunch time", "default", "Helper 2", "Weekdays", false, false, "PM"));
        alarmList.add(new Alarm(3, "18:00", "Exercise", "default", "Helper 3", "Everyday", true, false, "PM"));
        if(alarmList.size()!=0){
            recyclerView.setVisibility(View.VISIBLE);
            tvAlarmNo.setVisibility(View.GONE);
            alarmAdapter = new AlarmAdapter(alarmList, this);
            recyclerView.setAdapter(alarmAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else{
            recyclerView.setVisibility(View.GONE);
            tvAlarmNo.setVisibility(View.VISIBLE);
        }


    }
}
package com.haam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haam.adapter.AlarmAdapter;
import com.haam.models.Alarm;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    public static List<Alarm> alarmList;
    private TextView tvAlarmNo;
    private Button addAlarmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAlarmNo=findViewById(R.id.tvAlarmNo);
        addAlarmBtn=findViewById(R.id.addAlarmBtn);


        recyclerView = findViewById(R.id.rvAlarmList);
        alarmList = new ArrayList<>();
        alarmList.add(new Alarm(1, "08:00", "Wake up", "default", "Helper 1", "1000100", true, false, "AM"));
        alarmList.add(new Alarm(2, "12:30", "Lunch time", "default", "Helper 2", "1111111", false, false, "PM"));
        alarmList.add(new Alarm(3, "18:00", "Exercise", "default", "Helper 3", "1110000", true, false, "PM"));

        if(alarmList.size()!=0){
            recyclerView.setVisibility(View.VISIBLE);
            tvAlarmNo.setVisibility(View.GONE); //알람없음 표시 지우기
            //알람어댑터 설정
            alarmAdapter = new AlarmAdapter(alarmList, this);
            recyclerView.setAdapter(alarmAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else{
            recyclerView.setVisibility(View.GONE);
            tvAlarmNo.setVisibility(View.VISIBLE);
        }

        //알람추가 버튼
        addAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmAdapter.initPopup();
            }
        });



    }

}
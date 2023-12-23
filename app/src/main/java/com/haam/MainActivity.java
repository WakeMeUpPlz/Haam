package com.haam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.haam.adapter.AlarmAdapter;
import com.haam.alarm.PopRing;
import com.haam.models.Alarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        alarmList.add(new Alarm(1, "19:07", "Wake up", "default", "Helper 1", "1000110", true, false, "PM"));
        alarmList.add(new Alarm(2, "22:00", "Lunch time", "default", "Helper 2", "1111111", false, false, "PM"));
        alarmList.add(new Alarm(3, "23:00", "Exercise", "default", "Helper 3", "1110000", true, false, "PM"));

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
        // 알람 설정 및 울리도록 하는 코드
        setupAlarms();
    }// 알람 설정 메서드


    private void setupAlarms() {
        Toast.makeText(this, "setupAlarm", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        for (Alarm alarm : alarmList) {

            Intent intent = new Intent("com.haam.alarm.ACTION_ALARM");
            // 다른 구성 요소에서도 브로드캐스트를 수신할 수 있도록 패키지 이름을 추가
            intent.setPackage(getPackageName());
            intent.putExtra("ALARM_ID", alarm.getAlarmId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarm.getAlarmId(), intent, PendingIntent.FLAG_ONE_SHOT);
            Calendar calendar = Calendar.getInstance();
            String[] time= alarm.getTime().split(":");
            calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time[0]));
            calendar.set(Calendar.MINUTE,Integer.parseInt(time[1]));
            calendar.set(Calendar.SECOND,0);
            Log.d("Test", time[0]+time[1]);

            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            Log.d("Test", String.valueOf(alarm.getAlarmId()));
            Log.d("Test", String.valueOf(calendar.getTimeInMillis()));
            Log.d("Test", String.valueOf(calendar.getTime()));

        }
    }

}
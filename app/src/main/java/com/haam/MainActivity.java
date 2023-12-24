package com.haam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAlarmNo=findViewById(R.id.tvAlarmNo);
        addAlarmBtn=findViewById(R.id.addAlarmBtn);

        // SMS 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(this, "android.permission.SEND_SMS") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS"}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }

        recyclerView = findViewById(R.id.rvAlarmList);


        alarmList = SQLiteHelper.getAllAlarms(getApplicationContext());



        if (alarmList.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvAlarmNo.setVisibility(View.GONE); // 알람 없음 표시 지우기

            // 알람 어댑터 설정
            if (alarmAdapter == null) {
                // 어댑터가 null인 경우에만 새로운 어댑터 생성
                alarmAdapter = new AlarmAdapter(alarmList, this);
                recyclerView.setAdapter(alarmAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                // 어댑터가 이미 존재하는 경우에는 데이터를 업데이트
                alarmAdapter.setAlarmList(alarmList);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            tvAlarmNo.setVisibility(View.VISIBLE);
            // 알람이 0개인 경우 어댑터를 null로 설정
            alarmAdapter = new AlarmAdapter(new ArrayList<>(), this);
            recyclerView.setAdapter(alarmAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));;
        }

        //알람추가 버튼
        addAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmAdapter.initPopup();
                // 어댑터가 이미 존재하는 경우에는 데이터를 업데이트
                alarmAdapter.setAlarmList(alarmList);
            }
        });

        // 알람 설정 및 울리도록 하는 코드
        setupAlarms();


    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 사용자가 권한을 수락한 경우
            } else {
                // 사용자가 권한을 거부한 경우 메시지 표시
                Toast.makeText(this, "SMS 전송 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 알람 설정 메서드
    public void setupAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        for (Alarm alarm : alarmList) {
            Intent intent = new Intent("com.haam.alarm.ACTION_ALARM");
            // 다른 구성 요소에서도 브로드캐스트를 수신할 수 있도록 패키지 이름을 추가
            intent.setPackage(getPackageName());
            intent.putExtra("HELPER_PHONE_NUMBER", alarm.getHelper()); // 여기에 실제 번호를 넣어야 함
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarm.getAlarmId(), intent, PendingIntent.FLAG_IMMUTABLE);
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getBroadcast(this,
                         alarm.getAlarmId(),intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }else {
                pendingIntent = PendingIntent.getBroadcast(this,
                        alarm.getAlarmId(),intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            Calendar calendar = Calendar.getInstance();
            String[] time= alarm.getTime().split(":");
            calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time[0]));
            calendar.set(Calendar.MINUTE,Integer.parseInt(time[1]));
            calendar.set(Calendar.SECOND,0);
            Log.d("onReceive전송,캘린더시간", String.valueOf(calendar.getTimeInMillis()));
            Log.d("onReceive전송,현재시간", String.valueOf(System.currentTimeMillis()));
            // 현재 시간과 비교하여 알람이 현재 시간보다 이전이면 스킵
            if(calendar.getTimeInMillis()<=System.currentTimeMillis()){
                Log.d("onReceive전송,스킵", String.valueOf(calendar.getTimeInMillis()));
                Log.d("onReceive전송,스킵", String.valueOf(System.currentTimeMillis()));
                continue;

            }
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
    }

//    private void setRepeatingAlarm(AlarmManager alarmManager, Calendar calendar, boolean[] week, PendingIntent pendingIntent) {
//
//        for (int i = 0; i < 7; i++) {
//            if (week[i]) {
//                // 해당 요일에만 알람 설정
//                calendar.set(Calendar.DAY_OF_WEEK, i + 1); // Calendar.DAY_OF_WEEK는 1이 일요일, 2가 월요일, ..., 7이 토요일
//                Log.d("onReceive전송: ",calendar.getTime().toString());
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            }
//        }
//    }



}
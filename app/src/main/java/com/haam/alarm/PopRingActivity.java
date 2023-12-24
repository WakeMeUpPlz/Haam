package com.haam.alarm;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.haam.R;
import com.haam.game.MultiplyGameActivity;
import com.haam.game.PatternGameActivity;
import com.haam.game.TraceGameActivity;

import java.util.Random;

public class PopRingActivity extends AppCompatActivity {
    Button goPatternBtn;
    TextView viewTime;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_ring);
        goPatternBtn = findViewById(R.id.goPatternBtn);

        playAlarmSound();
        String alarmTime = getIntent().getStringExtra("ALARM_TIME");
        viewTime = findViewById(R.id.viewTime);
        viewTime.setText(alarmTime);


        //헬퍼에게 메세지 보내기
        // 메시지를 보낼 번호와 내용

        String helperPhoneNumber = getIntent().getStringExtra("HELPER_PHONE_NUMBER"); // 여기서 값을 받아옴
        String message = "깨워 주세요.";

        // SmsManager 인스턴스 가져오기
        SmsManager smsManager = SmsManager.getDefault();

        // PendingIntent를 사용하여 메시지 전송 결과를 처리
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);

        try {
            // 메시지 전송
            smsManager.sendTextMessage(helperPhoneNumber, null, message, sentIntent, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "메시지 전송 실패", Toast.LENGTH_SHORT).show();
        }

        goPatternBtn.setOnClickListener(view -> {
            Class<?>[] activities = {MultiplyGameActivity.class, PatternGameActivity.class, TraceGameActivity.class};
            Random random = new Random();
            int randomIndex = random.nextInt(activities.length);
            Class<?> selectedActivity = activities[randomIndex];

            Intent intent = new Intent(PopRingActivity.this, selectedActivity);
            startActivity(intent);
        });
    }
    private void playAlarmSound() {
        try {
            // 알람 기본음 울리기
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

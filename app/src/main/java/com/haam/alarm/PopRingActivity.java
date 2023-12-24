package com.haam.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.haam.MainActivity;
import com.haam.R;
import com.haam.game.MultiplyGameActivity;
import com.haam.game.PatternGameActivity;
import com.haam.game.TraceGameActivity;

import java.util.Random;

public class PopRingActivity extends AppCompatActivity {
    Button goPatternBtn;
    TextView viewTime;
    private Ringtone ringtone;
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

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        stopAlarm(); // 알람 종료
                        Intent intent1 = new Intent(PopRingActivity.this, MainActivity.class);
                        startActivity(intent1);
                    }
        });

        goPatternBtn.setOnClickListener(view -> {
            Class<?>[] activities = {MultiplyGameActivity.class, PatternGameActivity.class, TraceGameActivity.class};
            Random random = new Random();
            int randomIndex = random.nextInt(activities.length);
            Class<?> selectedActivity = activities[randomIndex];

            Intent intent = new Intent(PopRingActivity.this, selectedActivity);
            launcher.launch(intent);
        });

    }
    private void playAlarmSound() {
        try {
            // 알람 기본음 울리기
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stopAlarm() {
        if (ringtone != null && ringtone.isPlaying())
            ringtone.stop();
        Intent alarmIntent = new Intent(this, PopRing.class); // 알람을 등록한 BroadcastReceiver 클래스로 변경
        int alarmId = alarmIntent.getIntExtra("ALARM_ID", -1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            // 해당 PendingIntent가 존재하면 취소
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}

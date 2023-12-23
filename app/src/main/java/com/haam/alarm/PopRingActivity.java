package com.haam.alarm;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.haam.R;
import com.haam.game.MultiplyGameActivity;
import com.haam.game.PatternGameActivity;
import com.haam.game.TraceGameActivity;

import java.util.Random;

public class PopRingActivity extends AppCompatActivity {
    Button goPatternBtn;
    TextView viewTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_ring);
        goPatternBtn = findViewById(R.id.goPatternBtn);

        playAlarmSound();
        String alarmTime = getIntent().getStringExtra("ALARM_TIME");
        viewTime = findViewById(R.id.viewTime);
        viewTime.setText(alarmTime);


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

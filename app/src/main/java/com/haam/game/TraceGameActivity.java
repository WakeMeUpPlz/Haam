package com.haam.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haam.MainActivity;
import com.haam.R;
import com.haam.alarm.PopRing;

import java.util.Random;

public class TraceGameActivity extends AppCompatActivity {
    private Ringtone ringtone;
    private TextView randomTextView;
    private EditText userInput;
    private Button checkButton;
    private String randomString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_game);
        randomTextView = findViewById(R.id.randomTextView);
        userInput = findViewById(R.id.userInput);
        checkButton = findViewById(R.id.checkButton);

        generateRandomString();

        checkButton.setOnClickListener(view -> checkInput());
    }
    private void generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        int length = 6;
        for (int i=0; i<length; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            sb.append(randomChar);
        }
        randomString = sb.toString();
        randomTextView.setText(randomString);
    }
    private void checkInput() {
        String userIn = userInput.getText().toString();
        if (userIn.equals(randomString)) {
            Toast.makeText(getApplicationContext(), "정답입니다.", Toast.LENGTH_SHORT).show();
            ringtone.stop();
            int alarmId = getIntent().getIntExtra("ALARM_ID", -1);
            stopAlarm(alarmId); // 알람 종료
            Intent TraceIntent = new Intent(TraceGameActivity.this, MainActivity.class);
            startActivity(TraceIntent);
        } else {
            Toast.makeText(getApplicationContext(), "오답입니다.", Toast.LENGTH_SHORT).show();
            generateRandomString();
            userInput.setText("");
        }
    }
    private void stopAlarm(int alarmId) {
        Intent alarmIntent = new Intent(this, PopRing.class); // 알람을 등록한 BroadcastReceiver 클래스로 변경
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
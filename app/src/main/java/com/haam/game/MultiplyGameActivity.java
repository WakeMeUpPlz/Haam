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

public class MultiplyGameActivity extends AppCompatActivity {
    private Ringtone ringtone;
    private TextView question;
    private EditText answer;
    private Button btn_check;
    private int correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiply_game);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        btn_check = findViewById(R.id.btn_check);

        generateQuestion();

        btn_check.setOnClickListener(view -> checkAnswer());
    }
    private void generateQuestion() {
        Random random = new Random();
        int num1 = random.nextInt(9) + 1;
        int num2 = random.nextInt(9) + 1;
        correctAnswer = num1*num2;
        String q = num1 + " x " + num2 + " = ";
        question.setText(q);
    }
    private void checkAnswer() {
        String userAnswer = answer.getText().toString();
        if (!userAnswer.isEmpty()) {
            int a = Integer.parseInt(userAnswer);
            if (a == correctAnswer) {
                Toast.makeText(getApplicationContext(), "정답입니다.", Toast.LENGTH_SHORT).show();
                ringtone.stop();
                int alarmId = getIntent().getIntExtra("ALARM_ID", -1);
                stopAlarm(alarmId); // 알람 종료
                Intent TraceIntent = new Intent(MultiplyGameActivity.this, MainActivity.class);
                startActivity(TraceIntent);
            } else {
                Toast.makeText(getApplicationContext(), "오답입니다.", Toast.LENGTH_SHORT).show();
                generateQuestion();
                answer.setText("");
            }
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
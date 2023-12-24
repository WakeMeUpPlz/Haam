package com.haam.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haam.MainActivity;
import com.haam.R;
import com.haam.alarm.PopRing;
import com.haam.alarm.PopRingActivity;

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
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            PopRingActivity.INCORRECT_ANSWER_NUM= PopRingActivity.INCORRECT_ANSWER_NUM+1;
            Toast.makeText(getApplicationContext(), "오답입니다.", Toast.LENGTH_SHORT).show();
            generateRandomString();
            userInput.setText("");
        }
    }
}
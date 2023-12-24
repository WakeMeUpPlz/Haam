package com.haam.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.os.Bundle;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.haam.MainActivity;
import com.haam.R;
import com.haam.alarm.PopRing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatternGameActivity extends AppCompatActivity {
    //private static final int[] BLOCKED= {Integer.parseInt("02"),Integer.parseInt("06"),Integer.parseInt("08"),17,20,26,28,35,53,60,62,68,71,80,82,86};
    private Ringtone ringtone;
    PatternLockView patternLockView;
    TextView passwordTextView;
    String restorePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_game);
        passwordTextView = findViewById(R.id.password_text_view);
        patternLockView = findViewById(R.id.pattern_lock_view);

        restorePassword = generateRandom4DigitNumber();
        passwordTextView.setText(restorePassword);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String password = PatternLockUtils.patternToString(patternLockView, pattern);
                if (!restorePassword.equals("")) {
                    //2-1 입력과 저장된 패스워드가 같다면
                    if (password.equals(restorePassword)) {
                        //패턴 색상 변경
                        patternLockView.setCorrectStateColor(Color.parseColor("#0000FF"));
                        ringtone.stop();
                        int alarmId = getIntent().getIntExtra("ALARM_ID", -1);
                        stopAlarm(alarmId); // 알람 종료
                        Intent TraceIntent = new Intent(PatternGameActivity.this, MainActivity.class);
                        startActivity(TraceIntent);
                        //파랑
                    }
                    //2-2 입력과 저장된 패스워드가 다르다면
                    else {
                        //패턴 색상 변경
                        patternLockView.setCorrectStateColor(Color.parseColor("#FF0000"));
                        //빨강
                    }
                }
            }

            @Override
            public void onCleared() {

            }
        });
    }
    /*private static boolean containsExcludedNumbers(ArrayList<Integer> digits) {
        for(Integer excludedNumber : BLOCKED) {
            if (digits.contains(excludedNumber))
                return true;
        }
        return false;
    }*/
    public String generateRandom4DigitNumber() {
        ArrayList<Integer> digits = new ArrayList<>();
        // 0부터 9사이의 숫자
        for (int i = 0; i < 9; i++) {
            digits.add(i);
        }
        Collections.shuffle(digits);
        // BLOCKED 숫자 들어 가지 않을 떄 까지 순서 섞기
        /*do {
            Collections.shuffle(digits);
            // 해당 숫자 제외하기
        } while (containsExcludedNumbers(digits));*/
        StringBuilder randomNum = new StringBuilder();
        // 4자리 비밀번호
        for (int i = 0; i < 4; i++) {
            randomNum.append(digits.get(i));
        }
        return randomNum.toString();
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
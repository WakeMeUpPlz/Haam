package com.haam.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PopRing extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 현재 시간을 가져오기
        Date currentDate = new Date();
        // SimpleDateFormat을 사용하여 시간 형식 지정
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        // 형식에 맞게 시간을 문자열로 변환
        String currentTime = dateFormat.format(currentDate);
        // Intent에서 helperPhoneNumber를 추출
        String helperPhoneNumber = intent.getStringExtra("HELPER_PHONE_NUMBER");
        // 예를 들어, PopRing 액티비티를 시작하는 코드를 여기에 추가할 수 있습니다.
        Intent popRingIntent = new Intent(context, PopRingActivity.class);
        popRingIntent.putExtra("ALARM_TIME",currentTime);
        popRingIntent.putExtra("HELPER_PHONE_NUMBER", helperPhoneNumber);
        popRingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(popRingIntent);
    }
}
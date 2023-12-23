package com.haam.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class PopRing extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmTime = intent.getStringExtra("ALARM_TIME"); // 예를 들어, 알람 ID를 가져오는 코드
        Log.d("onReceive:", alarmTime);

//    TODO 알람리스트를 확인해서 울릴시점인지 판단? 이미 지난 시간인가? 요일인가?알람 리스트가 있는지

        Toast.makeText(context, "알람이 울렸습니다!", Toast.LENGTH_SHORT).show();
        // 예를 들어, PopRing 액티비티를 시작하는 코드를 여기에 추가할 수 있습니다.
        Intent popRingIntent = new Intent(context, PopRingActivity.class);
        popRingIntent.putExtra("ALARM_TIME",alarmTime);
        popRingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(popRingIntent);
    }
}
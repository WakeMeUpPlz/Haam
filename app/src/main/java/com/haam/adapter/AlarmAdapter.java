package com.haam.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haam.R;
import com.haam.models.Alarm;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private List<Alarm> alarmList;
    private Context context;

    public AlarmAdapter(List<Alarm> alarmList, Context context) {
        this.alarmList = alarmList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);

        holder.tvDorN.setText(alarm.getDorN());
        holder.tvTime.setText(alarm.getTime());
        holder.activateBtn.setChecked(alarm.getIsActivated());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(alarm.getTime(), position);
            }
        });
    }
    // 알람음 목록 가져오기
    private ArrayAdapter<String> getAlarmSoundsAdapter() {
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_NOTIFICATION); // 소리 재생
        //ringtoneManager.setIncludeDrm(false); // 이 부분을 주석 처리하거나 제거하세요

        Cursor cursor = ringtoneManager.getCursor();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            adapter.add(title);
        }

        return adapter;
    }

    private void showPopup(String alarmTime, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 레이아웃 리소스를 팝업에 설정
        View view = LayoutInflater.from(context).inflate(R.layout.activity_add_alarm_popup, null);
        builder.setView(view);
        TimePicker timePicker = view.findViewById(R.id.alarmTimeSelector2);

        String[] Time = alarmTime.split(":");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(Integer.parseInt(Time[0]));
            timePicker.setMinute(Integer.parseInt(Time[1]));
        }
        // 스피너 초기화 및 알람음 목록 가져오기
        Spinner spinnerAlarmSound = view.findViewById(R.id.spinnerAlarmSound);
        ArrayAdapter<String> alarmSoundsAdapter = getAlarmSoundsAdapter();
        spinnerAlarmSound.setAdapter(alarmSoundsAdapter);

        // 요일 레이아웃 요소 초기화
        Switch switchRepeat = view.findViewById(R.id.switchRepeat);
        LinearLayout layoutRepeatDays = view.findViewById(R.id.layoutRepeatDays);

        // 스위치 상태 변경 감지
        switchRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치가 켜지면 요일 목록을 보이게, 꺼지면 숨기게 설정
                layoutRepeatDays.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        // 팝업의 확인 버튼 등을 설정
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int hour, minute;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    // 안드로이드 M 미만에서는 deprecated된 getHour()와 getMinute() 대신
                    // getCurrentHour()와 getCurrentMinute()를 사용합니다.
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                // 리사이클러뷰에서 선택한 아이템의 데이터 가져오기
                Alarm selectedItem = alarmList.get(position);

                // 선택한 아이템의 시간 업데이트
                selectedItem.setTime(String.format("%02d:%02d", hour, minute));
                // 선택한 아이템의 알람음 업데이트
                String selectedAlarmSound = (String) spinnerAlarmSound.getSelectedItem();
                selectedItem.setRingTone(selectedAlarmSound);

                // 리사이클러뷰 업데이트
                notifyItemChanged(position);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 취소 버튼을 눌렀을 때 수행할 동작
                dialog.dismiss(); // 팝업을 닫습니다.
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvDorN, tvTime;  // 예시로 제목과 시간만 표시
        ToggleButton activateBtn;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDorN = itemView.findViewById(R.id.tvDorN);
            tvTime = itemView.findViewById(R.id.tvTime);
            activateBtn = itemView.findViewById(R.id.activateBtn);
        }
    }
}

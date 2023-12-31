package com.haam.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haam.MainActivity;
import com.haam.R;
import com.haam.SQLiteHelper;
import com.haam.models.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    public List<Alarm> alarmList;
    private Context context;
    private AlertDialog alertDialog;  // 클래스의 멤버 변수로 AlertDialog 선언

    public AlarmAdapter(List<Alarm> alarmList, Context context) {
        this.alarmList = alarmList;
        this.context = context;
    }
    public void setAlarmList(List<Alarm> newAlarmList) {
        this.alarmList = newAlarmList;
        Log.d("알람 추가됨(setAlarmList)",alarmList.toString());
        notifyDataSetChanged();
        // Call the setupAlarms method whenever alarmList is changed
        ((MainActivity) context).setupAlarms();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }
    private boolean charToBoolean(char c) {
        return c == '1';
    }
    private char booleanToChar(boolean value) {
        return value ? '1' : '0';
    }
    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);

        holder.tvDorN.setText(alarm.getDorN());
        holder.tvTime.setText(alarm.getTime());
        holder.activateBtn.setChecked(alarm.getIsActivated());
        holder.activateBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 알람의 활성화 상태 업데이트
            alarm.setIsActivated(isChecked);
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(alarm,position);
            }
        });

        for (int i=0;i<7;i++) {
            if (charToBoolean(alarm.getDates().charAt(i))) {
                holder.weekday.get(i).setTextColor(Color.BLACK);
            } else {
                holder.weekday.get(i).setTextColor(Color.WHITE);
            }
        }
    }

    //알람 추가를 위한 팝업
    public void initPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 레이아웃 리소스를 팝업에 설정
        View view = LayoutInflater.from(context).inflate(R.layout.activity_add_alarm_popup, null);
        builder.setView(view);

        Button deleteAlarmBtn= view.findViewById(R.id.deleteAlarmBtn);
        deleteAlarmBtn.setVisibility(View.GONE);
        TimePicker timePicker = view.findViewById(R.id.alarmTimeSelector2);

        EditText editTextAlarmName = view.findViewById(R.id.editTextAlarmName);

        Switch switchHelper= view.findViewById(R.id.switchHelper);
        EditText editTextPhoneNumber=view.findViewById(R.id.editTextPhoneNumber);

        switchHelper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    editTextPhoneNumber.setVisibility(View.GONE);
                } else {
                    editTextPhoneNumber.setVisibility(View.VISIBLE);
                }
            }
        });

        // 체크박스 초기화
        CheckBox checkBoxMonday = view.findViewById(R.id.checkBoxMonday);
        CheckBox checkBoxTuesday = view.findViewById(R.id.checkBoxTuesday);
        CheckBox checkBoxWednesday = view.findViewById(R.id.checkBoxWednesday);
        CheckBox checkBoxThursday = view.findViewById(R.id.checkBoxThursday);
        CheckBox checkBoxFriday = view.findViewById(R.id.checkBoxFriday);
        CheckBox checkBoxSaturday = view.findViewById(R.id.checkBoxSaturday);
        CheckBox checkBoxSunday = view.findViewById(R.id.checkBoxSunday);

        // 팝업의 확인 버튼 등을 설정
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            int hour, minute;
            String dorN;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                if (hour >= 12) {
                    dorN = "PM";
                } else {
                    dorN = "AM";
                }

                int alarmId = alarmList.size() + 1;
                String time = String.format("%02d:%02d", hour, minute);
                String title = String.valueOf(editTextAlarmName.getText());
                String phoneNumber;
                if(!String.valueOf(editTextPhoneNumber.getText()).isEmpty()){
                    phoneNumber=String.valueOf(editTextPhoneNumber.getText());
                }else{
                    phoneNumber="";
                }

                // 요일 정보 가져오기
                boolean[] week = {
                        checkBoxMonday.isChecked(),
                        checkBoxTuesday.isChecked(),
                        checkBoxWednesday.isChecked(),
                        checkBoxThursday.isChecked(),
                        checkBoxFriday.isChecked(),
                        checkBoxSaturday.isChecked(),
                        checkBoxSunday.isChecked()
                };
                // 새로운 알람 객체 생성
//                Alarm newAlarm = new Alarm(alarmId, time, title, "default", phoneNumber, booleanArrayToString(week), true, false, dorN);
                SQLiteHelper.insertAlarm(
                        context,alarmId, time, title, "default", phoneNumber,  booleanArrayToString(week), true, false, dorN);
                alarmList = SQLiteHelper.getAllAlarms(context);
                setAlarmList(alarmList);
                Log.d("알람 추가됨(AlarmAdapter)",alarmList.toString());

            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss(); // 팝업을 닫습니다.
            }
        });

        alertDialog = builder.create();  // AlertDialog 객체 생성
        alertDialog.show();
    }

    // boolean 배열을 문자열로 변환
    private String booleanArrayToString(boolean[] week) {
        StringBuilder result = new StringBuilder();
        for (boolean b : week) {
            result.append(b ? '1' : '0');
        }
        return result.toString();
    }

    //알람 수정을 위한 팝업
    public void showPopup(Alarm alarm,int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 레이아웃 리소스를 팝업에 설정
        View view = LayoutInflater.from(context).inflate(R.layout.activity_add_alarm_popup, null);
        builder.setView(view);


        //알람 삭제
        Button deleteAlarmBtn = view.findViewById(R.id.deleteAlarmBtn);
        deleteAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteHelper.deleteAlarm(context,alarm.getAlarmId());
                // 현재 위치의 알람을 삭제
                alarmList.remove(position);
                // 다이얼로그 닫기
                alertDialog.dismiss();
                // 리사이클러뷰 갱신
                notifyItemRemoved(position);
            }
        });


        TimePicker timePicker = view.findViewById(R.id.alarmTimeSelector2);

        String[] Time = alarm.getTime().split(":");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(Integer.parseInt(Time[0]));
            timePicker.setMinute(Integer.parseInt(Time[1]));
        }

        // 알람 활성화
        Switch switchAlarmActivity = view.findViewById(R.id.switchAlarmActivity);
        switchAlarmActivity.setChecked(alarmList.get(position).getIsActivated());

        //알람 요일 설정
        CheckBox checkBoxMonday=view.findViewById(R.id.checkBoxMonday);
        CheckBox checkBoxTuesday=view.findViewById(R.id.checkBoxTuesday);
        CheckBox checkBoxWednesday=view.findViewById(R.id.checkBoxWednesday);
        CheckBox checkBoxThursday=view.findViewById(R.id.checkBoxThursday);
        CheckBox checkBoxFriday=view.findViewById(R.id.checkBoxFriday);
        CheckBox checkBoxSaturday=view.findViewById(R.id.checkBoxSaturday);
        CheckBox checkBoxSunday=view.findViewById(R.id.checkBoxSunday);

        checkBoxMonday.setChecked(charToBoolean(alarm.getDates().charAt(0)));
        checkBoxTuesday.setChecked(charToBoolean(alarm.getDates().charAt(1)));
        checkBoxWednesday.setChecked(charToBoolean(alarm.getDates().charAt(2)));
        checkBoxThursday.setChecked(charToBoolean(alarm.getDates().charAt(3)));
        checkBoxFriday.setChecked(charToBoolean(alarm.getDates().charAt(4)));
        checkBoxSaturday.setChecked(charToBoolean(alarm.getDates().charAt(5)));
        checkBoxSunday.setChecked(charToBoolean(alarm.getDates().charAt(6)));


        EditText editTextAlarmName=view.findViewById(R.id.editTextAlarmName);
        editTextAlarmName.setText(alarmList.get(position).getTitle());

        Switch switchHelper= view.findViewById(R.id.switchHelper);
        EditText editTextPhoneNumber=view.findViewById(R.id.editTextPhoneNumber);
        switchHelper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    editTextPhoneNumber.setVisibility(View.GONE);
                } else {
                    editTextPhoneNumber.setVisibility(View.VISIBLE);
                }
            }
        });
        if (!alarmList.get(position).getHelper().isEmpty()) {
            switchHelper.setChecked(true); // 휴대폰 번호가 있으면 스위치를 활성화합니다.
            editTextPhoneNumber.setText(alarmList.get(position).getHelper());
        } else {
            switchHelper.setChecked(false); // 휴대폰 번호가 없으면 스위치를 비활성화합니다.
        }




        // 팝업의 확인 버튼 등을 설정
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            int hour, minute;
            String dorN;
            @Override
            public void onClick(DialogInterface dialog, int which) {


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
                //선택한 아이템의 알람제목 업데이트
                selectedItem.setTitle(String.valueOf(editTextAlarmName.getText()));
                // 선택한 아이템의 시간 업데이트
                selectedItem.setTime(String.format("%02d:%02d", hour, minute));
                if (hour>=12){
                    dorN="PM";
                }else{
                    dorN="AM";
                }
                // 선택한 아이템의 요일 업데이트
                StringBuilder updatedDates = new StringBuilder("0000000");
                updatedDates.setCharAt(0, booleanToChar(checkBoxMonday.isChecked()));
                updatedDates.setCharAt(1, booleanToChar(checkBoxTuesday.isChecked()));
                updatedDates.setCharAt(2, booleanToChar(checkBoxWednesday.isChecked()));
                updatedDates.setCharAt(3, booleanToChar(checkBoxThursday.isChecked()));
                updatedDates.setCharAt(4, booleanToChar(checkBoxFriday.isChecked()));
                updatedDates.setCharAt(5, booleanToChar(checkBoxSaturday.isChecked()));
                updatedDates.setCharAt(6, booleanToChar(checkBoxSunday.isChecked()));

                selectedItem.setDates(updatedDates.toString());

                selectedItem.setDorN(dorN);

                //선택한 아이템의 알람제목 업데이트
                selectedItem.setTitle(String.valueOf(editTextAlarmName.getText()));

                String phoneNumber = String.valueOf(editTextPhoneNumber.getText());
                if (!phoneNumber.isEmpty() && editTextPhoneNumber.getVisibility()==View.VISIBLE) {
                    selectedItem.setHelper(String.valueOf(editTextPhoneNumber.getText()));
                } else {
                    selectedItem.setHelper("");
                }
                SQLiteHelper.updateAlarm(context,alarm.getAlarmId(),alarm.getTime(),alarm.getTitle(),
                        alarm.getRingTone(),alarm.getHelper(),alarm.getDates(),alarm.getIsActivated(),
                        alarm.getIsHelperActivated(),alarm.getDorN());

                //알람갱신
                setAlarmList(alarmList);

            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 취소 버튼을 눌렀을 때 수행할 동작
                alertDialog.dismiss(); // 팝업을 닫습니다.
            }
        });

        alertDialog = builder.create();  // AlertDialog 객체 생성
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvDorN, tvTime; 
        ToggleButton activateBtn;
        TextView monday,tuesday,wednesday,thursday,friday,saturday,sunday;
        ArrayList<TextView> weekday;
        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDorN = itemView.findViewById(R.id.tvDorN);
            tvTime = itemView.findViewById(R.id.tvTime);
            activateBtn = itemView.findViewById(R.id.activateBtn);
            
            monday=itemView.findViewById(R.id.monday);
            tuesday=itemView.findViewById(R.id.tuesday);
            wednesday=itemView.findViewById(R.id.wednesday);
            thursday=itemView.findViewById(R.id.thursday);
            friday=itemView.findViewById(R.id.friday);
            saturday=itemView.findViewById(R.id.saturday);
            sunday=itemView.findViewById(R.id.sunday);

            // weekday 리스트 초기화
            weekday = new ArrayList<>();
            weekday.add(monday);
            weekday.add(tuesday);
            weekday.add(wednesday);
            weekday.add(thursday);
            weekday.add(friday);
            weekday.add(saturday);
            weekday.add(sunday);
        }
    }
}

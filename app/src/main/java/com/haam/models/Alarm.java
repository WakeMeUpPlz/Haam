package com.haam.models;

public class Alarm {
    int alarmId;
    String time;
    String title;
    String ringTone;
    String helper;
    String dates;
    boolean isActivated;
    boolean isHelperActivated;
    String dorN;

    public Alarm(int alarmId, String time, String title, String ringTone, String helper, String dates, boolean isActivated, boolean isHelperActivated, String dorN) {
        this.alarmId = alarmId;
        this.time = time;
        this.title = title;
        this.ringTone = ringTone;
        this.helper = helper;
        this.dates = dates;
        this.isActivated = isActivated;
        this.isHelperActivated = isHelperActivated;
        this.dorN = dorN;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarm_id) {
        this.alarmId = alarmId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRingTone() {
        return ringTone;
    }

    public void setRingTone(String ringTone) {
        this.ringTone = ringTone;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public boolean getIsHelperActivated() {
        return isHelperActivated;
    }

    public void setIsHelperActivated(boolean isHelperActivated) {
        this.isHelperActivated = isHelperActivated;
    }

    public String getDorN() {
        return dorN;
    }

    public void setDorN(String dorN) {
        this.dorN = dorN;
    }
}

package com.haam;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "haam.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS Users (ID TEXT PRIMARY KEY, password TEXT, Name TEXT, phoneNumber TEXT);");

        // 예시 데이터 삽입
        db.execSQL("INSERT INTO Users VALUES ('20211138', '12345', '오영서', '01041082379');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 업그레이드 로직 추가
    }

    // ID 존재 여부 확인 함수
    public static boolean isIDExist(Context context, String id) {
        SQLiteDatabase db = new SQLiteHelper(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE ID = ?", new String[]{id});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // ID와 비밀번호 일치 여부 확인 함수
    public static boolean isPasswordCorrect(Context context, String id, String password) {
        SQLiteDatabase db = new SQLiteHelper(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE ID = ? AND password = ?", new String[]{id, password});
        boolean correct = cursor.moveToFirst();
        cursor.close();
        return correct;
    }

    // 사용자 정보 삽입 함수
    public static void insertUser(Context context, String id, String password, String name, String phoneNumber) {
        SQLiteDatabase db = new SQLiteHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", id);
        values.put("password", password);
        values.put("Name", name);
        values.put("phoneNumber", phoneNumber);
        db.insert("Users", null, values);
        db.close();
    }

    // 전화번호 중복 확인 함수
    public static boolean isPhoneNumberExist(Context context, String phoneNumber) {
        SQLiteDatabase db = new SQLiteHelper(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE phoneNumber = ?", new String[]{phoneNumber});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Users 테이블의 모든 요소 조회 함수
    public static void getAllUsers(Context context) {
        SQLiteDatabase db = new SQLiteHelper(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users", null);

        // 커서를 이용하여 결과 순회
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Name"));
            @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));

            // 결과 출력 또는 다른 작업 수행
            Log.d("SQLiteHelper", "ID: " + id + ", Password: " + password + ", Name: " + name + ", Phone Number: " + phoneNumber);
        }

        cursor.close();
        db.close();
    }

}

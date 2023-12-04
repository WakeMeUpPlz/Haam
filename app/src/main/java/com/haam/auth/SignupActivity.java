package com.haam.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.haam.MainActivity;
import com.haam.R;
import com.haam.SQLiteHelper;

public class SignupActivity extends AppCompatActivity {
    SQLiteHelper sqLiteHelper;
    private boolean IDTrue = false;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextName;
    private EditText editTextPhoneNumber;

    private Button buttonCheckUsername;
    private Button buttonSignup;

    private TextView textViewPasswordMismatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sqLiteHelper = new SQLiteHelper(this);
        // XML에서 정의한 UI 요소들을 찾아와 변수에 할당
        editTextUsername = findViewById(R.id.editTextUsernameSignup);
        editTextPassword = findViewById(R.id.editTextPasswordSignup);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextName = findViewById(R.id.editTextNameSignup);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumberSignup);

        buttonCheckUsername = findViewById(R.id.buttonCheckUsername);

        buttonSignup = findViewById(R.id.buttonSignup);

        textViewPasswordMismatch = findViewById(R.id.textViewPasswordMismatch);

        // 아이디 중복 확인 버튼 클릭 시
        buttonCheckUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 아이디 가져오기
                String inputId = editTextUsername.getText().toString();

                // 서버로 아이디 중복 확인 요청 보내기
                IDTrue = checkUsernameAvailability(inputId);
                if (IDTrue) {
                    showToast("사용가능한 아이디입니다.");
                }
                else
                    showToast("사용할 수 없는 아이디입니다.");
            }
        });

        // 회원가입 버튼 클릭 시
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String phonenumber = editTextPhoneNumber.getText().toString();
                // 비밀번호 일치 여부 확인
                if (!TextUtils.equals(editTextPassword.getText().toString(), editTextConfirmPassword.getText().toString())) {
                    // 비밀번호 불일치 시 메시지 표시
                    textViewPasswordMismatch.setVisibility(View.VISIBLE);
                    textViewPasswordMismatch.setText("비밀번호가 일치하지 않습니다.");
                    return;
                }
                // 아이디 중복 확인
                String ID = editTextUsername.getText().toString();
                if (!IDTrue) {
                    showToast("사용할 수 없는 아이디입니다.");
                    return;
                }
                // 비밀번호 길이 확인
                String password = editTextPassword.getText().toString();
                if (password.length() < 4 || password.length() > 8) {
                    showToast("비밀번호는 4자리에서 8자리 사이여야합니다.");
                    return;
                }
                // 전화번호 길이 확인
                if (phonenumber.length() != 11) {
                    showToast("맞는 전화번호를 입력해주세요.");
                    return;
                }

                // 전화번호 중복 확인
                if (isPhoneNumberExist(getApplicationContext(), phonenumber)) {
                    showToast("이미 등록된 전화번호입니다.");
                    return;
                }

                // 회원가입 성공
                showToast("회원가입이 완료되었습니다.");

                //데이터베이스에 유저 추가
                SQLiteHelper.insertUser(getApplicationContext(), ID, password, name, phonenumber);
                SQLiteHelper.getAllUsers(getApplicationContext());
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    // 아이디 중복 확인 메서드
    private boolean checkUsernameAvailability(String ID) {
        // 아이디 길이 확인
        if (ID.length() < 4) {
            showToast("아이디는 4자리 이상 이여야합니다.");
            return false;
        }
        if (SQLiteHelper.isIDExist(getApplicationContext(), ID)) {
            return false;
        } else {
            return true;
        }
    }

    // 전화번호가 11자리인지 확인하는 함수
    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() == 11;
    }

    // 전화번호 중복 확인 함수
    private boolean isPhoneNumberExist(Context context, String phoneNumber) {
        return SQLiteHelper.isPhoneNumberExist(context, phoneNumber);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

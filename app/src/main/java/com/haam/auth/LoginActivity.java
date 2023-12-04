package com.haam.auth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haam.MainActivity;
import com.haam.R;
import com.haam.SQLiteHelper;

public class LoginActivity extends AppCompatActivity {
    SQLiteHelper sqLiteHelper;
    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sqLiteHelper = new SQLiteHelper(this);
        // XML에서 정의한 UI 요소들을 찾아와 변수에 할당
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 아이디와 비밀번호 가져오기
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                //데이터베이스의 "userID"와 "password"가 맞을 때 로그인 성공으로 가정
                if (SQLiteHelper.isPasswordCorrect(getApplicationContext(), username, password)) {
                    // 로그인 성공
                    showToast("로그인 성공!");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                } else {
                    // 로그인 실패
                    showToast("아이디 또는 비밀번호가 올바르지 않습니다.");
                }
            }
        });

        // 회원가입 button 클릭 시 동작 추가
        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

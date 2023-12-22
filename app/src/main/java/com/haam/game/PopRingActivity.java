package com.haam.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.haam.R;

import java.util.Random;

public class PopRingActivity  extends AppCompatActivity {
    Button goPatternBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_ring);
        goPatternBtn = findViewById(R.id.goPatternBtn);

        goPatternBtn.setOnClickListener(view -> {
            Class<?>[] activities = {MultiplyGameActivity.class, PatternGameActivity.class, TraceGameActivity.class};
            Random random = new Random();
            int randomIndex = random.nextInt(activities.length);
            Class<?> selectedActivity = activities[randomIndex];

            Intent intent = new Intent(PopRingActivity.this, selectedActivity);
            startActivity(intent);
        });
    }
}

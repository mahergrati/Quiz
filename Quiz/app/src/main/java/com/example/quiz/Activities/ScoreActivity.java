package com.example.quiz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quiz.R;
import com.example.quiz.databinding.ActivityScoreBinding;

public class ScoreActivity extends AppCompatActivity {
    ActivityScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int totalScore = getIntent().getIntExtra("total", 0);
        int correct = getIntent().getIntExtra("score", 0);
        int wrong = totalScore - correct;
        binding.totalAnswer.setText(String.valueOf(totalScore));
        binding.rightAnswer.setText(String.valueOf(correct));
        binding.wrongAnswer.setText(String.valueOf(wrong));
        binding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (getClass().getSimpleName().equals("SetsActivity")) {
                    intent = new Intent(ScoreActivity.this, SetsActivity.class);
                } else {
                    intent = new Intent(ScoreActivity.this, SetsActivityEnergie.class);
                }
                startActivity(intent);
                finish();
            }
        });
        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
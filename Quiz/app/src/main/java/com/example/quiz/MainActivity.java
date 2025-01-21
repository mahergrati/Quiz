package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.quiz.Activities.SetsActivity;
import com.example.quiz.Activities.SetsActivityEnergie;

public class MainActivity extends AppCompatActivity {
    CardView energie, air;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        air = findViewById(R.id.air);
        energie = findViewById(R.id.energie);

        air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetsActivity.class);
                startActivity(intent);
            }
        });
        energie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetsActivityEnergie.class);
                startActivity(intent);
            }
        });
    }
}

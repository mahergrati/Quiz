package com.example.quiz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quiz.Adapters.SetAdapter;
import com.example.quiz.MainActivity;
import com.example.quiz.Models.SetModel;
import com.example.quiz.databinding.ActivitySetsEnergie3Binding;

import java.util.ArrayList;

public class SetsActivityEnergie extends AppCompatActivity implements SetAdapter.OnSetClickListener {
    ActivitySetsEnergie3Binding binding;
    ArrayList<SetModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsEnergie3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.setsRecyEn.setLayoutManager(linearLayoutManager);
        list.add(new SetModel("المستوى 1"));
        list.add(new SetModel("المستوى 2"));
        list.add(new SetModel("المستوى 3"));
        list.add(new SetModel("المستوى 4"));

        SetAdapter adapter = new SetAdapter(this, list, this);
        binding.setsRecyEn.setAdapter(adapter);

        // Correction de l'initialisation de l'ImageView
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetsActivityEnergie.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onSetClick(SetModel setModel) {
        Intent intent = new Intent(SetsActivityEnergie.this, EnergieActivity.class);
        intent.putExtra("set", setModel.getSetName());
        startActivity(intent);
    }
}

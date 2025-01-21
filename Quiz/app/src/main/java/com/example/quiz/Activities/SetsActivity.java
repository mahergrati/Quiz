package com.example.quiz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quiz.Adapters.SetAdapter;
import com.example.quiz.MainActivity;
import com.example.quiz.Models.SetModel;
import com.example.quiz.R;
import com.example.quiz.databinding.ActivitySetsBinding;

import java.util.ArrayList;

public class SetsActivity extends AppCompatActivity implements SetAdapter.OnSetClickListener {
    ActivitySetsBinding binding;
    ArrayList<SetModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.setsRecy.setLayoutManager(linearLayoutManager);
        list.add(new SetModel("المستوى 1"));
        list.add(new SetModel("المستوى 2"));
        list.add(new SetModel("المستوى 3"));
        list.add(new SetModel("المستوى 4"));
        list.add(new SetModel("المستوى 5"));

        SetAdapter adapter = new SetAdapter(this, list, this);
        binding.setsRecy.setAdapter(adapter);

        ImageView back = findViewById(R.id.ic_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onSetClick(SetModel setModel) {
        Intent intent = new Intent(SetsActivity.this, QuestionActivity.class);
        intent.putExtra("set", setModel.getSetName());
        startActivity(intent);
    }
}

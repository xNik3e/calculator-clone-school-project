package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AboutMe extends AppCompatActivity {

    private ImageView animationFS;
    private ImageView goBack;
    private TextView description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        description = findViewById(R.id.textDescription);
        goBack = findViewById(R.id.go_back_arrow);
        animationFS = findViewById(R.id.animation);
        Glide.with(this).load(R.drawable.flutter_jam).into(animationFS);

        description.setText("Calculator made by Nikus\nIndex number: 228810");
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutMe.this, MainActivity.class));
            }
        });
    }
}
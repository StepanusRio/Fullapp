package com.example.fullapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainHomeProfile extends AppCompatActivity {

    ImageButton back,btnEditProfile;
    TextView tvNama, tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_profile);

        tvNama = (TextView) findViewById(R.id.tvNama);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        back = (ImageButton) findViewById(R.id.back);
        btnEditProfile = (ImageButton) findViewById(R.id.btnEditProfile);


        String nama = getIntent().getStringExtra("nama").toString();
        String email = getIntent().getStringExtra("email").toString();

        tvNama.setText(nama);
        tvEmail.setText(email);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHomeProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHomeProfile.this, MainEditProfile.class);
                intent.putExtra("nama",getIntent().getStringExtra("nama"));
                intent.putExtra("email",getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });

    }
}
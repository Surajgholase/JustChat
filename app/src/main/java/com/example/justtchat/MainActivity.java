package com.example.justtchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Adding main acitivity function
        Button btnStartButton = findViewById(R.id.btnStartButton);
        if(btnStartButton != null){
            btnStartButton.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            });
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.card), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
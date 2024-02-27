package com.example.a2games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.boton2048).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start2048();
            }
        });

        findViewById(R.id.botonSenku).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSenku();
            }
        });
    }

    private void start2048() {
        Intent intent = new Intent(this, Game2048.class);
        startActivity(intent);
    }

    private void startSenku() {
        Intent intent = new Intent(this, GameSenku.class);
        startActivity(intent);
    }
}
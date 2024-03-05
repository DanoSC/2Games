package com.example.a2games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registrar extends AppCompatActivity {
    private EditText user;
    private EditText contra;
    private EditText confirmar_contra;
    private Button volver;
    private Button registro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        user = findViewById(R.id.UserRegisterText);
        contra = findViewById(R.id.ContraRegisterText);
        confirmar_contra = findViewById(R.id.ContraConfirmRegisterText);

        volver = findViewById(R.id.boton_volver);
        registro = findViewById(R.id.boton_Registrar);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverLogin();
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);
                String username = sharedPreferences.getString("user", "");
                String newUser = user.getText().toString();
                if (!username.equals(newUser)){
                    String newContra = contra.getText().toString();
                    String ConfirmNewContra = confirmar_contra.getText().toString();
                    if(newContra.equals(ConfirmNewContra)){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user", newUser);
                        editor.putString("pass", newContra);
                        editor.putInt("score2048", 0);
                        editor.putInt("scoreSenku", 0);
                        editor.putInt("timer2048", 0);
                        editor.putInt("timeSenku", 0);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Nuevo Usuario Guardado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Contrseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Usuario ya existe", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void volverLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
package com.example.a2games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private Button inciar;
    private Button registro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.UserLoginText);
        pass = findViewById(R.id.ContraLoginText);

        inciar = findViewById(R.id.boton_Iniciar);
        registro = findViewById(R.id.boton_Registrar);


        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goRegister();
            }
        });

        inciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarDatos();
            }
        });
    }

    private void goRegister(){
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }
    private void comprobarDatos(){
        SharedPreferences sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);
        String username = sharedPreferences.getString("user", "");
        String contra = sharedPreferences.getString("pass", "");
        String userText = user.getText().toString();
        String contraText = pass.getText().toString();

        if(username.equals(userText) && contra.equals(contraText)){
            Intent intent = new Intent(this, GamesMenu.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }

    }

}
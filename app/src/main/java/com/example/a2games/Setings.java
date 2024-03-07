package com.example.a2games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Setings extends AppCompatActivity {
    private EditText editTextUser;
    private EditText editTextOldPass;
    private EditText editTextNewPass;
    private TextView highScore2048;
    private TextView highScoreSenku;

    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setinngs);

        highScore2048 = findViewById(R.id.settingScore2048);
        highScoreSenku = findViewById(R.id.settingScoreSemku);

        editTextUser = findViewById(R.id.usernameEditText);
        editTextOldPass = findViewById(R.id.passOdlEditText);
        editTextNewPass = findViewById(R.id.passNewEditText);

        saveButton = findViewById(R.id.saveButton);

        SharedPreferences sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);

        highScore2048.setText("2048: "+String.valueOf(sharedPreferences.getInt("score2048", 0)));

        String valorSenku;
        valorSenku = "Senku: "+String.valueOf(String.valueOf(sharedPreferences.getInt("scoreSenku", 0))) + " movimientos";
        long timeSenku = sharedPreferences.getLong("timeSenku", 0);

        if(timeSenku == 0){
            valorSenku = valorSenku + " Sin completar";
        } else{
            valorSenku = valorSenku +" "+timeSenku + " segundos restantes";
        }
        highScoreSenku.setText(valorSenku);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings(view);
            }
        });


    }

    private void saveSettings(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);
        String pass = sharedPreferences.getString("pass", "");

        if(pass.equals(editTextOldPass.getText().toString())){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user", editTextUser.getText().toString());
            editor.putString("pass", editTextNewPass.getText().toString());
            editor.apply();
            Toast.makeText(getApplicationContext(), "Usuario y contraseña modificados", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Antigua Contraseña no es correcta", Toast.LENGTH_SHORT).show();
        }

    }
}
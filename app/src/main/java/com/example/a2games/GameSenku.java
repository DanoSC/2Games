package com.example.a2games;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameSenku extends AppCompatActivity {

    public GridLayout gridLayout;
    public ImageButton[][] botones = new ImageButton[7][7];
    public int[][] undoBotones = new int[7][7];
    public Button resetButon;
    public Button backButon;
    public TextView score;
    public CountDownTimer timer;
    public TextView timeView;
    public TextView maxTimeView;
    public long timeLeft;
    public TextView maxScore;
    public boolean timeEnd;
    public int restMoveScore = 1;
    final int viva = 0;
    final int vacia = 1;
    final int seleccionada = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_senku);
        gridLayout = findViewById(R.id.gridLayoutSenku);
        resetButon = findViewById(R.id.resetSenku);
        backButon = findViewById(R.id.backSenku);
        score = findViewById(R.id.ScoreSenku);
        maxScore = findViewById(R.id.SenkuMaxScore);
        timeView = findViewById(R.id.timeView);
        maxTimeView = findViewById(R.id.timeViewScore);

        resetButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score.setText("0");
                iniciarJuego();
                timer.cancel();
                iniciarContador();
            }
        });

        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(score.getText().toString()) - restMoveScore;
                restMoveScore = 0;
                score.setText(""+i);
                volverEstadoAnterior();
            }
        });


        iniciarJuego();
        iniciarContador();
    }

    public void iniciarJuego() {

        SharedPreferences sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);
        int maxValue = sharedPreferences.getInt("scoreSenku", 0);
        long maxTime = sharedPreferences.getLong("timeSenku", 0);

        maxScore.setText(String.valueOf(maxValue));
        maxTimeView.setText(String.valueOf(maxTime));

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                // Ignorar las esquinas que no forman parte de la cruz
                if ((i == 0 || i == 1 || i == 5 || i == 6) && (j == 0 || j == 1 || j == 5 || j == 6)) {
                    botones[i][j] = null;
                }else{
                    ImageButton imageButton = new ImageButton(this);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    imageButton.setPadding(10, 10, 10, 10);
                    params.rowSpec = GridLayout.spec(i, 1f);
                    params.columnSpec = GridLayout.spec(j, 1f);
                    imageButton.setLayoutParams(params);
                    imageButton.setBackgroundColor(getResources().getColor(R.color.color_0));
                    imageButton.setId(i * 7 + j); // ID único para cada botón
                    final int fil = i;
                    final int col = j;
                    imageButton.setOnClickListener(v -> {
                        clicarBoton(imageButton, fil, col);
                    });

                    if(i == 3 && j ==3){
                        imageButton.setImageResource(R.drawable.ficha_vacia);
                        imageButton.setTag(vacia);
                        botones[i][j] = imageButton;
                        gridLayout.addView(imageButton);

                    }else {
                        imageButton.setImageResource(R.drawable.ficha);
                        imageButton.setTag(viva);
                        botones[i][j] = imageButton;
                        gridLayout.addView(imageButton);
                    }



                }
            }

        }
    }
    public void clicarBoton(ImageButton imageButton, int row, int col) {
        if(isAnySelected()){

            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    if (botones[i][j]!= null){
                        if((int)botones[i][j].getTag() == this.seleccionada) {
                            if((int)imageButton.getTag() == this.vacia){

                                this.move(i,j,row,col);
                                int aux = Integer.parseInt(score.getText().toString())+1;
                                score.setText(String.valueOf(aux));
                            }else{
                                botones[i][j].setTag(this.viva);
                                botones[i][j].setImageResource(R.drawable.ficha);
                            }
                        }
                    }
                }
            }

            if(this.hasGanado()){
                this.MensajeGanar();
            }else{
                if(this.hasPerdido()){
                    this.MensajePerder();
                }
            }





        }else{
            this.guardarEstado();
            if((int)imageButton.getTag() != this.vacia){
                imageButton.setImageResource(R.drawable.ficha_selecionada);
                imageButton.setTag(this.seleccionada);
            }
        }



    }
    public boolean isAnySelected(){
        if(timeEnd){
            return true;
        }else{
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    if (botones[i][j]!= null){
                        if((int)botones[i][j].getTag() == this.seleccionada) {

                            return true;
                        }
                    }
                }
            }
            return false;
        }

    }
    public boolean hasGanado(){
        int contador = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (botones[i][j]!= null){
                    if((int)botones[i][j].getTag() == this.viva) {
                        contador++;
                    }
                    if(contador > 1){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public  boolean hasPerdido(){
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (botones[i][j]!=null && (int)botones[i][j].getTag() == this.viva) {
                    if (canMove(i,j)) {
                        return false;
                    }
                }

            }
        }
        return true;
    }
    public void move(int iSeleccionadaPrimera,int jSeleccionadaPrimera,int iSeleccionadaSegunda, int jSeleccionadaSegunda) {
        if ((iSeleccionadaPrimera == iSeleccionadaSegunda && Math.abs(jSeleccionadaPrimera - jSeleccionadaSegunda) == 2) ||
                (jSeleccionadaPrimera == jSeleccionadaSegunda && Math.abs(iSeleccionadaPrimera - iSeleccionadaSegunda) == 2)) {

            int filaIntermedia = (iSeleccionadaPrimera + iSeleccionadaSegunda) / 2;
            int columnaIntermedia = (jSeleccionadaPrimera + jSeleccionadaSegunda) / 2;

            // Verificar si la casilla intermedia NO está vacía
            if ((int)botones[filaIntermedia][columnaIntermedia].getTag() == this.viva) {


                botones[iSeleccionadaPrimera][jSeleccionadaPrimera].setTag(this.vacia);
                botones[iSeleccionadaPrimera][jSeleccionadaPrimera].setImageResource(R.drawable.ficha_vacia);

                botones[iSeleccionadaSegunda][jSeleccionadaSegunda].setTag(this.viva);
                botones[iSeleccionadaSegunda][jSeleccionadaSegunda].setImageResource(R.drawable.ficha);

                botones[filaIntermedia][columnaIntermedia].setTag(this.vacia);
                botones[filaIntermedia][columnaIntermedia].setImageResource(R.drawable.ficha_vacia);

            } else {
                botones[iSeleccionadaPrimera][jSeleccionadaPrimera].setTag(this.viva);
                botones[iSeleccionadaPrimera][jSeleccionadaPrimera].setImageResource(R.drawable.ficha);
            }
        } else {
            botones[iSeleccionadaPrimera][jSeleccionadaPrimera].setTag(this.viva);
            botones[iSeleccionadaPrimera][jSeleccionadaPrimera].setImageResource(R.drawable.ficha);
        }
    }
    public boolean canMove(int row,int col){
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (botones[i][j] != null) {


                    if((int) botones[i][j].getTag() == this.vacia){

                        if ((row == i && Math.abs(col - j) == 2) ||
                                (col == j && Math.abs(row - i) == 2)) {
                            int filaIntermedia = (row + i) / 2;
                            int columnaIntermedia = (col + j) / 2;
                            if ((int)botones[filaIntermedia][columnaIntermedia].getTag() == this.viva) {
                                return true;
                            }
                        }
                    }


                }

            }
        }
        return false;
    }
    private void MensajeGanar() {
        timer.cancel();
        guardarScore();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡HAS GANADO!")
                .setMessage("Has llegado al 2048, si quieres puedes seguir jugando");

        AlertDialog gameOverDialog = builder.create();
        gameOverDialog.show();
    }
    private void MensajePerder() {
        timer.cancel();
        guardarScore();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡HAS PERDIDO!")
                .setMessage("Te has quedado sin movimientos");

        AlertDialog gameOverDialog = builder.create();
        gameOverDialog.show();
    }
    private void guardarEstado(){
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (botones[i][j]!= null){
                    undoBotones[i][j] = Integer.parseInt(botones[i][j].getTag().toString());
                }
            }
        }
        System.out.println("gaurdamos el estado");
    }
    private void volverEstadoAnterior(){
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (botones[i][j]!= null){
                    botones[i][j].setTag(undoBotones[i][j]);
                    if(undoBotones[i][j] == this.viva){
                        botones[i][j].setImageResource(R.drawable.ficha);
                    }else if(undoBotones[i][j] == this.vacia){
                        botones[i][j].setImageResource(R.drawable.ficha_vacia);
                    }else if(undoBotones[i][j] == this.seleccionada){
                        botones[i][j].setImageResource(R.drawable.ficha_selecionada);
                    }

                }
            }
        }
        System.out.println("Volvemos al estado anterior");
    }
    private void iniciarContador(){
        timeEnd = false;
        timer = new CountDownTimer(3000000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeView.setText("" + (millisUntilFinished / 1000));
                timeLeft = millisUntilFinished;
            }
            public void onFinish() {
                timeEnd = true;
                MensajePerder();
            }
        }.start();
    }
    private void guardarScore(){
        SharedPreferences sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);
        int scorePuntuation = Integer.parseInt(score.getText().toString());
        int maxScorePuntuation = Integer.parseInt(maxScore.getText().toString());
        long timeScoreSaved = sharedPreferences.getLong("timeSenku", 0);

        if(timeEnd){
            long time = timeLeft/1000;
                if(scorePuntuation > maxScorePuntuation && time > timeScoreSaved){
                    System.out.println("Guardamos los scores");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("scoreSenku", scorePuntuation);
                    editor.putLong("timeSenku", time);
                    editor.apply();
                }
            }else {
                if(timeScoreSaved == 0){
                    if(scorePuntuation > maxScorePuntuation){
                        System.out.println("Guardamos los scores solo puntiacion");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("scoreSenku", scorePuntuation);
                        editor.apply();
                    }
                }
            }
    }


}
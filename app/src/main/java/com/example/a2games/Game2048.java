package com.example.a2games;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Game2048 extends AppCompatActivity {

    private Button buttons[][];
    private int undoButtons[][];
    private Button resetButton;
    private Button backButton;
    private View panelPrincipal;
    private GridLayout buttonContainer;
    private TextView score;

    private TextView maxScore;
    private enum Direccion {
        IZQUIERDA,
        DERECHA,
        ARRIBA,
        ABAJO
    }
    private int sumado = 0;
    private static final int TableroX = 4;
    private static final int TableroY = 4;
    private static final int MIN_DISTANCE = 50;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);

        //Hacemos todos los Finds necesarios
        panelPrincipal = findViewById(R.id.panelPrincipal);
        buttonContainer = findViewById(R.id.gridLayout);
        resetButton = findViewById(R.id.ResetButton);
        backButton = findViewById(R.id.Back2048);
        score = findViewById(R.id.score);
        maxScore = findViewById(R.id.MaxScore);

        buttons = new Button[TableroX][TableroY];
        undoButtons = new int[TableroX][TableroY];
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score.setText("0");
                iniciarJuego();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(score.getText().toString()) - sumado;
                score.setText(String.valueOf(i));
                volverPosiciones();
                pintarCasillas();
                sumado = 0;

            }
        });

        iniciarJuego();

        for (int i = 0; i < TableroX; i++) {
            for (int j = 0; j < TableroY; j++) {
                buttons[i][j].setClickable(false);
            }
        }

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            private float startX, startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        float endY = event.getY();

                        direccionMovimiento(startX, startY, endX, endY);

                        generarDos();
                        pintarCasillas();

                        if (isLost()) {
                            MensajePerder();
                        }
                        if (isWin()) {
                            MensajeGanar();
                        }

                        break;
                }

                return true;
            }
        };


        panelPrincipal.setOnTouchListener(touchListener);
        buttonContainer.setOnTouchListener(touchListener);



    }
    public void iniciarJuego(){
        SharedPreferences sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);
        int maxValue = sharedPreferences.getInt("score2048", 0);
        maxScore.setText(String.valueOf(maxValue));
        int id = 1;
        String boton;
        for (int i = 0; i < TableroX; i++) {
            for (int j = 0; j < TableroY; j++) {

                boton = "Boton" + id;
                int resID = getResources().getIdentifier(boton, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(false);

                id++;
            }
        }

        int inicioI = (int)(Math.random() * TableroX);
        int inicioJ = (int)(Math.random() * TableroY);
        buttons[inicioI][inicioJ].setText("2");
    }
    public void generarDos(){
        int inicioI = (int)(Math.random() * TableroX);
        int inicioJ = (int)(Math.random() * TableroY);

        for(int i = 0;i<2 && !tableroLleno();i++){
            while(!buttons[inicioI][inicioJ].getText().equals(" ")){
                inicioI = (int)(Math.random() * TableroX);
                inicioJ = (int)(Math.random() * TableroY);
            }

            buttons[inicioI][inicioJ].setText("2");

        }
    }
    public void direccionMovimiento(float startX, float startY, float endX, float endY){
        float X = endX - startX;
        float Y = endY - startY;
        sumado = 0;
        this.guardarPosiciones();
        if ( Math.abs(X) > MIN_DISTANCE && Math.abs(X) > Math.abs(Y)) {
            if (X > 0) {

                moverFichasEnFila(0,Direccion.DERECHA);
                moverFichasEnFila(1,Direccion.DERECHA);
                moverFichasEnFila(2,Direccion.DERECHA);
                moverFichasEnFila(3,Direccion.DERECHA);
            } else {
                if(Math.abs(X) > MIN_DISTANCE){
                    moverFichasEnFila(0,Direccion.IZQUIERDA);
                    moverFichasEnFila(1,Direccion.IZQUIERDA);
                    moverFichasEnFila(2,Direccion.IZQUIERDA);
                    moverFichasEnFila(3,Direccion.IZQUIERDA);
                }

            }
        } else {
            if (Math.abs(Y) > MIN_DISTANCE && Y > 0) {
                moverFichasEnColumna(0,Direccion.ABAJO);
                moverFichasEnColumna(1,Direccion.ABAJO);
                moverFichasEnColumna(2,Direccion.ABAJO);
                moverFichasEnColumna(3,Direccion.ABAJO);
            } else {
                if(Math.abs(Y) > MIN_DISTANCE){
                    moverFichasEnColumna(0,Direccion.ARRIBA);
                    moverFichasEnColumna(1,Direccion.ARRIBA);
                    moverFichasEnColumna(2,Direccion.ARRIBA);
                    moverFichasEnColumna(3,Direccion.ARRIBA);
                }

            }
        }

    }
    private void moverFichasEnColumna(int columna, Direccion direccion) {
        int[] columnaActual = new int[TableroX];
        boolean movimientos = true;
        int sumar = 0;

        for (int i = 0; i < TableroX; i++) {
            if(buttons[i][columna].getText().toString().equals(" ")){
                columnaActual[i] = 0;
            }else{
                columnaActual[i] = Integer.parseInt(buttons[i][columna].getText().toString());
            }
        }

        switch (direccion) {
            case ABAJO:
                for (int i = TableroX - 2; i >= 0; i--) {
                    if (!buttons[i][columna].getText().toString().equals(" ")) {
                        int k = i;
                        while (k < TableroX - 1 && buttons[k + 1][columna].getText().toString().equals(" ")) {
                            buttons[k + 1][columna].setText(buttons[k][columna].getText());
                            buttons[k][columna].setText(" ");
                            k++;
                        }
                    }
                }

                for (int i = TableroX - 1; i > 0; i--) {
                    if (!buttons[i][columna].getText().toString().equals(" ") &&
                            buttons[i][columna].getText().toString().equals(buttons[i - 1][columna].getText().toString())) {
                        int valor = Integer.parseInt(buttons[i][columna].getText().toString()) * 2;
                        sumar = sumar + valor;
                        buttons[i][columna].setText(String.valueOf(valor));
                        buttons[i - 1][columna].setText(" ");
                    }
                }

                for (int i = TableroX - 2; i >= 0; i--) {
                    if (!buttons[i][columna].getText().toString().equals(" ")) {
                        int k = i;
                        while (k < TableroX - 1 && buttons[k + 1][columna].getText().toString().equals(" ")) {
                            buttons[k + 1][columna].setText(buttons[k][columna].getText());
                            buttons[k][columna].setText(" ");
                            k++;
                        }
                    }
                }
                sumado = sumar + sumado;
                System.out.println(sumado);
                sumar = sumar + Integer.parseInt(score.getText().toString());
                score.setText(String.valueOf(sumar));
                break;


            case ARRIBA:
                for (int i = 1; i < TableroX; i++) {
                    if (!buttons[i][columna].getText().toString().equals(" ")) {
                        int k = i;
                        while (k > 0 && buttons[k - 1][columna].getText().toString().equals(" ")) {
                            buttons[k - 1][columna].setText(buttons[k][columna].getText());
                            buttons[k][columna].setText(" ");
                            k--;
                        }
                    }
                }

                for (int i = 0; i < TableroX - 1; i++) {
                    if (!buttons[i][columna].getText().toString().equals(" ") &&
                            buttons[i][columna].getText().toString().equals(buttons[i + 1][columna].getText().toString())) {
                        int valor = Integer.parseInt(buttons[i][columna].getText().toString()) * 2;
                        sumar = sumar + valor;
                        buttons[i][columna].setText(String.valueOf(valor));
                        buttons[i + 1][columna].setText(" ");
                    }
                }


                for (int i = 1; i < TableroX; i++) {
                    if (!buttons[i][columna].getText().toString().equals(" ")) {
                        int k = i;
                        while (k > 0 && buttons[k - 1][columna].getText().toString().equals(" ")) {
                            buttons[k - 1][columna].setText(buttons[k][columna].getText());
                            buttons[k][columna].setText(" ");
                            k--;
                        }
                    }
                }
                sumado = sumar + sumado;
                System.out.println(sumado);
                sumar = sumar + Integer.parseInt(score.getText().toString());
                score.setText(String.valueOf(sumar));
                break;
        }
    }
    private void moverFichasEnFila(int fila, Direccion direccion) {
        int[] filaActual = new int[TableroX];
        boolean movimientos = true;
        int sumar = 0;

        for (int i = 0; i < TableroX; i++) {
            if (buttons[fila][i].getText().toString().equals(" ")) {
                filaActual[i] = 0;
            } else {
                filaActual[i] = Integer.parseInt(buttons[fila][i].getText().toString());
            }
        }
        switch (direccion) {
            case DERECHA:
                for (int j = TableroY - 2; j >= 0; j--) {
                    if (!buttons[fila][j].getText().toString().equals(" ")) {
                        int k = j;
                        while (k < TableroY - 1 && buttons[fila][k + 1].getText().toString().equals(" ")) {
                            buttons[fila][k + 1].setText(buttons[fila][k].getText());
                            buttons[fila][k].setText(" ");
                            k++;
                        }
                    }
                }

                for (int j = TableroY - 1; j > 0; j--) {
                    if (!buttons[fila][j].getText().toString().equals(" ") &&
                            buttons[fila][j].getText().toString().equals(buttons[fila][j - 1].getText().toString())) {
                        int valor = Integer.parseInt(buttons[fila][j].getText().toString()) * 2;
                        sumar = sumar + valor;
                        buttons[fila][j].setText(String.valueOf(valor));
                        buttons[fila][j - 1].setText(" ");
                    }
                }



                for (int j = TableroY - 2; j >= 0; j--) {
                    if (!buttons[fila][j].getText().toString().equals(" ")) {
                        int k = j;
                        while (k < TableroY - 1 && buttons[fila][k + 1].getText().toString().equals(" ")) {
                            buttons[fila][k + 1].setText(buttons[fila][k].getText());
                            buttons[fila][k].setText(" ");
                            k++;
                        }
                    }
                }


                sumado = sumar + sumado;
                System.out.println(sumado);
                sumar = sumar + Integer.parseInt(score.getText().toString());
                score.setText(String.valueOf(sumar));
                break;

            case IZQUIERDA:

                for (int j = 1; j < TableroY; j++) {
                    if (!buttons[fila][j].getText().toString().equals(" ")) {
                        int k = j;
                        while (k > 0 && buttons[fila][k - 1].getText().toString().equals(" ")) {
                            buttons[fila][k - 1].setText(buttons[fila][k].getText());
                            buttons[fila][k].setText(" ");
                            k--;
                        }
                    }
                }

                for (int j = 0; j < TableroY - 1; j++) {
                    if (!buttons[fila][j].getText().toString().equals(" ") &&
                            buttons[fila][j].getText().toString().equals(buttons[fila][j + 1].getText().toString())) {
                        int valor = Integer.parseInt(buttons[fila][j].getText().toString()) * 2;
                        sumar = sumar + valor;
                        buttons[fila][j].setText(String.valueOf(valor));
                        buttons[fila][j + 1].setText(" ");
                    }
                }

                for (int j = 1; j < TableroY; j++) {
                    if (!buttons[fila][j].getText().toString().equals(" ")) {
                        int k = j;
                        while (k > 0 && buttons[fila][k - 1].getText().toString().equals(" ")) {
                            buttons[fila][k - 1].setText(buttons[fila][k].getText());
                            buttons[fila][k].setText(" ");
                            k--;
                        }
                    }
                }
                sumado = sumar + sumado;
                System.out.println(sumado);
                sumar = sumar + Integer.parseInt(score.getText().toString());
                score.setText(String.valueOf(sumar));
                break;
        }
    }

    private void guardarPosiciones(){
        for (int i = 0; i < TableroX; i++) {
            for (int j = 0; j < TableroY; j++) {
                if(buttons[i][j].getText().toString().equals(" ")){
                    undoButtons[i][j] = 0;
                }else{
                    undoButtons[i][j] = Integer.parseInt(buttons[i][j].getText().toString());
                }
            }
        }
    }

    private void volverPosiciones(){
        for (int i = 0; i < TableroX; i++) {
            for (int j = 0; j < TableroY; j++) {

                if(undoButtons[i][j] == 0){
                    buttons[i][j].setText(" ");
                }else{
                    buttons[i][j].setText(String.valueOf(undoButtons[i][j]));
                }

            }
        }
    }

    private boolean isWin(){
        for (int i = 0; i < TableroX; i++) {
            for (int j = 0; j < TableroY; j++) {

                if (buttons[i][j].getText().toString().equals("2048")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLost() {
        for (int i = 0; i < TableroX; i++) {
            for (int j = 0; j < TableroY; j++) {
                String valor = buttons[i][j].getText().toString();

                if (i < TableroX - 1 && (buttons[i + 1][j].getText().toString().equals(" ") || buttons[i + 1][j].getText().toString().equals(valor))) {
                    return false;
                }

                if (j < TableroY - 1 && (buttons[i][j + 1].getText().toString().equals(" ") || buttons[i][j + 1].getText().toString().equals(valor))) {
                    return false;
                }

                if (i > 0 && (buttons[i - 1][j].getText().toString().equals(" ") || buttons[i - 1][j].getText().toString().equals(valor))) {
                    return false;
                }

                if (j > 0 && (buttons[i][j - 1].getText().toString().equals(" ") || buttons[i][j - 1].getText().toString().equals(valor))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean tableroLleno(){
        for (int i = 0; i < TableroX; i++) {
            for (int j = 0; j < TableroY; j++) {
                if (buttons[i][j].getText().toString().equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void MensajeGanar() {
        this.guardarScore();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡HAS GANADO!")
                .setMessage("Has llegado al 2048, si quieres puedes seguir jugando");

        AlertDialog gameOverDialog = builder.create();
        gameOverDialog.show();
    }

    private void MensajePerder() {
        this.guardarScore();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡HAS PERDIDO!")
                .setMessage("Te has quedado sin movimientos");

        AlertDialog gameOverDialog = builder.create();
        gameOverDialog.show();
    }

    private void pintarCasillas(){
        for (int i = 0; i < TableroX; i++) {
            for (int j = 0; j < TableroY; j++) {
                int aux;
                if(buttons[i][j].getText().equals(" ")){
                    aux = 0;
                }else {
                    aux = Integer.parseInt(buttons[i][j].getText().toString());
                }

                switch(aux){
                    case 0:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_0));
                        buttons[i][j].setTextSize(30);
                        break;
                    case 2:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_2));
                        buttons[i][j].setTextSize(30);
                        break;
                    case 4:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_4));
                        buttons[i][j].setTextSize(30);
                        break;
                    case 8:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_8));
                        buttons[i][j].setTextSize(30);
                        break;
                    case 16:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_16));
                        buttons[i][j].setTextSize(30);
                        break;
                    case 32:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_32));
                        buttons[i][j].setTextSize(30);
                        break;
                    case 64:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_64));
                        buttons[i][j].setTextSize(30);
                        break;
                    case 128:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_128));
                        buttons[i][j].setTextSize(22);
                        break;
                    case 256:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_256));
                        buttons[i][j].setTextSize(22);
                        break;
                    case 512:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_512));
                        buttons[i][j].setTextSize(22);
                        break;
                    case 1024:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_1024));
                        buttons[i][j].setTextSize(15);
                        break;
                    case 2048:
                        buttons[i][j].setBackgroundColor(getColor(R.color.color_2048));
                        buttons[i][j].setTextSize(15);
                        break;
                }
            }
        }
    }

    private void guardarScore(){
        int scorePuntuation = Integer.parseInt(score.getText().toString());
        int maxScorePuntuation = Integer.parseInt(maxScore.getText().toString());

        if(scorePuntuation > maxScorePuntuation){
            System.out.println("Guardamos los scores");
            SharedPreferences sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("score2048", scorePuntuation);
            editor.apply();
        }
    }


}
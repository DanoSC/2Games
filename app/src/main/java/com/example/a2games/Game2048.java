package com.example.a2games;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
                sumado = 0;
                //backButton.setVisibility(View.INVISIBLE);
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
                        if (isLost()) {
                            MensajePerder();
                        }
                        if (isWin()) {
                            MensajeGanar();
                        }
                        //guardarPosiciones();
                        backButton.setVisibility(View.VISIBLE);
                        break;
                }

                return true;
            }
        };


        panelPrincipal.setOnTouchListener(touchListener);
        buttonContainer.setOnTouchListener(touchListener);



    }
    public void iniciarJuego(){
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

        while(!buttons[inicioI][inicioJ].getText().equals(" ") && !tableroLleno()){
            inicioI = (int)(Math.random() * TableroX);
            inicioJ = (int)(Math.random() * TableroY);
        }
        if(!tableroLleno()) {
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
                while(movimientos){
                    movimientos = false;
                    for (int i = TableroX-1; i >= 0; i--) {
                        if (columnaActual[i] == 0) {
                            if(i != 0){
                                if(columnaActual[i-1] != 0){
                                    columnaActual[i] = columnaActual[i - 1];
                                    columnaActual[i - 1] = 0;
                                    buttons[i][columna].setText(buttons[i - 1][columna].getText());
                                    buttons[i - 1][columna].setText(" ");
                                    movimientos = true;
                                }
                            }
                        } else {
                            if (!(i == 0)) {
                                if (columnaActual[i] == columnaActual[i-1]) {
                                    int suma = columnaActual[i] + columnaActual[i];
                                    columnaActual[i] = columnaActual[i] + columnaActual[i];
                                    columnaActual[i - 1] = 0;
                                    buttons[i][columna].setText(String.valueOf(suma));
                                    buttons[i - 1][columna].setText(" ");
                                    sumar = sumar + suma;
                                    movimientos = true;
                                }
                            }
                        }
                    }
                }
                sumado = sumar + sumado;
                System.out.println(sumado);
                sumar = sumar + Integer.parseInt(score.getText().toString());
                score.setText(String.valueOf(sumar));
                break;


            case ARRIBA:
                while(movimientos){
                    boolean anteriorVacio = false;
                    movimientos = false;
                    for (int i = 0; i < TableroX; i++) {
                        if (columnaActual[i] == 0) {
                            if(!(i == TableroX-1)){
                                if(columnaActual[i+1] != 0){
                                    columnaActual[i] = columnaActual[i + 1];
                                    columnaActual[i + 1] = 0;
                                    buttons[i][columna].setText(buttons[i + 1][columna].getText());
                                    buttons[i + 1][columna].setText(" ");
                                    movimientos = true;
                                }
                            }
                        } else {
                            if (!(i == TableroX-1)) {
                                if (columnaActual[i] == columnaActual[i+1]) {
                                    int suma = columnaActual[i] + columnaActual[i];
                                    columnaActual[i] = columnaActual[i] + columnaActual[i];
                                    columnaActual[i + 1] = 0;
                                    buttons[i][columna].setText(String.valueOf(suma));
                                    buttons[i + 1][columna].setText(" ");
                                    sumar = sumar + suma;
                                    movimientos = true;
                                }
                            }
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
                while(movimientos){
                    movimientos = false;
                    for (int i = TableroY-1; i >= 0; i--) {
                        if (filaActual[i] == 0) {
                            if(i != 0){
                                if(filaActual[i-1] != 0){
                                    filaActual[i] = filaActual[i - 1];
                                    filaActual[i - 1] = 0;
                                    buttons[fila][i].setText(buttons[fila][i-1].getText());
                                    buttons[fila][i-1].setText(" ");
                                    movimientos = true;
                                }
                            }
                        } else {
                            if (!(i == 0)) {
                                if (filaActual[i] == filaActual[i-1]) {
                                    int suma = filaActual[i] + filaActual[i];
                                    filaActual[i] = filaActual[i] + filaActual[i];
                                    filaActual[i - 1] = 0;
                                    buttons[fila][i].setText(String.valueOf(suma));
                                    buttons[fila][i-1].setText(" ");
                                    sumar = sumar + suma;
                                    movimientos = true;
                                }
                            }
                        }
                    }
                }
                sumado = sumar + sumado;
                System.out.println(sumado);
                sumar = sumar + Integer.parseInt(score.getText().toString());
                score.setText(String.valueOf(sumar));
                break;

            case IZQUIERDA:
                while(movimientos){
                    boolean anteriorVacio = false;
                    movimientos = false;
                    for (int i = 0; i < TableroX; i++) {
                        if (filaActual[i] == 0) {
                            if(!(i == TableroX-1)){
                                if(filaActual[i+1] != 0){
                                    filaActual[i] = filaActual[i + 1];
                                    filaActual[i + 1] = 0;
                                    buttons[fila][i].setText(buttons[fila][i+1].getText());
                                    buttons[fila][i+1].setText(" ");
                                    movimientos = true;
                                }
                            }
                        } else {
                            if (!(i == TableroX-1)) {
                                if (filaActual[i] == filaActual[i+1]) {
                                    int suma = filaActual[i] + filaActual[i];
                                    filaActual[i] = filaActual[i] + filaActual[i];
                                    filaActual[i + 1] = 0;
                                    buttons[fila][i].setText(String.valueOf(suma));
                                    buttons[fila][i+1].setText(" ");
                                    sumar = sumar + suma;
                                    movimientos = true;
                                }
                            }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡HAS GANADO!")
                .setMessage("Has llegado al 2048, si quieres puedes seguir jugando");

        AlertDialog gameOverDialog = builder.create();
        gameOverDialog.show();
    }

    private void MensajePerder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡HAS PERDIDO!")
                .setMessage("Te has quedado sin movimientos");

        AlertDialog gameOverDialog = builder.create();
        gameOverDialog.show();
    }


}
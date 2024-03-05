package com.example.a2games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(MainActivity.this,
                        Login.class));
                MainActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ImageView logo2 =  (ImageView) findViewById(R.id.imagenPantallaPrincipal);
        logo2.startAnimation(animation);


    }


}
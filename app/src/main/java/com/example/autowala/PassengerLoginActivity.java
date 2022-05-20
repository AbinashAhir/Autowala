package com.example.autowala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class PassengerLoginActivity extends AppCompatActivity {

    private Button phoneButton, googleButton;
    Animation phoneAnimate, googleAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        phoneButton = (Button) findViewById(R.id.phoneButton);
        googleButton = (Button) findViewById(R.id.googleButton);

        phoneAnimate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_animation);
        googleAnimate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_animation);

        phoneButton.setAnimation(phoneAnimate);
        googleButton.setAnimation(googleAnimate);

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerLoginActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
//                Animatoo.animateSlideUp(this);
            }
        });
    }

//    public void phoneLoginClick(View view) {
//        Intent intent = new Intent(PassengerLoginActivity.this, PhoneLoginActivity.class);
//        startActivity(intent);
//        Animatoo.animateSlideUp(this);
//    }
}
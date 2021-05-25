package com.prasoonsoni.vidcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {
    TextView introText;
    LottieAnimationView introLottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        introText = findViewById(R.id.textView);
        introLottie = findViewById(R.id.introAnimation);

        introLottie.animate().translationYBy(-2000f).setDuration(1000);
        introText.animate().translationYBy(2000f).setDuration(1000);

        new Handler().postDelayed(() -> {
            introLottie.animate().translationYBy(2000f).setDuration(1000);
            introText.animate().translationYBy(-2000f).setDuration(1000);
        },3500);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        },4100);
    }
}
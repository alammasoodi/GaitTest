package com.example.alam.gaittest;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView timerView;
    private int timerCount = 5;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        context=ProgressBarActivity.this;
        progressBar=(ProgressBar) findViewById(R.id.progress_bar);
        timerView = (TextView) findViewById(R.id.timer_view);
        progressBar.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.pvst_buttonColor), android.graphics.PorterDuff.Mode.SRC_IN);
        Log.i("timing in oncreate", String.valueOf(System.currentTimeMillis()) +"  " +timerCount);

        startingProgressBar();
    }

    public void startingProgressBar() {
        new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("timing", String.valueOf(System.currentTimeMillis()) +"  " +timerCount);
                timerView.setText(String.valueOf(timerCount));
                startAnimator(progressBar, 5000);
                timerCount--;

            }

            @Override
            public void onFinish() {
                Log.i("timing in finish", String.valueOf(System.currentTimeMillis()));
                startGaitTestActivity();
            }

        }.start();
    }

    public void startAnimator(ProgressBar progressBar, int duration) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), 700);
        progressAnimator.setDuration(duration);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }
    public void startGaitTestActivity(){
       startActivity( new Intent(this,GaitTest.class));
    }
}

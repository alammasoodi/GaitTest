package com.example.alam.gaittest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.TextView;

public class GaitTest extends Activity implements SensorEventListener {

    private long timestamp;

    private TextView textViewStepCounter;

    private TextView textViewStepDetector;

    private TextView guideLine;

    private Thread detectorTimeStampUpdaterThread;
    private Sensor mStepCounterSensor;

    private Handler handler;
    float stepNo = 0;
    float lastStep = 0;

    private boolean isRunning = true;
    private boolean isMovingForward = true;
    private SensorManager sensorManager;
    private  long startingTime;
    private long runTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gait_test);

        textViewStepCounter = (TextView) findViewById(R.id.textView2);
        guideLine = (TextView) findViewById(R.id.guideline);
        //textViewStepDetector = (TextView) findViewById(R.id.textView4);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        registerListener();


        //setupDetectorTimestampUpdaterThread();
    }


    private void setupDetectorTimestampUpdaterThread() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                textViewStepDetector.setText(DateUtils
                        .getRelativeTimeSpanString(timestamp));
            }
        };

//        detectorTimeStampUpdaterThread = new Thread() {
//            @Override
//            public void run() {
//                while (isRunning) {
//                    try {
//                        Thread.sleep(5000);
//                        handler.sendEmptyMessage(0);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//
//        detectorTimeStampUpdaterThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        detectorTimeStampUpdaterThread.interrupt();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            calculateSteps(sensorEvent);

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        if (sensor == mStepCounterSensor) {
            Log.i("cs", "dd");

        }

    }

    public void registerListener() {
        sensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        startingTime = System.currentTimeMillis();
        Log.i("startingtime", "" +startingTime);
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);

    }

    public void calculateSteps(SensorEvent sensorEvent) {

        float steps = sensorEvent.values[0];
        if (stepNo < 20) {
            if (lastStep != steps && lastStep != 0) {

                stepNo++;
            }
            lastStep = steps;
            Log.i("step count", "" + steps);
            textViewStepCounter.setText((int) stepNo + "");
            if (stepNo == 19) {

                if (isMovingForward) {
                    guideLine.setText("Turn around, and walk back to where you started");
                    stepNo = 0;
                    isMovingForward = false;
                } else {
                    runTime=System.currentTimeMillis()-startingTime;
                    Log.i("run time", "" +runTime);
                    guideLine.setText("Stand Still for thirty seconds");
                    unregisterListener();
                    setTimer(30000, 1000);

                }
            }
        } else {


        }


    }

    public void setTimer(long millisInFuture, long countDownInterval) {
        new CountDownTimer(millisInFuture, countDownInterval) {

            public void onTick(long millisUntilFinished) {
                textViewStepCounter.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textViewStepCounter.setText("done! and run time is" +runTime);
            }
        }.start();

    }
}
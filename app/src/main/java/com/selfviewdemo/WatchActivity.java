package com.selfviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import selfview.com.selfview.JVWatchView;


public class WatchActivity extends AppCompatActivity {
    private JVWatchView jv_watchview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jv_watch);
        jv_watchview = (JVWatchView) findViewById(R.id.jv_watchview);
        startTime();
    }
    private void startTime(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    jv_watchview.setCurrentTime(System.currentTimeMillis());
                    Log.i("WatchActivity-Millis",""+System.currentTimeMillis()%1000);
                    Log.i("WatchActivity-Millis-6-",""+((float)(System.currentTimeMillis()%1000)/1000*6));
                    try {
                        Thread.sleep(1000/6);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                jv_watchview.setCurrentTime(System.currentTimeMillis());
//                Log.i("WatchActivity-Millis",""+System.currentTimeMillis()%1000);
//                Log.i("WatchActivity-Millis-6-",""+((System.currentTimeMillis()%1000)/100*6));
            }
        }, 1000 / 6);
    }
}

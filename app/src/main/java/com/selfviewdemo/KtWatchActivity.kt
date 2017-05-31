package com.selfviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_kt_watch.*

class KtWatchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kt_watch)

        startTime()

    }
    fun startTime(): Unit{
        Thread {
            while (true) {
                kt_watchview.setCurrentTime(System.currentTimeMillis())
                Log.i("WatchActivity-Millis",""+System.currentTimeMillis()%1000)
                Log.i("WatchActivity-Millis-6-",""+((System.currentTimeMillis()%1000)/1000f*6))
                try {
                    Thread.sleep(1000/6)
                }
                catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }
}

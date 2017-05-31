package com.selfviewdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class KtMainActivity : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        java_watchview.setOnClickListener(this)
        kotlin_watchview.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.java_watchview -> {
                startActivity(Intent(KtMainActivity@this,WatchActivity::class.java))
//                startActivity(Intent(KtMainActivity@this,WatchActivity().javaClass))
            }
            R.id.kotlin_watchview -> {
                startActivity(Intent(KtMainActivity@this,KtWatchActivity().javaClass))
            }
            else -> {
                //default
            }
        }
    }

}

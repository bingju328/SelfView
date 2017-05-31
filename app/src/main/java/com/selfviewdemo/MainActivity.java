package com.selfviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView java_watchview;
    private TextView kotlin_watchview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        java_watchview = (TextView) findViewById(R.id.java_watchview);
        kotlin_watchview = (TextView) findViewById(R.id.kotlin_watchview);
        java_watchview.setOnClickListener(this);
        kotlin_watchview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.java_watchview:
                startActivity(new Intent(MainActivity.this,WatchActivity.class));
                break;
            case R.id.kotlin_watchview:
                startActivity(new Intent(MainActivity.this,KtWatchActivity.class));
                break;
            default:
        }
    }
}

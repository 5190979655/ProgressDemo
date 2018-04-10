package com.demo.progressdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ProgressViewTest progress;
    private int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = findViewById(R.id.progress);

        progress.setMaxCount(100);




    }

    public void noStart(View view) {
        progress.noStart();
    }

    public void start(View view) {
        a++;
        progress.start(a);
    }

    public void pause(View view) {
        progress.pause();
    }

    public void finish(View view) {
        progress.finish();
    }
}

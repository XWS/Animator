package com.zhaoh.com.animator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        showNumberAnimator();
//        showViewAnimator();

        startThread();
        for (int i = 0; i < 5; i++) {
            thread.start();
        }

        thread.start();

    }

    private void startThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("PING", "aa" + Thread.currentThread().getId());
            }
        });
    }

    private void showViewAnimator() {

        TextView tv = (TextView) findViewById(R.id.tv);

        ObjectAnimator.ofFloat(tv, "alpha", 1f, 0f, 1f)
                .setDuration(50000)
                .start();
    }

    private void showNumberAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(10000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                Log.i("PING", animatedValue + "");
            }
        });
        valueAnimator.start();
    }
}

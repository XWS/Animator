package com.zhaoh.com.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by 赵桓 on 2017/5/11.
 */

public class TestAnimatior extends AppCompatActivity {

    private ValueAnimator valueAnimator;
    private ImageView goodView;
    private ImageView catView;
    private ConstraintLayout layout;
    private float[] measureLocation = new float[2];

    private float startX;   //商品开始掉落x坐标
    private float startY;   //商品开始掉落y坐标
    private float endX;     //商品结束掉落x坐标
    private float endY;     //商品结束掉落y坐标

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_animator);
    }

    @Override
    protected void onResume() {
        super.onResume();
        goodView = (ImageView) findViewById(R.id.good);
        catView = (ImageView) findViewById(R.id.cart);
        layout = (ConstraintLayout) findViewById(R.id.contain);
        final ImageView imageView = (ImageView) findViewById(R.id.goods2);
        findViewById(R.id.addToCart2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init(imageView, catView);
            }
        });
        findViewById(R.id.addToCat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init(goodView, catView);
            }
        });
    }

    private void init(View startView, final View endView) {

        final ImageView view = new ImageView(this);
        view.setImageDrawable(goodView.getDrawable());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(100, 100);
        layout.addView(view, params);

        int containLocation[] = new int[2];
        layout.getLocationInWindow(containLocation);

        int goodLocation[] = new int[2];
        startView.getLocationInWindow(goodLocation);
        startX = goodLocation[0] - containLocation[0] + view.getWidth() / 2;
        startY = goodLocation[1] - containLocation[1] + view.getHeight() / 2;

        int cartLocation[] = new int[2];
        endView.getLocationInWindow(cartLocation);
        endX = cartLocation[0] - containLocation[0] + view.getWidth() / 2;
        endY = cartLocation[1] - containLocation[1];

        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo((endX + startX) / 2, startY, endX, endY);
        final PathMeasure measure = new PathMeasure(path, false);

        valueAnimator = ValueAnimator.ofFloat(0f, measure.getLength())
                .setDuration(500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue(); //移动过程的曲线的长度
                measure.getPosTan(value, measureLocation, null);
                view.setTranslationX(measureLocation[0]);
                view.setTranslationY(measureLocation[1]);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layout.removeView(view);
                ObjectAnimator.ofFloat(endView, "scaleX", 1.0f, 1.2f, 1.0f).setDuration(300).start();
            }
        });
        valueAnimator.start();
    }
}

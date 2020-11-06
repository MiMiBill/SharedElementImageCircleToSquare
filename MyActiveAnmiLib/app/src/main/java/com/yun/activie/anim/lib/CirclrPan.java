package com.yun.activie.anim.lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class CirclrPan extends FrameLayout {

    private ImageView mCenterImg;

    public CirclrPan(Context context) {
        super(context);
        init();
    }

    public CirclrPan(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CirclrPan(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();


    }

    private void init(){
//        setBackgroundResource(R.mipmap.pan_circle);
        setBackgroundColor(Color.RED);
        mCenterImg = new ImageView(getContext());
        mCenterImg.setBackgroundColor(Color.GREEN);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(100,100,Gravity.CENTER);
        addView(mCenterImg,layoutParams);
        startRotate();
    }

    public void startRotate() {

        ObjectAnimator anim = ObjectAnimator.ofFloat(mCenterImg, "rotation", 0, 360);
        // 动画的持续时间，执行多久？
        anim.setDuration(1800);
        anim.setRepeatCount(-1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });
        final float[] f = {0};
        anim.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float t) {
//                float f1 = ( float ) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
//                Log.e("HHHHHHHh", "" + t + "     " + (f[0] - f1));
                f[0] = ( float ) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
                return f[0];
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //当旋转结束的时候回调给调用者当前所选择的内容

            }
        });
        // 正式开始启动执行动画
        anim.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
    }
}

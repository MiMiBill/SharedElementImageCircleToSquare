package com.yun.activie.anim.lib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yun.activie.anim.shared.SharedActivity;

import customactivitytransition.TransitionMainActivity;

public class MainActivity extends AppCompatActivity {


    public static final  String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1604637205135&di=0974fd1f7b08da176d9db0e6a51b63f0&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F56f8f4384c6f7.jpg";

    private View iv_circle_pan_center;
    private ImageView imageView;
    private View name;
    private ViewGroup content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_circle_pan_center = findViewById(R.id.iv_circle_pan_center);
        content = findViewById(R.id.mycontent);

        startRotate(iv_circle_pan_center);

        imageView = findViewById(R.id.head);
        name = findViewById(R.id.name);
        Glide.with(this).load(url).circleCrop().into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startChangeBounds();
//                startChangeClipBounds();
//                startTransitionActivity();
                startSharedView();

            }
        });


    }

    private void startTransitionActivity(){
        startActivity(new Intent(this, TransitionMainActivity.class));
    }



    private void startSharedView(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SharedActivity.class);

        Pair<View,String> headP = new Pair<>((View) imageView, ViewCompat.getTransitionName(imageView));
        Pair<View,String>  nameP = new Pair<>(name, ViewCompat.getTransitionName(name));
        ActivityOptionsCompat  activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, headP, nameP);
        MainActivity.this.startActivity(intent,activityOptions.toBundle());
    }


    /**
     * 此处开始同一个页面场景的切换，ChangeBounds 当 View 的位置或者大小发生变化时触发对应的转场效果
     */
    private void startChangeBounds(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setInterpolator(new AnticipateInterpolator());
            TransitionManager.beginDelayedTransition(content, changeBounds);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
            if (layoutParams.leftMargin > 400) {
                layoutParams.leftMargin = 50;
            } else {
                layoutParams.leftMargin = 500;
            }
            imageView.setLayoutParams(layoutParams);

        }


    }

    private void startChangeClipBounds(){
        ChangeClipBounds transition = new ChangeClipBounds();
        transition.setInterpolator(new BounceInterpolator());
        TransitionManager.beginDelayedTransition(content, transition);
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        int gap = 140;
        Rect rect = new Rect(0, gap, width, height - gap);
        if (rect.equals(imageView.getClipBounds())) {
            imageView.setClipBounds(null);
        } else {
            imageView.setClipBounds(rect);
        }

    }



    public void startRotate(View view) {

        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation", 0, 720);
        // 动画的持续时间，执行多久？
        anim.setDuration(1500);
        anim.setRepeatCount(-1);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });
//        final float[] f = {0};
//        anim.setInterpolator(new TimeInterpolator() {
//            @Override
//            public float getInterpolation(float t) {
////                float f1 = ( float ) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
////                Log.e("HHHHHHHh", "" + t + "     " + (f[0] - f1));
//                f[0] = ( float ) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
//                return f[0];
//            }
//        });
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
}
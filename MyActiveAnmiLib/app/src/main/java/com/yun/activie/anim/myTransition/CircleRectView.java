package com.yun.activie.anim.myTransition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * annotation动画再执行过程中会放大或则和缩小view的大小，这个时候就可以在ImageView内部处理一些问题啦
 */
@SuppressLint("AppCompatCustomView")
public class CircleRectView extends ImageView {

    private int circleRadius  = 1500;
    private float cornerRadius = 1300;

    private RectF bitmapRect;
    private Path clipPath;

    public CircleRectView(Context context) {
        super(context);
        init();
    }

    public CircleRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
//                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            setLayerType(LAYER_TYPE_SOFTWARE, null);
//        }

//        if (a.hasValue(R.styleable.styleable.CircleRectView_circleRadius)) {
//            circleRadius = a.getDimensionPixelSize(R.styleable.CircleRectView_circleRadius, 0);
//            cornerRadius = circleRadius;
//        }
        clipPath = new Path();
//        a.recycle();
    }

    public Animator animator(int startHeight, int startWidth, int endHeight, int endWidth) {
        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator heightAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        ValueAnimator widthAnimator = ValueAnimator.ofInt(startWidth, endWidth);

        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = val;

                setLayoutParams(layoutParams);
                requestLayoutSupport();
            }
        });

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = val;

                setLayoutParams(layoutParams);
                requestLayoutSupport();
            }
        });

        ValueAnimator radiusAnimator;
        if (startWidth < endWidth) {
            radiusAnimator = ValueAnimator.ofFloat(circleRadius, 0);
        } else {
            radiusAnimator = ValueAnimator.ofFloat(cornerRadius, circleRadius);
        }

        radiusAnimator.setInterpolator(new AccelerateInterpolator());
        radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cornerRadius = (float) (Float) animation.getAnimatedValue();
            }
        });

        animatorSet.playTogether(heightAnimator, widthAnimator, radiusAnimator);

        return animatorSet;
    }

    /**
     * this needed because of that somehow {@link #onSizeChanged} NOT CALLED when requestLayout while activity transition end is running
     */
    private void requestLayoutSupport() {
        View parent = (View) getParent();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.EXACTLY);
        parent.measure(widthSpec, heightSpec);
        parent.layout(parent.getLeft(), parent.getTop(), parent.getRight(), parent.getBottom());
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //This event-method provides the real dimensions of this custom view.

        Log.d("size changed", "w = " + w + " h = " + h);

        bitmapRect = new RectF(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        clipPath.reset();
        clipPath.addRoundRect(bitmapRect, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

}
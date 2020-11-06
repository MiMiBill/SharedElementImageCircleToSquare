package com.yun.activie.anim.myTransition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import customactivitytransition.transition.ChangePosition;
import customactivitytransition.transition.ShareElemEnterRevealTransition;


@TargetApi(Build.VERSION_CODES.KITKAT)
public class CircleToRectTransition extends Transition {
    private static final String TAG = CircleToRectTransition.class.getSimpleName();
    private static final String Start_BOUNDS = "Bounds";
    private static final String End_BOUNDS = "Bounds";
    private static final String[] PROPS = {Start_BOUNDS,End_BOUNDS};

    @Override
    public String[] getTransitionProperties() {
        return PROPS;
    }

    private void captureValues(String keyBOUNDS,TransitionValues transitionValues) {
        View view = transitionValues.view;
        Rect bounds = new Rect();
        bounds.left = view.getLeft();
        bounds.right = view.getRight();
        bounds.top = view.getTop();
        bounds.bottom = view.getBottom();
        transitionValues.values.put(keyBOUNDS, bounds);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(Start_BOUNDS,transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(End_BOUNDS,transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (endValues == null || startValues == null) {
            return null;
        }

        if (!(startValues.view instanceof CircleRectView)) {
            Log.w(CircleToRectTransition.class.getSimpleName(), "transition view should be CircleRectView");
            return null;
        }

        CircleRectView view = (CircleRectView) (startValues.view);

        Rect startRect = (Rect) startValues.values.get(Start_BOUNDS);
        final Rect endRect = (Rect) endValues.values.get(End_BOUNDS);

        Animator animator;

        //scale animator
        animator = view.animator(startRect.height(), startRect.width(), endRect.height(), endRect.width());

        //movement animators below
        //if some translation not performed fully, use it instead of start coordinate
        float startX = startRect.left + view.getTranslationX();
        float startY = startRect.top + view.getTranslationY();

        //somehow end rect returns needed value minus translation in case not finished transition available
        float moveXTo = endRect.left + Math.round(view.getTranslationX());
        float moveYTo = endRect.top + Math.round(view.getTranslationY());

        Animator moveXAnimator = ObjectAnimator.ofFloat(view, "x", startX, moveXTo);
        Animator moveYAnimator = ObjectAnimator.ofFloat(view, "y", startY, moveYTo);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, moveXAnimator, moveYAnimator);
//        prevent blinking when interrupt animation
        return new ShareElemEnterRevealTransition.NoPauseAnimator(animatorSet);

//        final View endView = endValues.view;
//
//        Path changePosPath = getPathMotion().getPath(startRect.centerX(), startRect.centerY(), endRect.centerX(), endRect.centerY());
//
//        int radius = startRect.centerY() - endRect.centerY();
//
//        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(view, new ChangePosition.PropPosition(PointF.class, "position", new PointF(endRect.centerX(), endRect.centerY())), null, changePosPath);
//        objectAnimator.setInterpolator(new FastOutSlowInInterpolator());
//        return new ShareElemEnterRevealTransition.NoPauseAnimator(objectAnimator);

    }

}
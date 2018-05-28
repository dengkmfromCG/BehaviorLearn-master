package com.gdut.dkmfromcg.behaviorlearn.behavior;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import java.util.logging.Logger;


/**
 * Created by dkmFromCG on 2018/5/20.
 * function:
 */

public class FabBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = "FabBehavior";

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private float viewY;//控件距离coordinatorLayout底部距离
    private boolean isAnimate = false;//动画是否在进行

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //在嵌套滑动开始前回调
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        if (child.getVisibility() == View.VISIBLE && viewY == 0) {
            //获取控件距离父布局（coordinatorLayout）底部距离
            viewY = coordinatorLayout.getHeight() - child.getY();
        }
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;//判断是否竖直滚动
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //Log.d(TAG, "onNestedScroll");
        //        if (dyConsumed > 0 && dyUnconsumed == 0) {
//            System.out.println("上滑中。。。");
//        }
//        if (dyConsumed == 0 && dyUnconsumed > 0) {
//            System.out.println("到边界了还在上滑。。。");
//        }
//        if (dyConsumed < 0 && dyUnconsumed == 0) {
//            System.out.println("下滑中。。。");
//        }
//        if (dyConsumed == 0 && dyUnconsumed < 0) {
//            System.out.println("到边界了，还在下滑。。。");
//        }
        if ((dyConsumed > 0 || dyUnconsumed > 0) && !isAnimate && child.getVisibility() == View.VISIBLE) {
            //上滑
            hideFab(child);
        } else if ((dyConsumed < 0 || dyUnconsumed < 0) && !isAnimate && child.getVisibility() != View.VISIBLE) {
            //下滑
            showFab(child);
        }
    }


    private void showFab(final View view) {
        view.setVisibility(View.VISIBLE);
        ViewPropertyAnimatorListener viewPropertyAnimatorListener = new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(View view) {
                isAnimate = false;
            }

            @Override
            public void onAnimationCancel(View view) {
                hideFab(view);
            }
        };
        ViewCompat.animate(view)
                .translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setDuration(200)
                .setListener(viewPropertyAnimatorListener)
                .start();
    }

    private void hideFab(final View view) {
        ViewPropertyAnimatorListener viewPropertyAnimatorListener = new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(View view) {
                isAnimate = false;
                view.setVisibility(View.INVISIBLE);
                //设置成 view.setVisibility(View.GONE);之后界面就检测不到FabBehavior了
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        };
        ViewCompat.animate(view)
                .translationY(viewY)
                .setInterpolator(INTERPOLATOR)
                .setDuration(200)
                .setListener(viewPropertyAnimatorListener)
                .start();
    }

}



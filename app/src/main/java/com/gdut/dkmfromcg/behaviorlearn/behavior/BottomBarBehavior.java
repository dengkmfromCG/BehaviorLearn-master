package com.gdut.dkmfromcg.behaviorlearn.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dkmFromCG on 2018/5/20.
 * function:
 */

public class BottomBarBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = "BottomBarBehavior";

    private boolean isFirstMeasure = true;
    private float dependencyHeight;
    private float childHeight;

    public BottomBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //确定所提供的子视图是否有另一个特定的同级视图作为布局从属。
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        //这个方法是说明这个子控件是依赖AppBarLayout的
        return dependency instanceof AppBarLayout;
    }

    //用于响应从属布局的变化
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (isFirstMeasure) {
            isFirstMeasure = false;
            dependencyHeight = dependency.getMeasuredHeight();//dependency的高度
            childHeight = child.getMeasuredHeight();
        }

        //按照比例获得 child移动的距离
        float dependencyTranslationTop = Math.abs(dependency.getTop());
        float translationY = dependencyTranslationTop * childHeight / dependencyHeight;//获取更随布局的顶部位置
        //Log.d(TAG, String.valueOf(translationY));
        child.setTranslationY(translationY);
        return true;
    }

}


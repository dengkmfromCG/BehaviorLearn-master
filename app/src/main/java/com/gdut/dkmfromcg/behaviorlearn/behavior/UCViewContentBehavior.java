package com.gdut.dkmfromcg.behaviorlearn.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.gdut.dkmfromcg.behaviorlearn.R;
import com.gdut.dkmfromcg.behaviorlearn.behavior.android.HeaderScrollingViewBehavior;
import com.gdut.dkmfromcg.behaviorlearn.widget.CustomViewPager;

import java.util.List;

/**
 * Created by ylhu on 17-2-22.
 */
public class UCViewContentBehavior extends HeaderScrollingViewBehavior {
    private static final String TAG = "UCViewContentBehavior";

    private int mTitleViewHeight = 0;
    private int mTabViewHeight = 0;

    private UCViewHeaderBehavior headerBehavior = null;

    public UCViewContentBehavior() {

        super();
    }

    public UCViewContentBehavior(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {

        return isDependency(dependency);
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {

        mTitleViewHeight = parent.findViewById(R.id.uc_title_layout).getMeasuredHeight();
        mTabViewHeight = parent.findViewById(R.id.uc_tab_layout).getMeasuredHeight();
        headerBehavior = (UCViewHeaderBehavior) ((CoordinatorLayout.LayoutParams) parent.findViewById(R.id.uc_header_layout).getLayoutParams()).getBehavior();
        super.layoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        int headerOffsetRange = -mTitleViewHeight;
        //因为UCViewContent与依赖UCViewHeader为同向滑动
        //所以UCViewHeader向上滑即translationY为负数时，UCViewContent也向上滑其translationY也为负数
        //所以UCViewHeader向上滑即translationY为正数时，UCViewContent也向上滑其translationY也为正数
        //而headerOffsetRange为负数，getScrollRange(dependency)为正数，所以最前面要加上一个负号

        //计算方式与UCViewTab的计算方式一样
        child.setTranslationY(-dependency.getTranslationY() / (headerOffsetRange * 1.0f) * getScrollRange(dependency));
        return false;
    }


    private boolean mCurrentState = false;
    private boolean isFirst = true;

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        if (headerBehavior == null) {
            headerBehavior = (UCViewHeaderBehavior) ((CoordinatorLayout.LayoutParams) parent.findViewById(R.id.uc_header_layout).getLayoutParams()).getBehavior();
        }
        //header closed时，ViewPager可以左右滑动
        boolean canHorizontalScroll = headerBehavior != null && headerBehavior.isClosed();
        if (child instanceof CustomViewPager) {
            if (isFirst) {
                isFirst = false;
                mCurrentState = canHorizontalScroll;
                ((CustomViewPager) child).setScanScroll(canHorizontalScroll);
            } else if (canHorizontalScroll != mCurrentState) {
                mCurrentState = canHorizontalScroll;
                ((CustomViewPager) child).setScanScroll(canHorizontalScroll);
            }
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    protected int getScrollRange(View dependency) {

        if (isDependency(dependency)) {
            //UCViewHeader的高度，减去UCViewTab和UCViewTitle的高度就是UCViewContent要滑动的高度
            return dependency.getMeasuredHeight() - mTitleViewHeight - mTabViewHeight;
        }
        return super.getScrollRange(dependency);
    }

    @Override
    public View findFirstDependency(List<View> views) {

        for (int i = 0; i < views.size(); i++) {
            if (isDependency(views.get(i))) {
                Log.d(TAG, views.get(i).toString());
                return views.get(i);
            }
        }
        return null;
    }

    private boolean isDependency(View dependency) {

        return dependency != null && dependency.getId() == R.id.uc_header_layout;
    }
}

package com.gdut.dkmfromcg.behaviorlearn.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gdut.dkmfromcg.behaviorlearn.R;
import com.gdut.dkmfromcg.behaviorlearn.behavior.android.HeaderScrollingViewBehavior;
import com.gdut.dkmfromcg.behaviorlearn.behavior.android.ViewOffsetBehavior;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Created by dkmFromCG on 2018/5/21.
 * function:
 */

@CoordinatorLayout.DefaultBehavior(ParallaxRelativeLayout.Behavior.class)
public class ParallaxRelativeLayout extends RelativeLayout {

    private static final float OFFSET_SCALE = 3.0f;
    private boolean isScrimInit = false;
    private FrameLayout mScrim = null;


    public ParallaxRelativeLayout(Context context) {
        this(context, null);
    }

    public ParallaxRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isScrimInit) {
            isScrimInit = true;
            mScrim = new FrameLayout(getContext());
            mScrim.setBackgroundResource(R.color.scrim_bg);
            RelativeLayout.LayoutParams lp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            int childCount = getChildCount();
            addViewInLayout(mScrim, childCount, lp);
            mScrim.setVisibility(View.GONE);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LayoutParams(lp);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public FrameLayout getScrim() {
        return mScrim;
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {

        @RestrictTo(LIBRARY_GROUP)
        @IntDef({
                PARALLAX_SPEED_SLOW,
                PARALLAX_SPEED_NORMAL,
                PARALLAX_SPEED_FAST
        })
        @Retention(RetentionPolicy.SOURCE)
        @interface ParallaxSpeed {
        }

        public static final int PARALLAX_SPEED_SLOW = 0;

        public static final int PARALLAX_SPEED_NORMAL = 1;

        public static final int PARALLAX_SPEED_FAST = 2;

        int mParallaxSpeed = PARALLAX_SPEED_NORMAL;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.ParallaxRelativeLayout_Layout);
            mParallaxSpeed = array.getInt(R.styleable.ParallaxRelativeLayout_Layout_parallax_speed, PARALLAX_SPEED_NORMAL);
            array.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
        }

        public void setParallaxSpeed(@ParallaxSpeed int parallaxSpeed) {
            mParallaxSpeed = parallaxSpeed;
        }

        public int getParallaxSpeed() {
            return mParallaxSpeed;
        }
    }

    public static class Behavior extends ViewOffsetBehavior<ParallaxRelativeLayout> {

        private static final String TAG = "Behavior";

        private final int DISPLAY_SCRIM = 0;
        private final int HIDE_SCRIM = 1;
        private int currentState = HIDE_SCRIM;
        private float mOffsetRange = 0;

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }


        @Override
        protected void layoutChild(CoordinatorLayout parent, ParallaxRelativeLayout child, int layoutDirection) {
            super.layoutChild(parent, child, layoutDirection);
            mOffsetRange = -child.getMeasuredHeight() / OFFSET_SCALE; //只能向上滑1/3的高度

        }

        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ParallaxRelativeLayout child, @NonNull View directTargetChild, @NonNull View target, int nestedScrollAxes, int type) {
            //开始滑动的条件，垂直方向滑动，滑动未结束
            return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && canScroll(child, 0);
        }

        @Override
        public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ParallaxRelativeLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
            //super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
            //dy>0 向上滑
            //dy<0 向下滑
            float halfOfDis = dy / OFFSET_SCALE;
            if (!canScroll(child, halfOfDis)) {//滑动结束
                if (halfOfDis > 0) {//向上滑结束
                    child.setVisibility(View.INVISIBLE);//滑动结束后，隐藏此视图
                    child.setTranslationY(mOffsetRange);
                } else {//向下滑结束
                    child.setTranslationY(0);
                }
            } else {//滑动未结束
                if (halfOfDis <= 0) {
                    child.setVisibility(View.VISIBLE);
                }
                //滑动
                float childTranslationY = child.getTranslationY() - halfOfDis;
                child.setTranslationY(childTranslationY);

                for (int i = 0, z = child.getChildCount(); i < z; i++) {
                    View view = child.getChildAt(i);
                    float viewTranslateY = 0f;
                    LayoutParams lp = (LayoutParams) view.getLayoutParams();
                    int parallaxSpeed = lp.getParallaxSpeed();
                    switch (parallaxSpeed) {
                        case 0:
                            viewTranslateY = childTranslationY * 0.5f;
                            break;
                        case 1:
                            viewTranslateY = childTranslationY * 1f;
                            break;
                        case 2:
                            viewTranslateY = childTranslationY * 2f;
                            break;
                        default:
                            break;
                    }
                    view.setTranslationY(viewTranslateY);
                }

                //消耗掉当前垂直方向上的滑动距离
                consumed[1] = dy;
            }
        }

        /**
         * 当前是否可以滑动
         *
         * @param child
         * @param pendingDy Y轴方向滑动的translationY
         * @return
         */
        private boolean canScroll(View child, float pendingDy) {

            int pendingTranslationY = (int) (child.getTranslationY() - pendingDy);
            if (pendingTranslationY >= mOffsetRange && pendingTranslationY <= 0) {
                return true;
            }
            return false;
        }


        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, ParallaxRelativeLayout child, MotionEvent ev) {
            if (child.getTranslationY() < mOffsetRange / 2) {
                if (currentState != DISPLAY_SCRIM) {
                    currentState = DISPLAY_SCRIM;
                    //超过一半时，显示蒙版
                    child.getScrim().setVisibility(View.VISIBLE);
                    Log.d(TAG, "显示蒙版");
                }
            } else {
                //隐藏蒙版
                if (currentState != HIDE_SCRIM) {
                    currentState = HIDE_SCRIM;
                    child.getScrim().setVisibility(View.GONE);
                    Log.d(TAG, "隐藏蒙版");
                }

            }
            return super.onInterceptTouchEvent(parent, child, ev);
        }
    }

    public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {

        private static final String TAG = "ScrollingViewBehavior";

        public ScrollingViewBehavior() {
        }

        public ScrollingViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return isDependency(dependency);
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
            float offsetY = dependency.getTranslationY() * OFFSET_SCALE;
            child.setTranslationY(offsetY);
            return false;
        }

        @Override
        public View findFirstDependency(List<View> views) {

            for (int i = 0; i < views.size(); i++) {
                if (isDependency(views.get(i))) {
                    return views.get(i);
                }
            }
            return null;
        }

        private boolean isDependency(View dependency) {
            return dependency != null && dependency instanceof ParallaxRelativeLayout;
        }
    }

}

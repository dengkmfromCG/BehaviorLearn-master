package com.gdut.dkmfromcg.behaviorlearn.util;

import android.app.Activity;
import android.graphics.Rect;

/**
 * Created by dkmFromCG on 2018/5/21.
 * function:
 */

public class Utils {

    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        if (statusBarHeight <= 0) { // 有时会获取失败
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
            }
        }

        if (statusBarHeight <= 0) {
            statusBarHeight = 63;
        }

        return statusBarHeight;
    }
}

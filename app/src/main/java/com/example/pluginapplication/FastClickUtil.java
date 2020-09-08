package com.example.pluginapplication;

import android.util.Log;

public class FastClickUtil {
    private static final int FAST_CLICK_TIME_DISTANCE = 300;
    private static long sLastClickTime = 0;
    public static int sLastClickViewId = 0;

    public static boolean isFastDoubleClick(int viewId) {
        if (sLastClickViewId == viewId) {
            long timeDistance = System.currentTimeMillis() - sLastClickTime;
            if (0 < timeDistance && timeDistance < FAST_CLICK_TIME_DISTANCE) {
                return true;
            }
        }
        sLastClickTime = System.currentTimeMillis();
        sLastClickViewId = viewId;
        return false;
    }

}

package com.het.host.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class DisplayUtil {
    /**
     * 屏幕宽度，横向像素点个数
     */
    private final int mScreenWidth;
    /**
     * 屏幕高度，纵向像素点个数
     */
    private final int mScreenHeight;

    /**
     * 屏幕分辨率
     */
    private final Point mScreenResolution;

    /**
     * 屏幕密度,dots-per-inch
     */
    private final int mDensityDpi;
    /**
     * 缩放系数
     */
    private final float mScaleFactor;
    private final float mFontScaleFactor;
    private final float mXdpi;
    private final float mYdpi;
    private static DisplayUtil mDisplayUtil;

    private DisplayUtil() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        mScreenResolution = new Point(dm.widthPixels, dm.heightPixels);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mDensityDpi = dm.densityDpi;
        mXdpi = dm.xdpi;
        mYdpi = dm.ydpi;
        mScaleFactor = dm.density;
        mFontScaleFactor = dm.scaledDensity;
    }

    private static void getInstance() {
        if (mDisplayUtil == null) {
            synchronized (DisplayUtil.class) {
                if (mDisplayUtil == null) {
                    mDisplayUtil = new DisplayUtil();
                }
            }
        }
    }

    public static Point getScreenResolution() {
        if (mDisplayUtil == null) {
            getInstance();
        }
        return mDisplayUtil.mScreenResolution;
    }

    public static int getScreenWidth() {
        if (mDisplayUtil == null) {
            getInstance();
        }
        return mDisplayUtil.mScreenWidth;
    }

    public static int getScreenHeight() {
        if (mDisplayUtil == null) {
            getInstance();
        }
        return mDisplayUtil.mScreenHeight;
    }

    public static float getScaleFactor() {
        if (mDisplayUtil == null) {
            getInstance();
        }
        return mDisplayUtil.mScaleFactor;
    }

    public static float getFontScaleFactor() {
        if (mDisplayUtil == null) {
            getInstance();
        }
        return mDisplayUtil.mFontScaleFactor;
    }

    public static int dp2px(float dpValue) {
        if (mDisplayUtil == null) {
            getInstance();
        }
        return (int) (mDisplayUtil.mScaleFactor * dpValue + 0.5f);
    }

    public static float getXDpi() {
        if (mDisplayUtil == null) {
            getInstance();
        }
        return mDisplayUtil.mXdpi;
    }

    public static float getYdpi() {
        if (mDisplayUtil == null) {
            getInstance();
        }
        return mDisplayUtil.mYdpi;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @TargetApi(14)
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return context.getResources().getDimensionPixelSize(tv.resourceId);
    }

    @Override
    public String toString() {
        if (mDisplayUtil != null) {
            return "DisplayMetrics{mScaleFactor=" + mScaleFactor + ", mDensityDpi" + mDensityDpi +
                    ", mScreenWidth=" + mScreenWidth + ", mScreenHeight=" +
                    mScreenHeight + ", mFontScaleFactor=" + mFontScaleFactor +
                    ", mXdpi=" + mXdpi + ", mYdpi=" + mYdpi + "}";
        }
        return super.toString();
    }

    public static int getViewYPosOnScreen(View view) {
        int[] locationArray = new int[2];
        view.getLocationOnScreen(locationArray);
        return locationArray[1];
    }

    public static int getViewXPosOnScreen(View view) {
        int[] locationArray = new int[2];
        view.getLocationOnScreen(locationArray);
        return locationArray[0];
    }

    public static int[] getViewPosOnScreen(View view) {
        int[] locationArray = new int[2];
        view.getLocationOnScreen(locationArray);
        return locationArray;
    }

    public static boolean hasBottomNav(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
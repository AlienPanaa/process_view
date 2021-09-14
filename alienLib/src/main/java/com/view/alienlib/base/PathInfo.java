package com.view.alienlib.base;

import android.util.Log;

public class PathInfo {
    private static final String TAG = PathInfo.class.getSimpleName();

    public final float x;
    public final float y;

    public PathInfo() {
        this(0, 0);
    }

    public PathInfo(float x, float y) {
        this.x = x;
        this.y = y;

        Log.d(TAG, "X: " + x + ", Y: " + y);
    }

    public PathInfo(PathInfo pathInfo) {
        this(pathInfo, 0);
    }

    public PathInfo(PathInfo pathInfo, int offsetX) {
        this(pathInfo.x + offsetX, pathInfo.y);
    }

}

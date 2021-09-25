package com.view.alienlib.process_view.path.single;

import android.graphics.Path;
import android.util.Log;

import androidx.annotation.NonNull;

import com.view.alienlib.base.PathInfo;
import com.view.alienlib.base.TextSpaceInfo;
import com.view.alienlib.process_view.path.BaseArrowPath;

public abstract class BaseSingleArrow extends BaseArrowPath {

    private static final String TAG = BaseSingleArrow.class.getSimpleName();

    protected Path curPath;
    protected PathInfo curPathInfo = new PathInfo();

    protected PathInfo nextStartPoint = new PathInfo();
    protected PathInfo nextEndPoint = new PathInfo();

    protected int curIndex;

    protected abstract void calcX1();

    protected abstract void calcX2();

    protected abstract void calcX3();

    protected abstract void calcX4();

    @Override
    protected Path[] getArrowPath(@NonNull Path[] results, TextSpaceInfo[] textSpaceInfo) {

        for(int i = 0; i < results.length; i++) {
            curIndex = i;
            curPath = new Path();

            calcX1();
            calcX2();
            calcX3();
            calcX4();

            results[i] = curPath;
            textSpaceInfo[i] = new TextSpaceInfo();

            Log.d(TAG, "Make path: " + i);
        }

        nextStartPoint = new PathInfo();

        return results;
    }

}

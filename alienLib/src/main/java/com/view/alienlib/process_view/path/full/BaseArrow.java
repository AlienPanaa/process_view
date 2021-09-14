package com.view.alienlib.process_view.path.full;

import android.graphics.Path;
import android.util.Log;

import androidx.annotation.NonNull;

import com.view.alienlib.base.PathInfo;
import com.view.alienlib.base.TextSpaceInfo;
import com.view.alienlib.process_view.path.BaseArrowPath;

public abstract class BaseArrow extends BaseArrowPath {

    private static final String TAG = BaseArrow.class.getSimpleName();

    private int curIndex;

    protected Path curPath;
    protected PathInfo curPathInfo = new PathInfo();
    protected PathInfo curStartPoint = new PathInfo();

    protected PathInfo nextStartPoint = new PathInfo();
    protected PathInfo nextEndPoint = new PathInfo();

    protected TextSpaceInfo curTextSpaceInfo;

    protected abstract void calcX1();

    protected abstract void calcX2(float triangleLen);

    protected abstract void calcX3();

    protected abstract void calcX4(float triangleLen);

    protected abstract void calcX5();

    protected abstract void calcX6(float triangleLen);

    protected boolean isFirstBlock() {
        return curIndex == 0;
    }

    protected boolean isLastBlock() {
        return curIndex == viewAttr.blockCount - 1;
    }

    protected int getCurBlockWidth() {
        return blocksWidth[curIndex] * (curIndex + 1);
    }

    /**
     * @param isArrowBottomTriangle 是否是箭頭的尾端三角形
     * @return 三角形的邊
     */
    private float getTriangleSideLen(boolean isArrowBottomTriangle) {
        float result = unnecessaryLength;

        if(!isArrowBottomTriangle && needFullEnd() && isLastBlock()) {
            result = 0;
        }

        if(isArrowBottomTriangle && needFullStart() && isFirstBlock()) {
            result = 0;
        }

        return result;
    }

    @Override
    protected Path[] getArrowPath(@NonNull Path[] results, TextSpaceInfo[] textSpaceInfo) {

        for(int i = 0; i < results.length; i++) {
            curIndex = i;
            curPath = new Path();

            calcX1();
            calcX2(getTriangleSideLen(false));

            calcX3();

            calcX4(getTriangleSideLen(false));
            calcX5();

            calcX6(getTriangleSideLen(true));

            results[i] = curPath;
            textSpaceInfo[i] = curTextSpaceInfo;

            Log.d(TAG, "Make path: " + i);
        }

        nextStartPoint = new PathInfo();

        return results;
    }


}

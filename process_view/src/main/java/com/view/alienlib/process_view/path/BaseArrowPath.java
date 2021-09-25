package com.view.alienlib.process_view.path;

import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.view.alienlib.base.TextSpaceInfo;
import com.view.alienlib.process_view.base.ProcessViewInfo;

public abstract class BaseArrowPath implements BlockPath<ProcessViewInfo> {

    private static final String TAG = BaseArrowPath.class.getSimpleName();

    protected ProcessViewInfo viewInfo;
    protected ProcessViewInfo.ViewAttr viewAttr;

    protected int[] blocksWidth;
    protected float unnecessaryLength;

    private boolean isCalcPath;
    private TextSpaceInfo[] textSpaceInfo;

    private void prepareTools(ProcessViewInfo viewInfo) {
        this.viewInfo = viewInfo;
        this.viewAttr = viewInfo.getViewAttr();

        blocksWidth = viewAttr.getBlocksWidth();

        unnecessaryLength = calcLength();
    }

    private float calcLength() {
        double height = (viewInfo.usefulHeight / 2f);

        double viewAngle = Math.toRadians(viewAttr.viewAngle);
        double tan = Math.tan(viewAngle);
        double length = height * tan;

        return (float) length;
    }

    public boolean needFullStart() {
        return (viewAttr.arrowFullFlag & ArrowTypeManager.START_FULL) == ArrowTypeManager.START_FULL;
    }

    public boolean needFullEnd() {
        return (viewAttr.arrowFullFlag & ArrowTypeManager.END_FULL) == ArrowTypeManager.END_FULL;
    }

    @Override
    public Path[] getArrowPath(ProcessViewInfo viewInfo) {
        prepareTools(viewInfo);

        textSpaceInfo = new TextSpaceInfo[viewAttr.blockCount];

        Path[] results = getArrowPath(new Path[viewAttr.blockCount], textSpaceInfo);

        isCalcPath = true;

        return results;
    }

    protected abstract Path[] getArrowPath(@NonNull Path[] results, TextSpaceInfo[] textSpaceInfo);

    @Override
    public RectF[] getTextSpace(ProcessViewInfo viewInfo) {
        prepareTools(viewInfo);

        if(!isCalcPath) {
            getArrowPath(viewInfo);

            Log.d(TAG, "GetTextSpace calc path");
        }

        RectF[] result = new RectF[viewAttr.blockCount];

        return getTextSpace(result);
    }

    protected RectF[] getTextSpace(@NonNull RectF[] rect) {

        for (int i = 0; i < rect.length; i++) {
            rect[i] = new RectF();

            RectF rectF = rect[i];

            rectF.top = 0;
            rectF.bottom = (float) viewAttr.usefulHeight;

            rectF.left = textSpaceInfo[i].startX;
            rectF.right = textSpaceInfo[i].endX;
        }

        return rect;
    }

}

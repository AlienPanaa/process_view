package com.view.alienlib.process_view.path.single;

import com.view.alienlib.base.PathInfo;

/**
 *     X1       X2
 *      *********
 *      *       *
 *      *********
 *     X4       X3
 */
public class SingleArrow extends BaseSingleArrow {

    private static final String TAG = SingleArrow.class.getSimpleName();

    @Override
    protected void calcX1() {
        float x = nextStartPoint.x;
        float y = 0;

        curPath.moveTo(x, y);

        curPathInfo = new PathInfo(x, y);
    }

    @Override
    protected void calcX2() {
        int blockWidth = viewAttr.getBlocksWidth()[curIndex];
        float x = curPathInfo.x + blockWidth;
        float y = 0;

        curPath.lineTo(x, y);

        curPathInfo = new PathInfo(x, y);

        nextStartPoint = new PathInfo(curPathInfo,
                viewAttr.betweenSpace);
    }

    @Override
    protected void calcX3() {
        float x = curPathInfo.x;
        float y = viewAttr.usefulHeight;

        double viewAngle = Math.toRadians(viewAttr.viewAngle);
        double tan = Math.tan(viewAngle);
        double length = y * tan;
        x = (float) (x + length);

        if(x > viewAttr.usefulWidth) {
            x = viewAttr.usefulWidth;
        }

        curPath.lineTo(x, y);

        curPathInfo = new PathInfo(x, y);
    }

    @Override
    protected void calcX4() {
        float x = 0;
        float y = curPathInfo.y;

        if(curIndex != 0) {
            x = nextEndPoint.x;
            y = nextEndPoint.y;
        }

        curPath.lineTo(x, y);
        curPath.close();

        nextEndPoint = new PathInfo(curPathInfo, viewAttr.betweenSpace);

        curPathInfo = new PathInfo(x, y);
    }

}

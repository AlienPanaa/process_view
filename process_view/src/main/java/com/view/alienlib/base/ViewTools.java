package com.view.alienlib.base;

import android.graphics.Paint;

public class ViewTools {

    public static float getTextHeight(Paint paint) {
        float offset;

        Paint.FontMetrics fm = paint.getFontMetrics();

        offset = (fm.top + fm.bottom);

        return Math.abs(offset);
    }

}

package com.view.alienlib.process_view.path;

import android.graphics.Path;
import android.graphics.RectF;

import com.view.alienlib.base.ViewInfo;

public interface BlockPath<T extends ViewInfo> {

    Path[] getArrowPath(T viewInfo);

    RectF[] getTextSpace(T viewInfo);

}

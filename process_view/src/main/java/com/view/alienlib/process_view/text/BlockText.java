package com.view.alienlib.process_view.text;

import android.graphics.RectF;

import com.view.alienlib.base.TextInfo;
import com.view.alienlib.process_view.base.ProgressViewInfo;

public interface BlockText {

    TextInfo[] getTextSpaceInfo(ProgressViewInfo.ViewAttr viewAttr, RectF[] rectFS) throws TextProcessException;

}

package com.view.alienlib.process_view.path;

import androidx.annotation.IntDef;

import com.view.alienlib.process_view.base.ProgressViewInfo;
import com.view.alienlib.process_view.path.full.Arrow;
import com.view.alienlib.process_view.path.single.SingleArrow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ArrowTypeManager {

    public static final int FULL_ARROW = 0x0000;
    public static final int SINGLE_ARROW = 0x0001;

    @IntDef({
            FULL_ARROW,
            SINGLE_ARROW
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ArrowType {}

    public static final int NON_FULL = 0x0000;
    public static final int START_FULL = 0x0001;
    public static final int END_FULL = 0x0002;

    @IntDef({
            NON_FULL,
            START_FULL,
            END_FULL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ArrowFull {}

    public static BlockPath<ProgressViewInfo> getBlockPath(@ArrowType int type) {
        BlockPath<ProgressViewInfo> result;

        switch (type) {
            case FULL_ARROW:
                result = new Arrow();
                break;

            case SINGLE_ARROW:
                result = new SingleArrow();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        return result;
    }

}

package com.view.alienlib.process_view.base;

import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import com.view.alienlib.base.ViewInfo;

public class ProcessViewInfo implements ViewInfo {

    public static final class ViewAttr {
        public int usefulHeight;
        public int usefulWidth;

        public int blockCount, blockProgress, betweenSpace;
        public float viewAngle;
        public ProcessView.Direction viewDirection;

        public boolean enableCycleLine;

        public int[] blockPercent;
        public int blockSelectedColor;
        public int blockUnselectedColor;
        public int[] blockColors;
        public int arrowFullFlag;

        public String[] textsString;
        public float textPaddingTopBottomDp;
        public int textColor;
        public boolean textAutoZoomOut;
        public String textSplitKey;
        public float textPxSize;
        public float textMinPxSize;

        private Shader shader;

        public Shader getShader() {
            if(shader != null) {
                return shader;
            }

            int middlePointX = usefulWidth / 2;

            shader = new LinearGradient(
                    middlePointX,
                    0,
                    middlePointX,
                    usefulHeight,
                    blockColors,
                    null,
                    Shader.TileMode.CLAMP
            );


            return shader;
        }

        public float getTextMinPxSize() {
            return textMinPxSize <= 0 ? 1 : textMinPxSize;
        }

        /// 字串以 ref 為主 (e.g @sting/hello_world)，string 為輔
        public String[] getTextContexts() {
            String[] result;

            if(textsString == null) {
                textsString = new String[1];
            }

            result = fixTextCountFitToBlockCount(textsString);

            return result;
        }

        private String[] fixTextCountFitToBlockCount(String[] strings) {
            if(strings == null || strings.length == blockCount) {
                return strings;
            }
            String[] result = new String[blockCount];

            int copyCount = Math.min(strings.length, blockCount);

            System.arraycopy(strings, 0, result, 0, copyCount);

            for(int i = 0; i < result.length; i++) {
                if(result[i] == null) {
                    result[i] = "";
                }
            }

            return result;
        }

        private void fixWeightCountFitToBlockCount() {
            if(blockPercent.length == blockCount) {
                return;
            }

            int[] fixBlockPercent = new int[blockCount];

            System.arraycopy(blockPercent,
                    0,
                    fixBlockPercent,
                    0,
                    Math.min(blockPercent.length, blockCount));

            blockPercent = fixBlockPercent;

            for(int i = 0; i < blockPercent.length; i++) {
                if(blockPercent[i] == 0) {
                    blockPercent[i] = 1;
                }
            }

        }

        // 計算總分配
        private int getTotalPercentWeight() {
            int total = 0;

            fixWeightCountFitToBlockCount();

            for(int item : blockPercent) {
                total += item;
            }
            return total == 0 ? 1 : total;
        }

        public int[] getBlocksWidth() {
            if(blockPercent == null || blockPercent.length == 0) {
                blockPercent = new int[1];
            }

            int[] result = new int[blockCount];
            int totalPercentWeight = getTotalPercentWeight();

            int everyBlockWidth = usefulWidth / totalPercentWeight;

            for(int i = 0; i < blockCount; i++) {
                result[i] = blockPercent[i] * everyBlockWidth;
            }

            return result;
        }
    }

    public static final class DrawTools {
        public Paint bolderPaint, blockPaint, textPaint;
    }

    public int usefulWidth;
    public int usefulHeight;

    private final ViewAttr viewAttr;
    private final DrawTools drawTools;

    public ProcessViewInfo() {
        viewAttr = new ViewAttr();
        drawTools = new DrawTools();
    }

    public ViewAttr getViewAttr() {
        return viewAttr;
    }

    public DrawTools getDrawTools() {
        return drawTools;
    }

    @Override
    public void setUsefulSpace(int width, int height) {
        usefulWidth = viewAttr.usefulWidth = width;
        usefulHeight = viewAttr.usefulHeight = height;
    }

}

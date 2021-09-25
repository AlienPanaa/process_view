package com.view.alienlib.process_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;

import com.view.alienlib.base.TextInfo;
import com.view.alienlib.process_view.base.ProcessView;
import com.view.alienlib.process_view.base.ProcessViewInfo;
import com.view.alienlib.process_view.path.ArrowTypeManager;
import com.view.alienlib.process_view.path.BlockPath;
import com.view.alienlib.process_view.text.BlockText;
import com.view.alienlib.process_view.text.TextProcessException;
import com.view.alienlib.process_view.text.TextWithRule;

public class CubeProcessView extends ProcessView {

    private static final String TAG = CubeProcessView.class.getSimpleName();
    private BlockPath<ProcessViewInfo> blockPath;

    private Canvas canvas;

    private Path[] pathResult;
    private RectF[] textSpace;

    private final BlockText blockText = new TextWithRule();

    public CubeProcessView(Context context) {
        super(context);
    }

    public CubeProcessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CubeProcessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void implDraw(Canvas canvas) {
        this.canvas = canvas;

        blockPath = blockPath == null ? getArrowBlockPath() : blockPath;

        pathResult = blockPath.getArrowPath(viewInfo);
        textSpace = blockPath.getTextSpace(viewInfo);

        drawBlock();

        drawBold();

        drawText();
    }

    private void drawBlock() {
        Paint blockPaint = drawTools.blockPaint;
        Shader shader = viewAttr.getShader();

        for(int i = 0; i < pathResult.length; i++) {
            Path path = pathResult[i];
            int targetColor = viewAttr.blockSelectedColor;
            Shader targetShader = shader;

            if(i > viewAttr.blockProgress - 1) {
                // 未被選擇區塊的顏色
                targetShader = null;
                targetColor = viewAttr.blockUnselectedColor;

                Log.d(TAG, "Unselected block index: " + (i + 1) + ", color: " + Integer.toHexString(targetColor));
            }

            blockPaint.setShader(targetShader);
            blockPaint.setColor(targetColor);

            canvas.drawPath(path, blockPaint);
        }

    }

    private void drawBold() {
        Paint boldPaint = drawTools.bolderPaint;

        for(Path path : pathResult) {
            canvas.drawPath(path, boldPaint);
        }

    }

    private void drawText() {
        Paint textPaint = drawTools.textPaint;

        TextInfo[] textInfo;

        try {
            textInfo = blockText.getTextSpaceInfo(viewAttr, textSpace);

            for(TextInfo info : textInfo) {
                textPaint.setTextSize(info.textSize);

                canvas.drawText(
                        info.context,
                        info.startX,
                        info.startY,
                        textPaint);
            }
        } catch (TextProcessException e) {
            e.printStackTrace();
        }

    }


/// TODO: 操作方法 ( 抽出
    public int getProgress() {
        return viewAttr.blockProgress;
    }

    public int getCount() {
        return viewAttr.blockCount;
    }

    private int checkValue(int value) {
        int result = value;

        if(value < 0) {
            result = 0;
        } else if(value > viewAttr.blockCount) {
            result = viewAttr.blockCount;
        }

        return result;
    }

    @MainThread
    public void setProgress(int value) {
        value = checkValue(value);

        viewAttr.blockProgress = value;

        invalidate();
    }

    @MainThread
    public void setCount(int value) {
        if(value < 0) {
            value = 0;
        }

        viewAttr.blockCount = value;

        invalidate();
    }

    @MainThread
    public void setArrowType(@ArrowTypeManager.ArrowType int type) {
        blockPath = ArrowTypeManager.getBlockPath(type);

        invalidate();
    }

    @MainThread
    public void setAngle(@IntRange(from = 1, to = 179) int value) {
        viewAttr.viewAngle = value;

        invalidate();
    }

}

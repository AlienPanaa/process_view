package com.view.alienlib.process_view.text;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.TypedValue;

import com.view.alienlib.base.TextInfo;
import com.view.alienlib.base.ViewTools;
import com.view.alienlib.process_view.base.ProgressViewInfo;

import java.util.LinkedList;
import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class TextWithRule implements BlockText {

    private static final List<TextInfo> result = new LinkedList<>();
    private static final Paint textSizePaint = new Paint();

    private ProgressViewInfo.ViewAttr viewAttr;

    private final float oneSpUnit;

    private float finalTextSize;

    public TextWithRule() {
        oneSpUnit = TypedValue.applyDimension(COMPLEX_UNIT_SP, 1, Resources.getSystem().getDisplayMetrics());
    }

    private String[] preCheck() throws TextProcessException {
        if(result.size() != 0) {
            result.clear();
        }

        String[] texts = viewAttr.getTextContexts();
        if(texts == null) {
            throw new TextProcessException();
        }

        return texts;
    }

    private float calcTopYAndAdjustTextSize(RectF rectF, float textHeight, int wordCount) {
        float textMinPxSize = viewAttr.getTextMinPxSize();
        finalTextSize = textHeight;

        if(textHeight < textMinPxSize) {
            return 1;
        }

        float totalHeight = rectF.bottom - rectF.top;

        float result = (totalHeight - (wordCount * textHeight)) / 2;

        if(result <= 0) {

            if(viewAttr.textAutoZoomOut) {
                result = calcTopYAndAdjustTextSize(rectF, textHeight - oneSpUnit, wordCount);
            } else {
                result = 0;
            }
        }

        return result;
    }

    private void splitText(String text, RectF rectF, float startX, float textHeight) throws TextProcessException {

        try {
            String[] words = text.split("\n");

            float startY = calcTopYAndAdjustTextSize(rectF, textHeight, words.length);

            for(int i = 0; i < words.length; i++) {
                TextInfo textInfo = new TextInfo();

                textInfo.textSize = finalTextSize;
                textInfo.context = words[i];
                textInfo.startX = startX;
                textInfo.startY = startY + ((i + 1) * textHeight) - viewAttr.textPaddingTopBottomDp + rectF.top;  // Reduce one padding

                result.add(textInfo);
            }
        } catch (Exception e) {
            throw new TextProcessException();
        }

    }

    private float getTextHeight(float size) {
        textSizePaint.setTextSize(size);

        return ViewTools.getTextHeight(textSizePaint);
    }

    @Override
    public TextInfo[] getTextSpaceInfo(ProgressViewInfo.ViewAttr viewAttr, RectF[] rectFS) throws TextProcessException {
        this.viewAttr = viewAttr;

        String[] texts = preCheck();

        float textHeight = getTextHeight(viewAttr.textPxSize);

        // Add text padding, must add top and bottom part
        textHeight += (viewAttr.textPaddingTopBottomDp * 2);

        int length = rectFS.length;

        for(int i = 0; i < length; i++) {

            RectF rectF = rectFS[i];

            float startX = (rectF.left + rectF.right) / 2;

            splitText(texts[i], rectF, startX, textHeight);

        }

        int size = result.size();

        return result.toArray(new TextInfo[size]);
    }
}

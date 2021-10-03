package com.view.alienlib.process_view.base;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Nullable;

import com.view.alienlib.R;
import com.view.alienlib.base.BaseView;
import com.view.alienlib.process_view.path.ArrowTypeManager;
import com.view.alienlib.process_view.path.BlockPath;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * 分析使用者設定 & 打包 Draw 所需工具
 */
public abstract class ProgressView extends BaseView<ProgressViewInfo> {

    @FunctionalInterface
    public interface ProgressViewListener {
        void OnProgress(int process);
    }

    private static final String TAG = ProgressView.class.getSimpleName();
    private static final int[] ATTR_RES_IDS = R.styleable.process_view;

/// Attr style
    private int betweenSpace;
    private float viewAngle;
    private Direction viewDirection;

    private boolean enableCycleLine;

    private ProgressViewListener progressViewListener;

    private boolean clickable;

    private int bolderColor;
    private float bolderWidth;

    private int blockCount, blockProgress;
    private int blockSelectedColor;
    private int blockUnselectedColor;
    private int[] blockColors;
    private int[] blockPercent;

    private float textPaddingTopBottomDp;
    private float textPxSize;
    private float textMinPxSize;
    private int textColor;
    private boolean textAutoZoomOut;
    private String textSplitKey;
    private String[] textsString;
    private Paint.Align textAlign;

    private int arrowFullFlag;
    private BlockPath<ProgressViewInfo> arrowBlockPath;

/// View Tools
    private Paint bolderPaint, blockPaint, textPaint;

/// Total view info
    protected ProgressViewInfo viewInfo;
    protected ProgressViewInfo.DrawTools drawTools;
    protected ProgressViewInfo.ViewAttr viewAttr;

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public enum SplitKey {
        SPLIT_KEY_1(0x0000, "\\*"),
        SPLIT_KEY_2(0x0001, "\\^"),
        SPLIT_KEY_3(0x0002, "\\:"),
        SPLIT_KEY_4(0x0003, "\\|"),
        SPLIT_KEY_5(0x0004, "\\."),
        SPLIT_KEY_6(0x0005, "\\");

        int value;
        String splitStr;
        SplitKey(int value, String splitStr) {
            this.value = value;
            this.splitStr = splitStr;
        }
    }

    public enum Direction {
        DIRECTION_RIGHT(0),
        DIRECTION_LEFT(1);

        int value;
        Direction(int value) {
            this.value = value;
        }
    }

    @Override
    protected void initAttrs(TypedArray typedArray) {
        viewAngle = typedArray.getFloat(R.styleable.process_view_angle, 25f);
        betweenSpace = typedArray.getInt(R.styleable.process_view_between_space, 10);

        enableCycleLine = typedArray.getBoolean(R.styleable.process_view_enable_cycle_line, false);
        int direct = typedArray.getInt(R.styleable.process_view_direction, Direction.DIRECTION_RIGHT.value);
        viewDirection = Direction.values()[direct];

        initBlockAttr(typedArray);

        initTextAttr(typedArray);

        initArrowAttr(typedArray);
    }

    private void initTextAttr(TypedArray typedArray) {
        float textSp = typedArray.getFloat(R.styleable.process_view_text_size_sp, 16);
        textPxSize = TypedValue.applyDimension(COMPLEX_UNIT_SP, textSp, Resources.getSystem().getDisplayMetrics());

        float textMinSp = typedArray.getFloat(R.styleable.process_view_text_min_size_sp, 1);
        textMinPxSize = TypedValue.applyDimension(COMPLEX_UNIT_SP, textMinSp, Resources.getSystem().getDisplayMetrics());

        textsString = getStringArrayFromRefs(R.styleable.process_view_texts);
        textColor = typedArray.getColor(R.styleable.process_view_text_color, Color.BLACK);
        int align = typedArray.getInt(R.styleable.process_view_text_align, Paint.Align.CENTER.ordinal());
        textAlign = Paint.Align.values()[align];
        float padding = typedArray.getFloat(R.styleable.process_view_text_padding_top_bottom_dp,1.5f);
        textPaddingTopBottomDp = TypedValue.applyDimension(COMPLEX_UNIT_DIP, padding, Resources.getSystem().getDisplayMetrics());
        int splitWord = typedArray.getInt(R.styleable.process_view_text_split_key, SplitKey.SPLIT_KEY_4.value);
        textSplitKey = SplitKey.values()[splitWord].splitStr;
        textAutoZoomOut = typedArray.getBoolean(R.styleable.process_view_text_auto_zoom_out, false);
    }

    private void initBlockAttr(TypedArray typedArray) {

        clickable = typedArray.getBoolean(R.styleable.process_view_clickable, true);

        bolderColor = typedArray.getColor(R.styleable.process_view_bolder_color, Color.YELLOW);
        float width = typedArray.getFloat(R.styleable.process_view_bolder_width_dp, 1.5f);
        bolderWidth = TypedValue.applyDimension(COMPLEX_UNIT_DIP, width, Resources.getSystem().getDisplayMetrics());
        blockCount = typedArray.getInt(R.styleable.process_view_block_count, 3);
        blockProgress = typedArray.getInt(R.styleable.process_view_block_progress, 1);
        blockSelectedColor = typedArray.getColor(R.styleable.process_view_block_selected_color, Color.RED);
        blockUnselectedColor = typedArray.getColor(R.styleable.process_view_block_unselected_color, Color.GRAY);
        blockColors = getIntArrayAttrsFromRefs(R.styleable.process_view_block_selected_colors);
        blockPercent = getIntArrayAttrsFromRefs(R.styleable.process_view_block_percent);
    }

    private void initArrowAttr(TypedArray typedArray) {
        arrowFullFlag = typedArray.getInt(R.styleable.process_view_arrow_full, ArrowTypeManager.END_FULL | ArrowTypeManager.START_FULL);

        int arrowType = typedArray.getInt(R.styleable.process_view_arrow_type, ArrowTypeManager.FULL_ARROW);
        arrowBlockPath = ArrowTypeManager.getBlockPath(arrowType);
    }

    @Override
    protected void initViewTools() {
        bolderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bolderPaint.setAntiAlias(true);
        bolderPaint.setStyle(Paint.Style.STROKE);
        bolderPaint.setStrokeCap(Paint.Cap.ROUND);
        bolderPaint.setColor(bolderColor);
        bolderPaint.setStrokeWidth(bolderWidth);

        blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockPaint.setAntiAlias(true);
        blockPaint.setStyle(Paint.Style.FILL);
        blockPaint.setColor(blockSelectedColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textPxSize);
        textPaint.setTextAlign(textAlign);
    }

    @Override
    protected ProgressViewInfo initViewInfo() {
        viewInfo = new ProgressViewInfo();
        drawTools = viewInfo.getDrawTools();
        viewAttr = viewInfo.getViewAttr();

        ProgressViewInfo.DrawTools drawTools = viewInfo.getDrawTools();
        drawTools.bolderPaint = bolderPaint;
        drawTools.blockPaint = blockPaint;
        drawTools.textPaint = textPaint;

        ProgressViewInfo.ViewAttr viewAttr = viewInfo.getViewAttr();
        viewAttr.clickable = clickable;

        viewAttr.bolderWidth = bolderWidth;

        viewAttr.blockCount = blockCount;
        viewAttr.blockProgress = blockProgress;
        viewAttr.blockPercent = blockPercent;
        viewAttr.blockSelectedColor = blockSelectedColor;
        viewAttr.blockUnselectedColor = blockUnselectedColor;
        viewAttr.blockColors = blockColors;

        viewAttr.betweenSpace = betweenSpace;
        viewAttr.viewAngle = viewAngle;
        viewAttr.viewDirection = viewDirection;
        viewAttr.enableCycleLine = enableCycleLine;
        viewAttr.arrowFullFlag = arrowFullFlag;

        viewAttr.textColor = textColor;
        viewAttr.textsString = textsString;
        viewAttr.textPaddingTopBottomDp = textPaddingTopBottomDp;
        viewAttr.textSplitKey = textSplitKey;
        viewAttr.textAutoZoomOut = textAutoZoomOut;
        viewAttr.textPxSize = textPxSize;
        viewAttr.textMinPxSize = textMinPxSize;

        return viewInfo;
    }

    @Override
    protected int[] styleAttrId() {
        return ATTR_RES_IDS;
    }

    protected BlockPath<ProgressViewInfo> getArrowBlockPath() {
        return arrowBlockPath;
    }

    public ProgressViewListener getProgressViewListener() {
        return progressViewListener;
    }

    public void setProcessViewListener(ProgressViewListener progressViewListener) {
        this.progressViewListener = progressViewListener;
    }
}

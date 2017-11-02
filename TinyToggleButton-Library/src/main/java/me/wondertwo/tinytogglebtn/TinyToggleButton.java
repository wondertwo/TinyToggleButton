package me.wondertwo.tinytogglebtn;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

/**
 * TinyToggleButton. Created by wondertwo on 2017.10.05.
 *
 * 后期考虑将开关控件功能扩展到三态点击。即全选、未选、半选三种状态
 */

public class TinyToggleButton extends View implements Checkable {

    /* 属性动画ValueAnimator */
    private ValueAnimator mValueAnimator;
    /* 默认动画时间，毫秒值 */
    private static int DEF_ANIMATION_DURATION = 200;
    /* 动画持续时间 */
    private long mAnimDuration = DEF_ANIMATION_DURATION;
    /* 滑动动画渐变值 */
    private float mAnimationValue;
    /* 是否支持滑动动画。默认支持 */
    private boolean mIsAnimEnable = true;


    /* 控件画布矩形对象 */
    private RectF mCanvasRect;
    /* 下层圆角矩形对象 */
    private RectF mDrawRect;
    /* 上层圆形按钮绘制画笔 */
    private Paint mCirclePaint;
    /* 下层圆角矩形绘制画笔 */
    private Paint mRectPaint;


    // 圆形按钮与矩形半径比率，控制圆形按钮与圆角矩形的相对大小
    private static final float RADIUS_RATIO_A = 1.00F;
    private static final float RADIUS_RATIO_B = 0.81F;
    private static final float RADIUS_RATIO_C = 1.67F; // 1.67
    // 矩形宽高比率，控制控件的长宽比例看起来保持协调
    private static final float DEF_RECT_RATIO = 2.24F;
    // private static final float MAX_CANVAS_RECT_HEIGHT = 180F;
    // private static final float MAX_CANVAS_RECT_WIDTH = MAX_CANVAS_RECT_HEIGHT * DEF_RECT_RATIO;


    /* 下层圆角矩形关闭状态颜色值 */
    private int mColorBelowOff = COLOR_BELOW_OFF;
    /* 上层圆形按钮关闭状态颜色值 */
    private int mColorAboveOff = COLOR_ABOVE_OFF;
    /* 下层圆角矩形打开状态颜色值 */
    private int mColorBelowOn = COLOR_BELOW_ON;
    /* 上层圆形按钮打开状态颜色值 */
    private int mColorAboveOn = COLOR_ABOVE_ON;


    // 默认颜色取值
    private static final int COLOR_BELOW_OFF = Color.parseColor("#FFCDCDCD");
    private static final int COLOR_BELOW_ON = Color.parseColor("#FF89CEA8");
    private static final int COLOR_ABOVE_OFF = Color.parseColor("#FFA7A7A7");
    private static final int COLOR_ABOVE_ON = Color.parseColor("#FF01A357");


    /* 基准矩形半径 */
    private float mBaseRadius;
    /* 画布矩形半径 */
    private float mDrawRectRadius;
    /* 圆形按钮半径 */
    private float mCircleRadius;
    /* 开关按钮X坐标，动画实时更新 */
    private float mCircleBtnX;
    /* 开关按钮Y坐标 */
    private float mCircleBtnY;
    /* 圆形按钮X坐标最大值 */
    private float mCircleBtnRX;
    /* 圆形按钮X坐标最小值 */
    private float mCircleBtnLX;


    private Resources mSysResources = Resources.getSystem();
    private OnToggleClickListener mListener;
    private Style mStyle = Style.TINY_STYLE_A; // 样式
    private boolean mIsChecked = false; // 默认关闭状态


    /**
     * 定义开关控件的三种展示风格
     *
     * TINY_STYLE_A 风格一，即圆形按钮半径等于圆角矩形半径
     * TINY_STYLE_B 风格二，即圆形按钮半径小于圆角矩形半径
     * TINY_STYLE_C 风格三，即圆形按钮半径大于圆角矩形半径
     */
    public enum Style {
        TINY_STYLE_A, TINY_STYLE_B, TINY_STYLE_C
    }

    public TinyToggleButton(Context context) {
        this(context, null);
    }

    public TinyToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TinyToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttr(attrs);
        initializePaint();
        initializeAnimator();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
                if (isAnimatorEnable()) {
                    startSlideAnimation(mValueAnimator);
                }
            }
        });
    }

    /**
     * 初始化属性动画ValueAnimator对象
     */
    private void initializeAnimator() {
        mValueAnimator = ValueAnimator.ofFloat(0f, 1.0f);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(mAnimDuration);
    }

    /**
     * 初始化XML布局设置的属性值
     *
     * @param attrs
     */
    private void initializeAttr(AttributeSet attrs) {
        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.tiny_toggle_btn);
        }
        if (typedArray == null) return;

        try {
            mColorAboveOff = typedArray.getColor(
                    R.styleable.tiny_toggle_btn_color_toggle_above_off, COLOR_ABOVE_OFF);
            int lighterAboveOff = calculateLighterColor(mColorAboveOff);
            mColorBelowOff = typedArray.getColor(
                    R.styleable.tiny_toggle_btn_color_toggle_below_off, lighterAboveOff);
            mColorAboveOn = typedArray.getColor(
                    R.styleable.tiny_toggle_btn_color_toggle_above_on, COLOR_ABOVE_ON);
            int lighterBelowOn = calculateLighterColor(mColorAboveOn);
            mColorBelowOn = typedArray.getColor(
                    R.styleable.tiny_toggle_btn_color_toggle_below_on, lighterBelowOn);
            mAnimDuration = typedArray.getInt(
                    R.styleable.tiny_toggle_btn_animator_duration, DEF_ANIMATION_DURATION);
            mIsAnimEnable = typedArray.getBoolean(
                    R.styleable.tiny_toggle_btn_animator_enable, true);
            int tinyStyleInt = typedArray.getInt(R.styleable.tiny_toggle_btn_toggle_btn_style, 0);
            switch (tinyStyleInt) {
                case 0:
                    mStyle = Style.TINY_STYLE_A;
                    break;
                case 1:
                    mStyle = Style.TINY_STYLE_B;
                    break;
                case 2:
                    mStyle = Style.TINY_STYLE_C;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * 初始化底层矩形，初始化矩形画笔，初始化圆形按钮画笔
     */
    private void initializePaint() {
        mCanvasRect = new RectF();
        mDrawRect = new RectF();

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setAntiAlias(true);
        mRectPaint = new Paint();
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setStrokeCap(Paint.Cap.ROUND);
        mRectPaint.setAntiAlias(true);
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        this.setChecked(!isChecked());
    }

    @Override
    public void setChecked(boolean isChecked) {
        mIsChecked = isChecked;
        updateCircleBtnCoordinator(mIsChecked);
        invalidate();
        if (mListener != null) {
            mListener.onToggleClick(this, mIsChecked);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        float measuredW = MeasureSpec.getSize(widthMeasureSpec);
        float measuredH = MeasureSpec.getSize(heightMeasureSpec);
        /* 格式化圆角矩形宽度 */
        float formatMeasureW = formatMeasuredSizeW(measuredW, measuredH);
        /* 格式化圆角矩形高度 */
        float formatMeasureH = formatMeasuredSizeH(measuredW, measuredH);

        mCanvasRect.set(0, 0, formatMeasureW, formatMeasureH);
        mBaseRadius = 0.5F * Math.min(formatMeasureW, formatMeasureH);
        mCircleRadius = calculateCircleRadius(mBaseRadius);

        switch (mStyle) {
            case TINY_STYLE_A:
            case TINY_STYLE_B:
            default:
                mDrawRect.set(0, 0, formatMeasureW, formatMeasureH);
                mDrawRectRadius = 0.5F * Math.min(mDrawRect.width(), mDrawRect.height());
                break;
            case TINY_STYLE_C:
                mDrawRectRadius = mCircleRadius / RADIUS_RATIO_C;
                float diffValue = mBaseRadius - mDrawRectRadius;
                mDrawRect.set(diffValue, diffValue, formatMeasureW - diffValue, formatMeasureH - diffValue);
                break;
        }

        // 设置默认测量宽高
        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) formatMeasureW, MeasureSpec.EXACTLY);
        }
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) formatMeasureH, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mCircleBtnLX = mBaseRadius;
        mCircleBtnRX = mCanvasRect.width() - mBaseRadius;
        mCircleBtnY = mBaseRadius;
        updateCircleBtnCoordinator(isChecked());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置画笔颜色
        if (isChecked()) {
            mCirclePaint.setColor(mColorAboveOn);
            mRectPaint.setColor(mColorBelowOn);
        } else {
            mCirclePaint.setColor(mColorAboveOff);
            mRectPaint.setColor(mColorBelowOff);
        }

        // 绘制背景
        switch (mStyle) {
            case TINY_STYLE_A:
            case TINY_STYLE_B:
                canvas.drawRoundRect(mCanvasRect, mBaseRadius, mBaseRadius, mRectPaint);
                break;
            case TINY_STYLE_C:
                canvas.drawRoundRect(mDrawRect, mDrawRectRadius, mDrawRectRadius, mRectPaint);
                break;
        }

        // 绘制按钮
        canvas.drawCircle(mCircleBtnX, mCircleBtnY, mCircleRadius, mCirclePaint);

    }

    private void updateCircleBtnCoordinator(boolean isChecked) {
        if (isChecked) {
            mCircleBtnX = mCircleBtnRX;
        } else {
            mCircleBtnX = mCircleBtnLX;
        }
    }

    private void startSlideAnimation(ValueAnimator valueAnimator) {
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationValue = (float) animation.getAnimatedValue();
                mCircleBtnX = calculateCircleBtnX(mAnimationValue);
                postInvalidate(); // 刷新界面
            }
        });
        valueAnimator.start();
    }

    private float calculateCircleBtnX(float animationValue) {
        if (isChecked()) {
            return mCircleBtnLX + (mCircleBtnRX - mCircleBtnLX) * animationValue;
        } else {
            return mCircleBtnRX - (mCircleBtnRX - mCircleBtnLX) * animationValue;
        }
    }

    private float formatMeasuredSizeW(float measuredW, float measuredH) {
        float userRatio = measuredW / measuredH;

        if (userRatio > DEF_RECT_RATIO) {
            return measuredH * DEF_RECT_RATIO;
        }
        return measuredW;
    }

    private float formatMeasuredSizeH(float measuredW, float measuredH) {
        float userRatio = measuredW / measuredH;

        if (userRatio < DEF_RECT_RATIO) {
            return measuredW / DEF_RECT_RATIO;
        }
        return measuredH;
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mSysResources.getDisplayMetrics());
    }

    private int dp2pxInt(float dpInt) {
        return (int) dp2px(dpInt);
    }

    private int calculateLighterColor(int originColor) {
        float[] hsvColor = new float[3];
        Color.colorToHSV(originColor, hsvColor); // 转换为HSV颜色值

        hsvColor[1] = hsvColor[1] - 0.4f; // 降低饱和度
        hsvColor[2] = hsvColor[2] + 0.4f; // 增加亮度
        return Color.HSVToColor(hsvColor);
    }

    private float calculateCircleRadius(float rectRadius) {
        switch (mStyle) {
            case TINY_STYLE_B:
                return rectRadius * RADIUS_RATIO_B;
            case TINY_STYLE_A:
            case TINY_STYLE_C:
            default:
                return rectRadius * RADIUS_RATIO_A;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setChecked(boolean isChecked, boolean animatorEnable) {
        this.setChecked(isChecked);
        mIsAnimEnable = animatorEnable;
    }

    public void setOnToggleClickListener(OnToggleClickListener listener) {
        this.mListener = listener;
    }

    public void setAnimatorDuration(int animatorMillis) {
        mAnimDuration = (long) animatorMillis;
    }

    public boolean isAnimatorEnable() {
        return mIsAnimEnable;
    }

    public void setAnimatorEnable(boolean animatorEnable) {
        mIsAnimEnable = animatorEnable;
    }

    public void setTinyStyle(Style style) {
        mStyle = style;
    }

    /**
     * 开关控件点击监听器
     */
    public interface OnToggleClickListener {
        void onToggleClick(TinyToggleButton button, boolean isChecked);
    }

}

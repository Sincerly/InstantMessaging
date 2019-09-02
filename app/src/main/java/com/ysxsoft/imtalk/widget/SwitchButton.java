package com.ysxsoft.imtalk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ysxsoft.imtalk.R;

/**
 * Create By 胡
 * on 2019/7/13 0013
 */
public class SwitchButton extends View {
    public static final double MBTNHEIGHT = 0.55;
    private Paint mPaint = new Paint();
    private int mSelectColor;
    private int mHeight;
    private boolean checked = false;
    private float mScale;
    private float mAnimate = 0L;
    public static final int OFFSET = 3;
    private OnCheckChangeListener mOnCheckChangeListener;
    public SwitchButton(Context context) {
        super(context, null);
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        Color.parseColor("#2eaa57");
        typedArray.recycle();
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获取宽
         */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        /**
         * 根据宽度获取高度
         */
        mHeight = (int) (MBTNHEIGHT * width);
        /**
         * 设置宽高
         */
        setMeasuredDimension(width, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);//Paint.Style.FILL  填充内部
        mPaint.setAntiAlias(true);//锯齿
        mPaint.setColor(mSelectColor);

        /**
         * 左上角的坐标是（0,0），右下角的坐标是（getWidth(), getHeight()）的矩形
         */
        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        RectF rectF = new RectF(rect);
        /**
         * 绘制矩形
         * mHeight / 2：x方向上的圆角半径。
         * mHeight / 2：y方向上的圆角半径。
         */
        canvas.drawRoundRect(rectF, mHeight / 2, mHeight / 2, mPaint);
        /**
         * 确保动画在中间一层
         */
        canvas.save();
        mPaint.setColor(Color.parseColor("#e6e6e6"));
        mAnimate = mAnimate - 0.1f > 0 ? mAnimate - 0.1f : 0;
        mScale = (!checked ? 1 - mAnimate : mAnimate);
        /**
         * 缩放效果
         * float sx, float sy, float px, float py
         */
//        canvas.scale(mScale, mScale, getWidth() - getHeight() / 2, rect.centerY());
        /**
         * 绘制矩形
         */
        canvas.drawRoundRect(rectF, mHeight / 2, mHeight / 2, mPaint);
        if (isChecked()){
            mPaint.setColor(Color.GREEN);
//            mPaint.setColor(getResources().getColor(R.color.list_item_no_select_bg));

        }else {
            mPaint.setColor(Color.WHITE);
        }
        Rect rect_inner = new Rect(OFFSET, OFFSET, getWidth() - OFFSET, getHeight() - OFFSET);
        RectF rectF_inner = new RectF(rect_inner);
        /**
         * 绘制缩放的白色圆角经和上班的重叠实现灰色边框的效果
         */
        canvas.drawRoundRect(rectF_inner, (mHeight - 8) / 2, (mHeight - 8) / 2, mPaint);
        canvas.restore();
        /**
         * 中间的圆形平移
         */
        int sWidth = getWidth();
        int bTranslateX = sWidth - getHeight();
        float tranlsale = bTranslateX * (!checked ? mAnimate : 1 - mAnimate);
        canvas.translate(tranlsale, 0);
        /**
         * 两个圆带灰色边框
         */
        mPaint.setColor(Color.parseColor("#e6e6e6"));
        canvas.drawCircle(getHeight() / 2, getHeight() / 2, getHeight() / 2 - OFFSET / 2, mPaint);
        if (isChecked()){
            mPaint.setColor(Color.WHITE);
        }else {
            mPaint.setColor(Color.GREEN);
//            mPaint.setColor(getResources().getColor(R.color.list_item_no_select_bg));

        }
        canvas.drawCircle(getHeight() / 2, getHeight() / 2, getHeight() / 2 - OFFSET, mPaint);
        if (mScale > 0) {
            mPaint.reset();
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mAnimate = 1;
                checked = !checked;
                if (mOnCheckChangeListener != null) {
                    mOnCheckChangeListener.OnCheckedChanged(checked);
                }
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public OnCheckChangeListener getmOnCheckChangeListener() {
        return mOnCheckChangeListener;
    }

    /**
     * 设置监听
     *
     * @param mOnCheckChangeListener
     */
    public void setmOnCheckChangeListener(OnCheckChangeListener mOnCheckChangeListener) {
        this.mOnCheckChangeListener = mOnCheckChangeListener;

    }

    public interface OnCheckChangeListener {
        void OnCheckedChanged(boolean ischecked);
    }

}

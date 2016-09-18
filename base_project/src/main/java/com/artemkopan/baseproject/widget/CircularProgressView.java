package com.artemkopan.baseproject.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.widget.drawable.CircularProgressDrawable;


public class CircularProgressView extends View {

    private CircularProgressDrawable mDrawable;
    private boolean mHeightAccent = false;

    public CircularProgressView(Context context) {
        this(context, null, 0);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int color = Color.BLACK;
        int borderWidth = -1;

        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CircularProgressView);
            try {
                color = array.getColor(R.styleable.CircularProgressView_cpv_progressColor, color);
                borderWidth = array.getDimensionPixelSize(
                        R.styleable.CircularProgressView_cpv_border_width,
                        getContext().getResources()
                                    .getDimensionPixelSize(R.dimen.base_progress_border_width));
                mHeightAccent = array.getBoolean(R.styleable.CircularProgressView_cpv_height_accent,
                        mHeightAccent);
            } finally {
                array.recycle();
            }
        }

        mDrawable = new CircularProgressDrawable(color, borderWidth);
        mDrawable.setCallback(this);
        mDrawable.start();
    }

    public void setProgressColor(@ColorInt int color) {
        if (mDrawable != null) {
            mDrawable.setColor(color);
        }
    }

    public void setBorderWidth(int borderWidth) {
        if (mDrawable != null) {
            mDrawable.setStrokeWidth(borderWidth);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mDrawable == null) {
            return;
        }
        if (visibility == VISIBLE) {
            mDrawable.start();
        } else {
            mDrawable.stop();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mHeightAccent) {
            mDrawable.setBounds(0, 0, h, h);
        } else {
            mDrawable.setBounds(0, 0, w, w);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mDrawable.draw(canvas);
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mDrawable || super.verifyDrawable(who);
    }
}

package com.example.calculator.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlignTextView extends androidx.appcompat.widget.AppCompatTextView {
    private int bottomPaddingOffset;
    private int topPaddingOffset;
    private final Rect tempRect;

    public AlignTextView(@NonNull Context context) {
        this(context, null);
    }

    public AlignTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlignTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.tempRect = new Rect();
        setIncludeFontPadding(false);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Paint paint = getPaint();
        paint.getTextBounds("H", 0, 1, this.tempRect);
        this.bottomPaddingOffset = (int) Math.min(getPaddingBottom(), Math.ceil(paint.descent()));
        this.topPaddingOffset = (int) Math.min(getPaddingTop(), Math.ceil(tempRect.top - paint.ascent()));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public int getCompoundPaddingTop() {
        return super.getCompoundPaddingTop() - topPaddingOffset;
    }

    @Override
    public int getCompoundPaddingBottom() {
        return super.getCompoundPaddingBottom() - bottomPaddingOffset;
    }
}

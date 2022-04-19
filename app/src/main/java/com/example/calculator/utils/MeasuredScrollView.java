package com.example.calculator.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class MeasuredScrollView extends HorizontalScrollView {
    public MeasuredScrollView(Context context) {
        this(context, null);
    }

    public MeasuredScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasuredScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static int getChildMeasureSpecExtended(int spec, int padding, int childDimension){
        if (MeasureSpec.getMode(spec) == MeasureSpec.UNSPECIFIED && (childDimension == -1 || childDimension == -2)) {
            return View.MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(spec) - padding), MeasureSpec.UNSPECIFIED);
        }
        return ViewGroup.getChildMeasureSpec(spec, padding, childDimension);
    }

    @Override
    public void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        int parentWidthMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(parentWidthMeasureSpec), MeasureSpec.UNSPECIFIED);
        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        child.measure(getChildMeasureSpecExtended(parentWidthMeasureSpec2, 0, layoutParams.width),
                getChildMeasureSpecExtended(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), layoutParams.height));
    }

    @Override
    public void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        int parentWidthMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(parentWidthMeasureSpec), MeasureSpec.UNSPECIFIED);
        ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
        child.measure(getChildMeasureSpecExtended(parentWidthMeasureSpec2, layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width),
                getChildMeasureSpecExtended(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height));
    }
}

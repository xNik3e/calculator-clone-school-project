package com.example.calculator.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calculator.R;

/*

Code snippets from https://gist.github.com/haroldolivieri/282831e2fef2cae3c5b8

 */

public class EquationFormulaAutoSize extends  AlignTextView{

    private final float maxTextSize;
    private final float minTextSize;
    private final float stepTextSize;
    private int widthConstraint = -1;

    private OnTextSizeChangedListener onTextSizeChangedListener;
    private final TextPaint textPaint;


    public interface OnTextSizeChangedListener{
        void onTextSizeChanged(TextView textView, float size);
    }

    public EquationFormulaAutoSize(@NonNull Context context) {
        this(context, null);
    }

    public EquationFormulaAutoSize(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EquationFormulaAutoSize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new TextPaint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EquationFormulaAutoSize, defStyleAttr, 0);
        maxTextSize = a.getDimension(R.styleable.EquationFormulaAutoSize_maxTextSize, getTextSize());
        minTextSize = a.getDimension(R.styleable.EquationFormulaAutoSize_minTextSize, getTextSize());
        stepTextSize = a.getDimension(R.styleable.EquationFormulaAutoSize_stepTextSize, (maxTextSize - minTextSize) / 4.0f);
        a.recycle();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!isLaidOut()){
            setTextSizeWithListener(0, maxTextSize, false);
            setMinimumHeight(getLineHeight() + getCompoundPaddingBottom() + getCompoundPaddingTop());
        }

        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        if(getMinimumWidth() != width)
            setMinWidth(width);
        widthConstraint = View.MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        float tempTextSize = getVariableTextSize(getText().toString());
        if(getTextSize() != tempTextSize)
            setTextSizeWithListener(0, tempTextSize, false);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public float getVariableTextSize(String text) {
        if (widthConstraint < 0 || maxTextSize <= minTextSize) {
            return getTextSize();
        }
        textPaint.set(getPaint());
        float lastFitTextSize = minTextSize;
        while(true){
            float temp = maxTextSize;
            if(lastFitTextSize >= temp){
                break;
            }
            textPaint.setTextSize(Math.min(stepTextSize + lastFitTextSize, temp));
            if (Layout.getDesiredWidth(text, textPaint) > widthConstraint){
                break;
            }else{
                lastFitTextSize = textPaint.getTextSize();
            }
        }
        return lastFitTextSize;
    }

    private void setTextSizeWithListener(int unit, float size, boolean notify){
        float oldSize = getTextSize();
        super.setTextSize(unit,size);
        if(notify && onTextSizeChangedListener != null){
            if(getTextSize() != oldSize)
                onTextSizeChangedListener.onTextSizeChanged(this, oldSize);
        }
    }

    public void setTextSize(int unit, float size){
        setTextSizeWithListener(unit, size, true);
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setTextSize(0, getVariableTextSize(text.toString()));
    }
}

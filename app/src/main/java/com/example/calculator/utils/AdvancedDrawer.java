package com.example.calculator.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.calculator.R;

public class AdvancedDrawer extends LinearLayout {
    private static final String TAG = "AdvancedDrawer";
    private final float cornerSize = getResources().getDimension(R.dimen.c_radius_r12);
    private final Paint paint = new Paint();
    private final int shadowSize = getResources().getDimensionPixelOffset(R.dimen.shadow_size);

    public AdvancedDrawer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.paint.setColor(getResources().getColor(R.color.startColor));
        this.paint.setAntiAlias(true);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        float top = (float) shadowSize;
        float width = (float) getWidth();
        float height = (float) getHeight();

        this.paint.setShadowLayer(shadowSize, 0.0f, 10.0f, getResources().getColor(R.color.shadowColor));
        RectF rectF = new RectF(0.0f, top, width, height);

        canvas.drawRoundRect(rectF, cornerSize, cornerSize, paint);
        paint.clearShadowLayer();
        rectF.set(0.0f, ((float) (shadowSize * 2) + shadowSize), width, height);
        canvas.drawRect(rectF, paint);
        super.dispatchDraw(canvas);
    }
}

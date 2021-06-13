package com.mhd.elemantary.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by MH D on 2018-04-06.
 */

public class CircleIndicator extends IndicatorBase {

    public CircleIndicator(Context context) {
        super(context);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected IndicatorType getType() {
        return IndicatorType.CIRCLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint defaultPaint = getFillPaint(defaultColor);

        Paint selectedPaint = getFillPaint(selectedColor);

        Paint strokePaint = getStrokePaint(strokeColor);

        if (itemCount <= 0)
            return;

        Rect rect = canvas.getClipBounds();
        float y = rect.centerY();
        float x = radius + strokeWidth;

        for (int i = 0; i < itemCount; i++) {
            canvas.drawCircle(x, y, radius, i == selectPosition ? selectedPaint : defaultPaint);
            canvas.drawCircle(x, y, radius, strokePaint);
            x = x + radius + gap + radius;
        }

    }
}

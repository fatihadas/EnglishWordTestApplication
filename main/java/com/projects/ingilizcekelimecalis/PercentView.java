package com.projects.ingilizcekelimecalis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PercentView extends View {

    public PercentView(Context context) {
        super(context);
        init();
    }

    public PercentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PercentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        //paint.setColor(getContext().getResources().getColor(R.color.lightblue));
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        bgpaint = new Paint();
        //bgpaint.setColor(getContext().getResources().getColor(R.color.darkblue));
        bgpaint.setColor(Color.RED);
        bgpaint.setAntiAlias(true);
        bgpaint.setStyle(Paint.Style.FILL);
        rect = new RectF();
    }

    Paint paint;
    Paint bgpaint;
    RectF rect;
    float percentage = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw background circle anyway
        int left = 0;
        int width = getWidth();
        int top = 0;
        rect.set(left, top, left + width, top + width);
        canvas.drawArc(rect, -90, 360, true, bgpaint);
        if (percentage != 0) {
            canvas.drawArc(rect, -90, (360 * percentage), true, paint);
        }
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage / 100;
        invalidate();
    }
}
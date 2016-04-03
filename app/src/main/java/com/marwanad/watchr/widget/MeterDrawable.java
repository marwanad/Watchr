package com.marwanad.watchr.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class draws a colorful graphical level indicator similar to an
 * LED VU bar graph.
 *
 * This is a user defined View UI element that contains a ShapeDrawable, which
 * means it can be placed using in the XML UI configuration and updated
 * dynamically at runtime.
 *
 * To set the level, use setLevel(level). Level should be in the range
 * [0.0 ; 1.0].
 *
 * To change the number of segments or colors, change the segmentColors array.
 *
 * @author Trausti Kristjansson
 *
 */
public class MeterDrawable extends View
{

    private android.graphics.drawable.ShapeDrawable mDrawable;
    private double mLevel = 0.1;

    final int[] segmentColors = {
            0xff5555ff,
            0xff5555ff,
            0xff00ff00,
            0xff00ff00,
            0xff00ff00,
            0xff00ff00,
            0xffffff00,
            0xffffff00,
            0xffff0000,
            0xffff0000};
    final int segmentOffColor = 0xff555555;

    public MeterDrawable(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initBarLevelDrawable();
    }

    public MeterDrawable(Context context)
    {
        super(context);
        initBarLevelDrawable();
    }

    /**
     * Set the bar level. The level should be in the range [0.0 ; 1.0], i.e.
     * 0.0 gives no lit LEDs and 1.0 gives full scale.
     *
     * @param level the LED level in the range [0.0 ; 1.0].
     */
    public void setLevel(double level)
    {
        mLevel = level;
        invalidate();
    }

    public double getLevel()
    {
        return mLevel;
    }

    private void initBarLevelDrawable()
    {
        mLevel = 0.1;
    }

    private void drawBar(Canvas canvas)
    {
        int padding = 5; // Padding on both sides.
        int x = 0;
        int y = 10;

        int width = (int) (Math.floor(getWidth() / segmentColors.length))
                - (2 * padding);
        int height = 50;

        mDrawable = new ShapeDrawable(new RectShape());
        for (int i = 0; i < segmentColors.length; i++) {
            x = x + padding;
            if ((mLevel * segmentColors.length) > (i + 0.5)) {
                mDrawable.getPaint().setColor(segmentColors[i]);
            }
            else {
                mDrawable.getPaint().setColor(segmentOffColor);
            }
            mDrawable.setBounds(x, y, x + width, y + height);
            mDrawable.draw(canvas);
            x = x + width + padding;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        drawBar(canvas);
    }
}

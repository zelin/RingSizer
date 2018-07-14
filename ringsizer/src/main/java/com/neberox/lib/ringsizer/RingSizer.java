package com.neberox.lib.ringsizer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Umar on 24/05/2018.
 */

public class RingSizer extends View
{
    public static final String MEASURE_UNIT_MM   = "mm";
    public static final String MEASURE_UNIT_CM   = "cm";
    public static final String MEASURE_UNIT_IN   = "in";


    private static final String TAG = RingSizer.class.getSimpleName();

    // Diameter of the ring. Should be in mm
    private float diameter = 9.91f;

    // Stroke width of the circle upon which ring will be placed
    private float mRingStrokeWidth  = 2.0f;
    // Stroke width of the arrows
    private float mArrowStrokeWidth = 1.0f;
    // Stroke width of the lines of the grid
    private float mLinesStrokeWidth = 1.0f;

    private int mRingStrokeColor  = Color.BLACK;
    // Color of the arrows drawn. To remove color set UIColor.clear
    private int mArrowStrokeColor = Color.GRAY;
    // Color of the grid lines drawn. To remove color set UIColor.clear
    private int mLinesStrokeColor = Color.GRAY;


    // Color of the text of textLabel.
    private int mTextColor   = Color.BLACK;
    // Color of the background of textLabel.
    private int mTextBgColor = Color.GRAY;

    // Font of the textLabel. Default is 12 sp
    private float fontSize = 12.0f;

    // left and right text Padding to add in textLabel
    private float textPaddingWidth = 10.0f;
    // top and bottom text Padding to add in textLabel
    private float textPaddingHeight = 10.0f;

    private Paint paint = new Paint();
    private Path path   = new Path();

    public RingSizer(Context context)
    {
        super(context);
    }

    public RingSizer(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.RingSizer));

    }

    public RingSizer(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.RingSizer));
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a)
    {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

        // We transform the default values from DIP to pixels
        mRingStrokeWidth  = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRingStrokeWidth, metrics);
        mLinesStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLinesStrokeWidth, metrics);
        mArrowStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mArrowStrokeWidth, metrics);

        mRingStrokeWidth  = (int) a.getDimension(R.styleable.RingSizer_ringStrokeWidth , mRingStrokeWidth);
        mLinesStrokeWidth = (int) a.getDimension(R.styleable.RingSizer_lineStrokeWidth , mLinesStrokeWidth);
        mArrowStrokeWidth = (int) a.getDimension(R.styleable.RingSizer_arrowStrokeWidth, mArrowStrokeWidth);

        mRingStrokeColor  = a.getColor(R.styleable.RingSizer_ringStrokeColor, mRingStrokeColor);
        mLinesStrokeColor = a.getColor(R.styleable.RingSizer_lineStrokeColor, mLinesStrokeColor);
        mArrowStrokeColor = a.getColor(R.styleable.RingSizer_arrowStrokeColor, mArrowStrokeColor);

        diameter = TypedValue.applyDimension(5, this.diameter, metrics);

        // Recycle
        a.recycle();
    }

    public void setDiameter(float diameter)
    {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        this.diameter = TypedValue.applyDimension(5, diameter, metrics);
        invalidate();
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float radius = diameter/2;
        float distance = radius;

        float midX = getWidth() / 2;
        float midY = getHeight() / 2;

        this.path.reset();
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(mLinesStrokeWidth);
        this.paint.setColor(mLinesStrokeColor);

        // Line 1
        this.path.moveTo(getLeft(), midY - this.diameter);
        this.path.lineTo(getLeft() + getWidth(), midY - this.diameter);

        // Line 2
        this.path.moveTo(getLeft(), midY + this.diameter);
        this.path.lineTo(getLeft() + getWidth(), midY + this.diameter);

        // Line 3
        this.path.moveTo(midX - this.diameter, getTop());
        this.path.lineTo(midX - this.diameter, getTop() + getHeight());

        // Line 4
        this.path.moveTo(midX + this.diameter, getTop());
        this.path.lineTo(midX + this.diameter, getTop() + getHeight());

        canvas.drawPath(this.path, this.paint);

        // Create ring
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(mRingStrokeWidth);
        this.paint.setColor(mRingStrokeColor);
        canvas.drawCircle(midX, midY, radius - 0.5F * mRingStrokeWidth, this.paint);

        this.path.reset();


        this.paint.setStrokeWidth(mArrowStrokeWidth);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(mArrowStrokeColor);

        this.path.moveTo(mArrowStrokeWidth + (midX - this.diameter), midY - 0.55F * mArrowStrokeWidth);
        this.path.lineTo(midX - this.diameter, midY);
        this.path.lineTo(mArrowStrokeWidth + (midX - this.diameter), midY + 0.55F * mArrowStrokeWidth);
        this.path.moveTo(midX - this.diameter, midY);
        this.path.lineTo(midX + this.diameter, midY);
        this.path.moveTo(midX + this.diameter - mArrowStrokeWidth, midY - 0.55F * mArrowStrokeWidth);
        this.path.lineTo(midX + this.diameter, midY);
        this.path.lineTo(midX + this.diameter - mArrowStrokeWidth, midY + 0.55F * mArrowStrokeWidth);
        canvas.drawPath(this.path, this.paint);

        float mDiff = this.diameter - mArrowStrokeWidth;

        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(mLinesStrokeColor);
        canvas.drawRect(midX - mDiff - 0.3F * mArrowStrokeWidth, midY - 0.3F * mArrowStrokeWidth, midX - mDiff + 0.3F * mArrowStrokeWidth, midY + 0.3F * mArrowStrokeWidth, this.paint);

        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(mLinesStrokeColor);
        this.path.reset();
        this.path.moveTo(midX - mDiff - 0.5F * mArrowStrokeWidth, midY - 1.4F * mArrowStrokeWidth);

        canvas.save();
        canvas.rotate(8.0F, midX - mDiff, midY - 0.35F * mArrowStrokeWidth);
        canvas.drawPath(this.path, this.paint);
        canvas.restore();
    }
}

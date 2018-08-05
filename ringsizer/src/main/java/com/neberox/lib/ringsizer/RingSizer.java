package com.neberox.lib.ringsizer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

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

    private float mmConstant = 0;
    private Paint paint = new Paint();
    private Path path   = new Path();
    private String code = "";

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

    public void setSize(float diameter, String code)
    {
        this.diameter = diameter;
        this.code = code;
        invalidate();
    }


    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);


        final DisplayMetrics dm = getResources().getDisplayMetrics();
        mmConstant = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, getResources().getDisplayMetrics());;

        // Diameter is in millimeter
        float radius = diameter/2;
        // converting diameter to number of pixel points
        float distance = radius / mmConstant;

        float midX = getWidth() / 2;
        float midY = getHeight() / 2;

        this.path.reset();
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(mLinesStrokeWidth);
        this.paint.setColor(mLinesStrokeColor);

        // Line 1
        this.path.moveTo(0, midY - distance);
        this.path.lineTo(getWidth(), midY - distance);

        // Line 2
        this.path.moveTo(0, midY + distance);
        this.path.lineTo(getWidth(), midY + distance);

        // Line 3
        this.path.moveTo(midX - distance, 0);
        this.path.lineTo(midX - distance, getHeight());

        // Line 4
        this.path.moveTo(midX + distance, 0);
        this.path.lineTo(midX + distance, getHeight());

        canvas.drawPath(this.path, this.paint);

        // Create ring
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(mRingStrokeWidth);
        this.paint.setColor(mRingStrokeColor);
        canvas.drawCircle(midX, midY, radius + ( 0.5f * mRingStrokeWidth), this.paint);

        this.path.reset();

        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeWidth(Color.TRANSPARENT);
        this.paint.setColor(mTextColor);

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(fontSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        RectF textRect = getTextBackgroundSize(midX, midY, code, textPaint)
        canvas.drawRect(textRect, this.paint);

        this.path.reset();

        this.paint.setStrokeWidth(mArrowStrokeWidth);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(mArrowStrokeColor);

        this.path.moveTo((midX - distance), midY);
        this.path.lineTo(textRect.left - 5, midY);

        this.path.lineTo(mArrowStrokeWidth + (midX - this.diameter), midY + 0.55F * mArrowStrokeWidth);
        this.path.moveTo(midX - this.diameter, midY);
        this.path.lineTo(midX + this.diameter, midY);
        this.path.moveTo(midX + this.diameter - mArrowStrokeWidth, midY - 0.55F * mArrowStrokeWidth);
        this.path.lineTo(midX + this.diameter, midY);
        this.path.lineTo(midX + this.diameter - mArrowStrokeWidth, midY + 0.55F * mArrowStrokeWidth);
        canvas.drawPath(this.path, this.paint);

        float mDiff = distance - mArrowStrokeWidth;

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

    public static ArrayList<RingSizeModel> getRingSizes(Context mContext)
    {
        ArrayList<RingSizeModel> sizes = new ArrayList<>();
        try {

            InputStream is = mContext.getAssets().open("sizes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            JSONObject obj = new JSONObject(new String(buffer, "UTF-8")).getJSONObject("data");
            JSONArray objArray = obj.getJSONArray("sizes");
            for(int i = 0; i < objArray.length(); i++)
            {
                JSONObject ringSize = objArray.getJSONObject(i);
                RingSizeModel newRingSize = new RingSizeModel(
                        Float.valueOf(ringSize.getString("diameter")),
                        ringSize.getString("usa"),
                        ringSize.getString("australia"),
                        ringSize.getString("europe"),
                        ringSize.getString("japan")
                );

                sizes.add(newRingSize);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sizes;
    }


    private @NonNull
    Rect getTextBackgroundSize(float x, float y, @NonNull String text, @NonNull TextPaint paint)
    {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float halfTextLength = paint.measureText(text) / 2 + 5;
        return new Rect((int) (x - halfTextLength), (int) (y + fontMetrics.top), (int) (x + halfTextLength), (int) (y + fontMetrics.bottom));
    }
}

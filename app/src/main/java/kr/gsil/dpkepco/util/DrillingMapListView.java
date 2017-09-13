package kr.gsil.dpkepco.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import kr.gsil.dpkepco.R;

/**
 * Created by gsil on 2017. 9. 8..
 *  kr.gsil.dpkepco.util.DrillingMapListView
 */

public class DrillingMapListView extends ListView implements Runnable{
    private Context context;

    Handler mHandler = new Handler();
    static final long FRAME_TIME = 100;
    static final int FRAME_MAX = 11;
    int mProgress = FRAME_MAX;

    private RectF sectionPositionRect;
    private Paint backgroundPaint;
    private Paint sectionBackgroundPaint;

    private static Float[] diameters = null;
    private static Integer[] colors = null;
    private static Paint[] circlePaints = null;
    private static Paint transPaint = new Paint();
    private static Paint donutPaint = new Paint();
    private static Canvas mCanvas = null;

    private int baseIndex = 10;
    private int currentIndex = 0;
    private int drillingMetter = 0;
    private float realRate = 1.0f;

    public final static float BASE_360 = 3.0f;
    public final static int BASE_X = 220;
    public final static int BASE_Y = 400;
    public final static int BOX_WIDTH = 484;
    public final static int BOX_HEIGHT = 2585;
    public final static int REAL_HEIGHT = 3376;


    public final static double GAP_WIDTH_BASE = 0.1433;
    public final static double GAP_HEIGHT_BASE = 0.7656;
    public DrillingMapListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public DrillingMapListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // initialize variables
        sectionPositionRect = new RectF();

        backgroundPaint = new Paint();
        sectionBackgroundPaint = new Paint();

        backgroundPaint.setAntiAlias(true);
        sectionBackgroundPaint.setAntiAlias(true);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DrillingMapListView, 0, 0);
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_1_color,0x33EB2227));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_2_color,0x33EB2227));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_3_color,0x33EB2227));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_4_color,0x7fFFFFFF));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_5_color,0x7fFFFFFF));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_6_color,0x33EB2227));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_7_color,0x33EB2227));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_8_color,0x33EB2227));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_9_color,0x33EB2227));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_10_color,0x33EB2227));
        colorList.add(array.getColor(R.styleable.DrillingMapListView_circle_11_color,0x33EB2227));
        colors = new Integer[colorList.size()];
        colorList.toArray(colors);

        ArrayList<Float> diameterList = new ArrayList<>();
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_1_diameter,12f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_2_diameter,18f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_3_diameter,32f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_4_diameter,40f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_5_diameter,53f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_6_diameter,59f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_7_diameter,66f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_8_diameter,73f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_9_diameter,82f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_10_diameter,93f));
        diameterList.add(array.getFloat(R.styleable.DrillingMapListView_circle_11_diameter,100f));
        diameters = new Float[diameterList.size()];
        diameterList.toArray(diameters);

        circlePaints = new Paint[diameterList.size()];
        for(int i = 0; i < circlePaints.length; i++){
            circlePaints[i] = new Paint();
            circlePaints[i].setColor(colors[i]);
            circlePaints[i].setAntiAlias(true);
        }

        array.recycle();
        currentIndex = baseIndex;

        mHandler.postDelayed(this, FRAME_TIME);
    }

    public void setPositionInfo(int drillingMetter, float realRate){
        if(drillingMetter >= 3376) this.drillingMetter = 3376;
        else this.drillingMetter = drillingMetter;
        this.realRate = realRate;
        //Log.e("setPositionInfo","drillingMetter = "+drillingMetter + " realRate = "+realRate + " getDensity() = "+getDensity());
        //mCanvas.
    }
    //canvas.drawCircle(300, 300, 40, paint); // 원의중심 x,y, 반지름,paint
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        float startX = (BASE_X/BASE_360 + BOX_WIDTH/BASE_360) * getDensity();
        float startY = BASE_Y/BASE_360 * getDensity();
        int gapY = (int)(708*realRate - startX);
        int gapX = (int)(401*realRate - startY);

        //Log.e("dispatchDraw 01","gapX = "+gapX + " gapY = "+gapY+" startX = "+startX + " startY = "+startY);
        if(drillingMetter >= 0){
            startY = gapX + startY + (float)(((drillingMetter * GAP_HEIGHT_BASE)/BASE_360) * getDensity());
            startX = gapY + startX - (float)(((drillingMetter * GAP_WIDTH_BASE)/BASE_360) * getDensity());

            //Log.e("dispatchDraw 02","startX = "+startX + " startY = "+startY);

            for(int i = circlePaints.length-1; i >= 0; i--){
                if(i == mProgress){
                    donutPaint.setColor(0x9fFFFFFF);
                    donutPaint.setAntiAlias(true);
                    transPaint.setColor(getResources().getColor(android.R.color.transparent));
                    transPaint.setAntiAlias(true);
                    canvas.drawCircle(startX, startY, diameters[FRAME_MAX-1-i]*getDensity(), donutPaint);
                    if(FRAME_MAX-1-i-1 >= 0)canvas.drawCircle(startX, startY, diameters[FRAME_MAX-1-i-1]*getDensity(), transPaint);
                }
                else canvas.drawCircle(startX, startY, diameters[i]*getDensity(), circlePaints[i]);
            }
        }
    }

    private float getDensity() {
        return context.getResources().getDisplayMetrics().density;
    }

    private float getScaledDensity() {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    @Override
    public void run() {
        mProgress -= 1;
        if(mProgress < 0) mProgress = FRAME_MAX;
        this.invalidate();
        mHandler.postDelayed(this, FRAME_TIME);
    }
}

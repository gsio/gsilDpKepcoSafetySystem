package kr.gsil.dpkepco.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
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
    public final static String ALERT_PASS_STRATA = "지질 이상대";

    public int txtBoxOuterColor3 = 0x1aFFFFFF;
    public int txtBoxOuterColor2 = 0x7fFFFFFF;
    public int txtBoxOuterColor1 = 0xffFFFFFF;
    private static Paint textPaintBasic = new Paint();
    private static Paint textPaintRed = new Paint();
    private static Paint boxPaint1 = new Paint();
    private static Paint boxPaint2 = new Paint();
    private static Paint boxPaint3 = new Paint();


    float boxX[] = {0f, 0f, 0f, 0f, 0f};
    float boxY[] = {0f, 0f, 0f, 0f, 0f};
    float boxW[] = {0f, 0f, 0f};
    float boxH[] = {0f, 0f, 0f};

    int scaledSize = getResources().getDimensionPixelSize(R.dimen.myFontSize);
    String strPassStrata = "미지정 지층";

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

    public void setPositionInfo(int drillingMetter, float realRate, String strPassStrata){
        if(drillingMetter >= 3376) this.drillingMetter = 3376;
        else this.drillingMetter = drillingMetter;
        this.realRate = realRate;
        this.strPassStrata = strPassStrata;
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

        //drillingMetter = 2499;

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

            //221,-187, 67,374
            //225,-192,78,385
            //210,-200,90,400

            //지질 이상대 통과중
            drawTextInfoTop(canvas, startX, startY);
            //누계 굴진량 1,000m
            drawTextInfoButtom(canvas, startX, startY, drillingMetter);
        }
    }

    private void drawTextInfoTop(Canvas canvas, float x, float y){
        int startTX = 260;
        boxX[2] = (float)(x + (startTX/BASE_360) * getDensity());
        boxY[2] = (float)(y + (-275/BASE_360) * getDensity());
        boxW[2] = (float)(boxX[2] + (124/BASE_360) * getDensity());
        boxH[2] = (float)(boxY[2] + (550/BASE_360) * getDensity());
        startTX = startTX + 8;
        boxX[1] = (float)(x + (startTX/BASE_360) * getDensity());
        boxY[1] = (float)(y + (-265/BASE_360) * getDensity());
        boxW[1] = (float)(boxX[1] + (107/BASE_360) * getDensity());
        boxH[1] = (float)(boxY[1] + (530/BASE_360) * getDensity());
        startTX = startTX + 7;
        boxX[0] = (float)(x + (startTX/BASE_360) * getDensity());
        boxY[0] = (float)(y + (-257/BASE_360) * getDensity());
        boxW[0] = (float)(boxX[0] + (92/BASE_360) * getDensity());
        boxH[0] = (float)(boxY[0] + (514/BASE_360) * getDensity());

        boxPaint3.setColor(txtBoxOuterColor3);
        boxPaint3.setStyle(Paint.Style.FILL);
        boxPaint3.setAntiAlias(true);

        boxPaint2.setColor(txtBoxOuterColor2);
        boxPaint2.setStyle(Paint.Style.FILL);
        boxPaint2.setAntiAlias(true);

        boxPaint1.setColor(txtBoxOuterColor1);
        boxPaint1.setStyle(Paint.Style.FILL);
        boxPaint1.setAntiAlias(true);

        canvas.drawRect(boxX[2], boxY[2], boxW[2], boxH[2], boxPaint3);
        canvas.drawRect(boxX[1], boxY[1], boxW[1], boxH[1], boxPaint2);
        canvas.drawRect(boxX[0], boxY[0], boxW[0], boxH[0], boxPaint1);

        String txtStr2 = " 통과중";



        textPaintBasic.setAntiAlias(true);
        textPaintBasic.setTextSize(scaledSize);
        textPaintBasic.setTypeface(Typeface.DEFAULT);
        textPaintBasic.setTextAlign(Paint.Align.RIGHT);
        textPaintBasic.setTypeface(Typeface.create((String)null, Typeface.BOLD));
        if(strPassStrata.equals(ALERT_PASS_STRATA)) textPaintBasic.setColor(0xffFF0000);
        else textPaintBasic.setColor(0xff000000);

        Rect bounds = new Rect();
        Rect bounds2 = new Rect();
        String text = strPassStrata + txtStr2;
        textPaintBasic.getTextBounds(text, 0, text.length(), bounds);
        textPaintBasic.getTextBounds(strPassStrata, 0, strPassStrata.length(), bounds2);

        float txtX = (float)(boxX[0] + ( boxW[0] - boxX[0] - scaledSize)/2);
        float textY = (float)(y - bounds.width()/2 + bounds2.width()) ;
        canvas.rotate(90, txtX, textY);
        canvas.drawText(strPassStrata , txtX , textY, textPaintBasic);



        textPaintBasic.setAntiAlias(true);
        textPaintBasic.setTextSize(scaledSize);
        textPaintBasic.setTextAlign(Paint.Align.LEFT);
        textPaintBasic.setTypeface(Typeface.create((String)null, Typeface.BOLD));
        textPaintBasic.setColor(0xff000000);
        //canvas.rotate(90, boxX[0], y);
        canvas.drawText(txtStr2, txtX, textY, textPaintBasic);
        canvas.rotate(-90, txtX, textY);
    }

    private void drawTextInfoButtom(Canvas canvas, float x, float y, int drillingMetter){

        if(drillingMetter < 2500){
            int startBX = -380;
            boxX[2] = (float)(x + (startBX/BASE_360) * getDensity());
            boxY[2] = (float)(y + (-275/BASE_360) * getDensity());
            boxW[2] = (float)(boxX[2] + (124/BASE_360) * getDensity());
            boxH[2] = (float)(boxY[2] + (550/BASE_360) * getDensity());
            startBX = startBX + 9;
            boxX[1] = (float)(x + (startBX/BASE_360) * getDensity());
            boxY[1] = (float)(y + (-265/BASE_360) * getDensity());
            boxW[1] = (float)(boxX[1] + (107/BASE_360) * getDensity());
            boxH[1] = (float)(boxY[1] + (530/BASE_360) * getDensity());
            startBX = startBX + 9;
            boxX[0] = (float)(x + (startBX/BASE_360) * getDensity());
            boxY[0] = (float)(y + (-257/BASE_360) * getDensity());
            boxW[0] = (float)(boxX[0] + (92/BASE_360) * getDensity());
            boxH[0] = (float)(boxY[0] + (514/BASE_360) * getDensity());
        }else{
            int startBX = 120;
            boxX[2] = (float)(x + (startBX/BASE_360) * getDensity());
            boxY[2] = (float)(y + (-275/BASE_360) * getDensity());
            boxW[2] = (float)(boxX[2] + (124/BASE_360) * getDensity());
            boxH[2] = (float)(boxY[2] + (550/BASE_360) * getDensity());
            startBX = startBX + 8;
            boxX[1] = (float)(x + (startBX/BASE_360) * getDensity());
            boxY[1] = (float)(y + (-265/BASE_360) * getDensity());
            boxW[1] = (float)(boxX[1] + (107/BASE_360) * getDensity());
            boxH[1] = (float)(boxY[1] + (530/BASE_360) * getDensity());
            startBX = startBX + 7;
            boxX[0] = (float)(x + (startBX/BASE_360) * getDensity());
            boxY[0] = (float)(y + (-257/BASE_360) * getDensity());
            boxW[0] = (float)(boxX[0] + (92/BASE_360) * getDensity());
            boxH[0] = (float)(boxY[0] + (514/BASE_360) * getDensity());
        }


        boxPaint3.setColor(txtBoxOuterColor3);
        boxPaint3.setStyle(Paint.Style.FILL);
        boxPaint3.setAntiAlias(true);

        boxPaint2.setColor(txtBoxOuterColor2);
        boxPaint2.setStyle(Paint.Style.FILL);
        boxPaint2.setAntiAlias(true);

        boxPaint1.setColor(txtBoxOuterColor1);
        boxPaint1.setStyle(Paint.Style.FILL);
        boxPaint1.setAntiAlias(true);

        canvas.drawRect(boxX[2], boxY[2], boxW[2], boxH[2], boxPaint3);
        canvas.drawRect(boxX[1], boxY[1], boxW[1], boxH[1], boxPaint2);
        canvas.drawRect(boxX[0], boxY[0], boxW[0], boxH[0], boxPaint1);

        String numComma = String.format("%,d", drillingMetter);

        String txtStr = "누계 굴진량: " + numComma + "m";
        textPaintBasic.setAntiAlias(true);
        textPaintBasic.setTextSize(scaledSize);
        textPaintBasic.setTextAlign(Paint.Align.CENTER);
        textPaintBasic.setTypeface(Typeface.create((String)null, Typeface.BOLD));
        textPaintBasic.setColor(0xff000000);


        //textPaintRed = new Paint();
        //boxPaint = new Paint();
        float txtX = (float)(boxX[0] + ( boxW[0] - boxX[0] - scaledSize)/2);
        canvas.rotate(90, txtX, y);
        canvas.drawText(txtStr, txtX, y, textPaintBasic);
    }

    private float getDensity() {
        return context.getResources().getDisplayMetrics().density;
    }

    @Override
    public void run() {
        mProgress -= 1;
        if(mProgress < 0) mProgress = FRAME_MAX;
        this.invalidate();
        mHandler.postDelayed(this, FRAME_TIME);
    }
}

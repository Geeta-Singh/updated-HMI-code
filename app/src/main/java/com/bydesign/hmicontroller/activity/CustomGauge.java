package com.bydesign.hmicontroller.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Toast;

import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.model.CurrentDataModel;
import com.bydesign.hmicontroller.model.ParameterForGaugeView;

import java.util.ArrayList;

import info.bydesign.hmicontroller.R;

public class CustomGauge extends View  {

    private static final int DEFAULT_LONG_POINTER_SIZE = 1;

    private Paint mPaint,tPaint,apaint;
    private float mStrokeWidth;
    private int mStrokeColor;
    private RectF mRect;
    private String mStrokeCap;
    private int mStartAngel;
    private int mSweepAngel;
    private int mStartValue;
    private int mEndValue;
    private int mValue;
    private double mPointAngel;
    private float mRectLeft;
    private float mRectTop;
    private float mRectRight;
    private float mRectBottom;
    private int mPoint;
    private int mPointSize;
    private int mPointStartColor;
    private int mPointEndColor;
    private int mDividerColor;
    private int mDividerSize;
    private int mDividerStepAngel;
    private int mDividersCount;
    private boolean mDividerDrawFirst;
    private boolean mDividerDrawLast;


    public CustomGauge(Context context) {
        super(context);

        setVerticalScrollBarEnabled(true);

        mStrokeWidth = 20;
        mStrokeColor =Color.GRAY;
        mStrokeCap = "BUTT";

        // angel start and sweep (opposite direction 0, 270, 180, 90)
        mStartAngel =180;
        mSweepAngel = 180;//

        // scale (from mStartValue to mEndValue)
        mStartValue = 0;
        mEndValue = 5000;//range


        mPointStartColor = Color.GREEN;
        mPointEndColor = Color.RED;
        // divider options

        mDividerColor = Color.BLACK;

        mDividerDrawFirst = true  ;

        mDividerDrawLast =  true;



        mPointAngel = 2.7;
        System.out.println("mPoint angle " + mPointAngel);

        // calculating divider step



            mDividerSize=1;
            mDividersCount =10;
            mDividerStepAngel =18;
            System.out.println("mPoint asixe " + mDividerSize);

        init();

    }



    public CustomGauge(Context context, AttributeSet attrs) {
        super(context, attrs);

    }



    private void init() {
        //main Paint
        System.out.println("in init");
        mPaint = new Paint();
        tPaint=new Paint();
        apaint=new Paint();
        tPaint.setColor(Color.BLUE);
        tPaint.setTextSize(18);

        apaint.setColor(Color.BLACK);
        //argb(255,46,97,34)
        apaint.setTextSize(18);
     //.   apaint.setTypeface(Typeface.DEFAULT_BOLD,Typeface.BOLD);
       // apaint.setTypeface(Typeface.DEFAULT, );
        mPaint.setColor(mStrokeColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        if (!TextUtils.isEmpty(mStrokeCap)) {
            if (mStrokeCap.equals("BUTT"))
                mPaint.setStrokeCap(Paint.Cap.BUTT);
            else if (mStrokeCap.equals("ROUND"))
                mPaint.setStrokeCap(Paint.Cap.ROUND);
        } else
            mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStyle(Paint.Style.STROKE);
        mRect = new RectF();

        mValue = mStartValue;
        mPoint = mStartAngel;
    }
    float paddingLeft=0;// = paddingleft+;
    float paddingRight=0; //= peddingright;
    float paddingTop=0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        DatabaseHandler db = new DatabaseHandler(getContext());
        ArrayList<ParameterForGaugeView> gaugeViews = new ArrayList<ParameterForGaugeView>();
        gaugeViews = db.getParameterForGaugeView();
       Long timestamp_array = db.CurrentTime();
        ArrayList<CurrentDataModel> Data = db.Currentparamvalue(timestamp_array);
      /* Bitmap bitmap = Bitmap.createBitmap(600,8000, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);*/
        try {
            int k=0,l=0;
           // canvas.drawText(" A1 : Alarm1 , A2: Alarm2", 150, 110, apaint);
            for (int i = 0; i < gaugeViews.size(); i++) {
                setVerticalScrollBarEnabled(true);
                //  System.out.println("in draw loop"+gaugeViews.size());

                paddingLeft = 80;
                paddingRight = 20;
                paddingTop = 100;


                float width;//= 250 - (paddingLeft + paddingRight);
                float height;
                float paddingBottom = 0;
                // if(i==0) {

                width = 300 - (paddingLeft + paddingRight);
                height = 300 - (paddingTop + paddingBottom);


                float radius = (width > height ? width / 2 : height / 2);

                float temp = 0;
                if(i<=4) {
                    if (i == 0) {
                        mRectLeft = width / 2 - radius + paddingLeft;
                        mRectRight = width / 2 - radius + paddingLeft + width;
                        System.out.println("1right " + mRectRight);
                        System.out.println("1left " + mRectLeft);
                        // temp = mRectBottom;
                    } else {
                        mRectLeft = mRectRight + 40;
                        mRectRight = mRectLeft + width;

                    }
                    mRectTop = height / 2 - radius + paddingTop;
                    mRectBottom = height / 2 - radius + paddingTop + height;
                   // System.out.print("bottom last update"+mRectBottom);
                }
                else if(i>4&&i<=9){

                    if (k == 0) {
                        mRectLeft = width / 2 - radius + paddingLeft;
                        mRectRight = width / 2 - radius + paddingLeft + width;
                       k++;

                    } else {
                        mRectLeft = mRectRight + 40;
                        mRectRight = mRectLeft + width;

                    }
                    mRectTop = 320;
                    System.out.println("top" +mRectTop);
                    mRectBottom =320 + height;
                    System.out.println("\n bottom" +mRectBottom);

                }
                else if(i>9&&i<=14){

                    if (l == 0) {
                        mRectLeft = width / 2 - radius + paddingLeft;
                        mRectRight = width / 2 - radius + paddingLeft + width;
                        l++;

                    } else {
                        mRectLeft = mRectRight + 40;
                        mRectRight = mRectLeft + width;

                    }
                    mRectTop = 570;
                    System.out.println("top" +mRectTop);
                    mRectBottom =570 + height;
                    System.out.println("\n bottom" +mRectBottom);


                }
                else{
                    if (l == 0) {
                        mRectLeft = width / 2 - radius + paddingLeft;
                        mRectRight = width / 2 - radius + paddingLeft + width;
                        l++;

                    } else {
                        mRectLeft = mRectRight + 40;
                        mRectRight = mRectLeft + width;

                    }
                    mRectTop = 590;
                    System.out.println("top" +mRectTop);
                    mRectBottom =5900 + height;
                    System.out.println("\n bottom" +mRectBottom);
                }

                mRect.set(mRectLeft, mRectTop, mRectRight, mRectBottom);
                // if(i==0)
                canvas.drawText(gaugeViews.get(i).getParam(), mRectLeft + 80, mRectTop-20, tPaint);




               mPointSize = Data.get(i).getValue().intValue();

                if (mPointSize>gaugeViews.get(i).getAlarm1().intValue() && mPointSize<gaugeViews.get(i).getAlarm2().intValue())
                {mPointStartColor = Color.YELLOW;//depend on alarm values
                    mPointEndColor = Color.YELLOW;
                }
                else if(mPointSize<gaugeViews.get(i).getAlarm1().intValue()){
                    mPointStartColor = Color.GREEN;//depend on alarm values
                    mPointEndColor = Color.GREEN;
                }
                else{
                    mPointStartColor = Color.RED;//depend on alarm values
                    mPointEndColor = Color.RED;
                }

                mPaint.setColor(mStrokeColor);
                mPaint.setShader(null);

                //this is main arc
                canvas.drawArc(mRect, 180, mPoint, false, mPaint);
                mPointSize=mPointSize/30;
                //this is for poinint to a value
                mPaint.setColor(mPointStartColor);
                mPaint.setShader(new LinearGradient(250, 250, 0, 0, mPointEndColor, mPointStartColor, Shader.TileMode.CLAMP));
                canvas.drawArc(mRect, 180, mPointSize, false, mPaint);

               // mDividerSize=0;
                if (mDividerSize > 0) {
                    mPaint.setColor(mDividerColor);
                    mPaint.setShader(null);
                    int j = mDividerDrawFirst ? 0 : 1;
                    int max = mDividerDrawLast ? mDividersCount + 1 : mDividersCount;
                    for (; j < max; j++) {
                        System.out.print("hi divider \n");
                        canvas.drawArc(mRect, mStartAngel + j * mDividerStepAngel, mDividerSize, false, mPaint);
                    }
                }

                canvas.drawText("" + Data.get(i).getValue()+"("+gaugeViews.get(i).getUnit()+")", mRectRight-130, mRectBottom - 110, tPaint);
                canvas.drawText("A1 : " + gaugeViews.get(i).getAlarm1()+" ,  A2: "+gaugeViews.get(i).getAlarm2(), mRectLeft, mRectBottom-70, apaint);

            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getContext(),"Parameter null",Toast.LENGTH_LONG).show();
        }
    }

}
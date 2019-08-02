package com.android.cusviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin
 *
 */
public class CusHeartView extends View {

    private static final String TAG = "CusHeartView";

    //点的画笔
    private Paint pointPaint;
    //连线的画笔
    private Paint pathPaint;

    //path
    private Path linPath;

    //点的颜色
    private int pointColor;
    //点的半径
    private float circleRadius;
    //线的颜色
    private int linColor;

    //绘制坐标轴的paint
    private Paint xyLinPaint;

    //绘制虚线的画笔
    private Paint benchPaint;

    //绘制y轴刻度的画笔
    private Paint yScalePaint;

    //是否显示x轴
    private boolean isShowX = false;
    //是否显示y轴
    private boolean isShowY = false;

    //宽高
    private float mWidth,mHeight;

    //数据源
    private List<Integer> dataLists = new ArrayList<>();

    public CusHeartView(Context context) {
        super(context);

    }

    public CusHeartView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
    }

    public CusHeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CusHeartView);
        if(typedArray != null){
            pointColor = typedArray.getColor(R.styleable.CusHeartView_cusHeartPointColor,context.getResources().getColor(R.color.colorAccent));
            linColor = typedArray.getColor(R.styleable.CusHeartView_cusLinColor,context.getResources().getColor(R.color.colorAccent));
            circleRadius = typedArray.getDimension(R.styleable.CusHeartView_circleRadius,dp2px(3));
            typedArray.recycle();
        }

        initPaints();
    }

    private void initPaints() {

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setColor(pointColor);
        pointPaint.setAntiAlias(true);

        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setColor(linColor);
        pathPaint.setStrokeWidth(dp2px(1));

        linPath = new Path();

        xyLinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xyLinPaint.setStrokeWidth(dp2px(2));
        xyLinPaint.setStyle(Paint.Style.STROKE);
        xyLinPaint.setColor(Color.WHITE);

        benchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        benchPaint.setStyle(Paint.Style.STROKE);
        benchPaint.setColor(Color.WHITE);
        benchPaint.setStrokeWidth(2f);
        benchPaint.setPathEffect(new DashPathEffect(new float[] {5, 5}, 0));

        yScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yScalePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        yScalePaint.setColor(Color.WHITE);
        yScalePaint.setStrokeWidth(0.5f);
        yScalePaint.setTextSize(dp2px(10));

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //坐标平移
        canvas.translate(0,mHeight);
        canvas.save();
        if(!dataLists.isEmpty()){
            //间隔的宽度
            float mCurrWidth = mWidth / dataLists.size();

            //绘制坐标轴
            drawXYLin(canvas,mCurrWidth);

            //绘制点
            for(int i = 0;i<dataLists.size();i++){
                //绘制点
                canvas.drawCircle(mCurrWidth*i+dp2px(10),-dp2px(dataLists.get(i)),circleRadius,pointPaint);

            }

            //连线
            for(int k = 0;k<dataLists.size();k++){
                if(k == 0){
                    linPath.moveTo(mCurrWidth * k +dp2px(10),-dp2px(dataLists.get(k)));
                }else{
                    linPath.lineTo(mCurrWidth * k +dp2px(10),-dp2px(dataLists.get(k)));
                }
            }
            canvas.drawPath(linPath,pathPaint);




        }
    }

    private void drawXYLin(Canvas canvas,float currWidth) {

        //绘制y的标线
        if(isShowY){
            //60
            canvas.drawLine(dp2px(10),-dp2px(60),mWidth,-dp2px(60),benchPaint);
            canvas.drawText("60",0,-dp2px(60)+getTextHeight(yScalePaint,60+"")/2,yScalePaint);

            //80
            canvas.drawLine(dp2px(10),-dp2px(90),mWidth,-dp2px(90),benchPaint);
            canvas.drawText("90",0,-dp2px(90)+getTextHeight(yScalePaint,80+"")/2,yScalePaint);
            //120
            canvas.drawLine(dp2px(10),-dp2px(120),mWidth,-dp2px(120),benchPaint);
            canvas.drawText("120",0,-dp2px(120)+getTextHeight(yScalePaint,120+"")/2,yScalePaint);
        }


        if(isShowX){
            //x轴
            canvas.drawLine(0,-dp2px(10),mWidth,-dp2px(10),xyLinPaint);
            //x轴刻度及下标
            for(int i = 0;i<dataLists.size();i++){

                canvas.drawLine(currWidth * i +dp2px(10),-dp2px(10),currWidth * i +dp2px(10),-dp2px(15),xyLinPaint);

                canvas.drawText(String.valueOf(i+1),currWidth * i +dp2px(10)-getTextWidth(yScalePaint,String.valueOf(i+1))/2,0,yScalePaint);

            }
        }

    }


    //设置数据
    public void setDataList(List<Integer> dataList) {
        this.dataLists = dataList;
        linPath.reset();
        invalidate();
    }

    //是否显示x轴的刻度
    public void setShowX(boolean showX) {
        isShowX = showX;
    }

    //是否显示y轴的刻度
    public void setShowY(boolean showY) {
        isShowY = showY;
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * 获取文字的高度
     *
     * @param paint
     * @param text
     * @return
     */
    private int getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * 获取文字的宽度
     *
     * @param paint
     * @param text
     * @return
     */
    private int getTextWidth(Paint paint, String text) {
        return (int) paint.measureText(text);
    }

}

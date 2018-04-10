package com.demo.progressdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cheng on 2018/4/3.
 */
public class ProgressViewTest extends View {
    /**进度条最大值*/
    private float maxCount;
    /**进度条当前值*/
    private float currentCount;
    /**画笔*/
    private Paint mPaint;
    private Paint textPaint;

    private int mWidth,mHeight;

    private Context context;


    //边框颜色
    private int frameColor;
    //背景颜色
    private int bgColor;
    //进度条颜色
    private int progressColor;
    //字体颜色
    private int textColor;
    //字体大小
    private int textSize;
    //圆角大小
    private int radius;

    //文字
    private String text="";

    public ProgressViewTest(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }

    public ProgressViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.Progress);
        try {
            textSize = (int) ta.getDimension(R.styleable.Progress_textSize, 12);
            radius = (int) ta.getDimension(R.styleable.Progress_radius, 10);
            frameColor = ta.getColor(R.styleable.Progress_frameColor,context.getResources().getColor(R.color.themeColor));
            bgColor = ta.getColor(R.styleable.Progress_bgColor,context.getResources().getColor(R.color.white));
            progressColor = ta.getColor(R.styleable.Progress_progressColor,context.getResources().getColor(R.color.downloadingBgColor));
            textColor = ta.getColor(R.styleable.Progress_textColor,context.getResources().getColor(R.color.themeColor));
        } finally {
            ta.recycle();
        }
    }

    public ProgressViewTest(Context context) {
        super(context);
        this.context=context;
    }

    /***
     * 设置最大的进度值
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }
    /**
     * 得到最大进度值
     */
    public double getMaxCount(){
        return maxCount;
    }

    public float getCurrentCount(){
        return currentCount;
    }

    /***
     * 设置当前的进度值
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        /**
         * invalidate()是用来刷新View的，必须是在UI线程中进行工作。比如在修改某个view的显示时，
         * 调用invalidate()才能看到重新绘制的界面。invalidate()的调用是把之前的旧的view从主UI
         * 线程队列中pop掉。
         */
        invalidate();
    }

    public void noStart(){
        text="下载";
        textColor=getNoStartColor();
        frameColor=getNoStartColor();
        bgColor=getWhite();
        setCurrentCount(0);
    }

    public void pause(){
        text="继续";
        textColor=getPauseColor();
        frameColor=getPauseColor();
        bgColor=getWhite();
        setCurrentCount(0);
    }

    public void start(float currentCount){
        text=currentCount+"%";
        textColor=getDownloadingTextColor();
        frameColor=getDownloadingTextColor();
        bgColor=getWhite();
        setCurrentCount(currentCount);
    }

    public void finish(){
        text="已下载";
        textColor=getDownloadedColor();
        frameColor=getDownloadedColor();
        bgColor=getWhite();
        setCurrentCount(0);
    }

    public String getText(){
        return text;
    }

    public int getDownloadingTextColor(){
        return context.getResources().getColor(R.color.downloadingTextColor);
    }

    public int getDownloadingBgColor(){
        return context.getResources().getColor(R.color.downloadingBgColor);
    }

    public int getPauseColor(){
        return context.getResources().getColor(R.color.downloadingPauseColor);
    }

    public int getDownloadedColor(){
        return context.getResources().getColor(R.color.downloadedColor);
    }

    public int getNoStartColor(){
        return context.getResources().getColor(R.color.themeColor);
    }

    public int getWhite(){
        return context.getResources().getColor(R.color.white);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        mPaint = new Paint();
        //设置抗锯齿效果
        mPaint.setAntiAlias(true);
        //设置画笔颜色
        mPaint.setColor(frameColor);
//        int round = mHeight/2;
//        int round = 10;
        /**
         * RectF：绘制矩形，四个参数分别是left,top,right,bottom
         * 类型是单精度浮点数
         */
        RectF rf = new RectF(0, 0, mWidth, mHeight);
        /*绘制圆角矩形，背景色为画笔颜色*/
        canvas.drawRoundRect(rf, radius, radius, mPaint);
        /*设置progress内部是灰色*/
        mPaint.setColor(bgColor);
        RectF rectBlackBg = new RectF(2, 2, mWidth-2, mHeight-2);
        canvas.drawRoundRect(rectBlackBg, radius, radius, mPaint);
        //设置进度条进度及颜色  进度不是0的时候才画进度条
        if (currentCount!=0f){
            float section = currentCount/maxCount;
            RectF rectProgressBg = new RectF(3, 3, (mWidth-3)*section, mHeight-3);
            if(section!=0.0f){
                mPaint.setColor(progressColor);
            }else{
                mPaint.setColor(progressColor);
            }
            canvas.drawRoundRect(rectProgressBg, radius, radius, mPaint);
        }

        //画文字
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);

        textPaint.setColor(textColor);

        Rect textRect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        int tWidth = textRect.width();
        int tHeight = textRect.height();
        float xCoordinate = (getMeasuredWidth() - tWidth) / 2;
        float yCoordinate = (getMeasuredHeight() + tHeight) / 2;
        canvas.drawText(text, xCoordinate, yCoordinate, textPaint);
    }
    //dip * scale + 0.5f * (dip >= 0 ? 1 : -1)
    private int dipToPx(int dip){
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));//加0.5是为了四舍五入
    }
    /**指定自定义控件在屏幕上的大小,onMeasure方法的两个参数是由上一层控件
     * 传入的大小，而且是模式和尺寸混合在一起的数值，需要MeasureSpec.getMode(widthMeasureSpec)
     * 得到模式，MeasureSpec.getSize(widthMeasureSpec)得到尺寸
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        //MeasureSpec.EXACTLY，精确尺寸
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        //MeasureSpec.AT_MOST，最大尺寸，只要不超过父控件允许的最大尺寸即可，MeasureSpec.UNSPECIFIED未指定尺寸
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(20);
        } else {
            mHeight = heightSpecSize;
        }
        //设置控件实际大小
        setMeasuredDimension(mWidth, mHeight);
    }

}

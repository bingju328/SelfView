package selfview.com.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Date;

/**
 * Created by bingj on 2017/5/30.
 */

public class JVWatchView extends View {
    /**
     * 默认宽高
     * */
    private final int DEFAUTSIZE = 200;
    /**
     * 最终的大小
     * */
    private int mSize;
    /**
     * Paint
     * */
    private Paint backCirclePaint; // 整个背景圆
    private Paint textPaint; // 文字
    private Paint scaleArcPaint; // 刻度弧
    private Paint pointPaint; // 中心圆
    private Paint timePaint;   //时针
    private Paint minutePaint; //分针
    private Paint secondPaint; //秒针

    private int scaleArcRadius; // 刻度弧的半径

    public JVWatchView(Context context) {
        this(context,null);
    }

    public JVWatchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JVWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        backCirclePaint = new Paint();
        backCirclePaint.setAntiAlias(true);
        backCirclePaint.setStrokeWidth(5);
        backCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backCirclePaint.setColor(getResources().getColor(R.color.watchBackground));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        scaleArcPaint = new Paint();
        scaleArcPaint.setAntiAlias(true);
        scaleArcPaint.setStrokeWidth(dp2px(2));
        scaleArcPaint.setStyle(Paint.Style.STROKE);

        timePaint = new Paint();
        timePaint.setAntiAlias(true);
        timePaint.setStyle(Paint.Style.STROKE);

        minutePaint = new Paint();
        minutePaint.setAntiAlias(true);
        minutePaint.setStyle(Paint.Style.STROKE);

        secondPaint = new Paint();
        secondPaint.setAntiAlias(true);
        secondPaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecSize = startMeasure(widthMeasureSpec);
        int heigthSpecSize = startMeasure(heightMeasureSpec);
        mSize = Math.min(widthSpecSize,heigthSpecSize);
        setMeasuredDimension(mSize,mSize);
    }
    private int startMeasure(int mode) {
        int result = 0;
        int size = MeasureSpec.getSize(mode);
        int modeNew = MeasureSpec.getMode(mode);
        //AT_MOST 表示wrap_content
        if (modeNew == MeasureSpec.AT_MOST) {
            result = (int) dp2px(DEFAUTSIZE);
        } else {
            result = size;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //移动画布到中央
        canvas.translate(mSize/2,mSize/2);
        //画背景圆
        drawCircle(canvas);
        //画背景上的文字
        drawText(canvas);
        // 画表盘
        drawPanel(canvas);
    }

    private void drawPanel(Canvas canvas) {
        // 画刻度弧
        drawScaleArc(canvas);
        //画时针
        drawTime(canvas);
        //画分针
        drawMinute(canvas);
        // 画秒指针
        drawPointer(canvas);
        drawInPoint(canvas);
    }
    private float pointRadius = dp2px(3); // 中心圆半径
    private void drawInPoint(Canvas canvas) {
        canvas.save();
        pointPaint.setColor(Color.parseColor("#FFFAFA"));
        canvas.drawCircle(0, 0, pointRadius, pointPaint);
        canvas.restore();
    }
    private void drawPointer(Canvas canvas) {
        //先将指针与刻度0位置对齐
        canvas.save();
        canvas.rotate(180,0,0);
        float angleMille = milleSecond % 360;
        float angleSecond = currentSecond * 6 % 360;
        canvas.rotate(angleSecond +angleMille ,0,0);

        secondPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        secondPaint.setColor(getResources().getColor(R.color.secondPointer));
        secondPaint.setStrokeWidth(dp2px(0.8f));

        Path timePath = new Path();
        timePath.moveTo(0,0);
        timePath.lineTo(0,scaleArcRadius - mTikeHeight - dp2px(10));
        timePath.close();
        canvas.drawPath(timePath,secondPaint);
        canvas.restore();
    }

    private void drawMinute(Canvas canvas) {
        //先将指针与刻度0位置对齐
        canvas.save();
        canvas.rotate(180,0,0);
        float angleMinute = currentMinute * 6 % 360;
        float angleSecond = currentSecond*(6f/60);
//        Log.i("watchview--","drawMinute-angleMinute--"+angleMinute);
//        Log.i("watchview--","drawMinute-angleSecond--"+angleSecond);
        canvas.rotate(angleMinute+angleSecond,0,0);

        minutePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        minutePaint.setColor(getResources().getColor(R.color.minutePointer));
        minutePaint.setStrokeWidth(dp2px(2f));

        Path timePath = new Path();
        timePath.moveTo(0,0);
        timePath.lineTo(0,scaleArcRadius - mTikeHeight - dp2px(10));
        timePath.close();
        canvas.drawPath(timePath,minutePaint);
        canvas.restore();


    }

    private int currentTime = 0;
    private int currentMinute = 30;
    private float currentSecond = 30;
    private float milleSecond = 0;//1/6秒

    private void drawTime(Canvas canvas) {
        //先将指针与刻度0位置对齐
        canvas.save();
        canvas.rotate(180,0,0);
        int angleTime = currentTime * 30 % 360;
        float angleMinute = (((float)(currentMinute % 60)/60) * 30) % 30;
        canvas.rotate(angleTime+angleMinute,0,0);

        timePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        timePaint.setColor(getResources().getColor(R.color.timePointer));
        timePaint.setStrokeWidth(dp2px(3));

        Path timePath = new Path();
        timePath.moveTo(0,0);
        timePath.lineTo(0,scaleArcRadius - mTikeHeight - dp2px(30));
        timePath.close();
        canvas.drawPath(timePath,timePaint);
        canvas.restore();

    }

    private int mTikeCount = 12; // 12条刻度
    private int mTikeHeight = (int)dp2px(15); // 长度
    private void drawScaleArc(Canvas canvas) {
        scaleArcRadius = mSize/2;
        canvas.save();
        scaleArcPaint.setColor(Color.parseColor("#aaaaaa"));//#FFFAFA
        // 旋转的角度
        float mAngle = 360f / mTikeCount;

        for(int i = 0; i <= mTikeCount; i++){
            //3的倍数画上宽刻度
            if (i%3 == 0) {
                scaleArcPaint.setStrokeWidth(dp2px(2));
            } else {
                scaleArcPaint.setStrokeWidth(dp2px(1));
            }
            canvas.drawLine(0, -scaleArcRadius+5, 0, -scaleArcRadius+mTikeHeight+5, scaleArcPaint);
            //旋转画布
            canvas.rotate(mAngle, 0, 0);
        }
        canvas.restore();

    }

    private void drawText(Canvas canvas) {
        textPaint.setColor(Color.WHITE);
        String textUp = "LOBING";
        String textDown = "AUTOMATIC";
        //表盘上方logo
        textPaint.setTextSize(sp2px(12));
        textPaint.setTypeface(Typeface.SERIF);
        //测量字体的宽度
        float textWidthUp = textPaint.measureText(textUp);
        canvas.drawText(textUp, -textWidthUp/2, -mSize/4, textPaint);

        //表盘下方字体
        textPaint.setTypeface(Typeface.SANS_SERIF);
        textPaint.setTextSize(sp2px(8));
        float textWidthDown = textPaint.measureText(textDown);
        canvas.drawText(textDown,-textWidthDown/2,mSize/4,textPaint);
    }

    private void drawCircle(Canvas canvas) {
        // 已将画布移到中心，所以圆心为（0,0）
        backCirclePaint.setColor(Color.parseColor("#3A3A3A"));
        //如果不关闭硬件加速，setShadowLayer无效
//        backCirclePaint.setShadowLayer(dp2px(5),dp2px(100),dp2px(100),Color.RED);
        canvas.drawCircle(0, 0, mSize /2-dp2px(1), backCirclePaint);
        canvas.save();
    }

    /**
     * 设置当前秒
     */
    public void setCurrentTime(long currentTime1) {
        Date date = new Date(currentTime1);
        float milliSecond = (float) (currentTime1 % 1000); //当前毫秒
        int second = date.getSeconds();
        int minute = date.getMinutes();
        int hours = date.getHours();
        currentTime = hours;
        currentMinute = minute;
        currentSecond = second;
        milleSecond = milliSecond/1000 * 6; //以 1/6秒为单位
//        currentSecond = second; //以 1/6秒为单位
        postInvalidate();
    }

    /**
     * 将 dp 转换为 px
     * @param dp
     * @return
     */
    private float dp2px(float dp) {
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private float sp2px(float sp){
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}

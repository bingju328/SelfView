package selfview.com.selfview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*

/**
 * Created by bingj on 2017/5/30.
 */
class KTWatchView: View {

    /**
     * 默认宽高
     * */
    private val DEFAUTSIZE: Float = 200f
    /**
     * 最终的大小
     * */
    private var mSize: Int = 200
    /**
     * Paint
     * */
    private lateinit var backCirclePaint: Paint // 整个背景圆
    private lateinit var textPaint: Paint // 文字
    private lateinit var scaleArcPaint: Paint // 刻度弧
    private lateinit var pointPaint: Paint // 中心圆
    private lateinit var timePaint: Paint // 时针
    private lateinit var minutePaint: Paint // 分针
    private lateinit var secondPaint: Paint // 秒针

    private var scaleArcRadius: Int = 0 //刻度弧的半径


    constructor(context: Context): this(context,null)
    constructor(context: Context, attrs: AttributeSet?): this(context,attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        initPaint()
    }
    fun initPaint(): Unit{
        backCirclePaint = Paint()
        backCirclePaint.isAntiAlias = true
        backCirclePaint.strokeWidth = 5f
        backCirclePaint.style = Paint.Style.FILL_AND_STROKE
        backCirclePaint.color = resources.getColor(R.color.watchBackground)

        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.color = resources.getColor(R.color.whiteback)
        textPaint.typeface = Typeface.DEFAULT_BOLD

        scaleArcPaint = Paint()
        scaleArcPaint.isAntiAlias = true
        scaleArcPaint.strokeWidth = dp2px(2f)
        scaleArcPaint.style = Paint.Style.STROKE

        timePaint = Paint()
        timePaint.isAntiAlias = true
        timePaint.style = Paint.Style.STROKE

        minutePaint = Paint()
        minutePaint.isAntiAlias = true
        minutePaint.style = Paint.Style.STROKE

        secondPaint = Paint()
        secondPaint.isAntiAlias = true
        secondPaint.style = Paint.Style.STROKE

        pointPaint = Paint()
        pointPaint.isAntiAlias = true
        pointPaint.style = Paint.Style.FILL_AND_STROKE

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mSize = Math.max(startMeasure(widthMeasureSpec),startMeasure(heightMeasureSpec))
        setMeasuredDimension(mSize,mSize)
    }
    private fun startMeasure(mode: Int): Int{
        var size: Int = MeasureSpec.getSize(mode)
        var modeNew: Int = MeasureSpec.getMode(mode)
        //AT_MOST 表示wrap_content
        return if (modeNew==MeasureSpec.AT_MOST) dp2px(DEFAUTSIZE).toInt() else size
    }

    override fun onDraw(canvas: Canvas?) {
        //移动画布到中央
        canvas!!.translate(mSize/2f,mSize/2f)
        //画背景圆
        drawCircle(canvas)
        //画背景上的文字
        drawText(canvas)
        // 画表盘
        drawPanel(canvas)
    }

    private fun drawPanel(canvas: Canvas) {
        // 画刻度弧
        drawScaleArc(canvas)
        //画时针
        drawTime(canvas)
        //画分针
        drawMinute(canvas)
        // 画秒指针
        drawPointer(canvas)
        drawInPoint(canvas)
    }
    private var pointRadius = dp2px(3f) // 中心圆半径
    private fun drawInPoint(canvas: Canvas) {
        canvas.save()
        pointPaint.color = resources.getColor(R.color.whiteback)
        canvas.drawCircle(0f, 0f, pointRadius, pointPaint)
        canvas.restore()
    }

    private fun drawPointer(canvas: Canvas) {
        //先将指针与刻度0位置对齐
        canvas.save()
        canvas.rotate(180f,0f,0f)
        var angleMille = milleSecond % 360
        var angleSecond = currentSecond * 6 % 360
        canvas.rotate(angleSecond +angleMille ,0f,0f)

        secondPaint.style = Paint.Style.FILL_AND_STROKE
        secondPaint.color = resources.getColor(R.color.secondPointer)
        secondPaint.strokeWidth = dp2px(0.8f)

        val timePath: Path = Path()
        timePath.moveTo(0f,0f)
        timePath.lineTo(0f,scaleArcRadius - mTikeHeight - dp2px(10f))
        timePath.close();
        canvas.drawPath(timePath,secondPaint)
        canvas.restore()
    }

    private fun drawMinute(canvas: Canvas) {
        //先将指针与刻度0位置对齐
        canvas.save()
        canvas.rotate(180f,0f,0f)
        var angleMinute: Float = currentMinute * 6f % 360
        var angleSecond: Float = currentSecond*(6f/60)
//        Log.i("watchview--","drawMinute-angleMinute--"+angleMinute);
//        Log.i("watchview--","drawMinute-angleSecond--"+angleSecond);
        canvas.rotate(angleMinute+angleSecond,0f,0f)

        minutePaint.style = Paint.Style.FILL_AND_STROKE
        minutePaint.color = resources.getColor(R.color.minutePointer)
        minutePaint.strokeWidth = (dp2px(2f))

        val timePath: Path = Path()
        timePath.moveTo(0f,0f)
        timePath.lineTo(0f,scaleArcRadius - mTikeHeight - dp2px(10f))
        timePath.close()
        canvas.drawPath(timePath,minutePaint)
        canvas.restore()
    }

    private var currentTime: Int = 0
    private var currentMinute: Int = 0
    private var currentSecond: Float = 0f
    private var milleSecond: Float = 0f //1/6秒
    private fun drawTime(canvas: Canvas) {
        //先将指针与刻度0位置对齐
        canvas.save()
        canvas.rotate(180f,0f,0f)
        var angleTime: Int = currentTime * 30 % 360
        var angleMinute: Float = (((currentMinute % 60)/60f) * 30f) % 30
        canvas.rotate(angleTime+angleMinute,0f,0f)

        timePaint.style = Paint.Style.FILL_AND_STROKE
        timePaint.color = resources.getColor(R.color.timePointer)
        timePaint.strokeWidth = dp2px(3f)

        val timePath: Path = Path()
        timePath.moveTo(0f,0f)
        timePath.lineTo(0f,scaleArcRadius - mTikeHeight - dp2px(30f))
        timePath.close()
        canvas.drawPath(timePath,timePaint)
        canvas.restore()
    }
    private var mTikeCount: Int = 12 //12条该度
    private var mTikeHeight: Int = dp2px(15f).toInt()// 长度
    private fun drawScaleArc(canvas: Canvas) {
        scaleArcRadius = mSize/2
        canvas.save()
        scaleArcPaint.color = Color.parseColor("#aaaaaa")//#FFFAFA
        // 旋转的角度
        var mAngle: Float = 360f / mTikeCount

        for (item: Int in 0..mTikeCount) {
            //3的倍数画上宽刻度
            if (item % 3 == 0) {
                scaleArcPaint.strokeWidth = dp2px(2f)
            } else {
                scaleArcPaint.strokeWidth = dp2px(1f)
            }
            canvas.drawLine(0f,-scaleArcRadius+5f, 0f, -scaleArcRadius+mTikeHeight+5f, scaleArcPaint)
            //旋转画布
            canvas.rotate(mAngle, 0f, 0f)
        }
        canvas.restore()
    }

    private fun drawText(canvas: Canvas) {
        textPaint.color = Color.WHITE
        val textUp = "LOBING";
        val textDown = "AUTOMATIC";
        //表盘上方logo
        textPaint.textSize = sp2px(12f)
        textPaint.typeface = Typeface.SERIF
        //测量字体的宽度
        val textWidthUp = textPaint.measureText(textUp)
        canvas.drawText(textUp, -textWidthUp/2, -mSize/4f, textPaint)

        //表盘下方字体
        textPaint.typeface = Typeface.SANS_SERIF
        textPaint.textSize = sp2px(8f)
        var textWidthDown = textPaint.measureText(textDown)
        canvas.drawText(textDown,-textWidthDown/2,mSize/4f,textPaint)
    }

    private fun drawCircle(canvas: Canvas) {
        // 已将画布移到中心，所以圆心为（0,0）
        backCirclePaint.color = Color.parseColor("#3A3A3A")
        //如果不关闭硬件加速，setShadowLayer无效
//        backCirclePaint.setShadowLayer(dp2px(5),dp2px(100),dp2px(100),Color.RED)
        canvas.drawCircle(0f, 0f, mSize /2-dp2px(1f), backCirclePaint)
        canvas.save()
    }
    fun setCurrentTime(currentTime1: Long) {

        val date: Date = Date(currentTime1)
        var milliSecond: Float =  (currentTime1 % 1000).toFloat() //当前毫秒
        currentTime = date.getHours()
        currentMinute = date.getMinutes()
        currentSecond = date.getSeconds().toFloat()
        milleSecond = milliSecond/1000 * 6; //以 1/6秒为单位
        postInvalidate()
    }
    /**
     * 将 dp 转换为 px
     * @param dp
     * @return
     */
    private fun dp2px(dp: Float): Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,resources.displayMetrics)
    }
    private fun sp2px(dp: Float): Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,dp,resources.displayMetrics)
    }

}
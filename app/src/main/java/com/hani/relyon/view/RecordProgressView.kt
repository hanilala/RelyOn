package com.hani.relyon.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.hani.relyon.R
import com.hani.relyon.kt.observableNew
import kotlin.math.min

/**
 * @author : Create by DS-F
 * @date : 2022/8/3
 * description: 录制进度view
 **/
class RecordProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),GestureDetector.OnGestureListener {

    private var mStopBmp : Bitmap ?= null
    private var mStartBmp : Bitmap ?= null

    private var mSize = 0
    private var mProgress = 0f
    private var mStartBmpSize = 0
    private var mStopBmpSize = 0
    private var mBorderSize = 0

    private var mBorderColor = 0
    private var mCircleColor = 0
    private var mProgressColor = 0

    private var mHalfSize = 0f

    private var mListener : ActionListener ?= null

    private val mPaint : Paint by lazy { Paint().also {
        it.isAntiAlias = true
    } }

    private val mRect : RectF by lazy {
        RectF()
    }

    private var mIsPressing by observableNew(false){
        invalidate()
    }

    var mIsRecording by observableNew(false){
        invalidate()
    }


    private val mGestureDetector by lazy { GestureDetector(context,this) }
    companion object{
        private const val STOP_BMP_RATIO = 0.43f
        private const val START_BMP_RATIO = 0.666f
        private const val BORDER_RATIO = 0.045f
    }

    init {
        mStartBmp = BitmapFactory.decodeResource(resources, R.drawable.icon_start_record)
        mStopBmp = BitmapFactory.decodeResource(resources, R.drawable.icon_record_stop)
        mBorderColor = resources.getColor(R.color.white_20)
        mCircleColor = resources.getColor(R.color.white_50)
        mProgressColor = resources.getColor(R.color.color_FF4200)
    }


    fun setListener(listener: ActionListener){
        mListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = min(width,height)
        setMeasuredDimension(size,size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSize = w
        mHalfSize = (w / 2).toFloat()
        mStartBmpSize = (w * START_BMP_RATIO).toInt()
        mStopBmpSize = (w * STOP_BMP_RATIO).toInt()
        mBorderSize = (w * BORDER_RATIO).toInt()
        mRect.set((mBorderSize / 2).toFloat(),
            (mBorderSize / 2).toFloat(), (mSize - mBorderSize / 2).toFloat(), (mSize - mBorderSize / 2).toFloat()
        )
    }

    private var mTriggerLongPress = false

    override fun onTouchEvent(event: MotionEvent): Boolean {

        mGestureDetector.onTouchEvent(event)
        when(event.action){
            MotionEvent.ACTION_UP -> {
                if (mIsRecording && mTriggerLongPress){
                    mIsPressing = false
                    mListener?.onStopRecord()
                }
                reset()
            }
        }
        return true
    }

    fun setProgress(progress:Float){
        mProgress = if (progress < 0f){
            0f
        }else if (progress > 1f){
            1f
        }else {
            progress
        }
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (mIsRecording){
            mPaint.color = mCircleColor
            mPaint.style = Paint.Style.FILL
            canvas?.drawCircle(mHalfSize, mHalfSize, (mHalfSize - mBorderSize),mPaint)

            mPaint.color = mBorderColor
            mPaint.strokeWidth = mBorderSize.toFloat()
            mPaint.style = Paint.Style.STROKE
            canvas?.drawCircle(mHalfSize,mHalfSize,mHalfSize - (mBorderSize / 2).toFloat(),mPaint)

            if (mIsPressing){
                mPaint.color = Color.WHITE
                mPaint.style = Paint.Style.FILL
                canvas?.drawCircle(mHalfSize,mHalfSize, (mStopBmpSize / 2).toFloat(),mPaint)
            }else{
                mStopBmp?.let {
                    canvas?.save()
                    canvas?.translate(mHalfSize,mHalfSize)
                    canvas?.drawBitmap(it,-mStopBmpSize / 2f, - mStopBmpSize / 2f,mPaint)
                    canvas?.restore()
                }
            }
            mPaint.color = mProgressColor
            mPaint.strokeWidth = mBorderSize.toFloat()
            mPaint.style = Paint.Style.STROKE
            canvas?.drawArc(mRect,-90f,mProgress * 360,false,mPaint)
        }else {
            mStartBmp?.let {
                canvas?.save()
                canvas?.translate(mHalfSize,mHalfSize)
                canvas?.drawBitmap(it,-mStartBmpSize / 2f, - mStartBmpSize / 2f,mPaint)
                canvas?.restore()
            }
        }

    }



    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        if (mIsRecording){
            mListener?.onStopRecord()
        }else {
            mListener?.onStartRecord()
        }
        Log.w("lala","onSingleTapUp:  ${e.action}")
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        mIsPressing = true
        mTriggerLongPress = true
        if (!mIsRecording){
            mListener?.onStartRecord()
        }
    }

    fun reset(){
        mIsPressing = false
        mTriggerLongPress = false
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    interface ActionListener{
        fun onStartRecord()
        fun onStopRecord()
    }

}
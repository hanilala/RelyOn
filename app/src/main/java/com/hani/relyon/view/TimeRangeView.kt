package com.hani.relyon.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.hani.relyon.R
import com.hani.relyon.kt.dp
import kotlin.math.abs

/**
 * @author : Create by DS-F
 * @date : 2022/8/5
 * description: 滑动条
 **/
class TimeRangeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var mMaskColor = 0 //遮罩颜色
    private var mSliderBarHeight = 0
    private var mLineHeight = 0

    private val mLeftSlider by lazy { SliderView(context) }
    private val mRightSlider by lazy { SliderView(context).also {
        it.useInLeft(false)
    } }

    //白色滚动条
    private val mSlideBar by lazy { SlideBarView(context) }

    private var mWidth = 0
    private var mHeight = 0
    private var mSlideLength = 0
    private var mTouchSlop = 0
    private var mMinSelRatio = 0.1f //最小的选择的比例，
    private var mMinSelLength = 0  //最小选择宽度
    private var mLineColor = 0

    private val mTopLineRec : Rect by lazy { Rect() }
    private val mBottomLineRec : Rect by lazy { Rect() }

    private val mLeftMaskRec : Rect by lazy { Rect() }
    private val mRightMaskRec : Rect by lazy { Rect() }

    //白色滚动条超过红色滑块的高度
    private var mBarOverHeight = 0

    private val mPaint : Paint by lazy { Paint().also {
        it.isAntiAlias = true
    } }

    init {
        mMaskColor = ContextCompat.getColor(context,R.color.white_20)
        mLineColor = ContextCompat.getColor(context,R.color.color_F25924)
        mPaint.color = mLineColor
        mSliderBarHeight = 80.dp
        mLineHeight = 2.dp
        mBarOverHeight = 10.dp
        mTouchSlop =ViewConfiguration.get(context).scaledTouchSlop

        addView(mLeftSlider)
        addView(mRightSlider)
        addView(mSlideBar)
        setWillNotDraw(false)

    }

    fun setMinSelectRatio(minRatio:Float){
        mMinSelRatio = minRatio
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val sliderHeight = height - mBarOverHeight * 2
        mLeftSlider.measure(widthMeasureSpec,sliderHeight)
        mRightSlider.measure(widthMeasureSpec,sliderHeight)
        mSlideBar.measure(widthMeasureSpec,heightMeasureSpec)
        setMeasuredDimension(width,height)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val sliderWidth = mLeftSlider.measuredWidth
        val sliderHeight = mLeftSlider.measuredHeight
        mLeftSlider.layout(0,mBarOverHeight,sliderWidth, mBarOverHeight + sliderHeight)
        mRightSlider.layout(0,mBarOverHeight,sliderWidth,mBarOverHeight + sliderHeight)
        mSlideBar.layout(0,0,mSlideBar.measuredWidth,mSlideBar.measuredHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mSlideLength = w - mLeftSlider.measuredWidth * 2
        mMinSelLength = (mSlideLength * mMinSelRatio).toInt()
        mLeftSlider.x = 0f
        mRightSlider.x = (w - mRightSlider.measuredWidth).toFloat()
        mSlideBar.x = mLeftSlider.x + mLeftSlider.measuredWidth
        calculateLineArea()
    }

    private var mDownX = 0
    private var mLastX = 0
    private var mIsDragging = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var handle = false
        val x = event.x.toInt()
        val y = event.y.toInt()
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                mDownX = x
                if (!mSlideBar.mIsPress && mSlideBar.clickIn(x,y)){
                    mSlideBar.mIsPress = true
                    handle = true
                }else if (!mLeftSlider.mIsPress && mLeftSlider.clickIn(x,y)){
                    mLeftSlider.mIsPress = true
                    handle = true
                }else if (!mRightSlider.mIsPress && mRightSlider.clickIn(x,y)){
                    mRightSlider.mIsPress = true
                    handle = true
                }
            }
            MotionEvent.ACTION_MOVE -> {

                if (!mIsDragging && abs(x - mDownX) > mTouchSlop){
                    mIsDragging = true
                }
                if (mIsDragging){
                    if (mLeftSlider.mIsPress){
                        updateLeftSliderPos(x - mLastX)
                    }
                    if (mRightSlider.mIsPress){
                        updateRightSliderPos(x - mLastX)
                    }
                    if (mSlideBar.mIsPress){
                        val distance = x - mLastX
                        updateSlideBarPos(mSlideBar.x.toInt() + distance)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mIsDragging){
                    if (mLeftSlider.mIsPress || mRightSlider.mIsPress){
                        updateSlideBarPos(mLeftSlider.x.toInt() + mLeftSlider.width)
                    }
                }
                mIsDragging = false
                mLeftSlider.mIsPress = false
                mRightSlider.mIsPress = false
                mSlideBar.mIsPress = false
                invalidate()
            }
        }
        mLastX = x
        return handle
    }

    private fun updateRightSliderPos(distance:Int){
        var xPos = mRightSlider.x.toInt() + distance
        if (xPos < mLeftSlider.x + mLeftSlider.width + mMinSelLength ){
            xPos = (mLeftSlider.x + mLeftSlider.width + mMinSelLength).toInt()
        }else if (xPos > mWidth - mRightSlider.width){
            xPos = mWidth - mRightSlider.width
        }
        mRightSlider.x = xPos.toFloat()
        val progress = xPos.toFloat() / mSlideLength
        mRightSlider.mProgress = progress
        Log.w("lala", "right slider progress: $progress")
        calculateLineArea()
    }

    private fun updateLeftSliderPos(distance:Int){
        var xPos = mLeftSlider.x.toInt() + distance
        if (xPos < 0){
            xPos = 0
        }else if ( xPos > mRightSlider.x - mMinSelLength - mLeftSlider.width ){
            xPos = (mRightSlider.x - mMinSelLength - mLeftSlider.width).toInt()
        }
        mLeftSlider.x = xPos.toFloat()
        val progress =  xPos.toFloat() / mSlideLength
        mLeftSlider.mProgress = progress
        Log.w("lala", "Left slider progress: $progress")

        calculateLineArea()
    }



    private fun updateSlideBarPos(finalXPos: Int){
        var xPos = finalXPos
        val startX = mLeftSlider.x.toInt() + mLeftSlider.width
        val endX = mRightSlider.x.toInt() - mSlideBar.width
        if (xPos < startX ){
            xPos = startX
        }else if (xPos > endX ){
            xPos = endX
        }
        mSlideBar.x = xPos.toFloat()
        val progress = xPos.toFloat() / (mSlideLength - mSlideBar.width)
        mSlideBar.mProgress = progress
        Log.w("lala", "slider bar progress: $progress")
        invalidate()
    }

    private fun calculateLineArea(){
        mTopLineRec.setEmpty()
        mBottomLineRec.setEmpty()
        mLeftMaskRec.setEmpty()
        mRightMaskRec.setEmpty()

        val lineStartX = mLeftSlider.x.toInt() + mLeftSlider.width
        val lineEndX = mRightSlider.x.toInt()
        mTopLineRec.set(lineStartX,mBarOverHeight,lineEndX,mBarOverHeight + mLineHeight)
        mBottomLineRec.set(lineStartX,mHeight - mLineHeight - mBarOverHeight,lineEndX,mHeight - mBarOverHeight)


        mLeftMaskRec.set( mLeftSlider.width,mBarOverHeight,lineStartX,mBarOverHeight + mLeftSlider.height)
        mRightMaskRec.set(lineEndX,mBarOverHeight,mWidth - mRightSlider.width,mBarOverHeight + mRightSlider.height)

        invalidate()
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.color = mMaskColor
        canvas.drawRect(mLeftMaskRec,mPaint)
        canvas.drawRect(mRightMaskRec,mPaint)

        mPaint.color = mLineColor
        canvas.drawRect(mTopLineRec,mPaint)
        canvas.drawRect(mBottomLineRec,mPaint)

    }



}
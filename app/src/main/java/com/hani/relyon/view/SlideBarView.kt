package com.hani.relyon.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hani.relyon.kt.dp

/**
 * @author : Create by DS-F
 * @date : 2022/8/5
 * description: 白色滑动条
 **/
class SlideBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var mProgress  = 0f
    var mIsPress = false

    private var mWidth = 0

    private val mRect : RectF by lazy { RectF() }
    private val mPaint : Paint by lazy { Paint().also {
        it.isAntiAlias = true
        it.color = Color.WHITE
    } }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(4.dp,height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mRect.set(0f,0f,w.toFloat(),h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(mRect, (mWidth / 2).toFloat(), (mWidth / 2).toFloat(),mPaint)
    }


    fun clickIn(x:Int,y:Int):Boolean{
        val regionRect = Rect()
        getHitRect(regionRect)
        val space = 7.dp
        regionRect.left -= space
        regionRect.top -= space
        regionRect.right += space
        regionRect.bottom += space
        return regionRect.contains(x,y)
    }

}
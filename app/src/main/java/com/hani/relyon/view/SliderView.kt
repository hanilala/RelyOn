package com.hani.relyon.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.hani.relyon.R
import com.hani.relyon.kt.dp

/**
 * @author : Create by DS-F
 * @date : 2022/8/5
 * description: 滑块
 **/
class SliderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mIsLeft = true
    var mProgress  = 0f
    var mIsPress = false

    init {
        scaleType = ScaleType.CENTER_INSIDE
        setBackgroundResource(R.drawable.bg_left_slider)
        setImageResource(R.drawable.icon_slider_left)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(24.dp,height)
    }

    fun useInLeft(inLeft:Boolean){
        if (!inLeft){
            setBackgroundResource(R.drawable.bg_right_slider)
            setImageResource(R.drawable.icon_slider_right)
            mProgress = 1f
        }
    }

    fun clickIn(x:Int,y:Int):Boolean{
        val regionRect = Rect()
        getHitRect(regionRect)
        val space = 5.dp
        regionRect.left -= space
        regionRect.top -= space
        regionRect.right += space
        regionRect.bottom += space
        return regionRect.contains(x,y)
    }





}
package com.hani.relyon.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hani.relyon.utils.VideoUtils2
import java.lang.ref.SoftReference

/**
 * @author : Create by DS-F
 * @date : 2022/8/6
 * description: 缩略图列表
 **/
class ThumbNailListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var mRvThumb : RecyclerView ?= null
    private var mThumbWidth = 0
    private var mThumbHeight = 0
    private var mAdapter : ThumbNailAdapter ?= null
    private var mVideoPath  = ""

    companion object{
        private const val LIST_NUM = 8
    }

    init {
        orientation = HORIZONTAL
        mAdapter = ThumbNailAdapter()
        mRvThumb = RecyclerView(context).also {
            it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = mAdapter
        }
        addView(mRvThumb)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width,height)
    }

    fun setVideoPath(path:String){
        mVideoPath = path
    }

    private fun startDecodeFrame(){
        val duration = VideoUtils2.getDurationFromVideo(mVideoPath)
        val splitTime = duration / LIST_NUM
    }

    private fun createThumbInfoList():List<ThumbInfo>{
        val list = mutableListOf<ThumbInfo>()
        return list
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mThumbWidth = w / LIST_NUM
        mThumbHeight = h
    }

    private inner class ThumbNailAdapter : BaseQuickAdapter<ThumbInfo,BaseViewHolder>(0) {

        override fun convert(holder: BaseViewHolder, item: ThumbInfo) {
            item.imgBmp.get()?.let {
                (holder.itemView as? ImageView)?.setImageBitmap(it)
            }
        }

        override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val img = ImageView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(mThumbWidth,mThumbHeight)
            }
            return BaseViewHolder(img)
        }
    }

    private data class ThumbInfo(val decodeTime:Int,var imgBmp:SoftReference<Bitmap>)
}
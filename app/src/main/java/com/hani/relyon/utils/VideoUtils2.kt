package com.hani.relyon.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import com.blankj.utilcode.util.FileUtils
import com.hani.relyon.BuildConfig

/**
 * @author : Create by DS-F
 * @date : 2022/8/6
 * description: 视频工具类
 **/
object VideoUtils2 {

    fun decodeFrameByTimeAndroidApi(videoPath: String?, timeInMills: Long): Bitmap? {
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        var bitmap: Bitmap? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (FileUtils.isFileExists(videoPath)) {
                mediaMetadataRetriever.setDataSource(videoPath)
                bitmap = mediaMetadataRetriever.getFrameAtTime(
                    timeInMills * 1000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                mediaMetadataRetriever?.release()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

    fun scaleVideoFrameBitmap(srcBitmap: Bitmap?, dstWidth: Int, dstHeight: Int): Bitmap? {
//        var srcBitmap = srcBitmap
        var dstBitmap: Bitmap? = null
        if (srcBitmap != null) {
            val matrix = Matrix()
            val srcBitmapWidth = srcBitmap.width
            val srcBitmapHeight = srcBitmap.height
            val scaledBitmap =
                Bitmap.createScaledBitmap(srcBitmap, srcBitmapWidth, srcBitmapHeight, true)
            val scaleX = dstWidth.toFloat() / srcBitmapWidth.toFloat()
            val scaleY = dstHeight.toFloat() / srcBitmapHeight.toFloat()
            val scale = Math.max(scaleX, scaleY)
            srcBitmap.recycle()
            matrix.postScale(scale, scale)
            dstBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
        }
        return dstBitmap
    }

    /**
     * 获取视频时长
     *
     * @param videoPath 视频路径
     * @return 视频时长，单位毫秒
     */
    fun getDurationFromVideo(videoPath: String?): Long {
        if (BuildConfig.DEBUG && !FileUtils.isFileExists(videoPath)) {
            return 0
        }
        val mmr = MediaMetadataRetriever()
        val duration: String?
        return try {
            mmr.setDataSource(videoPath)
            duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: "0"
            duration.toLong()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            0
        } finally {
            mmr.release()
        }
    }
}
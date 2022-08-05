package com.hani.relyon.kt

import android.content.res.Resources

/**
 * Author: cpf
 * Date: 2020/11/25
 * Email: cpf4263@gmail.com
 */

val Int.dp
    get() = (Resources.getSystem().displayMetrics.density * this + 0.5f).toInt()

val Float.dp
    get() = Resources.getSystem().displayMetrics.density * this

val Int.toDp
    get() = (this.toFloat() / Resources.getSystem().displayMetrics.density).toInt()

val Float.toDp
    get() = this / Resources.getSystem().displayMetrics.density

val Int.sp
    get() = (Resources.getSystem().displayMetrics.scaledDensity * this + 0.5f).toInt()

val Float.sp
    get() = Resources.getSystem().displayMetrics.scaledDensity * this

val Int.toSp
    get() = (this.toFloat() / Resources.getSystem().displayMetrics.scaledDensity).toInt()

val Float.toSp
    get() = this / Resources.getSystem().displayMetrics.scaledDensity

val density: Float
    get() = Resources.getSystem().displayMetrics.density

val scaledDensity: Float
    get() = Resources.getSystem().displayMetrics.scaledDensity

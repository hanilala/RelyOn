package com.hani.relyon.kt

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author DS-Z
 * @since 2022/6/8
 */
private class Delegates

interface ExtField {

    val map: MutableMap<String, Any?>

    fun put(key: String, value: Any?) {
        map[key] = value
    }

    fun remove(key: String) {
        map.remove(key)
    }

    fun <T> get(key: String): T {
        return map[key] as T
    }

    fun <T> getOrNull(key: String): T? {
        return map[key] as? T
    }
}

fun <T> extField(initialValue: T) = object : ReadWriteProperty<ExtField, T> {
    var init = true

    override fun getValue(thisRef: ExtField, property: KProperty<*>): T {
        val map = thisRef.map
        val name = property.name
        if (init) {
            init = false
            if (map[name] == null) {
                map[name] = initialValue
            }
        }
        return map[name] as T
    }

    override fun setValue(thisRef: ExtField, property: KProperty<*>, value: T) {
        thisRef.map[property.name] = value
    }
}

/**
 * 属性委托，监听赋值操作，回调新值
 *
 * @see [observable]
 */
inline fun <T> observableNew(
    initialValue: T,
    discardIfNotChange: Boolean = true,
    crossinline onChange: (newValue: T) -> Unit
): ReadWriteProperty<Any?, T> = observable(initialValue, discardIfNotChange) block@{ _, _, new ->
    onChange(new)
}

/**
 * 属性委托，监听赋值操作
 *
 * @param initialValue 初始值
 * @param discardIfNotChange 如果新旧值相等则不回调 onChange
 * @param onChange 回调旧值、新值
 */
inline fun <T> observable(
    initialValue: T,
    discardIfNotChange: Boolean = true,
    crossinline onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit
): ReadWriteProperty<Any?, T> = Delegates.observable(initialValue) block@{ p, old, new ->
    if (discardIfNotChange && old == new) {
        return@block
    }
    onChange(p, old, new)
}

/**
 * 属性委托，监听赋值操作，可否决赋值，回调新值
 *
 * @see [vetoable]
 */
inline fun <T> vetoableNew(
    initialValue: T,
    discardIfNotChange: Boolean = true,
    crossinline onChange: (newValue: T) -> Boolean
): ReadWriteProperty<Any?, T> = vetoable(initialValue, discardIfNotChange) { _, _, new ->
    onChange(new)
}

/**
 * 属性委托，监听赋值操作，可否决赋值
 *
 * @param initialValue 初始值
 * @param discardIfNotChange 如果新旧值相等则不回调 onChange ，并且丢弃这次赋值
 * @param onChange 回调旧值、新值，return false 表示否决这次赋值操作
 */
inline fun <T> vetoable(
    initialValue: T,
    discardIfNotChange: Boolean = true,
    crossinline onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Boolean
): ReadWriteProperty<Any?, T> = Delegates.vetoable(initialValue) block@{ p, old, new ->
    if (discardIfNotChange && old == new) {
        return@block false
    }
    onChange(p, old, new)
}
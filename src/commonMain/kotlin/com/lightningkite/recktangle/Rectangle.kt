package com.lightningkite.recktangle

class Rectangle(
        var left: Float = 0f,
        var top: Float = 0f,
        var right: Float = 0f,
        var bottom: Float = 0f
) {
    var centerX: Float
        get() = (left + right) / 2
        set(value) {
            val delta = value - (left + right) / 2
            left += delta
            right += delta
        }
    var centerY: Float
        get() = (top + bottom) / 2
        set(value) {
            val delta = value - (top + bottom) / 2
            top += delta
            bottom += delta
        }
    val width: Float
        get() = (right - left)

    fun setWidthFromCenter(value: Float) {
        val oldWidth = width
        val delta = value - oldWidth
        left -= delta * .5f
        right += delta * .5f
    }

    val height: Float
        get() = (bottom - top)

    fun setHeightFromCenter(value: Float) {
        val oldHeight = height
        val delta = value - oldHeight
        top -= delta * .5f
        bottom += delta * .5f
    }

    fun normalize() {
        if (left > right) {
            val temp = right
            right = left
            left = temp
        }
        if (top > bottom) {
            val temp = bottom
            bottom = top
            top = temp
        }
    }
}
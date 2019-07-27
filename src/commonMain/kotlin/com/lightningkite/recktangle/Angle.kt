package com.lightningkite.recktangle

import com.lightningkite.recktangle.abs
import kotlin.math.*

/**
 * Various math functions.
 * Created by jivie on 9/28/15.
 */

@Suppress("NOTHING_TO_INLINE")

inline class Angle(val circles: Float) {
    companion object {
        const val RADIANS_PER_CIRCLE = (PI * 2).toFloat()
        const val DEGREES_PER_CIRCLE = 360f
        fun degrees(degrees: Float): Angle = Angle(degrees / DEGREES_PER_CIRCLE)
        fun radians(radians: Float): Angle = Angle(radians / RADIANS_PER_CIRCLE)
        fun atan2(y: Float, x: Float) = radians(kotlin.math.atan2(y, x))
        val zero = Angle(0f)
        val circle = Angle(1f)
        val halfCircle = Angle(.5f)
        val quarterCircle = Angle(.25f)
        val eighthCircle = Angle(.125f)
        val thirdCircle = Angle(1 / 3.0f)
    }

    inline val degrees: Float get() = circles * DEGREES_PER_CIRCLE
    inline val radians: Float get() = circles * RADIANS_PER_CIRCLE

    //For absolute angles
    inline infix fun angleTo(other: Angle): Angle {
        return Angle((other.circles - this.circles + .5f).positiveRemainder(1f) - .5f)
    }

    //For relative angles
    inline operator fun plus(other: Angle): Angle = Angle(this.circles + other.circles)

    inline operator fun minus(other: Angle): Angle = Angle(this.circles - other.circles)
    inline operator fun times(scale: Float) = Angle(this.circles * scale)
    inline operator fun div(by: Float) = Angle(this.circles / by)

    fun normalized(): Angle = Angle(this.circles.plus(.5f).positiveRemainder(1f).minus(.5f))

    inline fun sin(): Float = sin(radians)
    inline fun cos(): Float = cos(radians)
    inline fun tan(): Float = tan(radians)

    inline operator fun unaryMinus() = Angle(-circles)
}

fun abs(angle: Angle): Angle = Angle(abs(angle.circles))
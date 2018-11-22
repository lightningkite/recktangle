package com.lightningkite.recktangle

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * Various math functions.
 * Created by jivie on 9/28/15.
 */

@Suppress("NOTHING_TO_INLINE")

/*inline*/ data class Angle(val circles: Float) {
    companion object {
        const val RADIANS_PER_CIRCLE = (PI * 2).toFloat()
        const val DEGREES_PER_CIRCLE = 360f
        fun degrees(degrees: Float): Angle = Angle(degrees / DEGREES_PER_CIRCLE)
        fun radians(radians: Float): Angle = Angle(radians / RADIANS_PER_CIRCLE)
        fun atan2(y: Float, x: Float) = radians(kotlin.math.atan2(y, x))
        val Circle = Angle(1f)
        val HalfCircle = Angle(.5f)
        val QuarterCircle = Angle(.25f)
        val ThirdCircle = Angle(1 / 3.0f)
    }

    inline val degrees: Float get() = circles * DEGREES_PER_CIRCLE
    inline val radians: Float get() = circles * RADIANS_PER_CIRCLE

    //For absolute angles
    inline infix fun angleTo(other: Angle): Angle {
        return Angle((other.circles - this.circles + 1.5f).rem(1.0f) - .5f)
    }

    //For relative angles
    inline operator fun plus(other: Angle): Angle = Angle(this.circles + other.circles)

    inline operator fun minus(other: Angle): Angle = Angle(this.circles - other.circles)
    inline operator fun times(scale: Float) = Angle(this.circles * scale)
    inline operator fun div(by: Float) = Angle(this.circles / by)

    inline fun sin(): Float = sin(radians)
    inline fun cos(): Float = cos(radians)
    inline fun tan(): Float = tan(radians)
}

@Suppress("NOTHING_TO_INLINE")

/*inline*/ data class DoubleAngle(val circles: Double) {
    companion object {
        const val RADIANS_PER_CIRCLE = PI * 2
        const val DEGREES_PER_CIRCLE = 360
        fun degrees(degrees: Double): DoubleAngle = DoubleAngle(degrees / DEGREES_PER_CIRCLE)
        fun radians(radians: Double): DoubleAngle = DoubleAngle(radians / RADIANS_PER_CIRCLE)
        fun atan2(y: Double, x: Double) = radians(kotlin.math.atan2(y, x))
        val Circle = DoubleAngle(1.0)
        val HalfCircle = DoubleAngle(.5)
        val QuarterCircle = DoubleAngle(.25)
        val ThirdCircle = DoubleAngle(1 / 3.0)
    }

    inline val degrees: Double get() = circles * DEGREES_PER_CIRCLE
    inline val radians: Double get() = circles * RADIANS_PER_CIRCLE

    //For absolute angles
    inline infix fun angleTo(other: DoubleAngle): DoubleAngle {
        return DoubleAngle((other.circles - this.circles + 1.5).rem(1.0) - .5)
    }

    //For relative angles
    inline operator fun plus(other: DoubleAngle): DoubleAngle = DoubleAngle(this.circles + other.circles)

    inline operator fun minus(other: DoubleAngle): DoubleAngle = DoubleAngle(this.circles - other.circles)
    inline operator fun times(scale: Double) = DoubleAngle(this.circles * scale)
    inline operator fun div(by: Double) = DoubleAngle(this.circles / by)

    inline fun sin(): Double = sin(radians)
    inline fun cos(): Double = cos(radians)
    inline fun tan(): Double = tan(radians)
}
package com.lightningkite.recktangle

import kotlin.math.max

@Suppress("NOTHING_TO_INLINE")
data class FloatRange(override var start: Float = Float.POSITIVE_INFINITY, override var endInclusive: Float = Float.NEGATIVE_INFINITY): ClosedRange<Float> {

    inline fun setToNothing() {
        start = Float.POSITIVE_INFINITY
        endInclusive = Float.NEGATIVE_INFINITY
    }

    inline fun include(value: Float){
        if(value < start){
            start = value
        } else if(value > endInclusive){
            endInclusive = value
        }
    }

    inline fun normalize() {
        if(start > endInclusive){
            val swap = start
            start = endInclusive
            endInclusive = swap
        }
    }

    inline infix fun distanceTo(other: FloatRange): Float {
        return max(
                this.start - other.endInclusive,
                other.start - this.endInclusive
        )
    }
}
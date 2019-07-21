package com.lightningkite.recktangle

import kotlin.math.min
import kotlin.math.sqrt


data class LineSegment(var first: Point = Point(), var second: Point = Point()) {

    fun set(other: LineSegment) {
        this.first.set(other.first)
        this.second.set(other.second)
    }

    infix fun intersects(other: LineSegment): Boolean {
        val denom =
                (other.second.y - other.first.y) * (second.x - first.x) - (other.second.x - other.first.x) * (second.y - first.y)
        if (denom == 0f) { // Lines are parallel.
            return false
        }
        val ratioFirst =
                ((other.second.x - other.first.x) * (first.y - other.first.y) - (other.second.y - other.first.y) * (first.x - other.first.x)) / denom
        val ratioSecond =
                ((second.x - first.x) * (first.y - other.first.y) - (second.y - first.y) * (first.x - other.first.x)) / denom
        return ratioFirst in 0f..1f && ratioSecond >= 0f && ratioSecond <= 1f
    }

    val vector get() = second - first
    fun vector(output: Point): Point {
        output.x = second.x - first.x
        output.y = second.y - first.y
        return output
    }

    val angle: Angle get() = first angleTo second
    val length: Float get() = first distanceTo second
    fun interpolate(amount: Float, existing: Point = Point()): Point {
        existing.x = first.x + amount * (second.x - first.x)
        existing.y = first.y + amount * (second.y - first.y)
        return existing
    }

    //To Point
    infix fun closestRatioToPoint(point: Point): Float {
        val lineLengthSqr = (this.second.x - this.first.x).squared() + (this.second.y - this.first.y).squared()
        if (lineLengthSqr == 0f) {
            return 0f
        }
        return (((point.x - this.first.x) * (this.second.x - this.first.x) + (point.y - this.first.y) * (this.second.y - this.first.y)) / lineLengthSqr)
    }

    infix fun distanceSquaredTo(point: Point): Float = distanceSquaredTo(point, closestRatioToPoint(point))
    fun distanceSquaredTo(point: Point, closestRatioToPoint: Float): Float {
        val coerced = closestRatioToPoint.coerceIn(0f, 1f)
        return (point.x - (this.first.x + coerced * (this.second.x - this.first.x))).squared() +
                (point.y - (this.first.y + coerced * (this.second.y - this.first.y))).squared()
    }

    infix fun distanceTo(point: Point): Float = distanceTo(point, closestRatioToPoint(point))
    fun distanceTo(point: Point, closestRatioToPoint: Float): Float = sqrt(distanceSquaredTo(point, closestRatioToPoint))

    infix fun relativeAngleFromFirstIsPositive(point: Point): Boolean {
        val total = (this.second.x - this.first.x) * (this.second.y + this.first.y) +
                (point.x - this.second.x) * (point.y + this.second.y) +
                (this.first.x - point.x) * (this.first.y + point.y)
        return total < 0f
    }

    //To Line
    infix fun intersection(other: LineSegment): LineResult = intersection(other, LineResult())
    fun intersection(other: LineSegment, lineResult: LineResult): LineResult {
        lineResult.first = this
        lineResult.second = other
        val denom =
                (other.second.y - other.first.y) * (this.second.x - this.first.x) - (other.second.x - other.first.x) * (this.second.y - this.first.y)
        if (denom == 0f) { // Lines are parallel.
            lineResult.ratioFirst = Float.NaN
            lineResult.ratioSecond = Float.NaN
            return lineResult
        }
        lineResult.ratioFirst =
                ((other.second.x - other.first.x) * (this.first.y - other.first.y) - (other.second.y - other.first.y) * (this.first.x - other.first.x)) / denom
        lineResult.ratioSecond =
                ((this.second.x - this.first.x) * (this.first.y - other.first.y) - (this.second.y - this.first.y) * (this.first.x - other.first.x)) / denom
        return lineResult
    }

    infix fun distanceTo(other: LineSegment): Float {
        return distanceTo(other.first)
                .coerceAtMost(distanceTo(other.second))
                .coerceAtMost(other.distanceTo(first))
                .coerceAtMost(other.distanceTo(second))
    }

    data class LineResult(
            var first: LineSegment = LineSegment(),
            var second: LineSegment = LineSegment(),
            var ratioFirst: Float = 0f,
            var ratioSecond: Float = 0f
    ) {
        val intersecting: Boolean get() = ratioFirst in 0f..1f && ratioSecond in 0f..1f
        fun getPoint(existing: Point = Point()): Point? = if (!ratioFirst.isNaN()) first.interpolate(ratioFirst, existing) else null
    }
}

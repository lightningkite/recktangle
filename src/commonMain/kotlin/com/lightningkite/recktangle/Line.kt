package com.lightningkite.recktangle

import kotlin.math.sqrt


data class Line(var first: Point = Point(), var second: Point = Point())

fun Line.delta(): Point = Point(second.x - first.x, second.y - first.y)

inline fun Line.interpolate(amount: Float, existing: Point = Point()): Point {
    existing.x = first.x + amount * (second.x - first.x)
    existing.y = first.y + amount * (second.y - first.y)
    return existing
}

data class PointLineResult(
        var point: Point = Point(),
        var line: Line = Line()
) {
    var ratio: Float = 0f

    fun calculate() {
        val lineLengthSqr = (line.second.x - line.first.x).squared() + (line.second.y - line.first.y).squared()
        if (lineLengthSqr == 0f) {
            ratio = .5f
            return
        }
        ratio = (((point.x - line.first.x) * (line.second.x - line.first.x) + (point.y - line.first.y) * (line.second.y - line.first.y)) / lineLengthSqr)
    }

    val clockwise: Boolean
        get() {
            val total = (line.second.x - line.first.x) * (line.second.y + line.first.y) +
                    (point.x - line.second.x) * (point.y + line.second.y) +
                    (line.first.x - point.x) * (line.first.y + point.y)
            return total > 0f
        }

    val distanceSquared: Float
        get() = (point.x - ((line.first.x + ratio * (line.second.x - line.first.x)))).squared() +
                (point.y - ((line.first.y + ratio * (line.second.y - line.first.y)))).squared()

    val distance: Float
        get() = sqrt(distanceSquared.toDouble()).toFloat()

    val signedDistance: Float
        get() = if (clockwise) distance else -distance

    val boundedDistanceSquared: Float
        get() = (point.x - ((line.first.x + ratio.coerceIn(0f, 1f) * (line.second.x - line.first.x)))).squared() +
                (point.y - ((line.first.y + ratio.coerceIn(0f, 1f) * (line.second.y - line.first.y)))).squared()

    val boundedDistance: Float
        get() = sqrt(boundedDistanceSquared.toDouble()).toFloat()

    val signedBoundedDistance: Float
        get() = if (clockwise) boundedDistance else -boundedDistance

    val normal: Point
        get() = if (ratio < 0f) {
            point - line.first
        } else if (ratio > 1f) {
            point - line.second
        } else {
            (line.second - line.first).apply {
                perpendicularAssign()
                timesAssign(-1f)
                length = signedDistance
            }
        }

    val linePoint: Point get() = line.interpolate(ratio)
    val boundedLinePoint: Point get() = line.interpolate(ratio.coerceIn(0f, 1f))
}

infix fun Line.intersects(other: Line): Boolean {
    val denom = (other.second.y - other.first.y) * (second.x - first.x) - (other.second.x - other.first.x) * (second.y - first.y)
    if (denom == 0f) { // Lines are parallel.
        return false
    }
    val ratioFirst = ((other.second.x - other.first.x) * (first.y - other.first.y) - (other.second.y - other.first.y) * (first.x - other.first.x)) / denom
    val ratioSecond = ((second.x - first.x) * (first.y - other.first.y) - (second.y - first.y) * (first.x - other.first.x)) / denom
    return ratioFirst in 0f..1f && ratioSecond >= 0f && ratioSecond <= 1f
}

data class LineLineResult(
        var first: Line = Line(),
        var second: Line = Line()
) {
    var ratioFirst: Float = 0f
    var ratioSecond: Float = 0f

    fun calculate() {
        val denom = (second.second.y - second.first.y) * (first.second.x - first.first.x) - (second.second.x - second.first.x) * (first.second.y - first.first.y)
        if (denom == 0f) { // Lines are parallel.
            ratioFirst = Float.NaN
            ratioSecond = Float.NaN
            return
        }
        ratioFirst = ((second.second.x - second.first.x) * (first.first.y - second.first.y) - (second.second.y - second.first.y) * (first.first.x - second.first.x)) / denom
        ratioSecond = ((first.second.x - first.first.x) * (first.first.y - second.first.y) - (first.second.y - first.first.y) * (first.first.x - second.first.x)) / denom
    }

    val segmentsIntersect: Boolean get() = ratioFirst in 0f..1f && ratioSecond >= 0f && ratioSecond <= 1f

    fun point(existing: Point = Point()): Point? = if (ratioFirst != Float.NaN) first.interpolate(ratioFirst) else null
    val point: Point? get() = point()
}
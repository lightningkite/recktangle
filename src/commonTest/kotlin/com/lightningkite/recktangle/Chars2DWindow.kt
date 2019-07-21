package com.lightningkite.recktangle

import com.lightningkite.recktangle.Point
import com.lightningkite.recktangle.Rectangle
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class Chars2DWindow(val window: Rectangle, val chars2D: Chars2D) {

    private fun map(inputStart: Float, inputEnd: Float, outputStart: Float, outputEnd: Float, input: Float): Float {
        val betweenZeroAndOne = (input - inputStart) / (inputEnd - inputStart)
        return betweenZeroAndOne * (outputEnd - outputStart) + outputStart
    }

    fun getX(x: Float): Int = map(window.left, window.right, 0f, chars2D.width - 1f, x).toInt()
    fun getY(y: Float): Int = map(window.bottom, window.top, 0f, chars2D.height - 1f, y).toInt()

    operator fun get(x: Float, y: Float): Char {
        return chars2D[getX(x), getY(y)]
    }

    operator fun set(x: Float, y: Float, value: Char) {
        chars2D[getX(x), getY(y)] = value
    }

    operator fun get(point: Point): Char {
        return chars2D[getX(point.x), getY(point.y)]
    }

    operator fun set(point: Point, value: Char) {
        chars2D[getX(point.x), getY(point.y)] = value
    }

    fun point(x: Float, y: Float, value: Char = '+') = set(x, y, value)
    fun point(point: Point, value: Char = '+') = set(point, value)
    fun text(point: Point, text: String, maxSize: Int = text.length) = chars2D.text(
            getX(point.x) - maxSize / 2,
            getY(point.y),
            text,
            maxSize
    )

    fun line(line: LineSegment) {
        val fromX = getX(line.first.x)
        val fromY = getY(line.first.y)
        val toX = getX(line.second.x)
        val toY = getY(line.second.y)
        val xDelta = abs(toX - fromX)
        val yDelta = abs(toY - fromY)

        if (xDelta < yDelta) {
            val m = (toX - fromX.toFloat()) / (toY - fromY.toFloat())
            val b = fromX - m * fromY

            val lower = min(fromY, toY)
            val upper = max(fromY, toY)
            for (y in lower..upper) {
                val x = round(m * y + b).toInt()
                val xNext = round(m * (y + 1) + b).toInt()
                when {
                    x == xNext -> chars2D[x, y] = '|'
                    x > xNext -> chars2D[x, y] = '/'
                    x < xNext -> chars2D[x, y] = '\\'
                }
            }
        } else {
            val m = (toY - fromY.toFloat()) / (toX - fromX.toFloat())
            val b = fromY - m * fromX

            val lower = min(fromX, toX)
            val upper = max(fromX, toX)
            for (x in lower..upper) {
                val y = round(m * x + b).toInt()
                val yNext = round(m * (x + 1) + b).toInt()
                when {
                    y == yNext -> chars2D[x, y] = '-'
                    y > yNext -> chars2D[x, y] = '/'
                    y < yNext -> chars2D[x, y] = '\\'
                }
            }
        }
    }

    fun box(rectangle: Rectangle){
        chars2D.box(
                getX(rectangle.left),
                getY(rectangle.top),
                getX(rectangle.right),
                getY(rectangle.bottom)
        )
    }
}
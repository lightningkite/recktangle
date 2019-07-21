package com.lightningkite.recktangle

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertTrue

class PointTest {

    @Test fun intializes(){
        val examplePoint = Point(x = 1f, y = 2f)
    }
    @Test fun adds(){
        val examplePointA = Point(x = 1f, y = 2f)
        val examplePointB = Point(x = 3f, y = 4f)
        assertCloseEnough(Point(x = 4f, y = 6f), examplePointA + examplePointB)
    }
    @Test fun subtracts(){
        val examplePointA = Point(x = 1f, y = 2f)
        val examplePointB = Point(x = 3f, y = 4f)
        assertCloseEnough(Point(x = -2f, y = -2f), examplePointA - examplePointB)
    }
    @Test fun multiplies(){
        val examplePointA = Point(x = 1f, y = 2f)
        val examplePointB = Point(x = 3f, y = 4f)
        assertCloseEnough(Point(x = 3f, y = 8f), examplePointA * examplePointB)
    }
    @Test fun divides(){
        val examplePointA = Point(x = 1f, y = 2f)
        val examplePointB = Point(x = 3f, y = 4f)
        assertCloseEnough(Point(x = 1f/3f, y = 2f/4f), examplePointA / examplePointB)
    }
    @Test fun polar(){
        assertCloseEnough(Point(x = 1f, y = 0f), Point.polar(Angle.zero, 1f))
        assertCloseEnough(Point(x = 0f, y = 1f), Point.polar(Angle.quarterCircle, 1f))
        assertCloseEnough(Point(x = -1f, y = 0f), Point.polar(Angle.halfCircle, 1f))
        assertCloseEnough(Point(x = 0f, y = -1f), Point.polar(Angle(circles = .75f), 1f))
    }
    @Test fun rotates() {
        assertCloseEnough(Point(x = 1f, y = 0f), Point(x = 1f, y = 0f).rotate(Angle.zero))
        assertCloseEnough(Point(x = 0f, y = 1f), Point(x = 1f, y = 0f).rotate(Angle.quarterCircle))
        assertCloseEnough(Point(x = -1f, y = 0f), Point(x = 1f, y = 0f).rotate(Angle.halfCircle))
        assertCloseEnough(Point(x = 0f, y = -1f), Point(x = 1f, y = 0f).rotate(Angle(circles = .75f)))
    }
    @Test fun length(){
        assertCloseEnough(2f, Point(x = 1f, y = 1f).lengthSquared)
        assertCloseEnough(sqrt(2f), Point(x = 1f, y = 1f).length)
    }
    @Test fun angle(){
        assertCloseEnough(Angle.eighthCircle.circles, Point(x = 1f, y = 1f).angle.circles)
    }
    @Test fun dotProduct(){
        assertCloseEnough(-14f, Point(x = -4f, y = -9f) dot Point(x = -1f, y = 2f))
    }
    @Test fun distance(){
        assertCloseEnough(2f, Point(x = 1f, y = 0f) distanceSquared Point(x = 0f, y = 1f))
        assertCloseEnough(sqrt(2f), Point(x = 1f, y = 0f) distanceTo Point(x = 0f, y = 1f))
    }
    @Test fun angleTo(){
        assertCloseEnough(Angle(circles = 3f/8f).circles, (Point(x = 1f, y = 0f) angleTo Point(x = 0f, y = 1f)).circles)
    }
}

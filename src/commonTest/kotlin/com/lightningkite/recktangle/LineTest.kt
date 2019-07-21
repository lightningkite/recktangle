package com.lightningkite.recktangle

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LineTest {

    @Test fun canInstantiate(){
        LineSegment(Point.zero, Point.zero)
    }

    @Test fun intersects(){
        assertTrue(LineSegment(Point(0f, 0f), Point(1f, 1f)) intersects LineSegment(Point(1f, 0f), Point(0f, 1f)))
        assertFalse(LineSegment(Point(0f, 0f), Point(1f, 1f)) intersects LineSegment(Point(2f, 2f), Point(3f, 2f)))
        assertFalse(LineSegment(Point(0f, 0f), Point(1f, 1f)) intersects LineSegment(Point(-1f, 0f), Point(0f, 1f)))
    }

    @Test fun compareToLine(){
        run {
            val lineA = LineSegment(Point(0f, 0f), Point(2f, 2f))
            val lineB = LineSegment(Point(1f, 0f), Point(0f, 1f))
            val intersection = lineA intersection lineB
            assertTrue(intersection.intersecting)
            assertCloseEnough(Point(.5f, .5f), intersection.getPoint())
            assertCloseEnough(.25f, intersection.ratioFirst)
            assertCloseEnough(.5f, intersection.ratioSecond)
        }
        run {
            val lineA = LineSegment(Point(1f, 1f), Point(2f, 2f))
            val lineB = LineSegment(Point(1f, 0f), Point(0f, 1f))
            val intersection = lineA intersection lineB
            assertFalse(intersection.intersecting)
            assertCloseEnough(Point(.5f, .5f), intersection.getPoint())
        }
    }

    @Test fun closestRatioToPoint(){
        assertCloseEnough(
                expected = .5f,
                actual = LineSegment(Point(0f, 0f), Point(1f, 1f)) closestRatioToPoint Point(1f, 0f)
        )
    }

    @Test fun distanceToPoint(){
        assertCloseEnough(
                expected = .5f.squared() + .5f.squared(),
                actual = LineSegment(Point(0f, 0f), Point(1f, 1f)) distanceSquaredTo Point(1f, 0f)
        )
    }

    @Test fun positiveAngleWorks(){
        val line = LineSegment(Point(0f, 0f), Point(0f, 100f))
        assertTrue(line relativeAngleFromFirstIsPositive Point(-1f, 1f))
        assertFalse(line relativeAngleFromFirstIsPositive Point(1f, 1f))
    }
}

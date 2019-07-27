package com.lightningkite.recktangle

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PolygonTest {
    @Test
    fun intersects() {
        assertFalse(Polygon.rect(1f, 1f) intersects Polygon.rect(1f, 1f, 2f, 0f))
        assertFalse(Polygon.rect(1f, 1f) intersects Polygon.rect(1f, 1f, 2f, 2f))
        assertFalse(Polygon.rect(1f, 1f) intersects Polygon.rect(1f, 1f, 0f, 2f))
        assertTrue(Polygon.rect(1f, 1f) intersects Polygon.rect(1f, 1f, .25f, 0f))
        assertTrue(Polygon.rect(1f, 1f) intersects Polygon.rect(1f, 1f, 0f, .25f))
        assertTrue(Polygon.rect(1f, 1f) intersects Polygon.rect(1f, 1f, .25f, .25f))
    }

    @Test
    fun distanceToPoint() {
        val polygon = Polygon(arrayListOf(
                Point(1f, 0f),
                Point(0f, 1f),
                Point(-1f, 0f),
                Point(0f, -1f)
        ))
        assertCloseEnough(1f, polygon distanceTo Point(2f, 0f))
        assertCloseEnough(sqrt(.5.squared() + .5.squared()).toFloat(), polygon distanceTo Point(1f, 1f))
        assertCloseEnough(-sqrt(.5.squared() + .5.squared()).toFloat(), polygon distanceTo Point(0f, 0f))
        assertCloseEnough(0f, polygon distanceTo Point(.5f, .5f))
    }

    @Test
    fun distanceToPoly() {
        assertFalse(Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 2f, 0f) <= 0f, "Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 2f, 0f) == ${Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 2f, 0f)}")
        assertFalse(Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 2f, 2f) <= 0f, "Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 2f, 2f) == ${Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 2f, 2f)}")
        assertFalse(Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 0f, 2f) <= 0f, "Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 0f, 2f) == ${Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 0f, 2f)}")
        assertTrue(Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, .25f, 0f) <= 0f, "Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, .25f, 0f) == ${Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, .25f, 0f)}")
        assertTrue(Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 0f, .25f) <= 0f, "Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 0f, .25f) == ${Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, 0f, .25f)}")
        assertTrue(Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, .25f, .25f) <= 0f, "Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, .25f, .25f) == ${Polygon.rect(1f, 1f) distanceTo Polygon.rect(1f, 1f, .25f, .25f)}")
    }

    @Test fun intersectionPerformance(){
        val satData = Polygon.SATData()
        val polyA = Polygon.rect(1f, 1f)
        val polyB = Polygon.rect(1f, 1f, 2f, 0f)
        repeat(10_000){
            polyA.intersects(polyB, satData)
        }
    }

    @Test
    fun positiveAngleOrder(){
        assertTrue(Polygon(arrayListOf(
                Point(0f, 0f),
                Point(1f, 0f),
                Point(0f, 1f)
        )).positiveAngleOrder())
        assertTrue(!Polygon(arrayListOf(
                Point(0f, 0f),
                Point(0f, 1f),
                Point(1f, 0f)
        )).positiveAngleOrder())
    }

    @Test
    fun defaultsToPositiveAngles(){
        assertTrue(Polygon.rect(1f, 1f).positiveAngleOrder())
    }

    @Test
    fun area(){
        assertCloseEnough(1f, Polygon.rect(1f, 1f).area())
        assertCloseEnough(2f, Polygon.rect(2f, 1f).area())
        assertCloseEnough(4f, Polygon.rect(2f, 2f).area())
    }
}

package com.lightningkite.recktangle

import kotlin.math.PI
import kotlin.test.Test

class AngleTest {

    @Test
    fun units() {
        assertCloseEnough(Angle(circles = .25f).circles, Angle.radians(PI.toFloat() / 2f).circles)
        assertCloseEnough(Angle(circles = .25f).circles, Angle.degrees(90f).circles)
    }

    @Test
    fun difference() {
        assertCloseEnough(Angle.quarterCircle, Angle.zero angleTo Angle.quarterCircle)
        assertCloseEnough(Angle.quarterCircle, Angle(7 / 8f) angleTo Angle(1 / 8f))
        assertCloseEnough(-Angle.quarterCircle, Angle.quarterCircle angleTo Angle.zero)
        assertCloseEnough(-Angle.quarterCircle, Angle(1 / 8f) angleTo Angle(7 / 8f))
    }

    @Test
    fun stretchIdea() {
        infix fun Angle.quadAngleTo(other: Angle): Angle {
            return this.times(4f).angleTo(other.times(4f)).div(4f)
        }

        assertCloseEnough(Angle.zero, Angle.zero quadAngleTo Angle.quarterCircle)
        assertCloseEnough(Angle(1 / 16f), Angle.zero quadAngleTo Angle(1 / 16f))
        assertCloseEnough(Angle(-1 / 16f), Angle.zero quadAngleTo Angle(-1 / 16f))
        assertCloseEnough(Angle(-1 / 16f), Angle.zero quadAngleTo Angle(15 / 16f))
        assertCloseEnough(Angle(1 / 16f), Angle(-1 / 32f) quadAngleTo Angle(1 / 32f))
        assertCloseEnough(Angle(-1 / 16f), Angle(1 / 32f) quadAngleTo Angle(-1 / 32f))
    }
}

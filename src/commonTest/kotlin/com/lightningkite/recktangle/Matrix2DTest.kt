package com.lightningkite.recktangle

import kotlin.test.Test
import kotlin.test.assertEquals

class Matrix2DTest {

    @Test
    fun rotate() {
        val point = Point(2f, 2f)
        val transform = Matrix2D.rotate(Angle.quarterCircle)

        println(transform.values.toList().chunked(3).joinToString("\n") { it.joinToString() })
        println("$point -> ${transform.transform(point)}")

        assertCloseEnough(expected = Point(-2f, 2f), actual = transform.transform(point))
    }

    @Test
    fun rotateAroundSomething() {
        val point = Point(2f, 2f)
        val transform = Matrix2D.translate(Point(-1f, -1f))
                .after(Matrix2D.rotate(Angle.quarterCircle))
                .after(Matrix2D.translate(Point(1f, 1f)))
        println("$point -> ${transform.transform(point)}")

        assertCloseEnough(expected = Point(0f, 2f), actual = transform.transform(point))
    }
}
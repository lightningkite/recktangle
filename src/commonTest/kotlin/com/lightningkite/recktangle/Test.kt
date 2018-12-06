package com.lightningkite.recktangle

import kotlin.test.Test

class Test {
    @Test fun example(){
        val point = Point(x = 1f, y = 2f)
        val rect = Rectangle(left = 0f, top = 0f, right = 5f, bottom = 5f)
        val line = Line(first = point, second = point.plus(Point(x = 2f, y = 2f)))
    }
}

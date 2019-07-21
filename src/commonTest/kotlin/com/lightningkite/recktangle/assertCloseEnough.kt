package com.lightningkite.recktangle

import kotlin.math.abs
import kotlin.test.assertTrue


fun assertCloseEnough(expected: Point?, actual: Point?) {
    assertTrue((expected == null && actual == null) ||
            (expected != null && actual != null && abs(expected.x - actual.x) < .0001 && abs(expected.y - actual.y) < .0001), "Expected close to $expected, actually $actual")
}
fun assertCloseEnough(expected: Float, actual: Float) {
    assertTrue(abs(expected - actual) < .0001, "Expected close to $expected, actually $actual")
}
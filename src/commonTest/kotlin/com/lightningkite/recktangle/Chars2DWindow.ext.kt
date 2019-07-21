package com.lightningkite.recktangle

import kotlin.math.abs
import kotlin.test.assertTrue

inline fun Chars2D.withWindow(rectangle: Rectangle, action: Chars2DWindow.() -> Unit) {
    Chars2DWindow(rectangle, this).apply(action)
}

interface GeometrySet {
    fun render(point: Point)
    fun render(line: LineSegment)
    fun render(polygon: Polygon)
    fun render(rectangle: Rectangle)
}

fun Chars2DWindow.geometry() = object : GeometrySet {
    override fun render(point: Point) {
        point(point)
    }

    override fun render(line: LineSegment) {
        line(line)
    }

    override fun render(polygon: Polygon) {
        polygon.lineSequence().forEach { line(it) }
    }

    override fun render(rectangle: Rectangle) {
        box(rectangle)
    }
}

fun getBounds(action: GeometrySet.() -> Unit): Rectangle {
    val output = Rectangle(Float.MAX_VALUE, Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE)
    val detector = object : GeometrySet {
        override fun render(point: Point) {
            output.left = output.left.coerceAtMost(point.x)
            output.right = output.right.coerceAtLeast(point.x)
            output.top = output.top.coerceAtMost(point.y)
            output.bottom = output.bottom.coerceAtLeast(point.y)
        }

        override fun render(line: LineSegment) {
            render(line.first)
            render(line.second)
        }

        override fun render(polygon: Polygon) {
            for(point in polygon.points){
                render(point)
            }
        }

        override fun render(rectangle: Rectangle) {
            render(Point(rectangle.left, rectangle.top))
            render(Point(rectangle.right, rectangle.bottom))
        }
    }
    detector.apply(action)
    return output
}

fun debugRender(title: String = "Debug Render", size: Int = 30, ratio: Float = 2.5f, action: GeometrySet.() -> Unit) {
    val bounds = getBounds(action)
    bounds.setHeightFromCenter(bounds.height * 1.2f)
    bounds.setWidthFromCenter(bounds.width * 1.2f)
    if (bounds.width > bounds.height * ratio) {
        bounds.setHeightFromCenter(bounds.width / ratio)
    } else {
        bounds.setWidthFromCenter(bounds.height * ratio)
    }
    assertTrue(abs(ratio - (bounds.width / bounds.height)) < .01f)
    Chars2D((size * ratio * 1.5).toInt(), size, ' ').apply {
        box(0, 0, width - 1, height - 1)
        text(2, 0, title, 60)
        withWindow(bounds) {
            geometry().apply(action)
        }
    }.apply {
        println(buildString { print(this) })
    }
}
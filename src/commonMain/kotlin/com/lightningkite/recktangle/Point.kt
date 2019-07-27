package com.lightningkite.recktangle

import kotlin.math.sqrt

@Suppress("NOTHING_TO_INLINE")

data class Point(var x: Float = 0f, var y: Float = 0f) {

    companion object {
        val zero = Point(0f, 0f)
        fun polar(angle: Angle, length: Float) = Point(angle.cos() * length, angle.sin() * length)
    }

    fun rotateAssign(angle: Angle) {
        val oldX = x
        val oldY = y
        val c = angle.cos()
        val s = angle.sin()
        x = oldX * c - oldY * s
        y = oldX * s + oldY * c
    }

    fun rotate(angle: Angle): Point {
        val c = angle.cos()
        val s = angle.sin()
        return Point(
                x = this.x * c - this.y * s,
                y = this.x * s + this.y * c
        )
    }

    inline fun projectOnto(angle: Angle): Float {
        return this.x * angle.cos() - this.y * angle.sin()
    }
    inline fun projectOnto(angleCos: Float, angleSin: Float): Float {
        return this.x * angleCos - this.y * angleSin
    }

    fun set(other: Point) {
        x = other.x
        y = other.y
    }

    fun setPolar(angle: Angle, length: Float) {
        x = angle.cos() * length
        y = angle.sin() * length
    }

    inline val lengthSquared: Float
        get() = x.squared() + y.squared()
    inline var length: Float
        get() = sqrt(x.squared() + y.squared().toDouble()).toFloat()
        set(value) {
            this *= value / length
        }

    inline var angle: Angle
        get() = Angle.atan2(y, x)
        set(value) {
            val len = length
            x = value.cos() * len
            y = value.sin() * len
        }

    inline fun plus(angle: Angle, length: Float): Point = Point(x + angle.cos() * length, y + angle.sin() * length)
    inline fun plusAssign(angle: Angle, length: Float) {
        x += angle.cos() * length
        y += angle.sin() * length
    }

    inline operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
    inline operator fun plusAssign(other: Point) {
        x += other.x
        y += other.y
    }

    inline operator fun minus(other: Point): Point = Point(x - other.x, y - other.y)
    inline operator fun minusAssign(other: Point) {
        x -= other.x
        y -= other.y
    }

    inline operator fun times(other: Point): Point = Point(x * other.x, y * other.y)
    inline operator fun timesAssign(other: Point) {
        x *= other.x
        y *= other.y
    }

    inline operator fun div(other: Point): Point = Point(x / other.x, y / other.y)
    inline operator fun divAssign(other: Point) {
        x /= other.x
        y /= other.y
    }

    inline operator fun times(scalar: Float): Point = Point(x * scalar, y * scalar)
    inline operator fun timesAssign(scalar: Float) {
        x *= scalar
        y *= scalar
    }

    inline operator fun div(scalar: Float): Point = Point(x / scalar, y / scalar)
    inline operator fun divAssign(scalar: Float) {
        x /= scalar
        y /= scalar
    }

    inline infix fun dot(other: Point): Float = x * other.x + y * other.y
    inline infix fun cross(other: Point): Float = x * other.y - y * other.x
    inline infix fun project(other: Point): Point {
        val len = this dot other
        return Point(other.x * len, other.y * len)
    }

    inline infix fun projectAssign(other: Point) {
        val len = this dot other
        x = other.x * len
        y = other.y * len
    }

    inline infix fun distanceTo(other: Point): Float = sqrt(distanceSquared(other))
    inline infix fun distanceSquared(other: Point): Float = (other.x - x).squared() + (other.y - y).squared()

    inline infix fun angleTo(other: Point): Angle = Angle.atan2(other.y - y, other.x - x)


    inline fun plus(otherX: Float, otherY: Float): Point = Point(x + otherX, y + otherY)
    inline fun plusAssign(otherX: Float, otherY: Float) {
        x += otherX
        y += otherY
    }

    inline fun minus(otherX: Float, otherY: Float): Point = Point(x - otherX, y - otherY)
    inline fun minusAssign(otherX: Float, otherY: Float) {
        x -= otherX
        y -= otherY
    }

    inline fun times(otherX: Float, otherY: Float): Point = Point(x * otherX, y * otherY)
    inline fun timesAssign(otherX: Float, otherY: Float) {
        x *= otherX
        y *= otherY
    }

    inline fun div(otherX: Float, otherY: Float): Point = Point(x / otherX, y / otherY)
    inline fun divAssign(otherX: Float, otherY: Float) {
        x /= otherX
        y /= otherY
    }

    inline fun dot(otherX: Float, otherY: Float): Float = x * otherX + y * otherY
    inline fun cross(otherX: Float, otherY: Float): Float = x * otherY - y * otherX
    inline fun project(otherX: Float, otherY: Float): Point {
        val len = this.dot(otherX, otherY)
        return Point(otherX * len, otherY * len)
    }

    inline fun projectAssign(otherX: Float, otherY: Float) {
        val len = this.dot(otherX, otherY)
        x = otherX * len
        y = otherY * len
    }

    inline fun distanceTo(otherX: Float, otherY: Float): Float = sqrt(distanceSquared(otherX, otherY))
    inline fun distanceSquared(otherX: Float, otherY: Float): Float = (otherX - x).squared() + (otherY - y).squared()

    inline fun angleTo(otherX: Float, otherY: Float): Angle = Angle.atan2(otherY - y, otherX - x)


    inline fun perpendicular() = Point(-y, x)
    inline fun perpendicularAssign() {
        val temp = x
        x = -y
        y = temp
    }
}

package com.lightningkite.recktangle

import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt


data class Polygon(
        @Suppress("RemoveExplicitTypeArguments") val points: MutableList<Point> = ArrayList<Point>()
) {

    companion object {
        fun rect(width: Float, height: Float = width, centerX: Float = 0f, centerY: Float = 0f): Polygon {
            return Polygon(
                    mutableListOf(
                            Point(centerX + width / 2, centerY + height / 2),
                            Point(centerX - width / 2, centerY + height / 2),
                            Point(centerX - width / 2, centerY - height / 2),
                            Point(centerX + width / 2, centerY - height / 2)
                    )
            )
        }
    }

    fun getLine(index: Int, existing: LineSegment = LineSegment()): LineSegment {
        existing.first = points[index]
        existing.second = points[index.plus(1).rem(points.size)]
        return existing
    }

    fun lineSequence(): Sequence<LineSegment> = points.indices.asSequence().map { getLine(it) }
    fun lineSequence(existing: LineSegment): Sequence<LineSegment> = points.indices.asSequence().map { getLine(it, existing) }

    fun getLineNormal(index: Int): Angle {
        val calcPoint = Point()
        calcPoint.x = points[index.plus(1).rem(points.size)].x - points[index].x
        calcPoint.y = points[index.plus(1).rem(points.size)].y - points[index].y
        return calcPoint.angle + Angle.quarterCircle
    }

    val isConvex: Boolean get(){
        val firstIsPositive = run {
            val first = points[0]
            val second = points[1]
            val third = points[2]
            (first.x - third.x) * (second.y - first.y) -
                    (first.x - second.x) * (third.y - first.y) > 0
        }
        return points.indices.none {
            val first = points[it]
            val second = points[(it + 1) % points.size]
            val third = points[(it + 2) % points.size]
            val isPositive = (first.x - third.x) * (second.y - first.y) -
                    (first.x - second.x) * (third.y - first.y) > 0
            isPositive != firstIsPositive
        }
    }

    val isComplex: Boolean get(){
        return abs(points.indices.fold(Angle.zero) { acc, it ->
            val first = points[it]
            val second = points[(it + 1) % points.size]
            val third = points[(it + 2) % points.size]
            acc + (first angleTo second) angleTo (second angleTo third)
        }.circles.roundToInt()) != 1
    }

    fun signedArea(calculationLine: LineSegment = LineSegment()): Float = lineSequence(calculationLine).fold(0f) { acc, it -> acc + (it.second.x - it.first.x) * (it.second.y + it.first.y) } / -2f
    fun area(calculationLine: LineSegment = LineSegment()): Float = abs(signedArea(calculationLine))
    fun positiveAngleOrder(calculationLine: LineSegment = LineSegment()): Boolean = signedArea(calculationLine) > 0.0
    val centroid: Point
        get() {
            val first = points.first()
            var xAverage = 0.0
            var yAverage = 0.0
            var areaTotal = 0.0
            for (i in 1..points.size - 2) {
                val second = points[i]
                val third = points[i + 1]
                val x = (first.x + second.x + third.x) / 3
                val y = (first.y + second.y + third.y) / 3
                val area = abs(
                        (first.x - third.x) * (second.y - first.y) -
                                (first.x - second.x) * (third.y - first.y)
                ) * .5
                xAverage += x * area
                yAverage += y * area
                areaTotal += area
            }
            xAverage /= areaTotal
            yAverage /= areaTotal
            return Point(xAverage.toFloat(), yAverage.toFloat())
        }

    fun normalize(calculationLine: LineSegment = LineSegment()) {
        if (!positiveAngleOrder(calculationLine)) points.reverse()
    }

    fun getBounds(existing: Rectangle = Rectangle()): Rectangle {
        existing.left = points[0].x
        existing.right = points[0].x
        existing.top = points[0].y
        existing.bottom = points[0].y
        for (point in points) {
            if (point.x < existing.left) existing.left = point.x
            if (point.y < existing.top) existing.top = point.y
            if (point.x > existing.right) existing.right = point.x
            if (point.y > existing.bottom) existing.bottom = point.y
        }
        return existing
    }

    //Point Operations

    class PointResult {
        lateinit var polygon: Polygon
        lateinit var point: Point
        val polygonLine: LineSegment = LineSegment()
        var polygonLineIndex: Int = 0
        var distance: Float = 0f

        val calculationLine = LineSegment()

        fun set(other: PointResult){
            polygon = other.polygon
            point = other.point
            polygonLine.set(other.polygonLine)
            polygonLineIndex = other.polygonLineIndex
            distance = other.distance
        }
    }

    fun distanceToDetailed(point: Point, pointResult: PointResult): PointResult {
        var bestOutsideDistanceSquared = Float.MAX_VALUE
        var bestOutsideIndex = -1
        var bestInsideDistanceSquared = Float.MAX_VALUE
        var bestInsideIndex = -1
        pointResult.point = point
        pointResult.polygon = this
        lineSequence(pointResult.calculationLine).forEachIndexed { index, it ->
            val distance = it distanceSquaredTo point
            val isInsideThisLine = it relativeAngleFromFirstIsPositive point
            if(isInsideThisLine){
                if (distance < bestInsideDistanceSquared) {
                    bestInsideDistanceSquared = distance
//                    pointResult.polygonLine.set(it)
//                    pointResult.polygonLineIndex = index
                    bestInsideIndex = index
                }
            } else {
                if (distance < bestOutsideDistanceSquared) {
                    bestOutsideDistanceSquared = distance
                    pointResult.polygonLine.set(it)
                    pointResult.polygonLineIndex = index
                    bestOutsideIndex = index
                }
            }
        }
        if(bestOutsideIndex == -1){
            pointResult.distance = -sqrt(bestInsideDistanceSquared)
            getLine(bestInsideIndex, pointResult.polygonLine)
            pointResult.polygonLineIndex = bestInsideIndex
        } else {
            pointResult.distance = sqrt(bestOutsideDistanceSquared)
            getLine(bestOutsideIndex, pointResult.polygonLine)
            pointResult.polygonLineIndex = bestOutsideIndex
        }

        return pointResult
    }

    infix fun distanceToDetailed(point: Point) = distanceToDetailed(point, PointResult())

    fun distanceTo(point: Point, calculationLine: LineSegment): Float {
        var bestOutsideDistanceSquared = Float.MAX_VALUE
        var bestInsideDistanceSquared = Float.MAX_VALUE
        lineSequence(calculationLine).forEachIndexed { index, it ->
            val distance = it distanceSquaredTo point
            val isInside = it relativeAngleFromFirstIsPositive point
            if(isInside){
                if(distance < bestInsideDistanceSquared ) {
                    bestInsideDistanceSquared = distance
                }
            } else {
                if(distance < bestOutsideDistanceSquared ) {
                    bestOutsideDistanceSquared = distance
                }
            }
        }
        return if(bestOutsideDistanceSquared == Float.MAX_VALUE){
            -sqrt(bestInsideDistanceSquared)
        } else {
            sqrt(bestOutsideDistanceSquared)
        }
    }
    infix fun distanceTo(point: Point): Float = distanceTo(point, LineSegment())

    operator fun contains(point: Point): Boolean = contains(point, LineSegment(), LineSegment())
    fun contains(point: Point, calculationLineA: LineSegment, calculationLineB: LineSegment): Boolean {
        calculationLineA.first.set(point)
        calculationLineA.second.set(point)
        calculationLineA.second.x = 100000f
        return lineSequence(calculationLineB).count { it intersects calculationLineA } % 2 == 0
    }


    //Line Operations
    infix fun intersects(other: LineSegment): Boolean = intersects(other, LineSegment())
    fun intersects(other: LineSegment, calculationLine: LineSegment): Boolean {
        return lineSequence(calculationLine).any {
            it intersects other
        }
    }

    fun intersectsDetailed(other: LineSegment, lineResult: LineResult): LineResult {
        lineResult.polygon = this
        lineResult.line = other
        lineResult.lineResult.clear()

        lineSequence(lineResult.calculationLine).forEachIndexed { index, lineSegment ->
            if (lineSegment.intersection(other, lineResult.calculationLineResult).intersecting) {
                if (lineResult.calculationLineResult.intersecting) {
                    lineResult.lineResult.add(LineResult.Intersection(lineResult.calculationLineResult.copy(), index))
                }
            }
        }

        return lineResult
    }

    class LineResult {
        lateinit var polygon: Polygon
        lateinit var line: LineSegment

        val lineResult = ArrayList<Intersection>()

        val intersecting: Boolean get() = lineResult.isNotEmpty()

        val calculationLineResult: LineSegment.LineResult = LineSegment.LineResult()
        val calculationLine = LineSegment()

        data class Intersection(
                val lineResult: LineSegment.LineResult = LineSegment.LineResult(),
                val polygonLineIndex: Int = 0
        )
    }


    //Polygon Operations

    private fun projectOnto(angleCos: Float, angleSin: Float, output: FloatRange = FloatRange()): FloatRange {
        output.setToNothing()
        for(point in points){
            output.include(point.projectOnto(angleCos, angleSin))
        }
        return output
    }

    data class SATData(
            val calculationLine: LineSegment = LineSegment(),
            val myRange: FloatRange = FloatRange(),
            val otherRange: FloatRange = FloatRange(),
            var angleCos: Float = 0f,
            var angleSin: Float = 0f,
            var distance: Float = 0f
    )

    fun separatingAxisTheoremSequence(other: Polygon, data: SATData = SATData()): Sequence<SATData> {
        return (this.lineSequence(data.calculationLine) + other.lineSequence(data.calculationLine))
                .map {
                    val length = it.length
                    //These seem flipped; that's because we're checking normals
                    val angleCos = it.deltaY// / length
                    val angleSin = it.deltaX// / length
                    data.angleCos = angleCos
                    data.angleSin = angleSin
                    val me = this.projectOnto(angleCos, angleSin, data.myRange)
                    val them = other.projectOnto(angleCos, angleSin, data.otherRange)
                    data.distance = me distanceTo them
                    data
                }
    }

    infix fun intersects(other: Polygon): Boolean = intersects(other, SATData())
    fun intersects(other: Polygon, data: SATData): Boolean {
        return separatingAxisTheoremSequence(other, data).all { it.distance < 0f }
    }

    infix fun distanceTo(other: Polygon): Float = distanceTo(other, SATData())
    fun distanceTo(other: Polygon, data: SATData): Float {
        return separatingAxisTheoremSequence(other, data).map { it.distance }.max() ?: Float.POSITIVE_INFINITY
    }

    infix fun distanceToWithAngle(other: Polygon): Point = distanceToWithAngle(other, SATData(), Point())
    fun distanceToWithAngle(other: Polygon, data: SATData, output: Point): Point {
        var bestAngleCos: Float = 0f
        var bestAngleSin: Float = 0f
        var bestDistance: Float = Float.NEGATIVE_INFINITY
        separatingAxisTheoremSequence(other, data).forEach {
            if(it.distance > bestDistance){
                bestDistance = it.distance
                bestAngleCos = it.angleCos
                bestAngleSin = it.angleSin
            }
        }
        output.x = bestDistance * bestAngleCos
        output.y = bestDistance * bestAngleSin
        return output
    }

//    infix fun intersects(other: Polygon): Boolean = intersects(other, LineSegment(), LineSegment())
//    fun intersects(other: Polygon, lineA: LineSegment, lineB: LineSegment): Boolean {
//        return lineSequence(lineA).any {
//            other.lineSequence(lineB).any {
//                lineA intersects lineB
//            }
//        }
//    }
//
//    //TODO: Do properly with Separating Axis Theorem
//    infix fun distanceTo(other: Polygon): Float = distanceTo(other, LineSegment())
//    fun distanceTo(other: Polygon, calculationLine: LineSegment): Float {
//        var bestDistance = Float.MAX_VALUE
//        for (point in this.points) {
//            val newDist = other.distanceTo(point, calculationLine)
//            if (newDist < bestDistance) {
//                bestDistance = newDist
//            }
//        }
//        for (point in other.points) {
//            val newDist = this.distanceTo(point, calculationLine)
//            if (newDist < bestDistance) {
//                bestDistance = newDist
//            }
//        }
//        return bestDistance
//    }
//
//    fun distanceToBestDetailed(other: Polygon, calculationPointResult: PointResult, output: PointResult): PointResult {
//        output.distance = Float.MAX_VALUE
//        for (point in this.points) {
//            other.distanceToDetailed(point, calculationPointResult)
//            if (calculationPointResult.distance < output.distance) {
//                output.set(calculationPointResult)
//            }
//        }
//        for (point in other.points) {
//            this.distanceToDetailed(point, calculationPointResult)
//            if (calculationPointResult.distance < output.distance) {
//                output.set(calculationPointResult)
//            }
//        }
//        return output
//    }
}

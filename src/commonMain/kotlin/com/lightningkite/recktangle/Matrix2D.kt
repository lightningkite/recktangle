package com.lightningkite.recktangle

inline class Matrix2D(val values: FloatArray = FloatArray(3 * 3)) {

    companion object {
        const val inputSize: Int = 3
        const val outputSize: Int = 3

        val identity = Matrix2D(
            floatArrayOf(
                1f, 0f, 0f,
                0f, 1f, 0f,
                0f, 0f, 1f
            )
        )

        fun rotate(angle: Angle) = Matrix2D(
            floatArrayOf(
                angle.cos(), -angle.sin(), 0f,
                angle.sin(), angle.cos(), 0f,
                0f, 0f, 1f
            )
        )

        fun translate(point: Point) = Matrix2D(
            floatArrayOf(
                1f, 0f, 0f,
                0f, 1f, 0f,
                point.x, point.y, 1f
            )
        )

        fun scale(x: Float, y: Float) = Matrix2D(
            floatArrayOf(
                x, 0f, 0f,
                0f, y, 0f,
                0f, 0f, 1f
            )
        )

        fun rotateThenTranslate(angle: Angle, point: Point) = Matrix2D(
            floatArrayOf(
                angle.cos(), -angle.sin(), 0f,
                angle.sin(), angle.cos(), 0f,
                point.x, point.y, 1f
            )
        )
    }


    operator fun get(input: Int, output: Int): Float {
        if (input < 0) throw IndexOutOfBoundsException("Input $input should be >= 0")
        if (output < 0) throw IndexOutOfBoundsException("Output $output should be >= 0")
        if (input >= inputSize) throw IndexOutOfBoundsException("Input $input should be < $inputSize")
        if (output >= outputSize) throw IndexOutOfBoundsException("Output $output should be < $outputSize")
        return assuredGet(input, output)
    }

    @Suppress("NOTHING_TO_INLINE")
    fun assuredGet(input: Int, output: Int): Float {
        return values[output * inputSize + input]
    }

    operator fun set(input: Int, output: Int, value: Float) {
        if (input < 0) throw IndexOutOfBoundsException("Input $input should be >= 0")
        if (output < 0) throw IndexOutOfBoundsException("Output $output should be >= 0")
        if (input >= inputSize) throw IndexOutOfBoundsException("Input $input should be < $inputSize")
        if (output >= outputSize) throw IndexOutOfBoundsException("Output $output should be < $outputSize")
        assuredSet(input, output, value)
    }

    @Suppress("NOTHING_TO_INLINE")
    fun assuredSet(input: Int, output: Int, value: Float) {
        values[output * inputSize + input] = value
    }

    inline fun forEachIndexed(action: (input: Int, output: Int, value: Float) -> Unit) {
        for (input in 0 until inputSize) {
            for (output in 0 until outputSize) {
                action(input, output, assuredGet(input, output))
            }
        }
    }

    operator fun times(other: Matrix2D): Matrix2D {
        val newMatrix = Matrix2D()
        for (input in 0 until inputSize) {
            for (output in 0 until outputSize) {
                for (cursor in 0 until outputSize) {
                    val adding = this.assuredGet(input, cursor) * other.assuredGet(cursor, output)
                    val newValue = assuredGet(input, output) + adding
                    assuredSet(input, output, newValue)
                }
            }
        }
        return newMatrix
    }

    infix fun before(other: Matrix2D): Matrix2D = this * other
    infix fun after(other: Matrix2D): Matrix2D = other * this

    fun transform(values: Point): Point {
        return Point(
            x = assuredGet(0, 0) * values.x + assuredGet(1, 0) * values.y + assuredGet(2, 0),
            y = assuredGet(0, 1) * values.x + assuredGet(1, 1) * values.y + assuredGet(2, 1)
        )
    }

    operator fun times(scale: Float): Matrix2D {
        val m = Matrix2D()
        for (i in m.values.indices) {
            m.values[i] = this.values[i] * scale
        }
        return m
    }
}

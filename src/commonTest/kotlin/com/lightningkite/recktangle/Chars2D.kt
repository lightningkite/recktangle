package com.lightningkite.recktangle

class Chars2D(val width: Int, val height: Int, val default: Char = ' ') {
    val array = CharArray(width * height) { default }
    operator fun get(x: Int, y: Int): Char {
        if (x < 0) return default
        if (y < 0) return default
        if (x >= width) return default
        if (y >= height) return default
        return array[y * width + x]
    }

    operator fun set(x: Int, y: Int, value: Char) {
        if (x < 0) return
        if (y < 0) return
        if (x >= width) return
        if (y >= height) return
        array[y * width + x] = value
    }

    fun positionX(index: Int): Int = index % width
    fun positionY(index: Int): Int = index / width

    inline fun forEach(action: (x: Int, y: Int, value: Char) -> Unit) {
        for (index in array.indices) {
            action(positionX(index), positionY(index), array[index])
        }
    }

    fun blit(left: Int, top: Int, other: Chars2D) {
        other.forEach { x, y, value ->
            if (value != default) {
                set(left + x, top + y, value)
            }
        }
    }

    fun box(left: Int, top: Int, right: Int, bottom: Int) {
        for (x in left + 1..right - 1) {
            this[x, top] = '-'
            this[x, bottom] = '-'
        }
        for (y in top + 1..bottom - 1) {
            this[left, y] = '|'
            this[right, y] = '|'
        }
        this[left, top] = '+'
        this[right, top] = '+'
        this[left, bottom] = '+'
        this[right, bottom] = '+'
    }

    fun text(x: Int, y: Int, string: String, maxSize: Int = string.length) {
        if (maxSize <= 0) return
        string.take(maxSize).forEachIndexed { index, char ->
            this[x + index, y] = char
        }
    }

    fun print(out: Appendable) {
        for (y in 0 until height) {
            for (x in 0 until width) {
                out.append(this[x, y])
            }
            out.append('\n')
        }
    }
}
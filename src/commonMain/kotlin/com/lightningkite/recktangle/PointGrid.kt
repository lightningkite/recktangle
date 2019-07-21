package com.lightningkite.recktangle

import kotlin.math.ceil
import kotlin.math.floor

class PointGrid<T>(
    val blockSize: Float = 32f,
    val width: Int = 12,
    val height: Int = 12,
    val startX: Float = -width * blockSize * .5f,
    val startY: Float = -height * blockSize * .5f
) {
    inner class Block(val x: Int, val y: Int, val entries: MutableList<Entry> = ArrayList()) {
        internal fun add(entry: Entry) {
            entries.add(entry)
            entry.block = this
        }

        internal fun remove(entry: Entry) {
            entries.remove(entry)
            entry.block = null
        }

        val nearbyBlocks: Sequence<Block>
            get() = (x - 1..x + 1).asSequence().flatMap { x ->
                (y - 1..y + 1).asSequence().mapNotNull { y -> get(x, y) }
            }

        fun nearbyBlocks(within: Float): Sequence<Block> {
            val delta = ceil(within / blockSize).toInt()
            return (x - delta..x + delta).asSequence().flatMap { x ->
                (y - delta..y + delta).asSequence().mapNotNull { y -> get(x, y) }
            }
        }
    }

    val blocks = Array<Block>(width * height) { Block(it % width, it / width) }

    inner class Entry(point: Point, val data: T) {
        var block: Block? = null
            internal set
        var point: Point = point
            set(value) {
                block?.remove(this)
                field = value
                getBlock(value)?.add(this)
            }

        fun update() {
            block?.remove(this)
            getBlock(point)?.add(this)
        }

        init {
            getBlock(point)?.add(this)
        }

        val nearbyEntries: Sequence<Entry>
            get() = block?.nearbyBlocks?.flatMap { it.entries.asSequence() } ?: sequenceOf()

        fun nearbyEntries(within: Float): Sequence<Entry> {
            return block?.nearbyBlocks(within)?.flatMap { it.entries.asSequence() } ?: sequenceOf()
        }

        fun removeSelf() {
            block?.remove(this)
        }

        override fun toString(): String {
            return "Entry(point=$point, data=$data, block=${block?.run { "($x, $y)" }})"
        }
    }

    private operator fun get(x: Int, y: Int): Block? {
        if (x < 0) return null
        if (y < 0) return null
        if (x >= width) return null
        if (y >= height) return null
        return assuredGet(x, y)
    }

    @Suppress("NOTHING_TO_INLINE")
    private fun assuredGet(x: Int, y: Int): Block {
        return blocks[y * width + x]
    }

    private fun getX(float: Float): Int = floor((float - startX) / blockSize).toInt()
    private fun getY(float: Float): Int = floor((float - startY) / blockSize).toInt()

    fun getBlock(point: Point): Block? = get(getX(point.x), getY(point.y))
    fun getBlock(x: Float, y: Float): Block? = get(getX(x), getY(y))

    fun nearbyEntries(point: Point, within: Float = blockSize - .000001f): Sequence<Entry> {
        return getBlock(point)?.nearbyBlocks(within)?.flatMap { it.entries.asSequence() } ?: sequenceOf()
    }
}

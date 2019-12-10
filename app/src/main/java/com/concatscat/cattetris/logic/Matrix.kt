package com.concatscat.cattetris.logic

class Matrix(val width: Int, val height: Int = width, initializer: (Int, Int) -> Int = { _, _ -> 0 }) {

    val size = width * height

    val array = Array(height) { row -> IntArray(width) { column -> initializer(row, column) } }

    operator fun get(row: Int): IntArray = array[row]

    operator fun get(column: Int, row: Int): Int = array[row][column]

    operator fun set(column: Int, row: Int, value: Int) {
        array[row][column] = value
    }

    fun clone() = Matrix(width, height) { row, column -> get(column, row) }

    operator fun Iterator<IntArray>.iterator(): Iterator<IntArray> {
        return array.iterator()
    }

    fun toList() = array.asSequence().flatMap { it.asSequence() }.toList()

    fun allIndexed(): Iterator<Pair<Int, Int>> = object : Iterator<Pair<Int, Int>> {
        var x = 0
        var y = 0

        override fun hasNext() = x < width && y < height

        override fun next(): Pair<Int, Int> {
            val pair = Pair(x, y)
            if (x == width - 1) {
                x = 0
                y++
            } else {
                x++
            }
            return pair
        }
    }

    fun all() = object : Iterator<Int> {
        val indexed = allIndexed()

        override fun hasNext() = indexed.hasNext()

        override fun next(): Int {
            val next = indexed.next()
            return get(next.first, next.second)
        }

    }

    override fun toString(): String {
        return StringBuilder().apply {
            append("matrix\n")
            for (row in array) {
                for (column in row) {
                    append(
                        when (column) {
                            1 -> "*"
                            2 -> "@"
                            else -> "_"
                        }
                    )
                    append(" ")
                }
                append("\n")
            }
        }.toString()
    }

}
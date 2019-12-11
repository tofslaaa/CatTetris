package com.concatscat.cattetris.logic

class Matrix(
    val width: Int,
    val height: Int = width,
    initializer: (Int, Int) -> Int = { _, _ -> 0 }
) {

    constructor(width: Int, height: Int, blocksType: Int, array: IntArray) : this(
        width,
        height,
        { row, column ->
            if (array[width * row + column] != 0) blocksType else 0
        })

    val array = Array(height) { row -> IntArray(width) { column -> initializer(row, column) } }

    operator fun get(column: Int, row: Int): Int = array[row][column]

    operator fun set(column: Int, row: Int, value: Int) {
        array[row][column] = value
    }

    val blocksCount: Int
        get() {
            var x = 0
            var y = 0
            var counter = 0
            while (x < width && y < height) {
                if (get(x, y) != 0) {
                    counter++
                }
                if (x == width - 1) {
                    x = 0
                    y++
                } else {
                    x++
                }
            }
            return counter
        }

    fun clone() = Matrix(width, height) { row, column -> get(column, row) }

    fun toList() = array.asSequence().flatMap { it.asSequence() }.toList()
}
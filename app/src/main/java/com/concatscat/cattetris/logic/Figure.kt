package com.concatscat.cattetris.logic

import kotlin.random.Random

sealed class Figure(var matrix: Matrix, val coordinates: Coordinates = Coordinates(0, 0)) {
    val blocksCount = matrix.all().asSequence().filter { it != 0 }.count()
    val width get() = matrix.width
    val height get() = if (this is None) 0 else matrix.height

    val left get() = coordinates.x
    val right get() = coordinates.x + width
    val top get() = coordinates.y
    val bottom get() = coordinates.y + height

    object None : Figure(Matrix(0))
    class Line(x: Int, blocksType: Int) : Figure(Matrix(1, 4) { _, _ -> blocksType }, Coordinates(x, 0))
    class Square(x: Int, blocksType: Int) : Figure(Matrix(2, 2) { _, _ -> blocksType }, Coordinates(x, 0))
    class Stairs(x: Int, blocksType: Int) :
        Figure(Matrix(3, 2) { row, column -> (!(row == 0 && (column == 0 || column == 2))).toInt(blocksType) }, Coordinates(x, 0))

    class SnakeLeft(x: Int, blocksType: Int) :
        Figure(
            Matrix(3, 2) { row, column -> (!((row == 0 && column == 2) || (row == 1 && column == 0))).toInt(blocksType) },
            Coordinates(x, 0)
        )

    class SnakeRight(x: Int, blocksType: Int) :
        Figure(
            Matrix(3, 2) { row, column -> (!((row == 0 && column == 0) || (row == 1 && column == 2))).toInt(blocksType) },
            Coordinates(x, 0)
        )

    class GunLeft(x: Int, blocksType: Int) :
        Figure(Matrix(3, 2) { row, column -> ((row == 0 && column == 0) || row == 1).toInt(blocksType) }, Coordinates(x, 0))

    class GunRight(x: Int, blocksType: Int) :
        Figure(Matrix(3, 2) { row, column -> ((row == 0 && column == 2) || row == 1).toInt(blocksType) }, Coordinates(x, 0))

    fun rotate() {
        val w = width
        matrix = Matrix(height, width) { row, column ->
            matrix[w - 1 - row, column]
        }
    }

    fun blocksIterator(): Iterator<Pair<Int, Int>> = object : Iterator<Pair<Int, Int>> {
        var blockIndex = 0
        var x = 0
        var y = 0

        override fun hasNext() = blockIndex < blocksCount

        override fun next(): Pair<Int, Int> {
            val block = nextBlock()
            blockIndex++
            return block
        }

        private fun nextBlock(): Pair<Int, Int> {
            while (matrix[x, y] == 0) {
                if (x == width - 1) {
                    x = 0
                    y++
                } else {
                    x++
                }
            }
            val pair = Pair(x + coordinates.x, y + coordinates.y)
            if (x == width - 1) {
                x = 0
                y++
            } else {
                x++
            }
            return pair
        }
    }

    companion object {
        fun random(x: Int, blocksType: Int = 1): Figure =
            when (Random.nextInt(0, 7)) {
                0 -> Line(x, blocksType)
                1 -> Square(x, blocksType)
                2 -> Stairs(x, blocksType)
                3 -> SnakeLeft(x, blocksType)
                4 -> SnakeRight(x, blocksType)
                5 -> GunLeft(x, blocksType)
                6 -> GunRight(x, blocksType)
                else -> throw Exception()
            }.apply {
                coordinates.x = x - width / 2
            }

        fun Boolean.toInt(blocksType: Int) = if (this) blocksType else 0
    }

}
package com.concatscat.cattetris.logic

import kotlin.random.Random

sealed class Figure(width: Int, height: Int, blocksType: Int, array: IntArray, val coordinates: Coordinates = Coordinates(0, 0)) {
    var matrix: Matrix = Matrix(width, height, blocksType, array)
    private val blocksCount get() = matrix.blocksCount
    val width get() = matrix.width
    val height get() = if (this is None) 0 else matrix.height

    val left get() = coordinates.x
    val right get() = coordinates.x + width
    val top get() = coordinates.y
    val bottom get() = coordinates.y + height

    object None : Figure(0, 0, 0, intArrayOf())
    class Line(blocksType: Int) :
        Figure(
            1, 4, blocksType, intArrayOf(
                1, 1, 1, 1
            )
        )

    class Square(blocksType: Int) : Figure(
        2, 2, blocksType, intArrayOf(
            1, 1,
            1, 1
        )
    )

    class Stairs(blocksType: Int) : Figure(
        3, 2, blocksType, intArrayOf(
            0, 1, 0,
            1, 1, 1
        )
    )

    class SnakeLeft(blocksType: Int) : Figure(
        3, 2, blocksType, intArrayOf(
            1, 1, 0,
            0, 1, 1
        )
    )

    class SnakeRight(blocksType: Int) : Figure(
        3, 2, blocksType, intArrayOf(
            0, 1, 1,
            1, 1, 0
        )
    )

    class GunLeft(blocksType: Int) : Figure(
        3, 2, blocksType, intArrayOf(
            1, 0, 0,
            1, 1, 1
        )
    )

    class GunRight(blocksType: Int) : Figure(
        3, 2, blocksType, intArrayOf(
            0, 0, 1,
            1, 1, 1
        )
    )

    fun rotate() {
        val w = width
        matrix = Matrix(width = height, height = width) { row, column ->
            matrix[w - 1 - row, column]
        }
    }

    fun blocks(): List<Coordinates> {
        var blockIndex = 0
        var x = 0
        var y = 0
        val result = mutableListOf<Coordinates>()

        while (blockIndex < blocksCount) {
            while (matrix[x, y] == 0) {
                if (x == width - 1) {
                    x = 0
                    y++
                } else {
                    x++
                }
            }
            result.add(Coordinates(x + coordinates.x, y + coordinates.y))
            blockIndex++
            if (x == width - 1) {
                x = 0
                y++
            } else {
                x++
            }
        }
        return result
    }

    companion object {
        fun random(x: Int, blocksType: Int = 1): Figure =
            when (Random.nextInt(0, 7)) {
                0 -> Line(blocksType)
                1 -> Square(blocksType)
                2 -> Stairs(blocksType)
                3 -> SnakeLeft(blocksType)
                4 -> SnakeRight(blocksType)
                5 -> GunLeft(blocksType)
                6 -> GunRight(blocksType)
                else -> throw Exception()
            }.apply {
                coordinates.x = x - width / 2
            }
    }

}
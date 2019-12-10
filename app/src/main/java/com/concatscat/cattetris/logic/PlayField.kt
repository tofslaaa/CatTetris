package com.concatscat.cattetris.logic

import android.util.Log
import com.concatscat.cattetris.BlockModel

class PlayField(val width: Int, val height: Int = width, private val listener: Listener) {

    val TAG = PlayField::class.java.simpleName

    val size = width * height

    var figure: Figure = Figure.None

    var matrix = Matrix(width, height)

    private var top = height

    fun updateTick() {
        val collides = collides()
        if (collides) {
            mergeFigure()
            update()
            listener.onCollides()
        } else {
            figure.coordinates.moveDown()
            update()
        }

        Log.d(TAG, merged().toString())
    }

    fun update() {
        listener.onUpdated(merged())
    }

    private fun merged() =
        matrix.clone().apply {
            for ((x, y) in figure.blocksIterator()) {
                this[x, y] = mapFigureType()
            }
        }

    private fun mergeFigure() {
        for ((x, y) in figure.blocksIterator()) {
            matrix[x, y] = mapFigureType()
        }
        if (figure.top < top) {
            top = figure.top
        }
    }

    private fun mapFigureType(): Int {
        return when (figure) {
            is Figure.None -> BlockModel.Type.NONE.type
            is Figure.Line -> BlockModel.Type.LINE.type
            is Figure.GunLeft -> BlockModel.Type.GUN.type
            is Figure.GunRight -> BlockModel.Type.GUN.type
            is Figure.SnakeLeft -> BlockModel.Type.SNAKE.type
            is Figure.SnakeRight -> BlockModel.Type.SNAKE.type
            is Figure.Square -> BlockModel.Type.SQUARE.type
            is Figure.Stairs -> BlockModel.Type.STAIRS.type
        }
    }

    fun removeFilledLines(): Int {
        var count = 0
        for (row in matrix.array) {
            if (!row.contains(0)) {
                for (j in row.indices) {
                    row[j] = 0
                }
                count++
            }
        }
        return count
    }

    fun fall() {
        val emptyRows = mutableListOf<Int>()
        for (rowIndex in matrix.array.indices.reversed()) {
            if (rowIndex < top) {
                continue
            }
            val row = matrix.array[rowIndex]
            val isEmptyRow = row.all { it == 0 }
            if (isEmptyRow) {
                emptyRows.add(rowIndex)
            }
        }

        for (emptyRowIndex in emptyRows.reversed()) {
            for (rowIndex in emptyRowIndex - 1 downTo 0) {
                val row = matrix.array[emptyRowIndex]
                for (columnIndex in row.indices) {
                    matrix[columnIndex, rowIndex + 1] = matrix[columnIndex, rowIndex]
                }
            }
        }
    }

    private fun collides() =
        figure.blocksIterator()
            .asSequence()
            .filter { (x, y) ->
                figure.bottom >= height || matrix[x, y + 1] != 0
            }
            .any()

    fun clear() {
        figure = Figure.None
        matrix = Matrix(width, height)
        top = height
    }

    interface Listener {
        fun onCollides()
        fun onUpdated(matrix: Matrix)
    }

}
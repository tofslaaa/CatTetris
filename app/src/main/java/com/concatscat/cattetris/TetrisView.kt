package com.concatscat.cattetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.concatscat.cattetris.shape.TetrisBlock

class TetrisView(context: Context) : View(context) {

    private val randomShape = TetrisBlock.getShapeGrid()

    private val startingCoords = Pair(0, 8)

    private val painter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        with(TetrisBlock){
            addShapeToGrid(screenGrid, randomShape, startingCoords)
            draw(screenGrid, canvas, painter)
        }
    }

    fun loop() {
        render()
    }

    private fun render() {
        //randomShape.move('D')
        invalidate()
    }
}
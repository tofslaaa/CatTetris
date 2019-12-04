package com.concatscat.cattetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.BoringLayout
import android.view.View
import android.widget.RelativeLayout

class Tetris(context: Context) : View(context) {

    private val randomBlock = TetrisBlock().getBlock()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        randomBlock.draw(canvas)
    }

    fun loop() {
        render()
    }

    private fun render() {
        randomBlock.move('D')
        invalidate()
    }
}
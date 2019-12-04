package com.concatscat.cattetris

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.concatscat.cattetris.shape.*
import java.util.*
import kotlin.collections.ArrayList

open class TetrisBlock {
    val cells = ArrayList<Rect>()

    val loweringSpeed = 10

    val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 10f
        style = Paint.Style.FILL
        color = Color.RED
    }

    fun getBlock() = when (Random().nextInt(5) + 1) {
        1 -> Box()
        2 -> ZBlock()
        3 -> Stick()
        4 -> Glider()
        5 -> TBlock()
        else -> throw Exception("Invalid choice")
    }

    fun draw(canvas: Canvas) {
        cells.forEach { canvas.drawRect(it, painter) }
    }

    fun move(direction: Char) = when (direction) {
        'L' -> cells.forEach {
            it.right -= loweringSpeed
            it.left -= loweringSpeed
        }
        'R' -> cells.forEach {
            it.right += loweringSpeed
            it.left += loweringSpeed
        }
        'U' -> cells.forEach {
            it.top -= loweringSpeed
            it.bottom -= loweringSpeed
        }
        'D' -> cells.forEach {
            it.top += loweringSpeed
            it.bottom += loweringSpeed
        }
        else -> throw Exception("Invalid move, choose from 'L', 'R', 'U', 'D'")
    }
}
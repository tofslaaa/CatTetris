package com.concatscat.cattetris.shape

import android.graphics.Rect
import com.concatscat.cattetris.TetrisBlock

class Box : TetrisBlock() {
    val cellHeight = 50
    val cellWidth = 50

    init {
        cells.add(Rect(0, 0, cellWidth * 2, cellHeight))
        cells.add(Rect(0, cellHeight, cellWidth * 2, cellHeight * 2))
    }
}
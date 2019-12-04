package com.concatscat.cattetris.shape

import android.graphics.Rect
import com.concatscat.cattetris.TetrisBlock

class ZBlock : TetrisBlock() {
    val cellHeight = 50
    val cellWidth = 50

    init {
        cells.add(Rect(cellWidth, 0, cellWidth * 3, cellHeight))
        cells.add(Rect(0, cellHeight, cellWidth * 2, cellHeight * 2))
    }
}
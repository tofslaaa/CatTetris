package com.concatscat.cattetris.logic

data class Coordinates(var x: Int, var y: Int) {

    fun moveDown() {
        y++
    }

}
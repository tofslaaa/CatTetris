package com.concatscat.cattetris.logic

class Control(private val game: Game) {

    private var collides = false
    private val fallTicker = Ticker(100)

    fun moveLeft() {
        val left = game.figure.left
        val right = game.figure.right
        if (left > 0 && right <= game.playField.width) {
            game.figure.coordinates.x -= 1
            if (intersects()) {
                game.figure.coordinates.x += 1
            } else {
                game.playField.update()
            }
        }
    }

    fun moveRight() {
        val left = game.figure.left
        val right = game.figure.right
        if (left >= 0 && right < game.playField.width) {
            game.figure.coordinates.x += 1
            if (intersects()) {
                game.figure.coordinates.x -= 1
            } else {
                game.playField.update()
            }
        }
    }

    fun rotate() {
        val oldMatrix = game.figure.matrix
        game.figure.rotate()
        if (intersects()) {
            game.figure.matrix = oldMatrix
        } else {
            game.playField.update()
        }
    }

    private fun intersects() = game.figure.blocks().any { (x, y) -> game.playField.matrix[x, y] != 0 }

    fun fall() {
        game.onTick()
        collides = false
        fallTicker.start {
            if (collides) {
                fallTicker.stop()
            } else {
                game.onTick()
            }
        }
    }

    fun stopFall() {
        fallTicker.stop()
    }

    fun onCollides() {
        collides = true
    }

}
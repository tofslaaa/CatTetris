package com.concatscat.cattetris.logic

class Control(private val game: Game) {

    private var collides = false
    private val fallTicker = Ticker(100)

    fun moveLeft() {
        val left = game.figure.left
        val right = game.figure.right
        if (left > 0 && right <= game.territory.width) {
            game.figure.coordinates.x -= 1
            if (collides()) {
                game.figure.coordinates.x += 1
            } else {
                game.territory.update()
            }
        }
    }

    fun moveRight() {
        val left = game.figure.left
        val right = game.figure.right
        game.territory.matrix
        if (left >= 0 && right < game.territory.width) {
            game.figure.coordinates.x += 1
            if (collides()) {
                game.figure.coordinates.x -= 1
            } else {
                game.territory.update()
            }
        }
    }

    fun rotate() {
        val oldMatrix = game.figure.matrix
        game.figure.rotateCcw()
        if (collides()) {
            game.figure.matrix = oldMatrix
        } else {
            game.territory.update()
        }
    }

    private fun collides() = game.figure.blocksIterator().asSequence().filter { (x, y) -> game.territory.matrix[x, y] != 0 }.any()

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
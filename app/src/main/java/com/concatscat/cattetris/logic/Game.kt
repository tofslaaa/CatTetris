package com.concatscat.cattetris.logic

class Game(val listener: Listener) {

    var figure: Figure = Figure.None

    private val ticker = Ticker(500)

    val control = Control(this)

    var isStarted = false

    fun start() {
        ticker.stop()
        isStarted = true
        playField.clear()
        figure = Figure.random(playField.width / 2)
        playField.figure = figure
        playField.update()
        ticker.start { onTick() }
        listener.onStarted()
    }

    fun pause() {
        ticker.stop()
        listener.onPaused()
    }

    fun resume() {
        if (!ticker.running) {
            ticker.start { onTick() }
            listener.onResumed()
        }
    }

    private fun end() {
        ticker.stop()
        figure = Figure.None
        playField.figure = figure
        isStarted = false
        listener.onGameEnded()
    }

    fun onTick() {
        playField.updateTick()
    }

    private val playFieldListener = object : PlayField.Listener {
        override fun onCollides() {
            control.onCollides()
            if (figure.coordinates.y == 0) {
                end()
                return
            }
            figure = Figure.random(playField.width / 2)
            playField.figure = figure
            val count = playField.removeFilledLines()
            listener.onFilledLinesRemoved(count)
            playField.update()
            playField.fall()
        }

        override fun onUpdated(matrix: Matrix) {
            listener.onUpdated(matrix)
        }
    }

    val width = 8
    val height = 12
    val playField: PlayField = PlayField(width, height, playFieldListener)

    interface Listener {
        fun onFilledLinesRemoved(count: Int)
        fun onUpdated(matrix: Matrix)
        fun onGameEnded()
        fun onPaused()
        fun onResumed()
        fun onStarted()
    }
}
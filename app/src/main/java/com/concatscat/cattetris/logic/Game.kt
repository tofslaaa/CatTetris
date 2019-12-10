package com.concatscat.cattetris.logic

class Game(val listener: Listener) {

    var figure: Figure = Figure.None

    private val ticker = Ticker(500)

    val control = Control(this)

    var isStarted = false

    fun start() {
        ticker.stop()
        isStarted = true
        territory.clear()
        figure = Figure.random(territory.width / 2)
        territory.figure = figure
        territory.update()
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
        territory.figure = figure
        isStarted = false
        listener.onGameEnded()
    }

    fun onTick() {
        territory.updateTick()
    }

    private val territoryListener = object : PlayField.Listener {
        override fun onCollides() {
            control.onCollides()
            if (figure.coordinates.y == 0) {
                end()
                return
            }
            figure = Figure.random(territory.width / 2)
            territory.figure = figure
            territory.removeFilledLines()
            listener.onFilledLinesRemoved()
            territory.update()
            territory.fall()
        }

        override fun onUpdated(matrix: Matrix) {
            listener.onUpdated(matrix)
        }
    }

    val width = 9
    val height = 12
    val territory: PlayField = PlayField(width, height, territoryListener)

    interface Listener {
        fun onFilledLinesRemoved() {}
        fun onUpdated(matrix: Matrix)
        fun onGameEnded()
        fun onPaused()
        fun onResumed()
        fun onStarted()
    }
}
package com.concatscat.cattetris

import com.concatscat.cattetris.logic.Game
import com.concatscat.cattetris.logic.Matrix

class MainPresenter(private val view: View) {

    fun onStart() {
        //view.updateSize(game.territory.width, game.territory.height)
        if (game.isStarted) {
            game.resume()
        } else {
            view.enableControls(false)
            view.showCatImage(true)
            view.showSplashText(true)
            view.updateItems((0 until game.territory.size).map { BlockModel(BlockModel.Type.NONE) }.toList())
        }
    }

    fun onStop() {
        if (game.isStarted) {
            game.pause()
        }
    }

    fun onStartClicked() {
        if (!game.isStarted) {
            game.start()
            //view.updateSize(game.territory.width, game.territory.height)
        } else {
            game.resume()
            view.showPauseOverlay(false)
        }
    }

    fun onPauseClicked() {
        if (game.isStarted) {
            game.pause()
            view.showPauseOverlay(true)
        }
    }

    fun onLeftClicked() {
        game.control.moveLeft()
    }

    fun onRightClicked() {
        game.control.moveRight()
    }

    fun onRotateClicked() {
        game.control.rotate()
    }

    fun onFallStarted() {
        game.control.fall()
    }

    fun onFallEnded() {
        game.control.stopFall()
    }

    fun onRestartClicked() {
        game.start()
    }

    private val listener = object : Game.Listener {

        override fun onUpdated(matrix: Matrix) {
            val items = matrix.toList().map { block ->
                BlockModel(BlockModel.Type.values().first { it.type == block })
            }
            view.updateItems(items)
        }

        override fun onGameEnded() {
            view.showPauseButton(false)
            view.showPauseOverlay(true)
            view.showSplashText(true)
            view.updateSplashText(R.string.end_text)
            view.enableControls(false)
        }

        override fun onPaused() {
            view.showPauseButton(false)
            view.enableControls(false)
        }

        override fun onResumed() {
            view.showPauseButton(true)
            view.enableControls(true)
        }

        override fun onStarted() {
            view.showPauseButton(true)
            view.showCatImage(false)
            view.showSplashText(false)
            view.showPauseOverlay(false)
            view.enableControls(true)
        }

    }

    val game = Game(listener)

    interface View {
        fun updateItems(items: List<BlockModel>)
        fun updateSize(width: Int, height: Int)
        fun showPauseButton(show: Boolean)
        fun showCatImage(show: Boolean)
        fun showPauseOverlay(show: Boolean)
        fun showSplashText(show: Boolean)
        fun updateSplashText(textId: Int)
        fun enableControls(enabled: Boolean)
    }
}
package com.concatscat.cattetris.logic

import android.os.Handler

class Ticker(private val interval: Long) {

    private val handler = Handler()
    private var onTick: (() -> Unit)? = null

    var running = false

    private val runnable: Runnable by lazy {
        Runnable {
            if (running) {
                onTick?.invoke()
                handler.postDelayed(runnable, interval)
            }
        }
    }

    fun start(onTick: () -> Unit) {
        handler.removeCallbacks(runnable)
        this.onTick = onTick
        running = true
        handler.postDelayed(runnable, interval)
    }

    fun stop() {
        handler.removeCallbacks(runnable)
        onTick = null
        running = false
    }
}
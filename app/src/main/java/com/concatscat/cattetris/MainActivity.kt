package com.concatscat.cattetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mBlocksAdapter: BlocksAdapter
    lateinit var presenter: Presenter

    var timer: Timer? = null
    lateinit var timerTask: MyTimerTask

    var isGameRun = false
    var mIsFirstBlockUpdate = false

    private var mPositions: MutableList<Int> = (0..95).map { 0 }.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(view)

        mBlocksAdapter = BlocksAdapter(mPositions)

        play_field.layoutManager = GridLayoutManager(this, 8)
        play_field.adapter = mBlocksAdapter

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        play_button.setOnClickListener {
            onPlayButtonClicked()
        }

        pause_button.setOnClickListener {
            timer?.cancel()

            pause_button.visibility = View.INVISIBLE
            pause_button.isClickable = false

            play_button.visibility = View.VISIBLE
            play_button.isClickable = true

            pause_text.visibility = View.VISIBLE
        }

        replay_button.setOnClickListener {
            isGameRun = false
            onPlayButtonClicked()
        }
    }

    private fun onPlayButtonClicked() {
        if (!isGameRun) {
            presenter.generateBlock {positions ->
                mPositions = positions
            }
            mIsFirstBlockUpdate = true
        }
        startTimer()

        play_button.visibility = View.INVISIBLE
        play_button.isClickable = false

        pause_button.visibility = View.VISIBLE
        pause_button.isClickable = true

        cat.visibility = View.INVISIBLE
        start_text.visibility = View.INVISIBLE
        pause_text.visibility = View.INVISIBLE

        isGameRun = true
    }

    private fun startTimer() {
        if (timer != null) {
            timer?.cancel()
        }

        timer = Timer()
        timerTask = MyTimerTask()

        timer?.schedule(timerTask, 1000, 2000)
    }

    private val view: Presenter.View = object : Presenter.View {
        override fun generateNewBlock() {
            presenter.generateBlock {positions ->
                mPositions = positions
            }
            mIsFirstBlockUpdate = true
        }

        override fun updateCurrentBlockPositions(positions: MutableList<Int>) {
            mPositions = positions
        }

        override fun updateGrid(grid: MutableList<Int>) {
            mIsFirstBlockUpdate = false
            mBlocksAdapter.setNewList(grid)
        }
    }

    inner class MyTimerTask :
        TimerTask() {
        var i = 0

        override fun run() {
            i++

            this@MainActivity.runOnUiThread {
                Log.d("TIMESSSS", i.toString())
                presenter.updateBlockPosition(mPositions, mIsFirstBlockUpdate)
            }
        }
    }
}

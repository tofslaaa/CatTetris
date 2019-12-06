package com.concatscat.cattetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Transformations.map
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mBlocksAdapter: BlocksAdapter
    lateinit var presenter: Presenter

    var timer: Timer? = null
    lateinit var timerTask: MyTimerTask

    var isGameRun = false

    private var mPositions: MutableList<Int> = (0..95).map { 0 }.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(view)

        mBlocksAdapter = BlocksAdapter(this, mPositions)

        play_field.layoutManager = GridLayoutManager(this, 8)
        play_field.adapter = mBlocksAdapter

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        play_button.setOnClickListener {
            if (isGameRun) {
                startTimer()
            } else {
                presenter.generateBlock {
                    updateCurrentBlockPosition(it)
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
            presenter.generateBlock {
                updateCurrentBlockPosition(it)
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
    }

    private fun startTimer() {
        if (timer != null) {
            timer?.cancel()
        }

        timer = Timer()
        timerTask = MyTimerTask(this, presenter, mPositions)

        timer?.schedule(timerTask,0,1000)
    }

    private fun updateCurrentBlockPosition(positions: MutableList<Int>) {
        mPositions = positions
    }

    private val view: Presenter.View = object : Presenter.View {
        override fun updateBlockPositions(positions: MutableList<Int>) {
            updateCurrentBlockPosition(positions)

            mBlocksAdapter.setNewList(positions) { isNeedNewBlock ->
                if (isNeedNewBlock) {
                    presenter.generateBlock {
                        updateBlockPositions(mPositions)
                    }
                    startTimer()
                }
            }
        }
    }

    class MyTimerTask(
        private val activity: MainActivity,
        private val presenter: Presenter,
        private val positions: MutableList<Int>
    ) :
        TimerTask() {
        var i = 0
        override fun run() {
            i++
            Log.d("TIMESSSS", i.toString())

            activity.runOnUiThread {
                    Log.d("RUNONUI", "oueee")
                    presenter.updateBlockPosition(positions)
            }
        }
    }
}

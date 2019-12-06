package com.concatscat.cattetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    private var mPositions: List<Int> = (0..95).map { 0 }.toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(view)

        mBlocksAdapter = BlocksAdapter(this, mPositions)

        play_field.layoutManager = GridLayoutManager(this, 8)
        play_field.adapter = mBlocksAdapter


        play_button.setOnClickListener {
            if (isGameRun){

            } else {
                presenter.generateBlocks()
                startTimer()

                play_button.visibility = View.INVISIBLE
                play_button.isClickable = false

                pause_button.visibility = View.VISIBLE
                pause_button.isClickable = true

                isGameRun = true
            }
        }

        pause_button.setOnClickListener {
                timer?.cancel()


            pause_button.visibility = View.INVISIBLE
            pause_button.isClickable = false

            play_button.visibility = View.VISIBLE
            play_button.isClickable = true
        }
    }

    private fun startTimer(){
        if (timer != null){
            timer?.cancel()
        }

        timer = Timer()
        timerTask = MyTimerTask()

        timer?.schedule(timerTask, 0, 500)
    }

    private val view: Presenter.View = object : Presenter.View{
        override fun setNewBlock(positions: List<Int>) {
            cat.visibility = View.INVISIBLE
            start_text.visibility = View.INVISIBLE

            mBlocksAdapter.setNewList(positions)
        }
    }

    class MyTimerTask(): TimerTask() {

        override fun run() {

        }
    }
}

package com.concatscat.cattetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var presenter: MainPresenter
    lateinit var adapter: BlocksAdapter
    lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(viewPresenter)

        play_field.apply {
            adapter = BlocksAdapter()
            layoutManager = GridLayoutManager(this@MainActivity, 8)
            }

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        left_button.setOnClickListener { presenter.onLeftClicked() }
        right_button.setOnClickListener { presenter.onRightClicked() }
        down_button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> presenter.onFallStarted().let { true }
                MotionEvent.ACTION_UP -> presenter.onFallEnded().let { true }
                else -> false
            }
        }

        rotation_button.setOnClickListener { presenter.onRotateClicked() }

        play_button.setOnClickListener { presenter.onStartClicked() }
        pause_button.setOnClickListener { presenter.onPauseClicked() }
        replay_button.setOnClickListener { presenter.onRestartClicked() }
    }

    private val viewPresenter = object : MainPresenter.View {

        override fun updateItems(items: List<BlockModel>) {
            adapter.updateItems(items)
            adapter.notifyDataSetChanged()
        }

        override fun updateSize(width: Int, height: Int) {
            layoutManager.spanCount = width
            //game_map.updateSize(width, height)
        }

        override fun showPauseButton(show: Boolean) {
            pause_button.visibility = if (show) View.VISIBLE else View.GONE
            play_button.visibility = if (show) View.GONE else View.VISIBLE
        }

        override fun showCatImage(show: Boolean) {
            cat.visibility = if (show) View.VISIBLE else View.INVISIBLE
        }

        override fun showSplashText(show: Boolean) {
            splash_text.visibility = if (show) View.VISIBLE else View.GONE
        }

        override fun updateSplashText(textId: Int) {
            splash_text.setText(textId)
        }

        override fun enableControls(enabled: Boolean) {
            left_button.isEnabled = enabled
            right_button.isEnabled = enabled
            down_button.isEnabled = enabled
            rotation_button.isEnabled = enabled
        }

        override fun showPauseOverlay(show: Boolean) {
            play_field.alpha = if (show) 0.2f else 1f
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

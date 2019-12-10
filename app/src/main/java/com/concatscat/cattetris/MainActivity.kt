package com.concatscat.cattetris

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var presenter: MainPresenter
    lateinit var adapter: BlocksAdapter
    lateinit var layoutManager: GridLayoutManager

    private val APP_PREFERENCES = "preferences"
    private val APP_PREFERENCES_RECORD = "record"
    lateinit var sharedPreferences: SharedPreferences

    private var currentRecord = 0
    private var currentLines = 0

    private var record = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeRecyclerView()
        getSharedPreferences()
        setOnClickListeners()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
        saveSharedPreferences()
    }

    override fun onBackPressed() {
        presenter.onBackClicked()
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

    private fun initializeRecyclerView() {
        presenter = MainPresenter(viewPresenter)

        adapter = BlocksAdapter()
        layoutManager = GridLayoutManager(this, 8)

        play_field.adapter = this.adapter
        play_field.layoutManager = this.layoutManager
    }

    private fun saveSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.putInt(APP_PREFERENCES_RECORD, record)
        editor.apply()
    }

    private fun getSharedPreferences() {
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        if (sharedPreferences.contains(APP_PREFERENCES_RECORD)) {
            record = sharedPreferences.getInt(APP_PREFERENCES_RECORD, 0)
            record_data.text = record.toString()
        }
    }

    private val viewPresenter = object : MainPresenter.View {
        override fun updateLinesCounter(count: Int) {
            if (count == -1){
                currentLines = 0
            } else {
                currentLines += count
            }

            lines_data.text = currentLines.toString()
        }

        override fun updateRecord(lines: Int) {
            currentRecord += when(lines){
                1 -> 100
                2 -> 300
                3 -> 700
                4 -> 1500
                else -> 0
            }

            if (record < currentRecord){
                record = currentRecord
                record_data.text = record.toString()
            }
        }

        override fun updateItems(items: List<BlockModel>) {
            adapter.updateItems(items)
            adapter.notifyDataSetChanged()
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

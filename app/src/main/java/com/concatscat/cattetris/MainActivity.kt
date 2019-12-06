package com.concatscat.cattetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mBlocksAdapter: BlocksAdapter
    lateinit var presenter: Presenter

    private var mPositions: List<Int> = (0..95).map { 0 }.toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(view)

        mBlocksAdapter = BlocksAdapter(this, mPositions)

        play_field.layoutManager = GridLayoutManager(this, 8)
        play_field.adapter = mBlocksAdapter


        play_button.setOnClickListener {
            presenter.generateBlocks()
        }
    }

    private val view: Presenter.View = object : Presenter.View{
        override fun setNewBlock(positions: List<Int>) {
            cat.visibility = View.INVISIBLE
            start_text.visibility = View.INVISIBLE

            mBlocksAdapter.setNewList(positions)
        }
    }
}

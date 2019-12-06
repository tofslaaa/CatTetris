package com.concatscat.cattetris

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_cat.view.*

class BlocksAdapter(
    val context: Context,
    private var positionsList: MutableList<Int>
) :
    RecyclerView.Adapter<BlocksAdapter.ViewHolder>() {

    private var grid: MutableList<Int> = positionsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return grid.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (grid[position]) {
            0 -> holder.cat.setBackgroundResource(R.drawable.cat_transparent)
            2 -> holder.cat.setBackgroundResource(R.drawable.cat_black)
            3 -> holder.cat.setBackgroundResource(R.drawable.cat_brown)
            4 -> holder.cat.setBackgroundResource(R.drawable.cat_white)
            else -> holder.cat.setBackgroundResource(R.drawable.cat_orange)
        }
    }

    fun setNewList(list: MutableList<Int>, callback: (isNeedNewBlock: Boolean) -> Unit) {
        updateGrid(list)
        notifyDataSetChanged()

        checkGridCompletion {
            callback(it)
        }
    }

    private fun updateGrid(list: MutableList<Int>) {
        for (i in grid.indices) {
            if (grid[i] == 0 && list[i] != 0) {
                grid[i] = list[i]
            }
        }
        Log.d("GRIDINDICIES", grid.toString())
    }

    private fun checkGridCompletion(callback: (isNeedNewBlock: Boolean) -> Unit) {
        var countOfNotNull = 0
        for (i in grid.size-1..grid.size-9){
            if (grid[i] != 0){
                countOfNotNull++
            }
        }

        if (countOfNotNull != 0){
            callback(true)
        } else callback(false)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cat: AppCompatImageView = itemView.item_cat
    }
}
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

class BlocksAdapter :
    RecyclerView.Adapter<BlocksAdapter.ViewHolder>() {

    private var grid = mutableListOf<BlockModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return grid.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cat.setImageResource(
            when(grid[position].type){
                BlockModel.Type.NONE -> R.drawable.cat_transparent
                BlockModel.Type.GUN -> R.drawable.cat_orange
                BlockModel.Type.LINE -> R.drawable.cat_black
                BlockModel.Type.SQUARE -> R.drawable.cat_white
                BlockModel.Type.SNAKE -> R.drawable.cat_brown
                else -> R.drawable.cat_transparent
            }
        )
    }

    fun updateItems(list: List<BlockModel>) {
        grid.clear()
        grid.addAll(list)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cat: AppCompatImageView = itemView.item_cat
    }
}
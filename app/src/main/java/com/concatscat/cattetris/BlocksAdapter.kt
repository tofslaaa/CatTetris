package com.concatscat.cattetris

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_cat.view.*

class BlocksAdapter(
    val context: Context,
    private var positionsList: List<Int>
) :
    RecyclerView.Adapter<BlocksAdapter.ViewHolder>() {

    private var grid: List<Int> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false)

        val height: Int = parent.measuredHeight / 12
        view.minimumHeight = height

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return positionsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (positionsList[position] == 1) {
            holder.cat.setBackgroundResource(R.drawable.cat_orange)
        } else {
            holder.cat.setBackgroundResource(R.drawable.cat_transparent)
        }
    }

    fun setNewList(list: List<Int>){
        positionsList = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cat: AppCompatImageView = itemView.item_cat
    }
}
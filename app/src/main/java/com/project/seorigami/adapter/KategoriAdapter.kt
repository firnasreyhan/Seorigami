package com.project.seorigami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.project.seorigami.R
import com.project.seorigami.model.response.KategoriDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.PixelHelper

class KategoriAdapter(private var listener: ItemClickListener<Int>) : RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {
    var data: List<KategoriDataModel> = emptyList()
    var selected: KategoriDataModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]
        holder.textViewKategori.text = currentData.keterangan

        if (currentData == selected) {
            holder.cardView.setCardBackgroundColor(holder.itemView.context.resources.getColor(R.color.primaryBlue))
            holder.textViewKategori.setTextColor(holder.itemView.context.resources.getColor(R.color.white))
        } else {
            holder.cardView.setCardBackgroundColor(holder.itemView.context.resources.getColor(R.color.backgroundUnselectedCategory))
            holder.textViewKategori.setTextColor(holder.itemView.context.resources.getColor(R.color.placeHolder))
        }

        (holder.itemView.layoutParams as ViewGroup.MarginLayoutParams).apply {
            if (position != data.lastIndex) {
                rightMargin = PixelHelper.convertDpToPx(16, holder.itemView.context.resources)
            }
        }

        holder.itemView.setOnClickListener {
            if (currentData != selected) {
                selected = currentData
                notifyDataSetChanged()

                listener.onClickItem(currentData.id)
            }
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewKategori = ItemView.findViewById<View>(R.id.textViewKategori) as TextView
        val cardView = ItemView.findViewById<View>(R.id.cardView) as CardView
    }
}
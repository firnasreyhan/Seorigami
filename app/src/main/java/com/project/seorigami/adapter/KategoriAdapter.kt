package com.project.seorigami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.seorigami.R
import com.project.seorigami.model.response.KategoriDataModel
import com.project.seorigami.util.PixelHelper

class KategoriAdapter() : RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {
    var data: List<KategoriDataModel> = emptyList()

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

        (holder.itemView.layoutParams as ViewGroup.MarginLayoutParams).apply {
            if (position != data.lastIndex) {
                rightMargin = PixelHelper.convertDpToPx(16, holder.itemView.context.resources)
            }
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewKategori = ItemView.findViewById<View>(R.id.textViewKategori) as TextView
    }
}
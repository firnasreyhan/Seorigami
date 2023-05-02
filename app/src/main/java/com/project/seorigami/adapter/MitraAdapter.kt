package com.project.seorigami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.seorigami.R
import com.project.seorigami.model.response.KategoriDataModel
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.PixelHelper

class MitraAdapter(private var listenerItem: ItemClickListener<MitraDataModel>, private var listenerMaps: ItemClickListener<String>) : RecyclerView.Adapter<MitraAdapter.ViewHolder>() {
    var data: List<MitraDataModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mitra, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]
        holder.textViewNamaMitra.text = currentData.nama
        holder.textViewKotaMitra.text = currentData.kota

        Glide.with(holder.itemView.context)
            .load(currentData.foto)
            .into(holder.imageViewMitra)

        holder.itemView.setOnClickListener {
            listenerItem.onClickItem(currentData)
        }

        holder.imageViewMapsMitra.setOnClickListener {
            listenerMaps.onClickItem(currentData.pinpoint)
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewNamaMitra = ItemView.findViewById<View>(R.id.textViewNamaMitra) as TextView
        val textViewKotaMitra = ItemView.findViewById<View>(R.id.textViewKotaMitra) as TextView
        val imageViewMitra = ItemView.findViewById<View>(R.id.imageViewMitra) as ImageView
        val imageViewMapsMitra = ItemView.findViewById<View>(R.id.imageViewMapsMitra) as ImageView
    }
}
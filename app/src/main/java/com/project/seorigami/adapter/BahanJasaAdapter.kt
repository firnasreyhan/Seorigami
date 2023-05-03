package com.project.seorigami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.seorigami.R
import com.project.seorigami.model.response.BahanJasaDataModel
import com.project.seorigami.model.response.KategoriDataModel
import com.project.seorigami.model.response.LayananDataModel
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.PixelHelper
import com.project.seorigami.util.Utils

class BahanJasaAdapter() : RecyclerView.Adapter<BahanJasaAdapter.ViewHolder>() {
    var data = mutableListOf<BahanJasaDataModel>()
    var selectedData = mutableListOf<BahanJasaDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bahan_jasa, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]
        holder.textViewKeterangan.text = currentData.keterangan
        holder.textViewHarga.text = Utils.changePrice(currentData.harga.toString())

        Glide.with(holder.itemView.context)
            .load(currentData.foto)
            .into(holder.imageViewBahanJasa)

        (holder.itemView.layoutParams as ViewGroup.MarginLayoutParams).apply {
            if (position != data.lastIndex) {
                rightMargin = PixelHelper.convertDpToPx(14, holder.itemView.context.resources)
            }
        }

        if (selectedData.contains(currentData)) {
            holder.cardView.setCardBackgroundColor(holder.itemView.context.resources.getColor(R.color.backgroundUnselectedCategory))
        }

        holder.itemView.setOnClickListener {
            if (selectedData.contains(currentData)) {
                selectedData.remove(currentData)
            } else {
                selectedData.add(currentData)
            }
            notifyDataSetChanged()
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewKeterangan = ItemView.findViewById<View>(R.id.textViewKeterangan) as TextView
        val textViewHarga = ItemView.findViewById<View>(R.id.textViewHarga) as TextView
        val imageViewBahanJasa = ItemView.findViewById<View>(R.id.imageViewBahanJasa) as ImageView
        val cardView = ItemView.findViewById<View>(R.id.cardView) as CardView
    }
}
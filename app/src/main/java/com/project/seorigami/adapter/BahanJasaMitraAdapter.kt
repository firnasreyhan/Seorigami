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

class BahanJasaMitraAdapter(private var listenerHapus: ItemClickListener<Int>) : RecyclerView.Adapter<BahanJasaMitraAdapter.ViewHolder>() {
    var data = mutableListOf<BahanJasaDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bahan_jasa_mitra, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]
        holder.textViewKeterangan.text = currentData.keterangan
        holder.textViewHarga.text = "Rp ${Utils.changePrice(currentData.harga.toString())}"

        Glide.with(holder.itemView.context)
//            .load(Utils.reformatImageUrl(currentData.foto))
            .load(currentData.foto)
            .placeholder(R.drawable.ic_logo_seorigami)
            .error(R.drawable.ic_logo_seorigami)
            .into(holder.imageViewBahanJasa)

        holder.imageViewHapus.setOnClickListener {
            listenerHapus.onClickItem(currentData.id)

            data.remove(currentData)
            notifyDataSetChanged()
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewKeterangan = ItemView.findViewById<View>(R.id.textViewKeterangan) as TextView
        val textViewHarga = ItemView.findViewById<View>(R.id.textViewHarga) as TextView
        val imageViewBahanJasa = ItemView.findViewById<View>(R.id.imageViewBahanJasa) as ImageView
        val imageViewHapus = ItemView.findViewById<View>(R.id.imageViewHapus) as ImageView
    }
}
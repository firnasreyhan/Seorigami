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
import com.project.seorigami.model.request.CartDataModel
import com.project.seorigami.model.response.BahanJasaDataModel
import com.project.seorigami.model.response.KategoriDataModel
import com.project.seorigami.model.response.LayananDataModel
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.PixelHelper
import com.project.seorigami.util.Utils
import de.hdodenhof.circleimageview.CircleImageView

class CartAdapter(private var listenerIsUpdatePrice: ItemClickListener<Boolean>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    var data = mutableListOf<CartDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
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
            .load(currentData.foto)
            .into(holder.circleImage)

        holder.textViewSubtotal.text = Utils.changePrice(currentData.subtotal.toString())
        holder.textViewQty.text = currentData.qty.toString()

        holder.imageViewPlus.setOnClickListener {
            currentData.qty++
            currentData.subtotal = currentData.harga * currentData.qty
            notifyDataSetChanged()

            listenerIsUpdatePrice.onClickItem(true)
        }

        holder.imageViewMinus.setOnClickListener {
            if (currentData.qty == 1) {
                data.remove(currentData)
            } else {
                currentData.qty = currentData.qty-1
                currentData.subtotal = currentData.harga * currentData.qty
            }

            notifyDataSetChanged()

            if (data.isEmpty()) {
                listenerIsUpdatePrice.onClickItem(false)
            } else {
                listenerIsUpdatePrice.onClickItem(true)
            }
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewKeterangan = ItemView.findViewById<View>(R.id.textViewKeterangan) as TextView
        val textViewHarga = ItemView.findViewById<View>(R.id.textViewHarga) as TextView
        val textViewSubtotal = ItemView.findViewById<View>(R.id.textViewSubtotal) as TextView
        val textViewQty = ItemView.findViewById<View>(R.id.textViewQty) as TextView
        val circleImage = ItemView.findViewById<View>(R.id.circleImage) as CircleImageView
        val imageViewMinus = ItemView.findViewById<View>(R.id.imageViewMinus) as ImageView
        val imageViewPlus = ItemView.findViewById<View>(R.id.imageViewPlus) as ImageView
    }
}
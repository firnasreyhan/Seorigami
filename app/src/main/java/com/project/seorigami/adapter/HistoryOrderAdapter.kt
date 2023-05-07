package com.project.seorigami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.seorigami.R
import com.project.seorigami.model.request.CartDataModel
import com.project.seorigami.model.response.BahanJasaDataModel
import com.project.seorigami.model.response.HistoryTransactionItemModel
import com.project.seorigami.model.response.KategoriDataModel
import com.project.seorigami.model.response.LayananDataModel
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.PixelHelper
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.Utils
import de.hdodenhof.circleimageview.CircleImageView

class HistoryOrderAdapter(private var listenerIsDone: ItemClickListener<HistoryTransactionItemModel>) : RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder>() {
    var data = mutableListOf<HistoryTransactionItemModel>()
    var isSeller = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_order, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]
        holder.textViewNama.text = currentData.mitra_id.toString()
        holder.textViewFaktur.text = currentData.faktur
        holder.textViewTotal.text = "${Utils.changePrice(currentData.total.toString())}"

        Glide.with(holder.itemView.context)
            .load(R.drawable.ic_logo_seorigami)
            .into(holder.circleImage)

        if (isSeller || currentData.status == 0) {
            holder.button.visibility = View.VISIBLE
        } else {
            holder.button.visibility = View.GONE
        }

        holder.button.setOnClickListener {
            listenerIsDone.onClickItem(currentData)
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val circleImage = ItemView.findViewById<View>(R.id.circleImage) as CircleImageView
        val textViewNama = ItemView.findViewById<View>(R.id.textViewNama) as TextView
        val textViewFaktur = ItemView.findViewById<View>(R.id.textViewFaktur) as TextView
        val textViewTotal = ItemView.findViewById<View>(R.id.textViewTotal) as TextView
        val button = ItemView.findViewById<View>(R.id.button) as Button
    }
}
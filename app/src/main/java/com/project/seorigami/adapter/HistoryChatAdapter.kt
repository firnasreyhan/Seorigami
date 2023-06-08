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
import com.project.seorigami.model.ChatModel
import com.project.seorigami.model.HistoryChatModel
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

class HistoryChatAdapter(private val listener: ItemClickListener<HistoryChatModel>) : RecyclerView.Adapter<HistoryChatAdapter.ViewHolder>() {
    var data = mutableListOf<HistoryChatModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]
        holder.apply {
            textViewNama.text = currentData.name.toString()
            textViewChat.text = currentData.lastMessage.toString()

            cardView.setOnClickListener {
                listener.onClickItem(currentData)
            }
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewNama = ItemView.findViewById<View>(R.id.textViewNama) as TextView
        val textViewChat = ItemView.findViewById<View>(R.id.textViewChat) as TextView
        val cardView = ItemView.findViewById<View>(R.id.cardView) as CardView
    }
}
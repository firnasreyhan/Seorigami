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

class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    var data = mutableListOf<ChatModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_bubble, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]
        if (currentData.sender == Prefs(holder.itemView.context).user?.id) {
            holder.tvMsgMe.text = currentData.message.toString()
            holder.tvMsgMe.visibility = View.VISIBLE
            holder.tvMsgYou.visibility = View.GONE
        } else {
            holder.tvMsgYou.text = currentData.message.toString()
            holder.tvMsgYou.visibility = View.VISIBLE
            holder.tvMsgMe.visibility = View.GONE
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cardViewMe = ItemView.findViewById<View>(R.id.card_view_me) as CardView
        val cardViewYou = ItemView.findViewById<View>(R.id.card_view_you) as CardView
        val tvMsgMe = ItemView.findViewById<View>(R.id.tv_msg_me) as TextView
        val tvMsgYou = ItemView.findViewById<View>(R.id.tv_msg_you) as TextView
    }
}
package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionDataItemModel(
    val created_at: String,
    val faktur: String,
    val harga: Int,
    val layanan_id: Int,
    val qty: Int,
    val updated_at: String
) : Parcelable
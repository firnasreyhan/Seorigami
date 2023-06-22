package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionDataModel(
    val biaya_admin: Int,
    val created_at: String,
    val detail: List<TransactionDataItemModel>,
    val faktur: String,
    val flip_url: String,
    val id: Int,
    val mitra_id: Int,
    val subtotal: Int,
    val tanggal: String,
    val total: Int,
    val updated_at: String,
    val user_id: Int,
) : Parcelable
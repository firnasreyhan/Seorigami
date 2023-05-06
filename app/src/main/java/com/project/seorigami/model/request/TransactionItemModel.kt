package com.project.seorigami.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionItemModel(
    val harga: Int,
    val layanan_id: Int,
    val qty: Int
) : Parcelable
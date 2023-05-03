package com.project.seorigami.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartDataModel(
    val foto: String,
    val harga: Int,
    val id: Int,
    val keterangan: String,
    var qty: Int,
    var subtotal: Int,
) : Parcelable
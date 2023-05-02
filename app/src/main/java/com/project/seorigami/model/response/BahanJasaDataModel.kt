package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BahanJasaDataModel(
    val foto: String,
    val harga: Int,
    val id: Int,
    val jenis: String,
    val kategori: String,
    val keterangan: String
) : Parcelable
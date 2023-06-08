package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LayananDataModel(
    val bahan: List<BahanJasaDataModel>,
    val deskripsi: String,
    val foto: String,
    val id: Int,
    val user_id: Int,
    val jasa: List<BahanJasaDataModel>,
    val kota: String,
    val nama: String,
    val pinpoint: String
) : Parcelable
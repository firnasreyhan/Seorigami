package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MitraDataModel(
    val foto: String,
    val id: Int,
    val kota: String,
    val nama: String,
    val pinpoint: String
) : Parcelable
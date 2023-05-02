package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KategoriDataModel(
    val id: Int,
    val keterangan: String
) : Parcelable
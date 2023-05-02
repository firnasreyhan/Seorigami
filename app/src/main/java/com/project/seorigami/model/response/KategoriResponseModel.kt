package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KategoriResponseModel(
    val data: List<KategoriDataModel>
) : Parcelable, BaseResponseModel()
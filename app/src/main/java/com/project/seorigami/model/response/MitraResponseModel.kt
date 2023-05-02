package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MitraResponseModel(
    val data: List<MitraDataModel>
) : Parcelable, BaseResponseModel()
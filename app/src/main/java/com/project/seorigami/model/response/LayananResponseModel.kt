package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LayananResponseModel(
    val data: LayananDataModel
) : Parcelable, BaseResponseModel()
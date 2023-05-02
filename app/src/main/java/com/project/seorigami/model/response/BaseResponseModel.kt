package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseResponseModel(
    val message: String? = null,
    val status: String? = null
) : Parcelable, ErrorResponseModel()
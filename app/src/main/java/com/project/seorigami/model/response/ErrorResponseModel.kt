package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class ErrorResponseModel(
    val errors: Errors? = null
) : Parcelable
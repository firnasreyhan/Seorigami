package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorizationDataModel(
    val token: String,
    val type: String
) : Parcelable
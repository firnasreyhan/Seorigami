package com.project.seorigami.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignOutRequestModel(
    val username: String = ""
) : Parcelable
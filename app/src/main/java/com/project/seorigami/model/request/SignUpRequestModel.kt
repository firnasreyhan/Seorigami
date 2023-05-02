package com.project.seorigami.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpRequestModel(
    val `as`: Int,
    val email: String,
    val nama: String,
    val password: String
) : Parcelable
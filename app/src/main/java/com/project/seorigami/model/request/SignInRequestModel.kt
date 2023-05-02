package com.project.seorigami.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignInRequestModel(
    val email: String,
    val os_token: String,
    val password: String
) : Parcelable
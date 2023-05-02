package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignInResponseModel(
    val authorization: AuthorizationResponseModel,
    val user: UserResponseModel
) : Parcelable, BaseResponseModel()
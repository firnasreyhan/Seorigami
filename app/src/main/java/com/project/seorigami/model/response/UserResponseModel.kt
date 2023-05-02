package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponseModel(
    val `as`: Int,
    val created_at: String,
    val email: String,
    val id: Int,
    val id_cms_privileges: Int,
    val name: String,
    val os_token: String,
    val photo: String,
    val status: String,
    val updated_at: String
) : Parcelable
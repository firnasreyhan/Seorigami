package com.project.seorigami.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDataModel(
    val `as`: String,
    val created_at: String,
    val email: String,
    val id: Int,
    val id_cms_privileges: Int,
    val name: String,
    val os_token: String,
    @SerializedName(value = "pelanggan", alternate = ["mitra"])
    val pelangganMitraData: PelangganMitraDataModel,
    val photo: String,
    val status: String,
    val updated_at: String
) : Parcelable
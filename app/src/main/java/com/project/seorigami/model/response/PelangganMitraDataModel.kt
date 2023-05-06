package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PelangganMitraDataModel(
    val `as`: String,
    val created_at: String,
    val deskripsi: String,
    val email: String,
    val foto: String,
    val id: Int,
    val kota: String,
    val nama: String,
    val password: String,
    val pinpoint: String,
    val updated_at: String,
    val user_id: Int
) : Parcelable
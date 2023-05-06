package com.project.seorigami.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionRequestModel(
    val biaya_admin: Int,
    val detail: List<TransactionItemModel>,
    val mitra_id: Int,
    val subtotal: Int,
    val total: Int,
    val user_id: Int
) : Parcelable
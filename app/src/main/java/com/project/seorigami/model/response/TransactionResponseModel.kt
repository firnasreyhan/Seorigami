package com.project.seorigami.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionResponseModel(
    val data: TransactionDataModel
) : Parcelable
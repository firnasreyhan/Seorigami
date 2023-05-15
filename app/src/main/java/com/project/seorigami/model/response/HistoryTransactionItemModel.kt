package com.project.seorigami.model.response

data class HistoryTransactionItemModel(
    val biaya_admin: Int,
    val created_at: String,
    val faktur: String,
    val id: Int,
    val mitra: MitraDataModel,
    val mitra_id: Int,
    val payment: Any,
    val payment_method: Any,
    val payment_user: Any,
    val ref_number: Any,
    val status: Int,
    val subtotal: Int,
    val tanggal: String,
    val total: Int,
    val updated_at: String,
    val user: UserDataModel,
    val user_id: Int
)
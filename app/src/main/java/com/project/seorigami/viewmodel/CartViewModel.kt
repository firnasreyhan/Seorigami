package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.seorigami.model.request.CartDataModel
import com.project.seorigami.model.request.TransactionItemModel
import com.project.seorigami.model.request.TransactionRequestModel
import com.project.seorigami.model.response.BaseResponseModel
import com.project.seorigami.model.response.TransactionDataModel
import com.project.seorigami.model.response.TransactionResponseModel
import com.project.seorigami.network.NetworkClient
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import com.project.seorigami.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {
    var stateTransaction = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()
    var transactionData = MutableLiveData<TransactionDataModel>()

    fun transaction(context: Context, mitra_id: Int, biaya_admin: Int, subtotal: Int, total: Int, detail: List<TransactionItemModel>) {
        stateTransaction.value = State.LOADING
        NetworkClient()
            .getService(context)
            .postTransaction(
                token = Utils.reformatToken(context),
                transactionRequestModel = TransactionRequestModel(
                    biaya_admin = biaya_admin,
                    detail = detail,
                    mitra_id = mitra_id,
                    subtotal = subtotal,
                    total = total,
                    user_id = Prefs(context).user!!.id
                )
            )
            .enqueue(object : Callback<TransactionResponseModel> {
                override fun onResponse(
                    call: Call<TransactionResponseModel>,
                    response: Response<TransactionResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateTransaction.value = State.COMPLETE
                        transactionData.value = response.body()?.data
                    } else {
                        stateTransaction.value = State.ERROR
                        errorMessage.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<TransactionResponseModel>, t: Throwable) {
                    stateTransaction.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

}
package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.seorigami.model.response.BaseResponseModel
import com.project.seorigami.model.response.HistoryTransactionItemModel
import com.project.seorigami.model.response.HistoryTransactionResponseModel
import com.project.seorigami.network.NetworkClient
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import com.project.seorigami.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel: ViewModel() {
    val stateOrder = MutableLiveData<State>()
    val stateConfirmOrder = MutableLiveData<State>()
    val orderData = MutableLiveData<List<HistoryTransactionItemModel>>()
    val errorMessage = MutableLiveData<String>()

    fun order(context: Context) {
        stateOrder.value = State.LOADING
        NetworkClient()
            .getService(context)
            .getHistoryTransaction(
                token = Utils.reformatToken(context),
                id = Prefs(context).user!!.id
            )
            .enqueue(object : Callback<HistoryTransactionResponseModel> {
                override fun onResponse(
                    call: Call<HistoryTransactionResponseModel>,
                    response: Response<HistoryTransactionResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateOrder.value = State.COMPLETE
                        orderData.value = response.body()?.data
                    } else {
                        stateOrder.value = State.ERROR
                        errorMessage.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<HistoryTransactionResponseModel>, t: Throwable) {
                    stateOrder.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

    fun confirmOrder(context: Context, id: Int) {
        stateConfirmOrder.value = State.LOADING
        NetworkClient()
            .getService(context)
            .getConfirmTransaction(
                token = Utils.reformatToken(context),
                id = id
            )
            .enqueue(object : Callback<BaseResponseModel> {
                override fun onResponse(
                    call: Call<BaseResponseModel>,
                    response: Response<BaseResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateConfirmOrder.value = State.COMPLETE
                        order(context)
                    } else {
                        stateConfirmOrder.value = State.ERROR
                        errorMessage.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                    stateConfirmOrder.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }
}
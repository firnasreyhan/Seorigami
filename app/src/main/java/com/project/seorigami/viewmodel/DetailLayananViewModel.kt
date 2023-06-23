package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.seorigami.model.response.LayananDataModel
import com.project.seorigami.model.response.LayananResponseModel
import com.project.seorigami.network.NetworkClient
import com.project.seorigami.util.State
import com.project.seorigami.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailLayananViewModel : ViewModel() {
    var stateLayanan = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()
    var dataLayanan = MutableLiveData<LayananDataModel>()

    fun layanan(context: Context, id: Int) {
        stateLayanan.value = State.LOADING
        NetworkClient()
            .getService(context)
            .getLayanan(
                token = Utils.reformatToken(context),
                id = id
            )
            .enqueue(object : Callback<LayananResponseModel> {
                override fun onResponse(
                    call: Call<LayananResponseModel>,
                    response: Response<LayananResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateLayanan.value = State.COMPLETE
                        dataLayanan.value = response.body()?.data
                    } else {
                        if (response.code() == 401) {
                            stateLayanan.value = State.FORCE_LOGOUT
                        } else {
                            stateLayanan.value = State.ERROR
                            errorMessage.value = response.errorBody()?.string()
                        }
                    }
                }

                override fun onFailure(call: Call<LayananResponseModel>, t: Throwable) {
                    stateLayanan.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }
}
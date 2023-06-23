package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.seorigami.model.response.BaseResponseModel
import com.project.seorigami.model.response.LayananDataModel
import com.project.seorigami.model.response.LayananResponseModel
import com.project.seorigami.network.NetworkClient
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import com.project.seorigami.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeMitraViewModel : ViewModel() {
    var stateHapusLayanan = MutableLiveData<State>()
    var stateLayanan = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()
    var dataLayanan = MutableLiveData<LayananDataModel>()

    fun deleteLayanan(context: Context, id: Int) {
        stateHapusLayanan.value = State.LOADING
        NetworkClient()
            .getService(context)
            .getDeleteLayanan(
                token = Utils.reformatToken(context),
                id = id
            )
            .enqueue(object : Callback<BaseResponseModel> {
                override fun onResponse(
                    call: Call<BaseResponseModel>,
                    response: Response<BaseResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateHapusLayanan.value = State.COMPLETE
                    } else {
                        if (response.code() == 401) {
                            stateHapusLayanan.value = State.FORCE_LOGOUT
                        } else {
                            stateHapusLayanan.value = State.ERROR
                            errorMessage.value = response.errorBody()?.string()
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                    stateHapusLayanan.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

    fun layanan(context: Context) {
        stateLayanan.value = State.LOADING
        NetworkClient()
            .getService(context)
            .getLayanan(
                token = Utils.reformatToken(context),
                id = Prefs(context).user!!.pelangganMitraData.id
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
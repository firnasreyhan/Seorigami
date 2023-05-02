package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.seorigami.model.request.SignOutRequestModel
import com.project.seorigami.model.response.BaseResponseModel
import com.project.seorigami.network.NetworkClient
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import com.project.seorigami.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    var stateLogout = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()

    fun logout(context: Context) {
        stateLogout.value = State.LOADING
        NetworkClient()
            .getService(context)
            .postSignOut(
                token = Utils.reformatToken(context),
                singOutRequestModel = SignOutRequestModel()
            )
            .enqueue(object : Callback<BaseResponseModel> {
                override fun onResponse(
                    call: Call<BaseResponseModel>,
                    response: Response<BaseResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateLogout.value = State.COMPLETE
                    } else {
                        stateLogout.value = State.ERROR
                        errorMessage.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                    stateLogout.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

}
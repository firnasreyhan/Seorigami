package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.seorigami.model.request.SignUpRequestModel
import com.project.seorigami.model.response.BaseResponseModel
import com.project.seorigami.network.NetworkClient
import com.project.seorigami.util.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {
    var stateRegister = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()

    fun signUp(context: Context, daftarSebagai: Int, nama: String, email: String, password: String) {
        stateRegister.value = State.LOADING
        NetworkClient()
            .getService(context)
            .postSignUp(
                signUpRequestModel = SignUpRequestModel(
                    daftarSebagai,
                    email,
                    nama,
                    password
                )
            )
            .enqueue(object : Callback<BaseResponseModel> {
                override fun onResponse(
                    call: Call<BaseResponseModel>,
                    response: Response<BaseResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateRegister.value = State.COMPLETE
                    } else {
                        stateRegister.value = State.ERROR
                        errorMessage.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                    stateRegister.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

}
package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.seorigami.model.request.SignInRequestModel
import com.project.seorigami.model.response.SignInResponseModel
import com.project.seorigami.network.NetworkClient
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel() {
    var stateLogin = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()

    fun signIn(context: Context, email: String, password: String, token: String) {
        stateLogin.value = State.LOADING
        NetworkClient()
            .getService(context)
            .postSignIn(
                signInRequestModel = SignInRequestModel(
                    email,
                    token,
                    password
                )
            )
            .enqueue(object : Callback<SignInResponseModel> {
                override fun onResponse(
                    call: Call<SignInResponseModel>,
                    response: Response<SignInResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateLogin.value = State.COMPLETE
                        response.body()?.user.let {
                            Prefs(context).user = it
                        }
                        response.body()?.authorization.let {
                            Prefs(context).jwt = it?.token
                        }
                    } else {
                        stateLogin.value = State.ERROR
                        errorMessage.value = "Email dan Password salah, silahkan coba lagi"
                    }
                }

                override fun onFailure(call: Call<SignInResponseModel>, t: Throwable) {
                    stateLogin.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

}
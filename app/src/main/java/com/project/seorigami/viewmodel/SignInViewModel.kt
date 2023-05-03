package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.seorigami.model.request.SignInRequestModel
import com.project.seorigami.model.response.SignInResponseModel
import com.project.seorigami.module.Repository
import com.project.seorigami.network.NetworkClient
import com.project.seorigami.util.ApiResponse
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    var stateLogin = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()
    var signInFlow = MutableSharedFlow<ApiResponse<SignInResponseModel>>()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInFlow.emitAll(
                repository.login(
                    email,
                    password
            ))
        }
    }

    fun signIn(context: Context, email: String, password: String) {
        stateLogin.value = State.LOADING
        NetworkClient()
            .getService(context)
            .postSignIn(
                signInRequestModel = SignInRequestModel(
                    email,
                    "222",
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
                        errorMessage.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<SignInResponseModel>, t: Throwable) {
                    stateLogin.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

}
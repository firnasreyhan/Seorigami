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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileViewModel : ViewModel() {
    var stateLogout = MutableLiveData<State>()
    var stateUpdatePelanggan = MutableLiveData<State>()
    var stateUpdateMitra = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()

    fun updatePelanggan(context: Context, userId: Int, email: String, nama: String, password: String) {
        stateUpdatePelanggan.value = State.LOADING
        NetworkClient()
            .getService(context)
            .postPelangganUpdate(
                token = Utils.reformatToken(context),
                userId = userId,
                email = email,
                nama = nama,
                password = password
            )
            .enqueue(object : Callback<BaseResponseModel> {
                override fun onResponse(
                    call: Call<BaseResponseModel>,
                    response: Response<BaseResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateUpdatePelanggan.value = State.COMPLETE
                    } else {
                        if (response.code() == 401) {
                            stateUpdatePelanggan.value = State.FORCE_LOGOUT
                        } else {
                            stateUpdatePelanggan.value = State.ERROR
                            errorMessage.value = response.errorBody()?.string()
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                    stateUpdatePelanggan.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

    fun updateMitra(context: Context, userId: Int, email: String, nama: String, password: String, pinPoint: String, deskripsi: String, kota: String, foto: File) {
        stateUpdateMitra.value = State.LOADING
        NetworkClient()
            .getService(context)
            .postMitraUpdate(
                token = Utils.reformatToken(context),
                userId = userId,
                email = email,
                nama = nama,
                password = password,
                pinpoint = pinPoint,
                deskripsi = deskripsi,
                kota = kota,
                foto = MultipartBody.Part.createFormData("foto", foto.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), foto))
            )
            .enqueue(object : Callback<BaseResponseModel> {
                override fun onResponse(
                    call: Call<BaseResponseModel>,
                    response: Response<BaseResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateUpdateMitra.value = State.COMPLETE
                    } else {
                        if (response.code() == 401) {
                            stateUpdateMitra.value = State.FORCE_LOGOUT
                        } else {
                            stateUpdateMitra.value = State.ERROR
                            errorMessage.value = response.errorBody()?.string()
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                    stateUpdateMitra.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

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
                        if (response.code() == 401) {
                            stateLogout.value = State.FORCE_LOGOUT
                        } else {
                            stateLogout.value = State.ERROR
                            errorMessage.value = response.errorBody()?.string()
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                    stateLogout.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

}
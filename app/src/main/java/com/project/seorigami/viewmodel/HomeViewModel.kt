package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.seorigami.model.response.KategoriDataModel
import com.project.seorigami.model.response.KategoriResponseModel
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.model.response.MitraResponseModel
import com.project.seorigami.network.NetworkClient
import com.project.seorigami.util.State
import com.project.seorigami.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    var stateKategori = MutableLiveData<State>()
    var stateMitra = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()
    var dataKategori = MutableLiveData<List<KategoriDataModel>>()
    var dataMitra = MutableLiveData<List<MitraDataModel>>()

    fun kategori(context: Context) {
        stateKategori.value = State.LOADING
        NetworkClient()
            .getService(context)
            .getKategori(
                token = Utils.reformatToken(context)
            )
            .enqueue(object : Callback<KategoriResponseModel> {
                override fun onResponse(
                    call: Call<KategoriResponseModel>,
                    response: Response<KategoriResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateKategori.value = State.COMPLETE
                        dataKategori.value = response.body()?.data
                    } else {
                        if (response.code() == 401) {
                            stateKategori.value = State.FORCE_LOGOUT
                        } else {
                            stateKategori.value = State.ERROR
                            errorMessage.value = response.errorBody()?.string()
                        }
                    }
                }

                override fun onFailure(call: Call<KategoriResponseModel>, t: Throwable) {
                    stateKategori.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }
            })
    }

    fun mitra(context: Context, kota: String, kategoriId: Int) {
        stateMitra.value = State.LOADING
        NetworkClient()
            .getService(context)
            .getMitra(
                token = Utils.reformatToken(context),
                kota = kota,
                kategoriId = kategoriId
            )
            .enqueue(object : Callback<MitraResponseModel> {
                override fun onResponse(
                    call: Call<MitraResponseModel>,
                    response: Response<MitraResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateMitra.value = State.COMPLETE
                        dataMitra.value = response.body()?.data
                    } else {
                        if (response.code() == 401) {
                            stateMitra.value = State.FORCE_LOGOUT
                        } else {
                            stateMitra.value = State.ERROR
                            errorMessage.value = response.errorBody()?.string()
                        }
                    }
                }

                override fun onFailure(call: Call<MitraResponseModel>, t: Throwable) {
                    stateMitra.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }
}
package com.project.seorigami.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.seorigami.model.response.BaseResponseModel
import com.project.seorigami.model.response.KategoriDataModel
import com.project.seorigami.model.response.KategoriResponseModel
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

class AddMaterialAndServiceViewModel : ViewModel() {
    var stateKategori = MutableLiveData<State>()
    var stateLayanan = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()
    var dataKategori = MutableLiveData<List<KategoriDataModel>>()

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
                        stateKategori.value = State.ERROR
                        errorMessage.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<KategoriResponseModel>, t: Throwable) {
                    stateKategori.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }
            })
    }

    fun tambahLayanan(context: Context, jenis: Int, kategoriId: Int, keterangan: String, harga: String, foto: File) {
        stateLayanan.value = State.LOADING
        NetworkClient()
            .getService(context)
            .postLayanan(
                token = Utils.reformatToken(context),
                mitraId = Prefs(context).user!!.pelangganMitraData.id,
                jenis = jenis,
                kategoriId = kategoriId,
                keterangan = keterangan,
                harga = harga,
                foto = MultipartBody.Part.createFormData("foto", foto.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), foto))
            )
            .enqueue(object : Callback<BaseResponseModel> {
                override fun onResponse(
                    call: Call<BaseResponseModel>,
                    response: Response<BaseResponseModel>
                ) {
                    if (response.isSuccessful) {
                        stateLayanan.value = State.COMPLETE
                    } else {
                        stateLayanan.value = State.ERROR
                        errorMessage.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                    stateLayanan.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

}
package com.project.seorigami.module

import com.project.seorigami.model.request.SignInRequestModel
import com.project.seorigami.model.request.SignOutRequestModel
import com.project.seorigami.model.request.SignUpRequestModel
import com.project.seorigami.model.response.BaseResponseModel
import com.project.seorigami.model.response.KategoriResponseModel
import com.project.seorigami.model.response.LayananResponseModel
import com.project.seorigami.model.response.MitraResponseModel
import com.project.seorigami.model.response.SignInResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkInterface {

    @POST("login")
    suspend fun postSignIn(
        @Header("Accept") accept: String? = "application/json",
        @Body signInRequestModel: SignInRequestModel
    ) : SignInResponseModel

    @POST("register")
    suspend fun postSignUp(
        @Header("Accept") accept: String? = "application/json",
        @Body signUpRequestModel: SignUpRequestModel
    ) : BaseResponseModel

    @POST("logout")
    suspend fun postSignOut(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Body singOutRequestModel: SignOutRequestModel
    ) : BaseResponseModel

    @GET("kategori")
    suspend fun getKategori(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
    ) : KategoriResponseModel

    @GET("mitra")
    suspend fun getMitra(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Query("kota") kota: String,
        @Query("kategori_id") kategoriId: String
    ) : MitraResponseModel

    @GET("layanan/{id}")
    suspend fun getLayanan(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ) : LayananResponseModel
}
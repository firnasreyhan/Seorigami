package com.project.seorigami.network

import com.project.seorigami.model.request.SignInRequestModel
import com.project.seorigami.model.request.SignOutRequestModel
import com.project.seorigami.model.request.SignUpRequestModel
import com.project.seorigami.model.request.TransactionRequestModel
import com.project.seorigami.model.response.BaseResponseModel
import com.project.seorigami.model.response.KategoriResponseModel
import com.project.seorigami.model.response.LayananResponseModel
import com.project.seorigami.model.response.MitraResponseModel
import com.project.seorigami.model.response.SignInResponseModel
import com.project.seorigami.model.response.TransactionResponseModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkInterface {

    @POST("login")
    fun postSignIn(
        @Header("Accept") accept: String? = "application/json",
        @Body signInRequestModel: SignInRequestModel
    ) : Call<SignInResponseModel>

    @POST("register")
    fun postSignUp(
        @Header("Accept") accept: String? = "application/json",
        @Body signUpRequestModel: SignUpRequestModel
    ) : Call<BaseResponseModel>

    @POST("logout")
    fun postSignOut(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Body singOutRequestModel: SignOutRequestModel
    ) : Call<BaseResponseModel>

    @GET("kategori")
    fun getKategori(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
    ) : Call<KategoriResponseModel>

    @GET("mitra")
    fun getMitra(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Query("kota") kota: String,
        @Query("kategori_id") kategoriId: Int
    ) : Call<MitraResponseModel>

    @GET("layanan/{id}")
    fun getLayanan(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ) : Call<LayananResponseModel>

    @POST("transaksi")
    fun postTransaction(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Body transactionRequestModel: TransactionRequestModel,
    ) : Call<TransactionResponseModel>

    @POST("pelanggan/update")
    @FormUrlEncoded
    fun postPelangganUpdate(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Field("email") email: String,
        @Field("nama") nama: String,
        @Field("password") password: String
    ) : Call<BaseResponseModel>

    @POST("mitra/update")
    @Multipart
    fun postMitraUpdate(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Part("user_id") userId: Int,
        @Part("email") email: String,
        @Part("nama") nama: String,
        @Part("password") password: String,
        @Part("pinpoint") pinpoint: String,
        @Part("deskripsi") deskripsi: String,
        @Part("kota") kota: String,
        @Part foto: MultipartBody.Part,
    ) : Call<BaseResponseModel>

    @GET("layanan/delete/{id}")
    fun getDeleteLayanan(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ) : Call<BaseResponseModel>

    @POST("layanan")
    @Multipart
    fun postLayanan(
        @Header("Accept") accept: String? = "application/json",
        @Header("Authorization") token: String,
        @Part("mitra_id") mitraId: Int,
        @Part("jenis") jenis: Int,
        @Part("kategori_id") kategoriId: Int,
        @Part("keterangan") keterangan: String,
        @Part("harga") harga: String,
        @Part foto: MultipartBody.Part,
    ): Call<BaseResponseModel>
}
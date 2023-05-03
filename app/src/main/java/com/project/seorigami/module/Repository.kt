package com.project.seorigami.module

import com.project.seorigami.model.request.SignInRequestModel
import com.project.seorigami.model.response.SignInResponseModel
import com.project.seorigami.util.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository(
    private val networkInterface: NetworkInterface
) {
    fun login(
        email: String,
        password: String
    ): Flow<ApiResponse<SignInResponseModel>> {
        return flow {
            emit(ApiResponse.Loading)
            val result = try {
                val response = networkInterface.postSignIn(
                    signInRequestModel = SignInRequestModel(
                        email = email,
                        password = password,
                        os_token = "222"
                    )
                )

                when {
                    !response.message.isNullOrEmpty() -> ApiResponse.Error(response.message)
                    else -> {
                        ApiResponse.Success(response)
                    }
                }
            } catch (e: Exception) {
                ApiResponse.Error(e.message.toString())
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}
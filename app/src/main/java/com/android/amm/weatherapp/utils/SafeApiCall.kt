package com.android.amm.weatherapp.utils

import com.android.amm.weatherapp.models.ErrorMessageModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    api: suspend () -> Response<T>
): Result<T> {
    return withContext(dispatcher) {
        try {
            val response = api.invoke()

            Timber.d("ApiResponse ${response} ${response.isSuccessful}")

            if (response.isSuccessful) {
                if (response.code() == 204) {
                    Result.Empty
                } else {
                    if (response.body() == null) {
                        Result.Empty
                    } else {

                        Result.Success(response.body()!!)
                    }
                }
            } else {
                val errorStr =
                    response.errorBody()?.string() ?: ""

                val errorResponse = ErrorMessageModel(errorStr, response.code())

                Timber.d("ErrorResponseSafeApiCall $errorResponse")

                Result.Error(
                    Exception(
                        Gson().toJson(errorResponse)
                    )
                )
            }
        } catch (e: Throwable) {
            val errorCode = when (e) {
                is UnknownHostException -> {
                    0
                }
                is SocketTimeoutException -> {
                    1
                }
                else -> {
                    -1
                }
            }

            val errorResponse = ErrorMessageModel(
                message = e.message,
                status = errorCode
            )


            Result.Error(
                Exception(
                    Gson().toJson(errorResponse)
                )
            )
        }

    }
}
package com.hossam.mobilemasrtask.auth.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.auth.data.dto.LoginDTO
import com.hossam.mobilemasrtask.auth.domain.repository.IAuthRepository
import com.hossam.mobilemasrtask.common.data.local.datastore.DataStoreUtil
import com.hossam.mobilemasrtask.common.data.remote.WebService
import com.hossam.mobilemasrtask.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: WebService,
    private val dataStore: DataStoreUtil
) : IAuthRepository {

    override fun login(loginDTO: LoginDTO): Flow<Result<Unit>> = flow {

        emit(Result.Loading())

        try {

            val result = api.login(loginDTO = loginDTO)
            when (result.code()) {

                HttpURLConnection.HTTP_CREATED -> {
                    result.body()?.let { tokenProvider ->
                        // TODO: Also store the refreshToken
                        dataStore.saveToken(token = tokenProvider.accessToken)
                        dataStore.saveRefreshToken(refreshToken = tokenProvider.refreshToken)

                        Log.e(TAG, "login: accessToken = ${tokenProvider.accessToken}")
                        Log.e(TAG, "login: refreshToken = ${tokenProvider.refreshToken}")

                        Log.e(TAG, "dataStore: accessToken = ${tokenProvider.accessToken}")
                        Log.e(TAG, "dataStore: refreshToken = ${tokenProvider.refreshToken}")
                        emit(Result.Success(Unit))
                    }
                }

                HttpURLConnection.HTTP_INTERNAL_ERROR ->
                    emit(Result.Error(message = R.string.error_http_internal_error))

                //TODO: add another expected Http-Errors
            }


        } catch (e: HttpException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
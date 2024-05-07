package com.hossam.mobilemasrtask.auth.data.repository

import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.auth.data.dto.LoginDTO
import com.hossam.mobilemasrtask.auth.domain.repository.IAuthRepository
import com.hossam.mobilemasrtask.common.data.local.datastore.DataStoreUtil
import com.hossam.mobilemasrtask.common.data.remote.WebService
import com.hossam.mobilemasrtask.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_OK
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject

/**
 * Implementation of the [IAuthRepository] interface responsible for handling authentication operations.
 * This class communicates with the remote API via [WebService] and manages user data using [DataStoreUtil].
 *
 * @property api Instance of [WebService] used for making network requests.
 * @property dataStore Instance of [DataStoreUtil] used for storing authentication-related data.
 */
class AuthRepositoryImpl @Inject constructor(
    private val api: WebService,
    private val dataStore: DataStoreUtil
) : IAuthRepository {

    /**
     * Performs a login operation using the provided [loginDTO] and emits the corresponding [Result] via a Flow.
     * This function handles various HTTP status codes and emits appropriate results accordingly.
     *
     * @param loginDTO The login credentials provided by the user.
     * @return A Flow emitting [Result] indicating the outcome of the login operation.
     */
    override fun login(loginDTO: LoginDTO): Flow<Result<Unit>> = flow {

        // Emit a loading initial state indicating the login operation is in progress
        emit(Result.Loading())

        try {
            // Attempt to perform the login operation via the API
            val result = api.login(loginDTO = loginDTO)
            val statusCode = result.code()

            // Handle different HTTP status codes
            when (statusCode) {

                // Handle successful login (HTTP_CREATED or HTTP_OK)
                HTTP_CREATED or HTTP_OK -> {
                    // Extract access token and refresh token from the response body if not NULL
                    result.body()?.let { tokenProvider ->
                        // Save the tokens to the data store
                        with(dataStore) {
                            saveToken(token = tokenProvider.accessToken)
                            saveRefreshToken(refreshToken = tokenProvider.refreshToken)
                        }.also {
                            // Emit success result if tokens are saved successfully
                            emit(Result.Success(Unit))
                        }
                    }
                }

                // Handle internal server error
                HTTP_INTERNAL_ERROR ->
                    emit(Result.Error(message = R.string.error_http_internal_error))

                // Handle unauthorized or not found error
                HTTP_UNAUTHORIZED or HTTP_NOT_FOUND ->
                    emit(Result.Error(message = R.string.the_user_name_or_password_is_incorrect))

                // Handle any other unexpected HTTP status codes
                else -> emit(Result.Error(message = R.string.error_unexpected))
            }

        } catch (e: IOException) {
            // Handle IOException, usually indicating a network issue
            e.printStackTrace()
            emit(Result.Error(message = R.string.error_no_internet))
        }
    }

}
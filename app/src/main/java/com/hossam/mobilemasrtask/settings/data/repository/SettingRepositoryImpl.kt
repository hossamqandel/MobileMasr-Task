package com.hossam.mobilemasrtask.settings.data.repository

import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.common.data.local.datastore.DataStoreUtil
import com.hossam.mobilemasrtask.settings.domain.repository.ISettingRepository
import com.hossam.mobilemasrtask.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class SettingRepositoryImpl @Inject constructor(
    private val dataStore: DataStoreUtil
) : ISettingRepository {


    /**
     * Initiates the logout process by clearing all data in the [dataStore].
     * Emits a loading state before starting the logout operation.
     * Emits a success state if the logout operation is successful.
     * Emits an error state with the corresponding message if there's an exception or if logout fails.
     * @return A flow emitting the result of the logout operation.
     */
    override fun logout(): Flow<Result<Unit?>> = flow {
        emit(Result.Loading())

        try {
            // Attempt to clear all data from the data store.
            val hasLoggedOut = dataStore.clearAll()

            // Introduce a delay for simulation purposes (optional).
            delay(2.seconds)

            // Emit success if logout is successful.
            if (hasLoggedOut) {
                emit(Result.Success(Unit))
            } else {
                // Emit error if logout fails.
                emit(Result.Error(R.string.please_try_again))
            }

        } catch (e: Exception) {
            // Catch any exceptions and emit an error state.
            e.printStackTrace()
            emit(Result.Error(R.string.please_try_again))
        }
    }

}
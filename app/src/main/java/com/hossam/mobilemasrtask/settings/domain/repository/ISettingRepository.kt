package com.hossam.mobilemasrtask.settings.domain.repository

import com.hossam.mobilemasrtask.util.Result
import kotlinx.coroutines.flow.Flow

interface ISettingRepository {


    fun logout(): Flow<Result<Unit?>>
}
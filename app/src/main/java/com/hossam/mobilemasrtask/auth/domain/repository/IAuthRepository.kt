package com.hossam.mobilemasrtask.auth.domain.repository

import com.hossam.mobilemasrtask.auth.data.dto.LoginDTO
import com.hossam.mobilemasrtask.util.Result
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {


    fun login(loginDTO: LoginDTO): Flow<Result<Unit>>

}
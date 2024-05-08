package com.hossam.mobilemasrtask.auth.presentation.login.utl

import androidx.annotation.StringRes

sealed interface LoginState {

    data class Loading(val isLoading: Boolean) : LoginState
    data object Success : LoginState
    data class Error(@StringRes val message: Int) : LoginState
}
package com.hossam.mobilemasrtask.splash.presentation.util

sealed interface AuthState {

    data object Authenticated : AuthState
    data object NotAuthenticated : AuthState

}
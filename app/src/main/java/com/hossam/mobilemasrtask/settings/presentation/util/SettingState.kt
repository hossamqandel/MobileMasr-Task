package com.hossam.mobilemasrtask.settings.presentation.util

sealed interface SettingState {

    data class Loading(val isLoading: Boolean): SettingState
    data object Success: SettingState
    data class Error(val message: Int): SettingState

}
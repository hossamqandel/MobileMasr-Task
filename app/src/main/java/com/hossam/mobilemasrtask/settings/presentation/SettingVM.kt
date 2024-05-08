package com.hossam.mobilemasrtask.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossam.mobilemasrtask.settings.domain.repository.ISettingRepository
import com.hossam.mobilemasrtask.settings.presentation.util.SettingState
import com.hossam.mobilemasrtask.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingVM @Inject constructor(
    private val settingRepo: ISettingRepository
): ViewModel() {


    /**
     * A private mutable shared flow used to emit [SettingState] values to observe state changes.
     */
    private val _state = MutableSharedFlow<SettingState>()

    /**
     * A shared flow representing the current state of the settings.
     */
    val state = _state.asSharedFlow()

    /**
     * Initiates the logout process by calling the logout function from the [settingRepo].
     * Emits loading state before making the network call.
     * Emits success state if the logout operation is successful.
     * Emits error state if there is an error during the logout operation, along with the error message.
     */
    fun logout() {
        viewModelScope.launch {
            settingRepo.logout().collectLatest { result ->
                when(result) {
                    is Result.Loading -> _state.emit(SettingState.Loading(true))
                    is Result.Success -> {
                        _state.emit(SettingState.Loading(false))
                        _state.emit(SettingState.Success)
                    }
                    is Result.Error -> {
                        _state.emit(SettingState.Loading(false))
                        result.message?.let {
                            _state.emit(SettingState.Error(result.message))
                        }
                    }
                }
            }
        }
    }

}
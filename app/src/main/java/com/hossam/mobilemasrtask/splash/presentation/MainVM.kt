package com.hossam.mobilemasrtask.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossam.mobilemasrtask.common.data.local.datastore.DataStoreUtil
import com.hossam.mobilemasrtask.splash.presentation.util.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val dataStore: DataStoreUtil
): ViewModel() {

    private val _state = MutableSharedFlow<AuthState>(replay = 1)
    val state = _state.asSharedFlow()

    suspend fun checkAuthentication() {
        viewModelScope.launch {
            val session = dataStore.getTokenAsFlow.first()
            if (!session.isNullOrEmpty()) _state.emit(AuthState.Authenticated)
            else _state.emit(AuthState.NotAuthenticated)
        }
    }


}
package com.hossam.mobilemasrtask.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossam.mobilemasrtask.auth.data.dto.LoginDTO
import com.hossam.mobilemasrtask.auth.domain.repository.IAuthRepository
import com.hossam.mobilemasrtask.auth.presentation.utl.LoginState
import com.hossam.mobilemasrtask.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val repo: IAuthRepository
) : ViewModel() {


    private var authJob: Job? = null
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }

    private val _state = MutableSharedFlow<LoginState>()
    val state = _state.asSharedFlow()

    fun getAuthState(email: String, password: String) {
        authJob?.cancel()
        authJob = viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            repo.login(LoginDTO(email, password)).collectLatest { result ->
                when (result) {

                    is Result.Loading -> submitState(LoginState.Loading(true))

                    is Result.Success -> {
                        submitState(LoginState.Loading(false))
                        submitState(LoginState.Success)
                    }

                    is Result.Error -> {
                        submitState(LoginState.Loading(false))
                        result.message?.let { errorMessage ->
                            submitState(LoginState.Error(errorMessage))
                        }
                    }
                }
            }
        }

    }


    private suspend fun submitState(state: LoginState) {
        _state.emit(state)
    }

    override fun onCleared() {
        authJob?.cancel()
        super.onCleared()
    }
}
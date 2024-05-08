package com.hossam.mobilemasrtask.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossam.mobilemasrtask.auth.data.dto.LoginDTO
import com.hossam.mobilemasrtask.auth.domain.repository.IAuthRepository
import com.hossam.mobilemasrtask.auth.domain.use_case.ValidateEmailUseCase
import com.hossam.mobilemasrtask.auth.domain.use_case.ValidatePasswordUseCase
import com.hossam.mobilemasrtask.auth.presentation.login.utl.LoginState
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

/**
 * ViewModel responsible for handling the login functionality.
 * This ViewModel interacts with the [IAuthRepository] to perform login operations,
 * and utilizes use cases for email and password validation.
 *
 * @property repo Instance of [IAuthRepository] for handling authentication operations.
 * @property validateEmailUseCase Instance of [ValidateEmailUseCase] for email validation.
 * @property validatePasswordUseCase Instance of [ValidatePasswordUseCase] for password validation.
 */
@HiltViewModel
class LoginVM @Inject constructor(
    private val repo: IAuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    // Coroutine job for authentication and validation
    private var authJob: Job? = null
    private var validateJob: Job? = null

    // Coroutine exception handler for handling uncaught exceptions
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    // Shared flow for emitting login state updates
    private val _state = MutableSharedFlow<LoginState>()
    val state = _state.asSharedFlow()

    // Shared flow for emitting email validation results
    private val _emailValidation = MutableSharedFlow<Int?>()
    val emailValidation = _emailValidation.asSharedFlow()

    // Shared flow for emitting password validation results
    private val _passwordValidation = MutableSharedFlow<Int?>()
    val passwordValidation = _passwordValidation.asSharedFlow()

    /**
     * Submits the login credentials for validation and initiates the login process.
     *
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     */
    fun submit(email: String, password: String) {
        validateJob?.cancel()
        validateJob = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val isValidEmail = validateEmail(email = email)
            val isValidPassword = validatePassword(password = password)
            val hasAnyErrors = listOf(isValidEmail, isValidPassword).any { isValid -> !isValid }

            if (hasAnyErrors) return@launch
            else {
                performLoginEvent(email, password)
            }
        }
    }

    /**
     * Validates the provided email using the [ValidateEmailUseCase].
     *
     * @param email The email to be validated.
     * @return True if the email is valid, false otherwise.
     */
    private suspend fun validateEmail(email: String): Boolean {
        val emailUseCase = validateEmailUseCase(email = email)
        if (!emailUseCase.isValid) {
            _emailValidation.emit(emailUseCase.errorMessage)
            return false
        }
        _emailValidation.emit(null)
        return true
    }

    /**
     * Validates the provided password using the [ValidatePasswordUseCase].
     *
     * @param password The password to be validated.
     * @return True if the password is valid, false otherwise.
     */
    private suspend fun validatePassword(password: String): Boolean {
        val passwordUseCase = validatePasswordUseCase(password = password)
        if (!passwordUseCase.isValid) {
            _passwordValidation.emit(passwordUseCase.errorMessage)
            return false
        }
        _passwordValidation.emit(null)
        return true
    }

    /**
     * Initiates the login request using the provided email and password.
     * Emits corresponding [LoginState] based on the login result.
     *
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     */
    private fun performLoginEvent(email: String, password: String) {
        authJob?.cancel()
        authJob = viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            repo.login(LoginDTO(email, password)).collectLatest { result ->
                when (result) {
                    is Result.Loading -> _state.emit(LoginState.Loading(true))
                    is Result.Success -> {
                        _state.emit(LoginState.Loading(false))
                        _state.emit(LoginState.Success)
                    }
                    is Result.Error -> {
                        _state.emit(LoginState.Loading(false))
                        result.message?.let { errorMessage ->
                            _state.emit(LoginState.Error(errorMessage))
                        }
                    }
                }
            }
        }
    }

    /**
     * Cancels the authentication and validation jobs when the ViewModel is cleared.
     */
    override fun onCleared() {
        authJob?.cancel()
        validateJob?.cancel()
        super.onCleared()
    }

}

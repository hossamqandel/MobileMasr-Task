package com.hossam.mobilemasrtask.auth.domain.use_case

import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.util.Validation

class ValidatePasswordUseCase {

    operator fun invoke(password: String): Validation {
        if (password.isBlank()) {
            return Validation(
                isValid = false,
                errorMessage = R.string.password_cannot_be_empty
            )
        }

        if (password.length < 8){
            return Validation(
                isValid = false,
                errorMessage = R.string.password_should_be_at_least_8_characters
            )
        }

        return Validation(isValid = true)
    }
}
package com.hossam.mobilemasrtask.auth.domain.use_case

import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.util.Validation

class ValidateEmailUseCase {

    operator fun invoke(email: String): Validation {

        if (email.isBlank()) {
            return Validation(
                isValid = false,
                errorMessage = R.string.email_cannot_be_empty
            )
        }

        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        if (!emailRegex.matches(email)) {
            return Validation(
                isValid = false,
                errorMessage = R.string.invalid_email
            )
        }

        return Validation(isValid = true)
    }
}
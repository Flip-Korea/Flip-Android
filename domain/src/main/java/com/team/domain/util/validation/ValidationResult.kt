package com.team.domain.util.validation

sealed interface ValidationResult {

    data object Success: ValidationResult
    data class Error(val error: ValidationErrorType): ValidationResult
}
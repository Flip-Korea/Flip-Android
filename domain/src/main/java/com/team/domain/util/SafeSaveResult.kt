package com.team.domain.util

sealed interface SafeSaveResult {
    data object CanSave : SafeSaveResult

    data object Discard : SafeSaveResult
}

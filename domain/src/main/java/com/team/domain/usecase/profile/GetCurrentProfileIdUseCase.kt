package com.team.domain.usecase.profile

import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentProfileIdUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    operator fun invoke(): Flow<String?> =
        dataStoreManager.getStringData(DataStoreType.AccountType.CURRENT_PROFILE_ID)
}
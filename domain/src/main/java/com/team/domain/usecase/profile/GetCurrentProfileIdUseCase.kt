package com.team.domain.usecase.profile

import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCurrentProfileIdUseCase
@Inject
constructor(private val dataStoreManager: DataStoreManager) {

    operator fun invoke(): Flow<String?> =
        dataStoreManager.getStringData(DataStoreType.AccountType.CURRENT_PROFILE_ID)
}

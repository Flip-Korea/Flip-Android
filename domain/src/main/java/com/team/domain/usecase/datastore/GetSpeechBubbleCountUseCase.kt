package com.team.domain.usecase.datastore

import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpeechBubbleCountUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    /**
     * 말풍선 힌트를 본 횟수를 가져온다.
     */
    operator fun invoke(): Flow<Int?> =
        dataStoreManager.getIntData(DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE)
}
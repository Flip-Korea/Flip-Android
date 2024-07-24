package com.team.domain.usecase.datastore

import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class IncrementSpeechBubbleCountUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    /**
     * 말풍선 힌트를 본 횟수를 1 증가시킨다.
     * 만약 count가 0이면 0으로 초기화 한다.
     */
    suspend operator fun invoke() {
        val count = dataStoreManager
            .getIntData(DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE)
            .first()

        if (count != null) {
            dataStoreManager
                .saveData(
                    DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE,
                    count + 1
                )
        } else {
            dataStoreManager
                .saveData(
                    DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE,
                    0
                )
        }
    }
}
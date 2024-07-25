package com.team.presentation.editcategories.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.data.di.IODispatcher
import com.team.domain.model.category.Category
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.speechbubble.GetSpeechBubbleCountUseCase
import com.team.domain.usecase.speechbubble.IncrementSpeechBubbleCountUseCase
import com.team.domain.usecase.interestcategory.UpdateMyCategoriesUseCase
import com.team.domain.usecase.profile.GetMyProfileUseCase
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.presentation.editcategories.EditMyCategoriesUiEvent
import com.team.presentation.editcategories.state.MyCategoriesState
import com.team.presentation.editcategories.state.MyCategoriesUpdateState
import com.team.presentation.util.UiText
import com.team.presentation.util.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMyCategoriesViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val updateMyCategoriesUseCase: UpdateMyCategoriesUseCase,
    private val getSpeechBubbleCountUseCase: GetSpeechBubbleCountUseCase,
    private val incrementSpeechBubbleCountUseCase: IncrementSpeechBubbleCountUseCase,
): ViewModel() {

    private val _myCategoriesState = MutableStateFlow(MyCategoriesState())
    val myCategoriesState = _myCategoriesState.asStateFlow()

    private val _nicknameState = MutableStateFlow("")
    val nicknameState = _nicknameState.asStateFlow()

    private val _myCategoriesUpdateState = MutableStateFlow(MyCategoriesUpdateState())
    val myCategoriesUpdateState = _myCategoriesUpdateState.asStateFlow()

    private val _speechBubbleState = MutableStateFlow(false)
    val speechBubbleState = _speechBubbleState.asStateFlow()

    private var myCategories by mutableStateOf(emptyList<Category>())
    private var exclusiveCategories by mutableStateOf(emptyList<Category>())
    private var myCategoriesCache by mutableStateOf(emptyList<Category>())

    init {
        viewModelScope.launch {
            fetchCategories()

            val speechBubbleResult = async(ioDispatcher) { checkSpeechBubbleCount() }
            _speechBubbleState.update { speechBubbleResult.await() }
        }
    }

    /**
     * 말풍선 힌트는 SPEECH_BUBBLE_COUNT_LIMIT 이하 횟수로만 표시한다.
     *
     *  SPEECH_BUBBLE_COUNT_LIMIT가 0이면 1번 표시하는 이유는
     *  최초의 count 값이 null이기 때문
     */
    private suspend fun checkSpeechBubbleCount(): Boolean {
        val count = getSpeechBubbleCountUseCase().first()

        return if (count == null || count < SPEECH_BUBBLE_COUNT_LIMIT) {
            incrementSpeechBubbleCountUseCase()
            true
        } else {
            false
        }
    }

    /**
     * 나의 관심 카테고리와 모든 카테고리를 가져온다.
     *
     * 1. myCategories: 나의 관심 카테고리
     * 2. exclusiveCategories: 모든 카테고리 - 나의 관심 카테고리
     */
    private fun fetchCategories() {

        viewModelScope.launch {

            _myCategoriesState.update { it.copy(loading = true) }

            try {
                val categories = getCategoriesUseCase().first()
                val myProfileResult = getMyProfileUseCase().first()

                when(myProfileResult) {

                    Result.Loading -> {
                        _myCategoriesState.update { it.copy(loading = true) }
                    }

                    is Result.Success -> {
                        val myCategoryIds = myProfileResult.data?.categories

                        _nicknameState.update { myProfileResult.data?.nickname ?: "" }

                        if (myCategoryIds != null) {
                            exclusiveCategories = categories.filter { !myCategoryIds.contains(it.id) }
                            myCategories = myCategoryIds.mapNotNull { id ->
                                categories.find { it.id == id }
                            }
                            myCategoriesCache = myCategories
                            _myCategoriesState.update { it.copy(
                                loading = false,
                                myCategories = myCategories,
                                exclusiveCategories = exclusiveCategories
                            ) }
                        } else {
                            _myCategoriesState.update { it.copy(
                                loading = false,
                                error = ErrorType.Local.EMPTY.asUiText()
                            ) }
                        }
                    }

                    is Result.Error -> {
                        _myCategoriesState.update { it.copy(
                            loading = false,
                            error = myProfileResult.errorBody?.let { errorBody ->
                                UiText.DynamicString(errorBody.message)
                            } ?: myProfileResult.error.asUiText()
                        ) }
                    }
                }
            } catch (e: CancellationException) {

                _myCategoriesState.update { it.copy(
                    loading = false,
                    error = e.localizedMessage?.let { error ->
                        UiText.DynamicString(error)
                    } ?: ErrorType.Exception.IO.asUiText()
                ) }
            } catch (e: Exception) {

                _myCategoriesState.update { it.copy(
                    loading = false,
                    error = e.localizedMessage?.let { error ->
                        UiText.DynamicString(error)
                    } ?: ErrorType.Exception.IO.asUiText()
                ) }
            }
        }
    }

    fun onUiEvent(uiEvent: EditMyCategoriesUiEvent) {

        when (uiEvent) {

            is EditMyCategoriesUiEvent.SelectAll -> {
                myCategories = myCategories + exclusiveCategories
                exclusiveCategories = emptyList()
            }
            is EditMyCategoriesUiEvent.SelectCategory -> {
                myCategories = myCategories.toMutableList().apply { add(uiEvent.category) }
                exclusiveCategories = exclusiveCategories.toMutableList().apply { remove(uiEvent.category) }
            }
            is EditMyCategoriesUiEvent.UnSelectCategory -> {
                myCategories = myCategories.toMutableList().apply { remove(uiEvent.category) }
                exclusiveCategories = exclusiveCategories.toMutableList().apply { add(uiEvent.category) }
            }
            is EditMyCategoriesUiEvent.MoveCategory -> {
                myCategories = uiEvent.categories
            }

        }

        _myCategoriesState.update { it.copy(
            myCategories = myCategories,
            exclusiveCategories = exclusiveCategories
        ) }
    }

    fun updateMyCategories(reorderedCategories: List<Category>) {

        if (reorderedCategories != myCategoriesCache) {

            updateMyCategoriesUseCase(reorderedCategories).onEach { result ->
                when(result) {
                    Result.Loading -> {
                        _myCategoriesUpdateState.update { it.copy(loading = true) }
                    }
                    is Result.Error -> {
                        _myCategoriesUpdateState.update { it.copy(
                            loading = false,
                            error = result.errorBody?.let { errorBody ->
                                UiText.DynamicString(errorBody.message)
                            } ?: result.error.asUiText()
                        ) }
                    }
                    is Result.Success -> {
                        _myCategoriesUpdateState.update { it.copy(
                            loading = false,
                            success = true
                        ) }
                    }
                }
            }.launchIn(viewModelScope)

        } else {

            _myCategoriesUpdateState.update { it.copy(
                success = true
            ) }
        }
    }
}

/**
 * 0 이면 1번 표시
 *
 * 1 이면 2번 표시
 */
private const val SPEECH_BUBBLE_COUNT_LIMIT = 0

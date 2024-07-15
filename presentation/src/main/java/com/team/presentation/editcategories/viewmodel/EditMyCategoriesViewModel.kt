package com.team.presentation.editcategories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.interestcategory.GetMyCategoriesUseCase
import com.team.domain.usecase.profile.GetMyProfileUseCase
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.presentation.editcategories.state.MyCategoriesState
import com.team.presentation.editcategories.state.MyProfileState
import com.team.presentation.util.UiText
import com.team.presentation.util.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EditMyCategoriesViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val getMyCategoriesUseCase: GetMyCategoriesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
): ViewModel() {

    private val _myCategoriesState = MutableStateFlow(MyCategoriesState())
    val myCategoriesState = _myCategoriesState.asStateFlow()

    private val _myProfileState = MutableStateFlow(MyProfileState())
    val myProfileState = _myProfileState.asStateFlow()

    init {
        fetchCategories()
        fetchMyProfile()
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

            val categoriesDeferred = async { getCategoriesUseCase().first() }
            val myCategoryIdsDeferred = async {
                val currentProfileId =
                    dataStoreManager.getData(DataStoreType.AccountType.CURRENT_PROFILE_ID).first()
                getMyCategoriesUseCase(currentProfileId ?: "").first()
            }

            try {
                val categories = categoriesDeferred.await()
                val myCategoryIds = myCategoryIdsDeferred.await()

                val exclusiveCategories = categories.filter { !myCategoryIds.contains(it.id) }
                val myCategories = categories.filter { myCategoryIds.contains(it.id) }

                _myCategoriesState.update { it.copy(
                    loading = false,
                    myCategories = myCategories,
                    exclusiveCategories = exclusiveCategories
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

    private fun fetchMyProfile() {
        viewModelScope.launch {
            getMyProfileUseCase().onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _myProfileState.update { it.copy(
                            loading = false,
                            myProfile = result.data
                        ) }
                    }
                    is Result.Error -> {
                        _myProfileState.update { it.copy(
                            loading = false,
                            error = result.errorBody?.let { errorBody ->
                                UiText.DynamicString(errorBody.message)
                            } ?: result.error.asUiText()
                        ) }
                    }
                    Result.Loading -> {
                        _myProfileState.update { it.copy(loading = true) }
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}
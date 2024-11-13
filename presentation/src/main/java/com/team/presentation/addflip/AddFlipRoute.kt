package com.team.presentation.addflip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.presentation.addflip.view.AddFlipScreen
import com.team.presentation.addflip.viewmodel.AddFlipViewModel

/**
 * 플립 글 작성 화면[AddFlipScreen]에 대한 Route
 *
 * @param popBackStack 뒤로가기 시
 */
@Composable
fun AddFlipRoute(
    addFlipViewModel: AddFlipViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    onNavigateToTempFlipBox: () -> Unit
) {

    val categoriesState by addFlipViewModel.categoriesState.collectAsStateWithLifecycle()
    val selectedCategory by addFlipViewModel.selectedCategory.collectAsStateWithLifecycle()
    val addPostState by addFlipViewModel.addPostState.collectAsStateWithLifecycle()
    val addTempPostState by addFlipViewModel.addTempPostState.collectAsStateWithLifecycle()
    val newPostState by addFlipViewModel.newPostState.collectAsStateWithLifecycle()
    val modalState by addFlipViewModel.modalState.collectAsStateWithLifecycle()

    LaunchedEffect(addPostState) {
        if (addPostState.postSave) {
            popBackStack()
        }
    }

    AddFlipScreen(
        newPostState = newPostState,
        categoriesState = categoriesState,
        addPostState = addPostState,
        addTempPostState = addTempPostState,
        modalState = modalState,
        selectedCategory = selectedCategory,
        onUiEvent = addFlipViewModel::onUiEvent,
        hideModal = { addFlipViewModel.hideModal() },
        onBackPress = popBackStack,
        onNavigateToTempFlipBox = onNavigateToTempFlipBox
    )
}
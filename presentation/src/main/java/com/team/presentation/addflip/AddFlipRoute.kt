package com.team.presentation.addflip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier,
    addFlipViewModel: AddFlipViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    onNavigateToTempFlipBox: () -> Unit
) {

    val categoriesState by addFlipViewModel.categoriesState.collectAsStateWithLifecycle()
    val selectedCategory by addFlipViewModel.selectedCategory.collectAsStateWithLifecycle()
    val addPostState by addFlipViewModel.addPostState.collectAsStateWithLifecycle()
    val newPostState by addFlipViewModel.newPostState.collectAsStateWithLifecycle()
    val dialogModalState by addFlipViewModel.dialogModalState.collectAsStateWithLifecycle()

    LaunchedEffect(addPostState) {
        if (addPostState.postSave) {
            popBackStack()
        }
    }

    AddFlipScreen(
        newPostState = newPostState,
        categoriesState = categoriesState,
        addPostState = addPostState,
        dialogModalState = dialogModalState,
        selectedCategory = selectedCategory,
        onUiEvent = addFlipViewModel::onUiEvent,
        hideModal = { addFlipViewModel.hideModal() },
        onBackPress = popBackStack,
        onNavigateToTempFlipBox = onNavigateToTempFlipBox
    )
}
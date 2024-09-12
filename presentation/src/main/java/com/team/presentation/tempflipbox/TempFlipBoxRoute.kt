package com.team.presentation.tempflipbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.presentation.tempflipbox.view.TempFlipBoxScreen
import com.team.presentation.tempflipbox.viewmodel.TempFlipBoxViewModel

@Composable
fun TempFlipBoxRoute(
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit
) {
    val tempFlipBoxViewModel: TempFlipBoxViewModel = hiltViewModel()
    val uiState by tempFlipBoxViewModel.uiState.collectAsStateWithLifecycle()

    TempFlipBoxScreen(
        uiState = uiState,
        uiEvent = tempFlipBoxViewModel::processEvent,
        onBackPress = onBackPress
    )
}
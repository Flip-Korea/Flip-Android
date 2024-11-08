package com.team.presentation.tempflipbox

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.team.presentation.common.paging.HandleLoadState
import com.team.presentation.common.snackbar.ObserveAsEvents
import com.team.presentation.tempflipbox.view.TempFlipBoxScreen
import com.team.presentation.tempflipbox.viewmodel.TempFlipBoxViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TempFlipBoxRoute(onBackPress: () -> Unit) {
    val tempFlipBoxViewModel: TempFlipBoxViewModel = hiltViewModel()
    val uiState by tempFlipBoxViewModel.uiState.collectAsStateWithLifecycle()
    val tempPostPaging = tempFlipBoxViewModel.tempPostPaging.collectAsLazyPagingItems()
    var isModalVisible by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(flow = tempFlipBoxViewModel.effect) { event ->
        when (event) {
            TempFlipBoxContract.UiEffect.ShowDialogModal -> {
                scope.launch {
                    isModalVisible = true
                    delay(100)
                    isModalVisible = false
                    Log.d("Modal_log", "Modal Triggered !")
                }
            }
        }
    }

    HandleLoadState(
        lazyPagingItems = tempPostPaging,
        updateState = tempFlipBoxViewModel::updateUiStateWithLoadState
    )

    TempFlipBoxScreen(
        tempPostPaging = tempPostPaging,
        tempPostTotalSize = 75, // TODO: 임시
        uiState = uiState,
        uiEvent = tempFlipBoxViewModel::processEvent,
        isModalVisible = isModalVisible,
        onBackPress = onBackPress
    )
}
package com.team.presentation.addflip

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.presentation.addflip.state.AddFlipContract
import com.team.presentation.addflip.view.AddFlipScreen
import com.team.presentation.addflip.view.PageDeleteWarningModal
import com.team.presentation.addflip.view.TempPostWarningModal
import com.team.presentation.addflip.viewmodel.AddFlipViewModel
import com.team.presentation.common.snackbar.ObserveAsEvents
import com.team.presentation.common.state.ModalState

/**
 * 플립 글 작성 화면[AddFlipScreen]에 대한 Route
 *
 * @param popBackStack 뒤로가기 시
 * @param onNavigateToTempFlipBox 임시저장함으로 이동
 */
@Composable
fun AddFlipRoute(
    addFlipViewModel: AddFlipViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    onNavigateToTempFlipBox: () -> Unit
) {
    val uiState by addFlipViewModel.uiState.collectAsStateWithLifecycle()
    var pageDelete by rememberSaveable { mutableStateOf(false) }
    var tempPostWarningModalVisible by rememberSaveable { mutableStateOf(false) }
    var pageDeleteWarningModalVisible by rememberSaveable { mutableStateOf(false) }
    var backPressed by rememberSaveable { mutableStateOf(false) }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    BackHandler { addFlipViewModel.processEvent(AddFlipContract.UiEvent.OnSafeSave) }

    ObserveAsEvents(flow = addFlipViewModel.effect) { event ->
        when (event) {
            AddFlipContract.UiEffect.NavigateBack -> { popBackStack() }

            is AddFlipContract.UiEffect.ShowTempPostWarningModal -> {
                when (event.modalState) {
                    ModalState.Hide -> {
                        tempPostWarningModalVisible = false
                    }

                    is ModalState.Result -> {
                        if (!event.modalState.isError) {
                            popBackStack()
                        }
                    }

                    ModalState.Show -> {
                        tempPostWarningModalVisible = true
                    }
                }
            }

            is AddFlipContract.UiEffect.ShowPageDeleteWarningModal -> {
                when (event.modalState) {
                    ModalState.Hide -> {
                        pageDeleteWarningModalVisible = false
                        pageDelete = false
                    }

                    is ModalState.Result -> {}
                    ModalState.Show -> {
                        pageDeleteWarningModalVisible = true
                    }
                }
            }
        }
    }

    /** 임시 저장 경고 모달 */
    TempPostWarningModal(
        isModalVisible = tempPostWarningModalVisible,
        onAccept = {
            addFlipViewModel.processEvent(AddFlipContract.UiEvent.SaveTempPost)
            tempPostWarningModalVisible = false
        },
        onDiscard = {
            tempPostWarningModalVisible = false
            backPressed = true
        },
        onCancel = { tempPostWarningModalVisible = false },
        hideModal = { tempPostWarningModalVisible = false },
        onAnimationFinished = { if (backPressed) popBackStack() },
    )

    /** 페이지 삭제 경고 모달 */
    PageDeleteWarningModal(
        isModalVisible = pageDeleteWarningModalVisible,
        onAccept = {
            pageDelete = true
            pageDeleteWarningModalVisible = false
        },
        onCancel = {
            pageDeleteWarningModalVisible = false
            pageDelete = false
        },
        onAnimationFinished = {
            pageDeleteWarningModalVisible = false
            pageDelete = false
        },
    )

    AddFlipScreen(
        pageDelete = pageDelete,
        uiState = uiState,
        onUiEvent = addFlipViewModel::processEvent,
        onNavigateToTempFlipBox = {
            // TODO: 임시저장함으로 이동
        },
        onBackPressedDispatcher = { onBackPressedDispatcher?.onBackPressed() }
    )
}
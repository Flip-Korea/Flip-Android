package com.team.presentation.common.state

sealed interface ModalState {
    data object Idle: ModalState
    data class Display(val showed: Boolean): ModalState
    data object Hide: ModalState
}

/** CompositionLocalProvider 를 이용한 Event Bus 주입 방식 */
///**
// * ### 예시 코드 1
// *
// *     val dialogModalViewModel: DialogModalViewModel = viewModel()
// *     val dialogModalEventState by dialogModalViewModel.dialogModalEvent.collectAsStateWithLifecycle()
// *
// *     var dialogModalEvent by remember { mutableStateOf(DialogModalEvent(false)) }
// *
// *     LaunchedEffect(dialogModalEventState) {
// *         dialogModalEvent = dialogModalEventState
// *     }
// *
// *     CompositionLocalProvider(LocalDialogModalEvent provides dialogModalEvent) {
// *              /* (Root) Composables */
// *     }
// *
// * ### 예시 코드 2
// *
// *     val mEvent = LocalDialogModalEvent.current
// *
// *     /** FlipModal(Dialog) */
// *     FlipModalWrapper(
// *         isOpen = mEvent.isModalVisible,
// *         onDismissRequest = { scope.launch { resetEvent() } }
// *     ) {
// *         FlipModal(
// *             mainTitle = mEvent.mainTitle.asString(),
// *             subTitle = mEvent.subTitle.asString(),
// *             itemText = mEvent.actionTitle.asString(),
// *             itemText2 = mEvent.actionTitle2.asString(),
// *             onItemClick = { scope.launch { mEvent.action.invoke() } },
// *             onItem2Click = { scope.launch { mEvent.action2.invoke() } }
// *         )
// *     }
// */
//val LocalDialogModalEvent = compositionLocalOf { DialogModalState() }
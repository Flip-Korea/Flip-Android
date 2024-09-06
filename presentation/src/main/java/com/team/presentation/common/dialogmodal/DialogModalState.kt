package com.team.presentation.common.dialogmodal

sealed interface DialogModalState {
    data object Idle: DialogModalState
    data class Display(val showed: Boolean): DialogModalState
    data object Hide: DialogModalState
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
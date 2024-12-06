package com.team.presentation.addflip.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.designsystem.component.modal.FlipModal
import com.team.designsystem.component.modal.FlipModalWrapper
import com.team.presentation.R

/** 페이지 삭제 경고 모달 */
@Composable
fun PageDeleteWarningModal(
    modifier: Modifier = Modifier,
    isModalVisible: Boolean,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    onAnimationFinished: () -> Unit,
) {
    FlipModalWrapper(
        isOpen = isModalVisible,
        onDismissRequest = onCancel,
        onAnimationFinished = onAnimationFinished,
    ) {
        FlipModal(
            mainTitle = stringResource(id = R.string.add_flip_screen_page_delete_modal_main_title),
            itemText = stringResource(id = R.string.add_flip_screen_page_delete_modal_item_1),
            itemText2 = stringResource(id = R.string.add_flip_screen_page_delete_modal_item_2),
            onItemClick = onAccept,
            onItem2Click = onCancel,
        )
    }
}

/** 임시 저장 경고 모달 */
@Composable
fun TempPostWarningModal(
    modifier: Modifier = Modifier,
    isModalVisible: Boolean,
    onAccept: () -> Unit,
    onDiscard: () -> Unit,
    onCancel: () -> Unit,
    hideModal: () -> Unit,
    onAnimationFinished: () -> Unit,
) {
    FlipModalWrapper(
        isOpen = isModalVisible,
        onDismissRequest = hideModal,
        onAnimationFinished = onAnimationFinished,
    ) {
        FlipModal(
            mainTitle = stringResource(id = R.string.add_flip_screen_modal_main_title),
            subTitle = stringResource(id = R.string.add_flip_screen_modal_sub_title),
            itemText = stringResource(id = R.string.add_flip_screen_modal_item_2),
            itemText2 = stringResource(id = R.string.add_flip_screen_modal_item_1),
            itemText3 = stringResource(id = R.string.add_flip_screen_modal_item_3),
            onItemClick = onAccept,
            onItem2Click = onDiscard,
            onItem3Click = onCancel,
        )
    }
}

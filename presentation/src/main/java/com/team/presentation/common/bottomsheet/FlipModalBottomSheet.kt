package com.team.presentation.common.bottomsheet

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipTheme

//@MustBeDocumented
//@Retention(AnnotationRetention.SOURCE)
//@Target(
//    AnnotationTarget.VALUE_PARAMETER,
//    AnnotationTarget.TYPE,
//)
//annotation class RequiredModifier

/**
 * Flip ModalBottomSheet
 *
 * @param sheetState SheetState
 * @param onDismissRequest ModalBottomSheet 가 사라질 때 수행할 동작
 * @param content ModalBottomSheet Content
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlipModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {

    ModalBottomSheet(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest,
        dragHandle = { FlipDragHandle(verticalPadding = BottomSheetTokens.handlePadding) },
        sheetState = sheetState,
        containerColor = FlipTheme.colors.white,
        windowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        /** BottomSheet 내부영역 하단 패딩 */
        content(Modifier.padding(bottom = 12.dp))
    }
}
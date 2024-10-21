package com.team.designsystem.component.modal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.team.designsystem.component.utils.getDialogWindow
import com.team.designsystem.theme.FlipTheme

/**
 * FlipModal(Dialog) 과 함께 사용하는 Wrapper
 *
 * @param isOpen Modal 활성화 여부
 * @param animated 애니메이션 활성화 여부
 * @param onDismissRequest Modal이 사라질 때 실행할 작업
 * @param onAnimationFinished 모달 애니메이션이 끝난 직후 실행할 작업
 * @param content Modal Composable
 */
@Composable
fun FlipModalWrapper(
    isOpen: Boolean,
    animated: Boolean = true,
    onDismissRequest: () -> Unit,
    onAnimationFinished: () -> Unit = {},
    content: @Composable () -> Unit
) {

    var isOpenAnimated by remember { mutableStateOf(false) }
    LaunchedEffect(isOpen) { if (isOpen) isOpenAnimated = true }

    if (isOpenAnimated) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val dialogWindow = getDialogWindow()

            SideEffect {
                dialogWindow.let { window ->
                    window?.setDimAmount(0.5f)
                    window?.setWindowAnimations(-1)
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                var animateIn by rememberSaveable { mutableStateOf(false) }
                LaunchedEffect(Unit) { animateIn = true }

                AnimatedVisibility(
                    visible = animateIn && isOpen,
                    enter = FlipTheme.transition.fadeIn,
                    exit = FlipTheme.transition.fadeOut,
                ) {
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) { detectTapGestures { onDismissRequest() } }
//                            .background(FlipTheme.colors.main.copy(.5f))
                            .fillMaxSize()
                    )
                }

                AnimatedVisibility(
                    visible = animateIn && isOpen,
                    enter = if (animated) FlipTheme.transition.dialogEnter else EnterTransition.None,
                    exit = if (animated) FlipTheme.transition.dialogExit else ExitTransition.None
                ) {
                    content()

                    DisposableEffect(Unit) {
                        onDispose {
                            onAnimationFinished()
                            isOpenAnimated = false
                        }
                    }
                }
            }

        }
    }
}
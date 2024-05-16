package com.team.designsystem.component.modal

import android.view.Window
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.team.designsystem.theme.FlipTheme

@ReadOnlyComposable
@Composable
private fun getDialogWindow(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window

@Composable
fun FlipModalWrapper(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
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
                    window?.setDimAmount(0f)
                    window?.setWindowAnimations(-1)
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                var animateIn by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { animateIn = true }

                AnimatedVisibility(
                    visible = animateIn && isOpen,
                    enter = FlipTheme.transition.fadeIn,
                    exit = FlipTheme.transition.fadeOut,
                ) {
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) { detectTapGestures { onDismissRequest() } }
                            .background(FlipTheme.colors.main.copy(.5f))
                            .fillMaxSize()
                    )
                }

                AnimatedVisibility(
                    visible = animateIn && isOpen,
                    enter = FlipTheme.transition.dialogEnter,
                    exit = FlipTheme.transition.dialogExit
                ) {
                    content()

                    DisposableEffect(Unit) {
                        onDispose {
                            isOpenAnimated = false
                        }
                    }
                }
            }

        }
    }
}
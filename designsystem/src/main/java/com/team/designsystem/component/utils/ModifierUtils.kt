package com.team.designsystem.component.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** 일정 시간 동안 연속 클릭을 제한하는 Modifier 확장 함수 - 1 **/
//@SuppressLint("ModifierFactoryUnreferencedReceiver")
//fun Modifier.clickableOnce(onClick: () -> Unit): Modifier = composed(
//    inspectorInfo = {
//        name = "clickableOnce"
//        value = onClick
//    }
//) {
//    var enableAgain by remember { mutableStateOf(true) }
//    LaunchedEffect(enableAgain, block = {
//        if (enableAgain) return@LaunchedEffect
//        delay(timeMillis = 500L)
//        enableAgain = true
//    })
//    Modifier.clickable {
//        if (enableAgain) {
//            enableAgain = false
//            onClick()
//        }
//    }
//}

/** 일정 시간 동안 연속 클릭을 제한하는 Modifier 확장 함수 **/
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.clickableSingle(
    throttleTime: Long = 300,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed {
    val lastClickTimestamp = remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    clickable(
        interactionSource = interactionSource ?: remember { MutableInteractionSource() },
        indication = indication ?: rememberRipple(),
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = {
            val currentTimestamp = System.currentTimeMillis()
            if (currentTimestamp - lastClickTimestamp.value >= throttleTime) {
                coroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        onClick()
                    }
                }
                lastClickTimestamp.value = currentTimestamp
            }
        }
    )
}

/** 일정 시간 동안 연속 클릭을 제한하는 Modifier 확장 함수(Ripple 효과 제거 버전) */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.clickableSingleWithoutRipple(
    throttleTime: Long = 300,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed {
    val lastClickTimestamp = remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = {
            val currentTimestamp = System.currentTimeMillis()
            if (currentTimestamp - lastClickTimestamp.value >= throttleTime) {
                coroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        onClick()
                    }
                }
                lastClickTimestamp.value = currentTimestamp
            }
        }
    )
}

/** 포커싱 해제하는 Modifier 확장 함수
 *
 * 전체화면의 터치 이벤트 감지해서 보통 텍스트필드의 포커싱을 해제하려는 목적으로 사용 **/
// Ex) TextField 를 감싸고 있는 상위(부모) 컴포저블에 적용
fun Modifier.focusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}

/** 멀티 터치를 방지하는 Modifier 확장 함수 **/
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.disableMultiTouch() = composed {
    val coroutineScope = rememberCoroutineScope()
    pointerInput(Unit) {
        coroutineScope.launch {
            var currentId: Long = -1L
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent(PointerEventPass.Initial).changes.forEach { pointerInfo ->
                        when {
                            pointerInfo.pressed && currentId == -1L -> currentId = pointerInfo.id.value
                            pointerInfo.pressed.not() && currentId == pointerInfo.id.value -> currentId = -1
                            pointerInfo.id.value != currentId && currentId != -1L -> pointerInfo.consume()
                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}
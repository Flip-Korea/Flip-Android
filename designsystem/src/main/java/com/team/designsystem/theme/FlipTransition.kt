package com.team.designsystem.theme

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

data class FlipTransition(
    val fadeIn: EnterTransition = fadeIn(),
    val fadeOut: ExitTransition = fadeOut(),
    val dialogEnter: EnterTransition = fadeIn(spring(stiffness = Spring.StiffnessHigh)) + scaleIn(
        initialScale = .8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    ),
    val dialogExit: ExitTransition = fadeOut(tween(easing = EaseInCirc)) + scaleOut(
        targetScale = .8f,
        animationSpec = tween(easing = EaseInBack)
    ),
) {
    fun enterTransition(direction: FlipTransitionDirection): EnterTransition {
        val delay = 100

        return when(direction) {
            FlipTransitionDirection.Left -> {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(delayMillis = delay)
                )
            }
            FlipTransitionDirection.Right -> {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(delayMillis = delay)
                )
            }
            FlipTransitionDirection.Top -> {
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(delayMillis = delay)
                )
            }
            FlipTransitionDirection.Bottom -> {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(delayMillis = delay)
                )
            }
        }
    }

    fun exitTransition(direction: FlipTransitionDirection): ExitTransition {
        val delay = 100

        return when(direction) {
            FlipTransitionDirection.Left -> {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(delayMillis = delay)
                )
            }
            FlipTransitionDirection.Right -> {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(delayMillis = delay)
                )
            }
            FlipTransitionDirection.Top -> {
                slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(delayMillis = delay)
                ) + fadeOut()
            }
            FlipTransitionDirection.Bottom -> {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(delayMillis = delay)
                ) + fadeOut()
            }
        }
    }
}

object FlipTransitionObject {
    private val flipTransition = FlipTransition()
    val fadeIn = flipTransition.fadeIn
    val fadeOut = flipTransition.fadeOut
    val dialogEnter = flipTransition.dialogEnter
    val dialogExit = flipTransition.dialogExit
    val enterTransition: (FlipTransitionDirection) -> EnterTransition = {
        flipTransition.enterTransition(it)
    }
    val exitTransition: (FlipTransitionDirection) -> ExitTransition = {
        flipTransition.exitTransition(it)
    }
}

enum class FlipTransitionDirection {
    Left, Right,
    Top, Bottom
}
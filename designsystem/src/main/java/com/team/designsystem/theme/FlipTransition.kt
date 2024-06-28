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
import androidx.compose.animation.slideInVertically
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
    val slideInVertically: EnterTransition = slideInVertically(initialOffsetY = { it }),
    val slideOutVertically: ExitTransition = slideOutVertically(targetOffsetY = { it }) + fadeOut()
)

object FlipTransitionObject {
    private val flipTransition = FlipTransition()
    val fadeIn = flipTransition.fadeIn
    val fadeOut = flipTransition.fadeOut
    val dialogEnter = flipTransition.dialogEnter
    val dialogExit = flipTransition.dialogExit
    val slideInVertically = flipTransition.slideInVertically
    val slideOutVertically = flipTransition.slideOutVertically
}
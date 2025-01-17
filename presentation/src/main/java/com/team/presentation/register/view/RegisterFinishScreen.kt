package com.team.presentation.register.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R
import com.team.presentation.common.lottie.FlipLottieIcon

@Composable
fun RegisterFinishScreen(modifier: Modifier = Modifier) {
    // TODO 로딩 후 회원가입 완료시 체크 표시 후 완료 처리

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CheckAnimatedIcon(Modifier.size(57.dp))
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = stringResource(id = R.string.register_screen_finish_title),
                style = FlipTheme.typography.headline8,
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = stringResource(id = R.string.register_screen_finish_sub_title),
                style = FlipTheme.typography.body5,
            )
        }

//        RegisterScreenBottomBar(
//            title =,
//            enabled =,
//            isLoading =,
//            onClick = { /*TODO*/ },
//        )
    }
}

@Composable
private fun CheckAnimatedIcon(modifier: Modifier = Modifier) {
    DotLottieAnimation(
        modifier = modifier,
        source = DotLottieSource.Url(FlipLottieIcon.CHECK),
        autoplay = true,
        speed = 0.5f,
        useFrameInterpolation = true,
        playMode = Mode.FORWARD,
    )
}

@Preview
@Composable
private fun RegisterFinishScreenPreview() {
    FlipAppTheme {
        RegisterFinishScreen(Modifier.fillMaxSize())
    }
}

package com.team.presentation.register.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieEventListener
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R
import com.team.presentation.common.lottie.FlipLottieIcon
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.RegisterContract

@Composable
fun RegisterFinishScreen(
    modifier: Modifier = Modifier,
    registerFinishLoading: Boolean,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    // TODO 로딩 후 회원가입 완료시 체크 표시 후 완료 처리
    var buttonEnabled by rememberSaveable { mutableStateOf(true) }

    // 마지막 화면이므로 뒤로가기 방지
    BackHandler { }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CheckAnimatedIcon(
                modifier = Modifier.size(57.dp),
                onComplete = {
                    buttonEnabled = true
                },
            )
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = stringResource(id = R.string.register_screen_finish_title),
                style = FlipTheme.typography.headline8,
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = stringResource(id = R.string.register_screen_finish_sub_title),
                style = FlipTheme.typography.body5,
                textAlign = TextAlign.Center,
            )
        }

        RegisterScreenBottomBar(
            title = stringResource(id = R.string.register_screen_finish_btn),
            enabled = buttonEnabled,
            isLoading = registerFinishLoading,
            onClick = {
                onUiEvent(RegisterContract.UiEvent.RequestToNextPage(RegisterScreenPage.Finish))
            },
        )
    }
}

@Composable
private fun CheckAnimatedIcon(
    modifier: Modifier = Modifier,
    onComplete: () -> Unit,
) {
    DotLottieAnimation(
        modifier = modifier,
        source = DotLottieSource.Url(FlipLottieIcon.CHECK),
        autoplay = true,
        speed = 0.5f,
        useFrameInterpolation = true,
        playMode = Mode.FORWARD,
        eventListeners =
            listOf(
                object : DotLottieEventListener {
                    override fun onComplete() {
                        onComplete()
                    }
                },
            ),
    )
}

@Preview
@Composable
private fun RegisterFinishScreenPreview() {
    FlipAppTheme {
        RegisterFinishScreen(
            modifier = Modifier.fillMaxSize(),
            registerFinishLoading = false,
            onUiEvent = { },
        )
    }
}

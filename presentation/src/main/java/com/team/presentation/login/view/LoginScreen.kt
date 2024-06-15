package com.team.presentation.login.view

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.type.SocialLoginPlatform
import com.team.presentation.R
import com.team.presentation.login.state.LoginState
import com.team.presentation.util.asUiText

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginState: LoginState,
    onLoginClick: (SocialLoginPlatform) -> Unit,
) {

    val context = LocalContext.current

    LaunchedEffect(loginState.error) {
        if (loginState.error != null) {
            val error = loginState.error.asUiText().asString(context)
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.img_login_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        TopSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 134.dp),
            logo = R.drawable.ic_logo
        )
        BottomSection(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(vertical = 127.dp),
            loginPlatforms = remember {
                listOf(
                    Pair(SocialLoginPlatform.KAKAO, R.drawable.ic_login_kakao),
                    Pair(SocialLoginPlatform.GOOGLE, R.drawable.ic_login_google),
                )
            },
            onLoginClick = { onLoginClick(it) }
        )
        if (loginState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FlipTheme.colors.main.copy(0.5f))
                    .zIndex(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { }
                    )
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp),
                    color = FlipTheme.colors.main
                )
            }
        }
    }
}

/** 로고 & 타이틀이 있는 부분 **/
@Composable
private fun TopSection(
    modifier: Modifier = Modifier,
    @DrawableRes logo: Int,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Image(
            modifier = Modifier.size(72.dp, 45.dp),
            imageVector = ImageVector.vectorResource(logo),
            contentDescription = null
        )
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.login_screen_title_1))
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.login_screen_title_2))
                }
                append("\n")
                append(stringResource(id = R.string.login_screen_title_3))
            },
            style = FlipTheme.typography.headline7.copy(
                fontSize = 32.sp,
                lineHeight = 32.sp * 1.4
            ),
            color = Color.White,
            textAlign = TextAlign.Start
        )
    }
}

/** 소셜로그인 플랫폼 버튼들이 있는 부분 **/
@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    loginPlatforms: List<Pair<SocialLoginPlatform, Int>>,
    onLoginClick: (SocialLoginPlatform) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(
            text = stringResource(id = R.string.login_screen_select_platform),
            style = FlipTheme.typography.body3,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            loginPlatforms.forEach { (platform, logo) ->
                Image(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .clickableSingle { onLoginClick(platform) },
                    painter = painterResource(id = logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FlipAppTheme {
        LoginScreen(
            loginState = LoginState(loading = true),
            onLoginClick = { }
        )
    }
}
package com.team.flip.feature.ui_test.token_flow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    loginState: LoginState,
    login: (String) -> Unit,
    getProfile: (String) -> Unit,
    getTokenByDataStore: () -> Unit,
    logout: () -> Unit,
    register: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxSize()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                if (loginState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(45.dp),
                        color = Color.White,
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(10.dp),
                        text = loginState.body.asString(),
                        fontSize = 20.sp,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CustomButton(modifier = Modifier.weight(1f), text = "Login", onClick = { login("kakao123test") })
                CustomButton(modifier = Modifier.weight(1f), text = "Register", onClick = { register() })
                CustomButton(modifier = Modifier.weight(1f), text = "GetUser", onClick = { getProfile("testprofileid") })
                CustomButton(modifier = Modifier.weight(1f), text = "Token", onClick = { getTokenByDataStore() })
                CustomButton(modifier = Modifier.weight(1f), text = "Logout", onClick = { logout() })
            }
        }
    }
}

@Composable
private fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 15.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreen2Preview() {

    LoginScreen(
        loginState = LoginState(),
        login = {},
        getProfile = {},
        getTokenByDataStore = {},
        logout = {},
        register = {}
    )
}
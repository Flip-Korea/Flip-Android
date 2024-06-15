package com.team.presentation.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @param deleteToken 토큰 제거(테스트 용)
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    deleteToken: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(text = "(MainActivity)\nHome Screen")
        Button(onClick = deleteToken) {
            Text(text = "delete token")
        }
    }
}
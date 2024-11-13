package com.team.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun FlipThemeTestScreen() {

    FlipAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FlipTheme.colors.white),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                15.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
//            CompositionLocalProvider(LocalRippleTheme provides FlipNoRipple()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 16.dp)
                        .clip(FlipTheme.shapes.roundedCornerSmall)
                        .background(FlipTheme.colors.main)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(),
                            onClick = { }
                        ),
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Flip 디자인 시스템 테스트(Box)",
                        style = FlipTheme.typography.headline2,
                        color = FlipTheme.colors.white
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 16.dp),
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FlipTheme.colors.main,
                        contentColor = FlipTheme.colors.white
                    ),
                    shape = FlipTheme.shapes.roundedCornerSmall
                ) {
                    Text(
                        text = "Flip 디자인 시스템 테스트(Button)",
                        style = FlipTheme.typography.headline2,
                        color = FlipTheme.colors.white
                    )
                }
//            }
        }
    }
}
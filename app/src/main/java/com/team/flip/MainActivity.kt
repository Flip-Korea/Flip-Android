package com.team.flip

import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var keepOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        splashScreen.apply {
            setKeepOnScreenCondition {
                keepOnScreen
            }
        }
        Handler().postDelayed({ keepOnScreen = false }, 1000)
//        startSomeNextActivity()
//        finish()

        setContent {
            FlipAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FlipTheme.colors.white)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    TempBox(Modifier.align(Alignment.TopStart))
                    TempBox(Modifier.align(Alignment.TopCenter))
                    TempBox(Modifier.align(Alignment.TopEnd))
                    TempBox(Modifier.align(Alignment.CenterStart))
                    TempBox(Modifier.align(Alignment.Center))
                    TempBox(Modifier.align(Alignment.CenterEnd))
                    TempBox(Modifier.align(Alignment.BottomStart))
                    TempBox(Modifier.align(Alignment.BottomCenter))
                    TempBox(Modifier.align(Alignment.BottomEnd))
                }
            }
        }
    }
}

@Composable
private fun TempBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Black)
            .padding(16.dp)
            .wrapContentSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "MainActivity"
        )
    }
}
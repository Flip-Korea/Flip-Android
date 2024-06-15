package com.team.flip

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.team.data.datastore.TokenDataStore
import com.team.designsystem.theme.FlipAppTheme
import com.team.flip.navigation.MainNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Flip 메인액티비티 **/
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenDataStore: TokenDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        /** statusBarsPadding() & navigationBarsPadding() 사용하기 **/
        setContent {

            val navController = rememberNavController()

            FlipAppTheme {
                MainNavigation(
                    navController = navController,
                    deleteToken = {
                        //TODO 임시 테스트용 코드이므로 반드시 삭제할 것
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                                tokenDataStore.deleteToken(TokenDataStore.TokenType.ACCESS_TOKEN)
                                tokenDataStore.deleteToken(TokenDataStore.TokenType.REFRESH_TOKEN)
                            }
                        }
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}
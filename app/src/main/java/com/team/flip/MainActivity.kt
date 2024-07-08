package com.team.flip

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.team.designsystem.theme.FlipAppTheme
import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import com.team.flip.navigation.MainNavigation
import com.team.flip.navigation.bottom_nav.FlipBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Flip 메인액티비티 **/
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenDataStore: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        /** statusBarsPadding() & navigationBarsPadding() 사용하기 **/
        setContent {

            val navController = rememberNavController()

            FlipAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { FlipBottomNavigation(navController = navController) }
                ) { paddingValues ->
                    MainNavigation(
                        //TODO 아직 못정함, 정하고 변경 예정
//                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        deleteToken = {
                            //TODO 임시 테스트용 코드이므로 반드시 삭제할 것
                            lifecycleScope.launch {
                                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                                    tokenDataStore.deleteData(DataStoreType.TokenType.ACCESS_TOKEN)
                                    tokenDataStore.deleteData(DataStoreType.TokenType.REFRESH_TOKEN)
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
}
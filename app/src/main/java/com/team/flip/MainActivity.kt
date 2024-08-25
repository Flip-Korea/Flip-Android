package com.team.flip

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.team.designsystem.component.snackbar.FlipSnackbar
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import com.team.flip.navigation.MainNavigation
import com.team.presentation.common.snackbar.ObserveAsEvents
import com.team.presentation.common.snackbar.SnackbarController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


/** Flip 메인액티비티 **/
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // for scaffold innerPadding
@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenDataStore: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        /** statusBarsPadding() & navigationBarsPadding() 사용하기 **/
        setContent {

            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            val mainNavController = rememberNavController()

            /** 스낵바 */
            val snackbarHostState = remember { SnackbarHostState() }
            val dismissSnackbarState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value != SwipeToDismissBoxValue.Settled) {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        true
                    } else {
                        false
                    }
                }
            )
            LaunchedEffect(dismissSnackbarState.currentValue) {
                if (dismissSnackbarState.currentValue != SwipeToDismissBoxValue.Settled) {
                    dismissSnackbarState.reset()
                }
            }
            ObserveAsEvents(
                flow = SnackbarController.events,
                key1 = snackbarHostState,
                onEvent = { event ->
                    coroutineScope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()

                        val result = snackbarHostState.showSnackbar(
                            message = event.message.asString(context),
                            actionLabel = event.action?.name,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }
            )

            FlipAppTheme {

                /**
                 * Snackbar 를 위한 Scaffold
                 * TODO: Snackbar 를 전역적으로 사용하기 위해 또 다른 방법 생각해보기
                 */
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        FlipSnackbar(
                            snackBarHostState = snackbarHostState,
                            dismissSnackbarState = dismissSnackbarState
                        )
                    }
                ) { innerPadding ->
                    MainNavigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(FlipTheme.colors.white),
                        mainNavController = mainNavController,
                        deleteToken = {
                            //TODO 임시 테스트용 코드이므로 반드시 삭제할 것
                            lifecycleScope.launch {
                                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                                    tokenDataStore.deleteData(DataStoreType.TokenType.ACCESS_TOKEN)
                                    tokenDataStore.deleteData(DataStoreType.TokenType.REFRESH_TOKEN)
                                    tokenDataStore.clearAll()
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
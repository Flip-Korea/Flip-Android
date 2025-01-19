package com.team.flip

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.team.designsystem.component.snackbar.FlipSnackbar
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.DataStoreManager
import com.team.presentation.common.snackbar.ObserveAsEvents
import com.team.presentation.common.snackbar.SnackbarController
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.register.view.RegisterScreen
import com.team.presentation.register.viewmodel.RegisterViewModel
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

        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            val mainNavController = rememberNavController()

            /** 스낵바 */
            val snackbarHostState = remember { SnackbarHostState() }
            val dismissSnackbarState =
                rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        if (value != SwipeToDismissBoxValue.Settled) {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            true
                        } else {
                            false
                        }
                    },
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

                        val result =
                            snackbarHostState.showSnackbar(
                                message = event.message.asString(context),
                                actionLabel = event.action?.name,
                                duration = SnackbarDuration.Short,
                            )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                },
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
                            dismissSnackbarState = dismissSnackbarState,
                        )
                    },
                    contentWindowInsets = WindowInsets.systemBars,
                    containerColor = FlipTheme.colors.white,
                ) { innerPadding ->
                    // TODO: 회원가입 테스트를 위한 임시코드
                    val navController = rememberNavController()
                    val viewModel = hiltViewModel<RegisterViewModel>()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    ObserveAsEvents(flow = viewModel.effect) { effect ->
                        when (effect) {
                            RegisterContract.UiEffect.BackPress -> {
                                navController.popBackStack()
                            }

                            is RegisterContract.UiEffect.NavigateTo -> {
                                navController.navigate(route = effect.destination.route)
                            }
                        }
                    }

                    RegisterScreen(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        navController = navController,
                        uiState = uiState,
                        onUiEvent = viewModel::processEvent,
                        onBackPress = {
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            }
                        },
                    )

//                    MainNavigation(
//                        modifier =
//                            Modifier
//                                .fillMaxSize()
//                                .background(FlipTheme.colors.white),
//                        mainNavController = mainNavController,
//                        deleteToken = {
//                            // TODO 임시 테스트용 코드이므로 반드시 삭제할 것
//                            lifecycleScope.launch {
//                                repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                                    tokenDataStore.deleteData(DataStoreType.TokenType.ACCESS_TOKEN)
//                                    tokenDataStore.deleteData(DataStoreType.TokenType.REFRESH_TOKEN)
//                                    tokenDataStore.clearAll()
//                                }
//                            }
//
//                            val intent = Intent(this, LoginActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        },
//                    )
                }
            }
        }
    }
}

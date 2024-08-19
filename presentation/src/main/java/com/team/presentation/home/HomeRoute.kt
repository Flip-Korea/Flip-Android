package com.team.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.common.bottomsheet.ReportAndBlockBottomSheet
import com.team.presentation.common.bottomsheet.ReportAndBlockUiEvent
import com.team.presentation.home.view.HomeScreen
import com.team.presentation.home.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

/**
 * 홈 화면[HomeScreen]에 대한 Route
 *
 * @param innerPadding 상위 Scaffold 에서 계산된 innerPadding
 * @param onSettingClick 세팅 버튼 클릭 시
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onSettingClick: () -> Unit,
) {

    val postState by homeViewModel.postState.collectAsStateWithLifecycle()
    val reportState by homeViewModel.reportState.collectAsStateWithLifecycle()
    val blockState by homeViewModel.blockState.collectAsStateWithLifecycle()
    val filteredMyCategoriesState by homeViewModel.filteredMyCategoriesState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var startFromReportView: Boolean? by remember { mutableStateOf(null) }
    var reportAndBlockProfileId by remember { mutableStateOf("") }
    var reportAndBlockPhotoUrl by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

//    val lifecycleOwner = LocalLifecycleOwner.current
//    LaunchedEffect(lifecycleOwner) {
//        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
//            homeViewModel.fetchCategories()
//        }
//    }

    ReportAndBlockBottomSheet(
        startFromReportView = startFromReportView,
        sheetState = sheetState,
        reportState = reportState,
        blockState = blockState,
        profileId = reportAndBlockProfileId,
        photoUrl = reportAndBlockPhotoUrl,
        onDismissRequest = {
            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    startFromReportView = null
                }
            }
        },
        onReport = { reportType -> homeViewModel.onReport() },
        onBlockClick = homeViewModel::onBlock,
    )

    HomeScreen(
        modifier = modifier
            .fillMaxSize()
            .background(FlipTheme.colors.white)
            .padding(bottom = innerPadding.calculateBottomPadding()),
        myCategories = filteredMyCategoriesState,
        postState = postState,
        flipCardUiEvent = homeViewModel::onFlipCardEvent,
        homeUiEvent = homeViewModel::onHomeUiEvent,
        onSettingClick = onSettingClick,
        reportAndBlockUiEvent = { reportAndBlockUiEvent ->
            when (reportAndBlockUiEvent) {
                is ReportAndBlockUiEvent.OnReport -> {
                    reportAndBlockProfileId = reportAndBlockUiEvent.profileId
                    reportAndBlockPhotoUrl = reportAndBlockUiEvent.photoUrl
                    startFromReportView = true
                }
                is ReportAndBlockUiEvent.OnBlock -> {
                    reportAndBlockProfileId = reportAndBlockUiEvent.profileId
                    reportAndBlockPhotoUrl = reportAndBlockUiEvent.photoUrl
                    startFromReportView = false
                }
            }
        },
    )
}
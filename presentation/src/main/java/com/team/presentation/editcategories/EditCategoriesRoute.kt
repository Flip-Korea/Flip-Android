package com.team.presentation.editcategories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R
import com.team.presentation.editcategories.view.EditMyCategoriesScreen
import com.team.presentation.editcategories.viewmodel.EditMyCategoriesViewModel

/**
 * 나의 관심 카테고리 수정 화면[EditMyCategoriesScreen]에 대한 Route
 *
 * @param popBackStack 뒤로가기 시
 */
@Composable
fun EditCategoriesRoute(
    modifier: Modifier = Modifier,
    editMyCategoriesViewModel: EditMyCategoriesViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
) {
    val context = LocalContext.current
    val myCategoriesState by editMyCategoriesViewModel.myCategoriesState.collectAsStateWithLifecycle()
    val nicknameState by editMyCategoriesViewModel.nicknameState.collectAsStateWithLifecycle()
    val speechBubbleState by editMyCategoriesViewModel.speechBubbleState.collectAsStateWithLifecycle()
    val myCategoriesUpdateState by editMyCategoriesViewModel.myCategoriesUpdateState.collectAsStateWithLifecycle()

    val nickname = nicknameState.ifEmpty {
        context.resources.getString(R.string.edit_interest_categories_placeholder_nickname)
    }

    LaunchedEffect(myCategoriesUpdateState) {
        if (myCategoriesUpdateState.success) {
            popBackStack()
        }
    }

    EditMyCategoriesScreen(
        modifier = modifier
            .fillMaxSize()
            .background(FlipTheme.colors.white)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(bottom = 26.dp),
        nickname = nickname,
        myCategoriesState = myCategoriesState,
        speechBubbleState = speechBubbleState,
        myCategoriesUpdateState = myCategoriesUpdateState,
        updateMyCategories = editMyCategoriesViewModel::updateMyCategories,
        uiEvent = editMyCategoriesViewModel::onUiEvent,
        onBackPress = {
            //TODO: 실행 시 HomeScreen 으로 돌아가는데 이 부분에서 딜레이 발생, 해결 필요
            // (HomeScreen, HomeViewModel 체크)
            popBackStack()
        }
    )
}
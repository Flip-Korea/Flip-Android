package com.team.presentation.tempflipbox.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipLargeButton
import com.team.designsystem.component.button.FlipTextButton
import com.team.designsystem.component.topbar.FlipCenterAlignedTopBar
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.post.TempPost
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType
import com.team.presentation.R
import com.team.presentation.common.error.FlipErrorScreen
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.tempflipbox.TempFlipBoxContract
import com.team.presentation.util.asColor

/**
 * 임시저장함 화면
 */
@Composable
fun TempFlipBoxScreen(
    modifier: Modifier = Modifier,
    uiState: TempFlipBoxContract.UiState,
    uiEvent: (TempFlipBoxContract.UiEvent) -> Unit,
    onBackPress: () -> Unit,
) {

    var selectedTempPost by rememberSaveable { mutableStateOf(listOf<TempPost>()) }
    var selectMode by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(!selectMode) {
        selectedTempPost = listOf()
    }

    /** 삭제 경고모달 */


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            FlipCenterAlignedTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CommonPaddingValues.TopBarWithTouchTarget),
                title = stringResource(id = R.string.temp_flip_box_screen_topbar_title),
                onBackPress = onBackPress,
                options = {
                    FlipTextButton(
                        text = stringResource(id = R.string.temp_flip_box_screen_topbar_btn),
                        onClick = { selectMode = !selectMode }
                    )
                }
            )
        },
        bottomBar = {
            if (selectMode) {
                FlipLargeButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.temp_flip_box_screen_btn_delete),
                    onClick = {
                        uiEvent(TempFlipBoxContract.UiEvent.OnTempPostsDelete(selectedTempPost.map { it.tempPostId }))
                    },
                    enabled = selectedTempPost.isNotEmpty()
                )
            }
        },
        containerColor = FlipTheme.colors.white
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(top = 4.dp, start = 16.dp, end = 16.dp)
        ) {
            when(uiState) {
                TempFlipBoxContract.UiState.Idle -> {
                    TempFlipBoxSkeletonScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        itemCount = 7
                    )
                }

                TempFlipBoxContract.UiState.Loading -> {
                    TempFlipBoxSkeletonScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        itemCount = 7
                    )
                }

                is TempFlipBoxContract.UiState.Error -> {
                    FlipErrorScreen(
                        modifier = Modifier.padding(innerPadding),
                        errorMessage = uiState.error,
                        onRetry = { }
                    )
                }

                is TempFlipBoxContract.UiState.TempPosts -> {
                    /** 초기화 구문 */
                    LaunchedEffect(Unit) {
                        selectMode = false
                        selectedTempPost = emptyList()
                    }
                    TempPostsSection(
                        tempPosts = uiState.tempPosts,
                        selectedTempPosts = selectedTempPost,
                        selectMode = selectMode,
                        innerPadding = innerPadding,
                        onSelect = { tempPost, selected ->
                            selectedTempPost = selectedTempPost
                                .toMutableList()
                                .apply {
                                    if (selected) add(tempPost) else remove(tempPost)
                                }
                        },
                        onSelectAll = { allSelected ->
                            selectedTempPost = if (allSelected) uiState.tempPosts else listOf() }
                        ,
                        onOpenCard = { tempPost ->
                            //TODO: 공통적으로 사용되는 플립 화면으로 연결(아직 개발 안 됨)
                        }
                    )
                }
            }
        }
    }
}

/**
 * 임시저장플립 섹션 (메인 컨텐츠)
 *
 * @param tempPosts 임시저장플립 리스트
 * @param selectMode 편집모드(선택모드)
 * @param selectedTempPosts 선택 된 임시저장플립 리스트
 * @param onSelect 임시저장플립 선택 시 수행할 작업
 * @param onSelectAll 전체선택 시 수행할 작업 (Boolean 값이 true면, 전체 선택 실행)
 * @param onOpenCard 임시저장플립 열기
 */
@Composable
private fun TempPostsSection(
    modifier: Modifier = Modifier,
    tempPosts: List<TempPost>,
    selectMode: Boolean,
    selectedTempPosts: List<TempPost>,
    innerPadding: PaddingValues,
    onSelect: (TempPost, Boolean) -> Unit,
    onSelectAll: (Boolean) -> Unit,
    onOpenCard: (TempPost) -> Unit
) {

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Top)
        ) {
            TopToolBar(
                modifier = Modifier.fillMaxWidth(),
                selectMode = selectMode,
                selectedTempPostsSize = selectedTempPosts.size,
                tempPostsSize = tempPosts.size,
                onSelectAll = { onSelectAll(it) }
            )
            if (tempPosts.isNotEmpty()) {
                TempPostList(
                    modifier = Modifier.fillMaxWidth(),
                    tempPosts = tempPosts,
                    selectMode = selectMode,
                    selectedTempPosts = selectedTempPosts,
                    onSelect = onSelect,
                    onOpenCard = onOpenCard
                )
            }
        }

        if (tempPosts.isEmpty()) {
            TempPostListEmptyScreen(Modifier.fillMaxSize())
        }
    }
}

/**
 * TopBar 바로 하단에 위치한 (총 개수와 전체선택 버튼이 있는) 툴바
 *
 * @param selectMode 편집모드(선택모드)
 * @param selectedTempPostsSize 선택 된 임시저장플립 리스트 사이즈
 * @param tempPostsSize 임시저장플립 리스트 사이즈
 * @param onSelectAll 전체선택 시 수행할 작업 (Boolean 값이 true면, 전체 선택 실행)
 */
@Composable
private fun TopToolBar(
    modifier: Modifier = Modifier,
    selectMode: Boolean,
    selectedTempPostsSize: Int,
    tempPostsSize: Int,
    onSelectAll: (Boolean) -> Unit,
) {

    val selectModeText = if (tempPostsSize == selectedTempPostsSize) {
        stringResource(id = R.string.temp_flip_box_screen_card_all_select_off_btn)
    } else { stringResource(id = R.string.temp_flip_box_screen_card_all_select_btn) }
    val selectModeTextColor = if (tempPostsSize == selectedTempPostsSize) FlipTheme.colors.gray5 else FlipTheme.colors.main

    Row(modifier = modifier) {
        Text(
            text = buildAnnotatedString {
                append("${stringResource(id = R.string.temp_flip_box_screen_card_sub_1)} ")
                withStyle(SpanStyle(color = FlipTheme.colors.main)) {
                    append((if (selectMode) selectedTempPostsSize else tempPostsSize).toString())
                }
                append(stringResource(id = R.string.temp_flip_box_screen_card_sub_2))
                if (selectMode) {
                    append(" ${stringResource(id = R.string.temp_flip_box_screen_card_sub_3)}")
                }
            },
            style = FlipTheme.typography.headline1,
            color = FlipTheme.colors.gray5,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
        )
        if (selectMode) {
            Text(
                text = selectModeText,
                style = FlipTheme.typography.headline1,
                color = selectModeTextColor,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End)
                    .clickableSingleWithoutRipple {
                        onSelectAll(tempPostsSize != selectedTempPostsSize)
                    }
            )
        }
    }
}

/**
 * 임시저장플립 리스트
 *
 * @param tempPosts 임시저장플립 리스트
 * @param selectMode 편집모드(선택모드)
 * @param selectedTempPosts 선택 된 임시저장플립
 * @param onSelect 임시저장플립 선택 시 수행 할 작업
 * @param onOpenCard 임시저장플립 열기
 */
@Composable
private fun TempPostList(
    modifier: Modifier = Modifier,
    tempPosts: List<TempPost>,
    selectMode: Boolean,
    selectedTempPosts: List<TempPost>,
    onSelect: (TempPost, Boolean) -> Unit,
    onOpenCard: (TempPost) -> Unit
) {

    var selectedTempPostIds by rememberSaveable { mutableStateOf(listOf<Long>()) }
    LaunchedEffect(selectedTempPosts) {
        selectedTempPostIds = selectedTempPosts.map { it.tempPostId }
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        items(
            items = tempPosts,
            key = { it.tempPostId }
        ) { tempPost ->

            val selected = selectedTempPostIds.contains(tempPost.tempPostId)

            TempPostCard(
                title = tempPost.title,
                postAt = tempPost.postAt,
                bgColorType = tempPost.bgColorType,
                selectMode = selectMode,
                selected = selected,
                onSelect = { onSelect(tempPost, !selected) },
                onOpenCard = { onOpenCard(tempPost) }
            )
        }
    }
}

@Composable
private fun TempPostListEmptyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(
            text = stringResource(id = R.string.temp_flip_box_screen_empty_title_1),
            style = FlipTheme.typography.headline4,
            color = FlipTheme.colors.gray5
        )
        Text(
            text = stringResource(id = R.string.temp_flip_box_screen_empty_title_2),
            style = FlipTheme.typography.body5,
            color = FlipTheme.colors.gray5,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 임시저장 플립 카드
 *
 * @param selectMode 편집모드(선택모드)
 * @param selected 선택 여부
 * @param onSelect 선택 시 수행 할 작업
 * @param onOpenCard 임시저장플립 열기
 */
@Composable
private fun TempPostCard(
    modifier: Modifier = Modifier,
    title: String,
    postAt: String,
    bgColorType: BackgroundColorType,
    selectMode: Boolean,
    selected: Boolean,
    onSelect: () -> Unit,
    onOpenCard: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(FlipTheme.shapes.roundedCornerSmall)
            .background(bgColorType.asColor())
            .padding(16.dp)
            .clickableSingleWithoutRipple {
                if (selectMode) onSelect()
                else onOpenCard()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title.ifEmpty { stringResource(id = R.string.temp_flip_box_screen_card_title_placeholder) },
                style = FlipTheme.typography.headline2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = postAt, style = FlipTheme.typography.body3, color = FlipTheme.colors.gray5)
        }
        if (selectMode) {
            SelectButton(modifier = Modifier, selected = selected, onSelect = onSelect)
        }
    }
}

/**
 * 선택(체크) 버튼
 */
@Composable
private fun SelectButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = if (selected) FlipTheme.colors.main else Color.White
    val borderColor = if (selected) FlipTheme.colors.main else FlipTheme.colors.gray5

    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(23.dp)
            .background(backgroundColor)
            .border(1.dp, borderColor, CircleShape)
            .clickableSingleWithoutRipple { onSelect() }
    ) {
        if (selected) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_check),
                contentDescription = stringResource(id = R.string.temp_flip_box_screen_content_desc_card_sub_btn),
                tint = Color.White
            )
        }
    }
}

@Preview(name = "selectMode = false")
@Composable
private fun TempPostCardPreview() {
    FlipAppTheme {
        TempPostCard(
            title = "소록소록 도담도담 산들림",
            postAt = "2024.01.24 21:57",
            bgColorType = BackgroundColorType.YELLOW,
            selectMode = false,
            selected = false,
            onSelect = { },
            onOpenCard = { }
        )
    }
}

@Preview(name = "selectMode = true")
@Composable
private fun TempPostCardPreview2() {
    var selected by remember { mutableStateOf(true) }

    FlipAppTheme {
        TempPostCard(
            title = "소록소록 도담도담 산들림",
            postAt = "2024.01.24 21:57",
            bgColorType = BackgroundColorType.YELLOW,
            selectMode = true,
            selected = selected,
            onSelect = { selected = !selected },
            onOpenCard = { }
        )
    }
}

@Preview(name = "Long title")
@Composable
private fun TempPostCardPreview3() {
    var selected by remember { mutableStateOf(true) }

    FlipAppTheme {
        TempPostCard(
            title = "소록소록 도담도담 산들림, 소록소록 도담도담 산들림, 소록소록 도담도담 산들림",
            postAt = "2024.01.24 21:57",
            bgColorType = BackgroundColorType.YELLOW,
            selectMode = true,
            selected = selected,
            onSelect = { selected = !selected },
            onOpenCard = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TempPostListPreview() {
    TempPostList(
        tempPosts = tempPostsTestData,
        selectMode = true,
        selectedTempPosts = emptyList(),
        onSelect = { tempPost, select -> },
        onOpenCard = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun TempFlipBoxScreenPreview() {
    FlipAppTheme {
        TempFlipBoxScreen(
            uiState = TempFlipBoxContract.UiState.TempPosts(tempPostsTestData),
            uiEvent = { },
            onBackPress = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TempFlipBoxEmptyScreenPreview() {
    FlipAppTheme {
        TempFlipBoxScreen(
            uiState = TempFlipBoxContract.UiState.TempPosts(listOf()),
            uiEvent = { },
            onBackPress = { }
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun TempFlipBoxScreenPreview2() {
    FlipAppTheme {
        TempFlipBoxScreen(
            uiState = TempFlipBoxContract.UiState.Loading,
            uiEvent = { },
            onBackPress = { }
        )
    }
}

private val tempPostsTestData = List(15) {
    TempPost(it.toLong(), "테스트 Title #$it", "", BackgroundColorType.entries.random(), FontStyleType.NORMAL, 1, "일상", emptyList(), "2024-08-28 17:19:17")
}
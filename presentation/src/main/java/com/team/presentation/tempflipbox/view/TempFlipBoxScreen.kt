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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.team.designsystem.component.button.FlipLargeButton
import com.team.designsystem.component.button.FlipTextButton
import com.team.designsystem.component.modal.FlipModal
import com.team.designsystem.component.modal.FlipModalStyle
import com.team.designsystem.component.modal.FlipModalWrapper
import com.team.designsystem.component.topbar.FlipCenterAlignedTopBar
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.post.TempPost
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType
import com.team.presentation.R
import com.team.presentation.common.error.FlipErrorScreen
import com.team.presentation.common.paging.FlipLoadingIndicator
import com.team.presentation.common.paging.isEndOfPaginationReached
import com.team.presentation.common.paging.isError
import com.team.presentation.common.paging.isLoading
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.tempflipbox.TempFlipBoxContract
import com.team.presentation.util.asColor
import kotlinx.coroutines.flow.flowOf

/** 임시저장함 화면 */
@Composable
fun TempFlipBoxScreen(
    modifier: Modifier = Modifier,
    tempPostPaging: LazyPagingItems<TempPost>,
    tempPostTotalSize: Int,
    uiState: TempFlipBoxContract.UiState,
    uiEvent: (TempFlipBoxContract.UiEvent) -> Unit,
    isModalVisible: Boolean,
    onBackPress: () -> Unit,
) {

    var selectedTempPost by rememberSaveable { mutableStateOf(listOf<TempPost>()) }
    var selectMode by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(!selectMode) {
        if (!selectMode) {
            selectedTempPost = listOf()
        }
    }

    var isOpen by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(isModalVisible) {
        if (isModalVisible) {
            isOpen = true
        }
    }

    /** 삭제 경고모달 */
    FlipModalWrapper(isOpen = isOpen, onDismissRequest = { isOpen = false }) {
        FlipModal(
            modalStyle = FlipModalStyle.MEDIUM,
            mainTitle = stringResource(id = R.string.temp_flip_box_screen_modal_title),
            subTitle = stringResource(id = R.string.temp_flip_box_screen_modal_sub_title_1) +
                    " ${selectedTempPost.size}" +
                    stringResource(id = R.string.temp_flip_box_screen_modal_sub_title_2),
            itemText = stringResource(id = R.string.temp_flip_box_screen_modal_action_1),
            itemText2 = stringResource(id = R.string.temp_flip_box_screen_modal_action_2),
            onItemClick = {
                uiEvent(TempFlipBoxContract.UiEvent.OnTempPostsDelete(selectedTempPost.map { it.tempPostId }))
                isOpen = false
            },
            onItem2Click = { isOpen = false }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            FlipCenterAlignedTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CommonPaddingValues.TopBarWithTouchTarget),
                title = stringResource(id = R.string.temp_flip_box_screen_topbar_title),
                onAction = onBackPress,
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
                        uiEvent(TempFlipBoxContract.UiEvent.OnTempPostsDelete())
                    },
                    enabled = selectedTempPost.isNotEmpty()
                )
            }
        },
        containerColor = FlipTheme.colors.white
    ) { innerPadding ->

        Column(modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp)) {
            when (uiState) {
                TempFlipBoxContract.UiState.Idle -> {
                    TempFlipBoxSkeletonScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        itemCount = SKELETON_ITEM_COUNT
                    )
                }

                TempFlipBoxContract.UiState.Loading -> {
                    TempFlipBoxSkeletonScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        itemCount = SKELETON_ITEM_COUNT
                    )
                }

                is TempFlipBoxContract.UiState.Error -> {
                    FlipErrorScreen(
                        modifier = Modifier.padding(innerPadding),
                        errorMessage = uiState.error,
                        onRetry = { }
                    )
                }

                TempFlipBoxContract.UiState.TempPostSuccess -> {
                    TempPostsSection(
                        tempPostPaging = tempPostPaging,
                        tempPostTotalSize = tempPostTotalSize,
                        selectedTempPosts = selectedTempPost,
                        selectMode = selectMode,
                        innerPadding = innerPadding,
                        onSelect = { tempPost, selected ->
                            selectedTempPost = selectedTempPost
                                .toMutableList()
                                .apply { if (selected) add(tempPost) else remove(tempPost) }
                        },
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
 * @param tempPostPaging 임시저장플립 리스트
 * @param selectMode 편집모드(선택모드)
 * @param selectedTempPosts 선택 된 임시저장플립 리스트
 * @param innerPadding Scaffold에서 받아와서 개별 적용을 위한 innerPadding
 * @param onSelect 임시저장플립 선택 시 수행할 작업
 * @param onOpenCard 임시저장플립 열기
 */
@Composable
private fun TempPostsSection(
    modifier: Modifier = Modifier,
    tempPostPaging: LazyPagingItems<TempPost>,
    tempPostTotalSize: Int,
    selectMode: Boolean,
    selectedTempPosts: List<TempPost>,
    innerPadding: PaddingValues,
    onSelect: (TempPost, Boolean) -> Unit,
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
                tempPostsSize = tempPostTotalSize,
            )
            if (tempPostTotalSize != 0) {
                TempPostList(
                    modifier = Modifier.fillMaxWidth(),
                    tempPostPaging = tempPostPaging,
                    selectMode = selectMode,
                    selectedTempPosts = selectedTempPosts,
                    onSelect = onSelect,
                    onOpenCard = onOpenCard
                )
            }
        }

        if (tempPostTotalSize == 0) {
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
 */
@Composable
private fun TopToolBar(
    modifier: Modifier = Modifier,
    selectMode: Boolean,
    selectedTempPostsSize: Int,
    tempPostsSize: Int,
) {
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
    }
}

/**
 * 임시저장플립 리스트
 *
 * @param tempPostPaging 임시저장플립 리스트
 * @param selectMode 편집모드(선택모드)
 * @param selectedTempPosts 선택 된 임시저장플립
 * @param onSelect 임시저장플립 선택 시 수행 할 작업
 * @param onOpenCard 임시저장플립 열기
 */
@Composable
private fun TempPostList(
    modifier: Modifier = Modifier,
    tempPostPaging: LazyPagingItems<TempPost>,
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        items(
            count = tempPostPaging.itemCount,
            key = tempPostPaging.itemKey { it.tempPostId }
        ) { idx ->
            val tempPost = tempPostPaging[idx]
            tempPost?.let {
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

        // TODO: 임시
        item {
            val pagingError = tempPostPaging.isError()
            when {
                pagingError != null -> {
                    Text(text = pagingError.asString(), color = Color.Red)
                }

                tempPostPaging.isLoading() -> {
                    FlipLoadingIndicator()
                }

                tempPostPaging.isEndOfPaginationReached() -> {
                    Text(
                        text = "총 \$Total 개",
                        color = Color.Green
                    )
                }
            }
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

/** 선택(체크) 버튼 */
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

private const val SKELETON_ITEM_COUNT = 7

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
    val tempPostPaging = tempPostPagingTestData.collectAsLazyPagingItems()

    FlipAppTheme {
        TempPostList(
            tempPostPaging = tempPostPaging,
            selectMode = true,
            selectedTempPosts = emptyList(),
            onSelect = { tempPost, select -> },
            onOpenCard = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TempFlipBoxScreenPreview() {
    val tempPostPaging = tempPostPagingTestData.collectAsLazyPagingItems()

    FlipAppTheme {
        TempFlipBoxScreen(
            tempPostPaging = tempPostPaging,
            tempPostTotalSize = 10,
            uiState = TempFlipBoxContract.UiState.Idle,
            uiEvent = { },
            isModalVisible = false,
            onBackPress = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TempFlipBoxEmptyScreenPreview() {
    val tempPostPaging = tempPostPagingTestData.collectAsLazyPagingItems()

    FlipAppTheme {
        TempFlipBoxScreen(
            tempPostPaging = tempPostPaging,
            tempPostTotalSize = 10,
            uiState = TempFlipBoxContract.UiState.Idle,
            uiEvent = { },
            isModalVisible = false,
            onBackPress = { }
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun TempFlipBoxScreenPreview2() {
    val tempPostPaging = tempPostPagingTestData.collectAsLazyPagingItems()

    FlipAppTheme {
        TempFlipBoxScreen(
            tempPostPaging = tempPostPaging,
            tempPostTotalSize = 10,
            uiState = TempFlipBoxContract.UiState.Loading,
            uiEvent = { },
            isModalVisible = false,
            onBackPress = { }
        )
    }
}

private val tempPostPagingTestData = flowOf(
    PagingData.from(
        List(15) {
            TempPost(
                it.toLong(),
                "테스트 Title #$it",
                "",
                BackgroundColorType.entries.random(),
                FontStyleType.NORMAL,
                1,
                "일상",
                emptyList(),
                "2024-08-28 17:19:17"
            )
        }
    )
)
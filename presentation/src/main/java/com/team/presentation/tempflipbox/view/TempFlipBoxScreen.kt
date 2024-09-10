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
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.tempflipbox.TempFlipBoxContract
import com.team.presentation.util.asColor

@Composable
fun TempFlipBoxScreen(
    modifier: Modifier = Modifier,
    uiState: TempFlipBoxContract.UiState
) {

    var enableSaveButton by rememberSaveable { mutableStateOf(false) }
    var selectedTempPost by rememberSaveable { mutableStateOf(listOf<TempPost>()) }
    var selectMode by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(!selectMode) {
        selectedTempPost = listOf()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            FlipCenterAlignedTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CommonPaddingValues.TopBarWithTouchTarget),
                title = stringResource(id = R.string.temp_flip_box_screen_topbar_title),
                onBackPress = { },
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
                    onClick = {},
                    enabled = selectedTempPost.isNotEmpty()
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 4.dp, start = 16.dp, end = 16.dp)
        ) {
            when(uiState) {
                TempFlipBoxContract.UiState.Idle -> {
                    TempFlipBoxSkeletonScreen(
                        modifier = Modifier.fillMaxWidth(),
                        itemCount = 7
                    )
                }

                TempFlipBoxContract.UiState.Loading -> {
                    TempFlipBoxSkeletonScreen(
                        modifier = Modifier.fillMaxWidth(),
                        itemCount = 7
                    )
                }

                is TempFlipBoxContract.UiState.TempPosts -> {
                    TempPostsSection(
                        tempPosts = uiState.tempPosts,
                        selectedTempPosts = selectedTempPost,
                        selectMode = selectMode,
                        onSelect = { tempPost, selected ->
                            selectedTempPost = selectedTempPost
                                .toMutableList()
                                .apply {
                                    if (selected) add(tempPost) else remove(tempPost)
                                }
                        },
                        onSelectAll = { allSelected ->
                            selectedTempPost = if (allSelected) uiState.tempPosts else listOf()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TempPostsSection(
    modifier: Modifier = Modifier,
    tempPosts: List<TempPost>,
    selectedTempPosts: List<TempPost>,
    selectMode: Boolean,
    onSelect: (TempPost, Boolean) -> Unit,
    onSelectAll: (Boolean) -> Unit,
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Top)
    ) {
        TopToolBar(
            modifier = Modifier.fillMaxWidth(),
            selectMode = selectMode,
            selectedTempPostsSize = selectedTempPosts.size,
            tempPostsSize = tempPosts.size,
            onSelectAll = { onSelectAll(it) }
        )
        TempPostList(
            modifier = Modifier.fillMaxWidth(),
            tempPosts = tempPosts,
            selectMode = selectMode,
            selectedTempPosts = selectedTempPosts,
            onSelect = onSelect
        )
    }
}

/**
 * @param onSelectAll Boolean 값이 true면, 전체 선택 실행
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
        stringResource(id = R.string.temp_flip_box_screen_card_btn_all_select_off)
    } else { stringResource(id = R.string.temp_flip_box_screen_card_btn_all_select) }
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

@Composable
private fun TempPostList(
    modifier: Modifier = Modifier,
    tempPosts: List<TempPost>,
    selectMode: Boolean,
    selectedTempPosts: List<TempPost>,
    onSelect: (TempPost, Boolean) -> Unit
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
                onSelect = {
                    onSelect(tempPost, !selected)
                }
            )
        }
    }
}

@Composable
private fun TempPostCard(
    modifier: Modifier = Modifier,
    title: String,
    postAt: String,
    bgColorType: BackgroundColorType,
    selectMode: Boolean,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(FlipTheme.shapes.roundedCornerSmall)
            .background(bgColorType.asColor())
            .padding(16.dp),
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
            onSelect = { }
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
            onSelect = { selected = !selected }
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
            onSelect = { selected = !selected }
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
        onSelect = { tempPost, select ->

        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TempFlipBoxScreenPreview() {
    FlipAppTheme {
        TempFlipBoxScreen(
            uiState = TempFlipBoxContract.UiState.TempPosts(tempPostsTestData)
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun TempFlipBoxScreenPreview2() {
    FlipAppTheme {
        TempFlipBoxScreen(
            uiState = TempFlipBoxContract.UiState.Loading
        )
    }
}

private val tempPostsTestData = List(15) {
    TempPost(it.toLong(), "테스트 Title #$it", "", BackgroundColorType.entries.random(), FontStyleType.NORMAL, 1, "일상", emptyList(), "2024-08-28 17:19:17")
}
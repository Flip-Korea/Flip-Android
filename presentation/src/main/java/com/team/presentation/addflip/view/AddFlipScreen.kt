package com.team.presentation.addflip.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipLargeButton
import com.team.designsystem.component.chip.FlipMediumChip
import com.team.designsystem.component.loading.FlipLoadingScreen
import com.team.designsystem.component.topbar.FlipCenterAlignedTopBar
import com.team.designsystem.component.topbar.FlipCenterAlignedTopBarActions
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.component.utils.flipGradient
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.category.Category
import com.team.presentation.R
import com.team.presentation.addflip.state.AddFlipContract
import com.team.presentation.addflip.state.AddPostLoadingType
import com.team.presentation.addflip.state.NewPostState
import com.team.presentation.addflip.state.PostSaveState
import com.team.presentation.common.bottomsheet.FlipModalBottomSheet
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.util.CategoryIconsMap
import com.team.presentation.util.asColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Flip 작성 화면 */
@Composable
fun AddFlipScreen(
    modifier: Modifier = Modifier,
    pageDelete: Boolean,
    uiState: AddFlipContract.UiState,
    onUiEvent: (AddFlipContract.UiEvent) -> Unit,
    onNavigateToTempFlipBox: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var isShowMoreClicked by rememberSaveable { mutableStateOf(false) }
    var enabledSaveButton by remember { mutableStateOf(false) }

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                        isShowMoreClicked = false
                    }
                    detectDragGestures(
                        onDrag = { _, _ ->
                            focusManager.clearFocus()
                            isShowMoreClicked = false
                        },
                    )
                },
        topBar = {
            TopBar(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(CommonPaddingValues.TopBarWithTouchTarget),
                onNavigateToTempFlipBox = onNavigateToTempFlipBox,
                onBackPressed = { onUiEvent(AddFlipContract.UiEvent.SafeNavigateBack) },
            )
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.fillMaxWidth(),
                enableSaveButton = enabledSaveButton,
                onClick = {
                    onUiEvent(
                        AddFlipContract.UiEvent.SavePost(
                            title = (uiState as AddFlipContract.UiState.Content).newPostState.title,
                            contents = uiState.newPostState.contents,
                            bgColorType = uiState.newPostState.bgColorType,
                            category = uiState.newPostState.category,
                        ),
                    )
                },
            )
        },
        containerColor = FlipTheme.colors.white,
    ) { innerPadding ->
        when (uiState) {
            is AddFlipContract.UiState.Content -> {
                IsContentSavable(uiState = uiState) {
                    enabledSaveButton = true
                }

                ContentScreen(
                    modifier =
                        Modifier
                            .consumeWindowInsets(innerPadding)
                            .padding(innerPadding)
                            .imePadding(),
                    pageDelete = pageDelete,
                    focusManager = focusManager,
                    isShowMoreClicked = isShowMoreClicked,
                    showMore = { isShowMoreClicked = !isShowMoreClicked },
                    newPostState = uiState.newPostState,
                    postSaveState = uiState.postSaveState,
                    categories = uiState.categories,
                    onUiEvent = onUiEvent,
                )
            }

            is AddFlipContract.UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    pageDelete: Boolean,
    newPostState: NewPostState,
    postSaveState: PostSaveState,
    categories: List<Category>,
    focusManager: FocusManager,
    isShowMoreClicked: Boolean,
    showMore: () -> Unit,
    onUiEvent: (AddFlipContract.UiEvent) -> Unit,
) {
    var contents by rememberSaveable { mutableStateOf(listOf("")) }
    LaunchedEffect(contents) { onUiEvent(AddFlipContract.UiEvent.OnContentsChanged(contents)) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { contents.size }

    var showCategoryBottomSheet by rememberSaveable { mutableStateOf(false) }
    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var contentTextFieldFocused by rememberSaveable { mutableStateOf(false) }
    val currentContentLength =
        rememberSaveable(pagerState.currentPage, contents) {
            contents
                .getOrNull(
                    pagerState.currentPage.coerceIn(
                        0,
                        contents.lastIndex,
                    ),
                )?.length ?: 0
        }

    LaunchedEffect(pageDelete) {
        if (pageDelete && contents.size > 1) {
            coroutineScope.launch {
                val currentPage = pagerState.currentPage
                val newPage = currentPage.minusPage()
                pagerState.animateScrollToPage(newPage)
                contents = contents.remove(currentPage)
                onUiEvent(AddFlipContract.UiEvent.OnPageDelete(true))
            }
        }
    }

    /** 로딩 화면 */
    LaunchedEffect(postSaveState.tempPostSave, postSaveState.postSave) {
        if (postSaveState.tempPostSave || postSaveState.postSave) {
            onUiEvent(AddFlipContract.UiEvent.NavigateBack)
        }
    }
    FlipLoadingScreen(
        isLoading = postSaveState.loading != AddPostLoadingType.NotLoading,
        text =
            when (postSaveState.loading) {
                AddPostLoadingType.Post -> stringResource(id = R.string.add_flip_screen_post_save)
                AddPostLoadingType.NotLoading ->
                    stringResource(id = R.string.add_flip_screen_not_loading)

                AddPostLoadingType.TempPost ->
                    stringResource(id = R.string.add_flip_screen_temp_post_save)
            },
    )

    /** 분야 선택 바텀 시트 */
    if (showCategoryBottomSheet) {
        SelectCategoryBottomSheet(
            sheetState = categorySheetState,
            categories = categories,
            onSelect = { category ->
                onUiEvent(AddFlipContract.UiEvent.OnCategoryChanged(category))
            },
            onDismissRequest = {
                bottomSheetDismissRequester(
                    coroutineScope = coroutineScope,
                    sheetState = categorySheetState,
                    onDismissRequest = { keyboardController?.hide() },
                    onDismissCompletion = { showCategoryBottomSheet = false },
                )
            },
        )
    }

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        FlipImeDoneToolbarWrapper(onDone = { keyboardController?.hide() }) {
            LazyColumn(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                contentPadding = PaddingValues(vertical = 9.dp),
                state = lazyListState,
            ) {
                item {
                    /** 카테고리 선택 바 */
                    SelectCategoryBar(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = CommonPaddingValues.HorizontalPadding),
                        selectedCategory = newPostState.category,
                        onClick = { showCategoryBottomSheet = true },
                    )

                    /** 제목 입력 텍스트필드 */
                    AddFlipTitleTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 17.dp)
                                .padding(horizontal = CommonPaddingValues.HorizontalPadding),
                        title = newPostState.title,
                        onTitleChanged = { onUiEvent(AddFlipContract.UiEvent.OnTitleChanged(it)) },
                        placeholder =
                            stringResource(id = R.string.add_flip_screen_title_tf_placeholder),
                    )

                    /** 본문 입력 영역 */
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        /** 본문 입력 텍스트 필드 */
                        AddFlipContentSection(
                            modifier = Modifier.fillMaxWidth(),
                            focusManager = focusManager,
                            newPostState = newPostState,
                            pagerState = pagerState,
                            contents = contents,
                            onContentsChanged = { changedContents -> contents = changedContents },
                            onFocusChanged = { contentTextFieldFocused = it },
                        )

                        /** 페이지 카운터, 페이지 추가&삭제 버튼 */
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(PAGE_COUNTER_BAR_PADDING),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            /** 페이지 카운터 */
                            PageCounter(
                                modifier = Modifier.fillMaxWidth(),
                                currentPage = pagerState.currentPage + 1,
                                currentMaxPage = contents.size,
                            )

                            /** 페이지 추가&삭제 버튼 */
                            PageAddDeleteButton(
                                isDeletable = contents.size > 1,
                                isAddable = pagerState.currentPage != MAX_PAGE - 1,
                                onAdd = {
                                    coroutineScope.launch {
                                        val newPageIndex = pagerState.currentPage.addPage()
                                        if (contents.size + 1 <= MAX_PAGE) {
                                            contents = contents.add(newPageIndex)
                                        }
                                        pagerState.animateScrollToPage(newPageIndex)
                                    }
                                },
                                onDelete = {
                                    onUiEvent(AddFlipContract.UiEvent.OnPageDelete(false))
                                },
                            )
                        }
                    }

                    /** 구분 선 */
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = FlipTheme.colors.gray3,
                    )

                    /** 글자 수 도우미 */
                    if (contentTextFieldFocused) {
                        LetterCounterGuide(
                            modifier = Modifier.fillMaxWidth(),
                            length = currentContentLength.toFloat(),
                            limit = MAX_LETTER_LIMIT,
                            progress = currentContentLength.toFloat() / MAX_LETTER_LIMIT,
                        )
                    }

                    /** 배경 컬러 설정 바 */
                    SettingBackgroundColorBar(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = CommonPaddingValues.HorizontalPadding,
                                    vertical = 28.dp,
                                ),
                        selectedColor = newPostState.bgColorType,
                        isShowMoreClicked = isShowMoreClicked,
                        showMore = showMore,
                        onSelectedColor = { color ->
                            onUiEvent(AddFlipContract.UiEvent.OnBackgroundColorChanged(color))
                        },
                    )
                }
            }
        }
    }
}

/**
 * 카테고리 선택 바
 *
 * @param selectedCategory 선택한 카테고리
 * @param onClick 카테고리 선택 바 클릭 시
 */
@Composable
private fun SelectCategoryBar(
    modifier: Modifier = Modifier,
    selectedCategory: Category?,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .clip(CATEGORY_BAR_SHAPE)
                .fillMaxWidth()
                .border(1.dp, FlipTheme.colors.gray5, CATEGORY_BAR_SHAPE)
                .clickableSingle { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(calculateCategoryBarInnerPadding(selectedCategory)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selectedCategory == null) {
                Text(
                    modifier =
                        Modifier
                            .weight(1f)
                            .wrapContentWidth(align = Alignment.Start),
                    text =
                        stringResource(
                            id = R.string.add_flip_screen_select_category_bar_placeholder,
                        ),
                    style = FlipTheme.typography.body5,
                    color = FlipTheme.colors.gray5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CategoryIconsMap[selectedCategory.id]?.let { iconRes ->
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = ImageVector.vectorResource(iconRes),
                            contentDescription = null,
                            tint = FlipTheme.colors.main,
                        )
                    }
                    Text(text = selectedCategory.name, style = FlipTheme.typography.headline1)
                }
            }

            Icon(
                modifier =
                    Modifier
                        .weight(1f)
                        .wrapContentWidth(align = Alignment.End)
                        .size(7.dp, 14.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription =
                    stringResource(id = R.string.add_flip_screen_content_desc_select_category),
                tint = FlipTheme.colors.gray7,
            )
        }
    }
}

/** 본문 입력 텍스트 필드 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddFlipContentSection(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    newPostState: NewPostState,
    pagerState: PagerState,
    contents: List<String>,
    onContentsChanged: (List<String>) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = true,
        contentPadding = PaddingValues(horizontal = CommonPaddingValues.HorizontalPadding),
        pageSpacing = CommonPaddingValues.HorizontalPadding,
        verticalAlignment = Alignment.Top,
    ) { page ->
        AddFlipContentTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .flipGradient(color = newPostState.bgColorType.asColor()),
            focusManager = focusManager,
            placeholder = stringResource(id = R.string.add_flip_screen_content_tf_placeholder),
            content = contents[page],
            onContentChanged = {
                onContentsChanged(contents.toMutableList().apply { this[page] = it })
            },
            onFocusChanged = onFocusChanged,
        )
    }
}

/** 페이지 카운터 */
@Composable
private fun PageCounter(
    modifier: Modifier = Modifier,
    currentPage: Int,
    currentMaxPage: Int,
) {
    Text(
        text =
            buildAnnotatedString {
                withStyle(SpanStyle(color = FlipTheme.colors.point)) { append("$currentPage") }
                append("/$currentMaxPage")
            },
        style = FlipTheme.typography.body3,
        color = FlipTheme.colors.gray6,
    )
}

/** 페이지 추가&삭제 버튼 */
@Composable
private fun PageAddDeleteButton(
    modifier: Modifier = Modifier,
    isDeletable: Boolean,
    isAddable: Boolean,
    onAdd: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        if (isAddable) {
            Row(modifier = Modifier.clickableSingleWithoutRipple { onAdd() }) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_plus_small),
                    contentDescription =
                        stringResource(id = R.string.add_flip_screen_content_desc_add_content),
                    tint = FlipTheme.colors.gray4,
                )
                Text(
                    text = stringResource(id = R.string.add_flip_screen_add_page_btn),
                    style = FlipTheme.typography.body3,
                    color = FlipTheme.colors.gray4,
                )
            }
        }

        Spacer(
            modifier =
                Modifier
                    .padding(vertical = 7.dp)
                    .size(width = 8.dp, height = 13.5.dp),
        )

        if (isDeletable) {
            Icon(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(FlipTheme.colors.gray2)
                        .clickableSingle { onDelete() }
                        .padding(7.dp)
                        .size(12.dp, 13.5.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_trash),
                contentDescription =
                    stringResource(id = R.string.add_flip_screen_content_desc_delete),
            )
        }
    }
}

/** 카테고리 선택 바텀시트 (메인) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectCategoryBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    categories: List<Category>,
    onSelect: (Category) -> Unit,
    onDismissRequest: () -> Unit,
) {
    FlipModalBottomSheet(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) { bottomSheetModifier ->
        SelectCategoryBottomSheetContent(
            modifier =
                bottomSheetModifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 89.dp),
            categories = categories,
            onSelect = { category ->
                onSelect(category)
                onDismissRequest()
            },
        )
    }
}

/** 카테고리 선택 바텀시트 (내부) */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectCategoryBottomSheetContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onSelect: (Category) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.Top),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_setting),
                contentDescription =
                    stringResource(id = R.string.add_flip_screen_content_desc_select_category),
                tint = FlipTheme.colors.main,
            )
            Text(
                text = stringResource(id = R.string.add_flip_screen_select_bottom_sheet_title),
                style = FlipTheme.typography.headline3,
            )
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            categories.forEach { category ->
                FlipMediumChip(
                    text = category.name,
                    icon = CategoryIconsMap[category.id],
                    onClick = { onSelect(category) },
                    solid = false,
                )
            }
        }
    }
}

/**
 * 바텀시트를 안전하게 즉, 애니메이션과 함께 닫히게 하기 위한 함수
 *
 * @param coroutineScope CoroutineScope
 * @param sheetState Sheet State
 * @param onDismissRequest CoroutineScope 에서 실행할 작업
 * @param onDismissCompletion [onDismissRequest] 이후 실행할 작업
 */
@OptIn(ExperimentalMaterial3Api::class)
private fun bottomSheetDismissRequester(
    coroutineScope: CoroutineScope,
    sheetState: SheetState,
    onDismissRequest: () -> Unit = {},
    onDismissCompletion: () -> Unit,
) {
    onDismissRequest()

    coroutineScope
        .launch { sheetState.hide() }
        .invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissCompletion()
            }
        }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onNavigateToTempFlipBox: () -> Unit,
    onBackPressed: () -> Unit,
) {
    FlipCenterAlignedTopBar(
        modifier = modifier,
        actions = FlipCenterAlignedTopBarActions.CLOSE,
        onAction = { onBackPressed() },
        title = stringResource(id = R.string.add_flip_screen_topbar_title),
        options = {
            Text(
                modifier =
                    Modifier
                        .clickableSingleWithoutRipple { onNavigateToTempFlipBox() }
                        .padding(10.dp),
                text = stringResource(id = R.string.add_flip_screen_topbar_btn),
                style = FlipTheme.typography.body6,
                color = FlipTheme.colors.gray5,
            )
        },
    )
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    enableSaveButton: Boolean,
    onClick: () -> Unit,
) {
    FlipLargeButton(
        modifier = modifier,
        text = stringResource(id = R.string.add_flip_screen_bottom_btn),
        enabled = enableSaveButton,
        onClick = onClick,
    )
}

@Composable
fun IsContentSavable(
    uiState: AddFlipContract.UiState.Content,
    block: () -> Unit,
) {
    LaunchedEffect(uiState.newPostState) {
        val title = uiState.newPostState.title
        val contents = uiState.newPostState.contents
        val category = uiState.newPostState.category
        if (
            title.isNotEmpty() &&
            contents.any { it.isNotEmpty() } &&
            category != null
        ) {
            block()
        }
    }
}

/** 플립을 작성할 수 있는 최대 페이지 수 */
private const val MAX_PAGE = 3

/** 플립 작성 시 권장하는 글자 수 */
private const val MAX_LETTER_LIMIT = 300f
private val CATEGORY_BAR_SHAPE = RoundedCornerShape(50.dp)

private fun calculateCategoryBarInnerPadding(selectedCategory: Category?): PaddingValues =
    PaddingValues(
        horizontal = 20.dp,
        vertical = if (selectedCategory == null) 8.dp else 6.dp,
    )

private val PAGE_COUNTER_BAR_PADDING =
    PaddingValues(
        horizontal = CommonPaddingValues.HorizontalPadding,
        vertical = 10.dp,
    )

private fun List<String>.add(newPageIndex: Int): List<String> =
    this.toMutableList().apply {
        add(newPageIndex, "")
    }

private fun List<String>.remove(currentPage: Int): List<String> =
    this.toMutableList().apply {
        removeAt(currentPage)
    }

private fun Int.addPage(): Int = (this + 1).coerceAtMost(MAX_PAGE)

private fun Int.minusPage(): Int = (this - 1).coerceAtLeast(0)

@Preview
@Composable
private fun AddFlipScreenPreview() {
    FlipAppTheme {
        AddFlipScreen(
            pageDelete = false,
            uiState = AddFlipContract.UiState.Content(),
            onUiEvent = { },
            onNavigateToTempFlipBox = { },
        )
    }
}

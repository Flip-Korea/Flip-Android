package com.team.presentation.addflip.view

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
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
import com.team.designsystem.component.modal.FlipModal
import com.team.designsystem.component.modal.FlipModalWrapper
import com.team.designsystem.component.topbar.FlipCenterAlignedTopBar
import com.team.designsystem.component.topbar.FlipCenterAlignedTopBarActions
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.component.utils.flipGradient
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.asString
import com.team.presentation.R
import com.team.presentation.addflip.AddFlipUiEvent
import com.team.presentation.addflip.state.AddPostState
import com.team.presentation.addflip.state.AddTempPostState
import com.team.presentation.addflip.state.CategoriesState
import com.team.presentation.addflip.state.NewPostState
import com.team.presentation.common.bottomsheet.FlipModalBottomSheet
import com.team.presentation.common.state.ModalState
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.util.CategoriesTestData
import com.team.presentation.util.CategoryIconsMap
import com.team.presentation.util.asColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Flip 작성 화면
 *
 * @param newPostState [NewPostState] 작성 중인 글 상태 값
 * @param categoriesState [CategoriesState] 전체 카테고리
 * @param addPostState [AddPostState] 저장, 임시저장 상태
 * @param modalState [ModalState]
 * @param selectedCategory 선택된 카테고리
 * @param onUiEvent AddFlipScreen 의 UiEvent
 * @param hideModal 모달 숨기기 & 뒤로가기
 * @param onBackPress 뒤로가기 시
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun AddFlipScreen(
    modifier: Modifier = Modifier,
    newPostState: NewPostState,
    categoriesState: CategoriesState,
    addPostState: AddPostState,
    addTempPostState: AddTempPostState,
    modalState: ModalState,
    selectedCategory: Category?,
    onUiEvent: (AddFlipUiEvent) -> Unit,
    hideModal: () -> Unit,
    onBackPress: () -> Unit,
    onNavigateToTempFlipBox: () -> Unit
) {
    //TODO: 키보드 포커싱 처리 좀 더 수정하기

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    var contents by rememberSaveable { mutableStateOf(listOf("")) }
    LaunchedEffect(contents) {
        onUiEvent(AddFlipUiEvent.OnContentsChanged(contents))
    }

    val pagerState = rememberPagerState { contents.size }
    val currentContent = rememberSaveable(pagerState.currentPage, contents) {
        contents.getOrNull(pagerState.currentPage.coerceIn(0, contents.lastIndex)) ?: ""
    }

    var showCategoryBottomSheet by remember { mutableStateOf(false) }
    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val focusManager = LocalFocusManager.current
//    val focusRequester = remember { FocusRequester() }

    var contentTextFieldFocused by rememberSaveable { mutableStateOf(false) }

    var isShowMoreClicked by remember { mutableStateOf(false) }

    var enableSaveButton by remember { mutableStateOf(false) }
    LaunchedEffect(selectedCategory, newPostState.title, contents) {
        enableSaveButton =
            selectedCategory != null &&
                    newPostState.title.isNotEmpty() &&
                    contents.filter { it.isEmpty() }.isEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    /** 뒤로가기 핸들러 */
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressed by rememberSaveable { mutableStateOf(false) }
    var isModalVisible by rememberSaveable { mutableStateOf(false) }
    BackHandler {
        onUiEvent(AddFlipUiEvent.OnSafeSave(newPostState.title, contents))
    }
    LaunchedEffect(modalState) {
        when (modalState) {
            ModalState.Idle -> { isModalVisible = false }
            ModalState.Hide -> { isModalVisible = false }
            is ModalState.Display -> {
                if (modalState.showed) {
                    isModalVisible = true
                } else onBackPress()
            }
        }
    }

    /** 임시저장 경고모달 */
    FlipModalWrapper(
        isOpen = isModalVisible,
        onDismissRequest = { hideModal() },
        onAnimationFinished = { if (backPressed) onBackPress() }
    ) {
        FlipModal(
            mainTitle = stringResource(id = R.string.add_flip_screen_modal_main_title),
            subTitle = stringResource(id = R.string.add_flip_screen_modal_sub_title),
            itemText = stringResource(id = R.string.add_flip_screen_modal_item_2),
            itemText2 = stringResource(id = R.string.add_flip_screen_modal_item_1),
            itemText3 = stringResource(id = R.string.add_flip_screen_modal_item_3),
            onItemClick = {
                onUiEvent(AddFlipUiEvent.OnSaveTempPost(newPostState.title, contents, newPostState.bgColorType, newPostState.tags))
                backPressed = true
                hideModal()
            },
            onItem2Click = {
                backPressed = true
                hideModal()
            },
            onItem3Click = {
                hideModal()
            }
        )
    }

    /** 로딩 화면 */
    LaunchedEffect(addTempPostState) {
        if (addTempPostState.tempPostSave) {
            onBackPress()
        }
    }
    FlipLoadingScreen(isLoading = addTempPostState.loading, text = "임시 저장 중")

    /** 분야 선택 바텀 시트 */
    if (showCategoryBottomSheet) {
        SelectCategoryBottomSheet(
            sheetState = categorySheetState,
            categories = categoriesState.categories,
            onSelect = { category -> onUiEvent(AddFlipUiEvent.OnSelectedCategoryChanged(category)) },
            onDismissRequest = {
                dismissRequester(
                    coroutineScope = coroutineScope,
                    sheetState = categorySheetState,
                    onDismissRequest = { keyboardController?.hide() },
                    onDismissCompletion = { showCategoryBottomSheet = false }
                )
            }
        )
    }

    /** 메인 컨텐츠 */
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    isShowMoreClicked = false
                }
                detectDragGestures(
                    onDrag = { change, offset ->
                        focusManager.clearFocus()
                        isShowMoreClicked = false
                    }
                )
            },
        topBar = {
            FlipCenterAlignedTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CommonPaddingValues.TopBarWithTouchTarget),
                actions = FlipCenterAlignedTopBarActions.CLOSE,
                onAction = { onBackPressedDispatcher?.onBackPressed() },
                title = stringResource(id = R.string.add_flip_screen_topbar_title),
                options = {
                    Text(
                        modifier = Modifier
                            .clickableSingleWithoutRipple { onNavigateToTempFlipBox() }
                            .padding(10.dp),
                        text = stringResource(id = R.string.add_flip_screen_topbar_btn),
                        style = FlipTheme.typography.body6,
                        color = FlipTheme.colors.gray5
                    )
                }
            )
        },
        bottomBar = {
            FlipLargeButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.add_flip_screen_bottom_btn),
                enabled = enableSaveButton,
                onClick = {
                    onUiEvent(AddFlipUiEvent.OnSavePost(newPostState.title, contents, newPostState.bgColorType, newPostState.tags))
                }
            )
        },
        containerColor = FlipTheme.colors.white,
    ) { innerPadding ->

        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            FlipImeDoneToolbarWrapper(onDone = {
                keyboardController?.hide()
            }) {
                LazyColumn(
                    modifier = Modifier
                        .consumeWindowInsets(innerPadding)
                        .padding(innerPadding)
                        .imePadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    contentPadding = PaddingValues(vertical = 9.dp),
                    state = lazyListState,
                ) {
                    item {
                        /** 카테고리 선택 바 */
                        SelectCategoryBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = CommonPaddingValues.HorizontalPadding),
                            selectedCategory = selectedCategory,
                            onClick = { showCategoryBottomSheet = true }
                        )

                        /** 제목 입력 텍스트필드 */
                        AddFlipTitleTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 17.dp)
                                .padding(horizontal = CommonPaddingValues.HorizontalPadding),
                            title = newPostState.title,
                            onTitleChanged = { onUiEvent(AddFlipUiEvent.OnTitleChanged(it)) },
                            placeholder = stringResource(id = R.string.add_flip_screen_title_tf_placeholder),
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            /** 본문 입력 텍스트 필드 */
                            AddFlipContentSection(
                                modifier = Modifier.fillMaxWidth(),
                                focusManager = focusManager,
                                newPostState = newPostState,
                                pagerState = pagerState,
                                contents = contents,
                                onContentsChanged = { changedContents -> contents = changedContents },
                                onFocusChanged = { contentTextFieldFocused = it }
                            )

                            /** 페이지 카운터, 페이지 추가&삭제 버튼 */
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = CommonPaddingValues.HorizontalPadding,
                                        vertical = 12.dp
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                /** 페이지 카운터 */
                                PageCounter(
                                    modifier = Modifier.fillMaxWidth(),
                                    currentPage = pagerState.currentPage + 1,
                                    currentMaxPage = contents.size,
                                    onClick = {
                                        if (contents.size > 1) {
                                            coroutineScope.launch {
                                                val currentPage = pagerState.currentPage

                                                val newPage = (currentPage - 1).coerceAtLeast(0)
                                                pagerState.animateScrollToPage(newPage)

                                                contents = contents.toMutableList().apply {
                                                    removeAt(currentPage)
                                                }
                                            }
                                        }
                                    }
                                )

                                /** 페이지 추가&삭제 버튼 */
                                if (pagerState.currentPage != MAX_PAGE) {
                                    //TODO: 삭제버튼 위치 미정
                                    PageAddDeleteButton(
                                        isDeletable = false,
                                        onAdd = {
                                            coroutineScope.launch {
                                                val newPageIndex = pagerState.currentPage + 1

                                                if (contents.size + 1 <= MAX_PAGE) {
                                                    contents = contents
                                                        .toMutableList()
                                                        .apply { add(newPageIndex, "") }
                                                }

                                                pagerState.animateScrollToPage(newPageIndex)
                                            }
                                        },
                                        onDelete = {
                                            if (contents.size > 1) {
                                                coroutineScope.launch {
                                                    val currentPage = pagerState.currentPage

                                                    val newPage = (currentPage - 1).coerceAtLeast(0)
                                                    pagerState.animateScrollToPage(newPage)

                                                    contents = contents.toMutableList().apply {
                                                        removeAt(currentPage)
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        /** 구분 선 */
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = FlipTheme.colors.gray3
                        )

                        /** 글자 수 도우미 */
                        if (contentTextFieldFocused) {
                            LetterCounterGuide(
                                modifier = Modifier.fillMaxWidth(),
                                length = currentContent.length.toFloat(),
                                limit = MAX_LETTER_LIMIT,
                                progress = currentContent.length.toFloat() / MAX_LETTER_LIMIT
                            )
                        }

                        /** 배경 컬러 설정 바 */
                        SettingBackgroundColor(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = CommonPaddingValues.HorizontalPadding,
                                    vertical = 28.dp
                                ),
                            selectedColor = newPostState.bgColorType,
                            isShowMoreClicked = isShowMoreClicked,
                            showMore = { isShowMoreClicked = !isShowMoreClicked },
                            onSelectedColor = { color -> onUiEvent(AddFlipUiEvent.OnBackgroundColorChanged(color)) }
                        )
                    }
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
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .fillMaxWidth()
            .border(1.dp, FlipTheme.colors.gray5, RoundedCornerShape(50.dp))
            .clickableSingle { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = if (selectedCategory == null) 8.dp else 6.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedCategory == null) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(align = Alignment.Start),
                    text = stringResource(id = R.string.add_flip_screen_select_category_bar_placeholder),
                    style = FlipTheme.typography.body5,
                    color = FlipTheme.colors.gray5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryIconsMap[selectedCategory.id]?.let { iconRes ->
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = ImageVector.vectorResource(iconRes),
                            contentDescription = null,
                            tint = FlipTheme.colors.main
                        )
                    }
                    Text(
                        text = selectedCategory.name,
                        style = FlipTheme.typography.headline1,
                    )
                }
            }

            Icon(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.End)
                    .size(7.dp, 14.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription = stringResource(id = R.string.add_flip_screen_content_desc_select_category),
                tint = FlipTheme.colors.gray7
            )
        }
    }
}

/**
 * 카테고리 선택 바텀시트 (메인)
 */
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
            modifier = bottomSheetModifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 89.dp),
            categories = categories,
            onSelect = { category ->
                onSelect(category)
                onDismissRequest()
            }
        )
    }
}

/**
 * 카테고리 선택 바텀시트 (내부)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectCategoryBottomSheetContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onSelect: (Category) -> Unit,
) {

    var isTap by remember { mutableIntStateOf(-1) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.Top)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_setting),
                contentDescription = stringResource(id = R.string.add_flip_screen_content_desc_select_category),
                tint = FlipTheme.colors.main
            )
            Text(
                text = stringResource(id = R.string.add_flip_screen_select_bottom_sheet_title),
                style = FlipTheme.typography.headline3
            )
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            categories.forEach { category ->
                FlipMediumChip(
                    modifier = Modifier.pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                                when {
                                    event.changes.any { it.changedToDown() } -> {
                                        isTap = category.id
                                    }
                                    event.changes.any { it.changedToUp() } -> {
                                        isTap = -1
                                    }
                                }
                            }
                        }
                    },
                    text = category.name,
                    icon = CategoryIconsMap[category.id],
                    onClick = { onSelect(category) },
                    solid = isTap == category.id
                )
            }
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
        verticalAlignment = Alignment.Top
    ) { page ->

        AddFlipContentTextField(
            modifier = Modifier
                .fillMaxWidth()
                .flipGradient(color = newPostState.bgColorType.asColor())
                .padding(start = 8.dp, end = 8.dp, top = 8.dp),
            focusManager = focusManager,
            placeholder = stringResource(id = R.string.add_flip_screen_content_tf_placeholder),
            content = contents[page],
            onContentChanged = {
                onContentsChanged(
                    contents.toMutableList().apply {
                        this[page] = it
                    }
                )
            },
            onFocusChanged = onFocusChanged
        )
    }
}

/** 페이지 카운터 */
@Composable
private fun PageCounter(
    modifier: Modifier = Modifier,
    currentPage: Int,
    currentMaxPage: Int,
    onClick: () -> Unit
) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = FlipTheme.colors.point)) {
                append("$currentPage")
            }
            append("/$currentMaxPage")
        },
        style = FlipTheme.typography.body3,
        color = FlipTheme.colors.gray6
    )
}

/** 페이지 추가&삭제 버튼 */
@Composable
private fun PageAddDeleteButton(
    modifier: Modifier = Modifier,
    isDeletable: Boolean,
    onAdd: () -> Unit,
    onDelete: () -> Unit
) {

    if (isDeletable) {
        //TODO: 색상, 폰트 미정
        Text(
            modifier = Modifier.clickableSingleWithoutRipple { onDelete() },
            text = stringResource(id = R.string.add_flip_screen_delete_page_btn),
            style = FlipTheme.typography.body3,
            color = FlipTheme.colors.gray4
        )
    } else {
        Row(
            modifier = modifier.clickableSingleWithoutRipple { onAdd() },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.sizeIn(maxWidth = 18.dp, maxHeight = 18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_plus_small),
                contentDescription = stringResource(id = R.string.add_flip_screen_content_desc_add_content),
                tint = FlipTheme.colors.gray4
            )
            Text(
                text = stringResource(id = R.string.add_flip_screen_add_page_btn),
                style = FlipTheme.typography.body3,
                color = FlipTheme.colors.gray4
            )
        }
    }
}

/**
 * 배경 컬러 설정 바
 *
 * @param selectedColor 선택된 컬러타입
 * @param isShowMoreClicked 색상 더보기 클릭 여부
 * @param showMore 색상 더보기 클릭 시
 * @param onSelectedColor 색상 선택 시
 */
@Composable
private fun SettingBackgroundColor(
    modifier: Modifier = Modifier,
    selectedColor: BackgroundColorType,
    isShowMoreClicked: Boolean,
    showMore: () -> Unit,
    onSelectedColor: (BackgroundColorType) -> Unit,
) {

    val animateRotateValue = animateFloatAsState(
        targetValue = if (isShowMoreClicked) 90f else 0f,
        label = ""
    )

    Row(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_background_color),
                contentDescription = stringResource(id = R.string.add_flip_screen_content_desc_setting_background_color),
                tint = FlipTheme.colors.main
            )
            Text(
                text = stringResource(id = R.string.add_flip_screen_setting_background_color),
                style = FlipTheme.typography.body6,
                maxLines = 1
            )
        }

        Row(
            modifier = Modifier
                .weight(2f)
                .wrapContentWidth(Alignment.End)
                .clickableSingleWithoutRipple { showMore() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End)
        ) animatedRow@ {
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                this@animatedRow.AnimatedVisibility(
                    modifier = Modifier.wrapContentSize(),
                    visible = isShowMoreClicked,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    BackgroundColorOptions(
                        selectedColor = selectedColor,
                        onSelectedColor = { selectedColor ->
                            onSelectedColor(selectedColor)
                            showMore()
                        },
                    )
                }
                this@animatedRow.AnimatedVisibility(
                    modifier = Modifier.wrapContentSize(),
                    visible = !isShowMoreClicked,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    BackgroundColorOptionDisplay(selectedColor = selectedColor)
                }
            }

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        rotationZ = animateRotateValue.value
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { showMore() }
                    ),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription = stringResource(id = R.string.add_flip_screen_content_desc_show_more),
                tint = FlipTheme.colors.gray5
            )
        }
    }
}

/** 배경 컬러 설정 바(선택 옵션) */
@Composable
private fun BackgroundColorOptions(
    modifier: Modifier = Modifier,
    selectedColor: BackgroundColorType,
    onSelectedColor: (BackgroundColorType) -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BackgroundColorType.entries.forEach { color ->

            val itemModifier = if (selectedColor == color) {
                Modifier
                    .clip(CircleShape)
                    .background(FlipTheme.colors.point3)
                    .border(1.dp, FlipTheme.colors.point, CircleShape)
            } else Modifier

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .then(itemModifier)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(16.dp)
                        .border(1.dp, Color(0xFF212121), CircleShape)
                        .clickableSingleWithoutRipple { onSelectedColor(color) },
                    imageVector = Icons.Default.Circle,
                    contentDescription = "bg-color",
                    tint = color.asColor()
                )
            }
        }
    }
}

/** 선택된 배경 컬러 표시 부분 */
@Composable
private fun BackgroundColorOptionDisplay(
    modifier: Modifier = Modifier,
    selectedColor: BackgroundColorType
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(16.dp)
                .border(1.dp, Color(0xFF212121), CircleShape),
            imageVector = Icons.Default.Circle,
            contentDescription = null,
            tint = selectedColor.asColor()
        )
        Text(
            text = selectedColor.asString(),
            style = FlipTheme.typography.body5,
            color = FlipTheme.colors.gray5
        )
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
private fun dismissRequester(
    coroutineScope: CoroutineScope,
    sheetState: SheetState,
    onDismissRequest: () -> Unit = {},
    onDismissCompletion: () -> Unit
) {
    onDismissRequest()

    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
        if (!sheetState.isVisible) {
            onDismissCompletion()
        }
    }
}

/** 플립을 작성할 수 있는 최대 페이지 수 */
private const val MAX_PAGE = 3
/** 플립 작성 시 권장하는 글자 수 */
private const val MAX_LETTER_LIMIT = 300f

@Preview(showBackground = true)
@Composable
private fun SelectCategoryBarPreview() {

    FlipAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            SelectCategoryBar(
                selectedCategory = null,
                onClick = { }
            )
            SelectCategoryBar(
                selectedCategory = Category(6, "예술/문화"),
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectCategoryBottomSheetContentPreview() {

    FlipAppTheme {
        SelectCategoryBottomSheetContent(
            categories = CategoriesTestData,
            onSelect = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingBackgroundColorPreview() {

    var isShowMoreClicked by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(BackgroundColorType.DEFAULT) }

    FlipAppTheme {
        SettingBackgroundColor(
            selectedColor = selectedColor,
            isShowMoreClicked = isShowMoreClicked,
            showMore = { isShowMoreClicked = !isShowMoreClicked },
            onSelectedColor = { color -> selectedColor = color },
        )
    }
}

@Preview
@Composable
private fun AddFlipScreenPreview() {

    FlipAppTheme {
        AddFlipScreen(
            categoriesState = CategoriesState(categories = CategoriesTestData),
            newPostState = NewPostState(),
            addPostState = AddPostState(),
            addTempPostState = AddTempPostState(),
            modalState = ModalState.Idle,
            selectedCategory = null,
            hideModal = { },
            onUiEvent = { },
            onBackPress = { },
            onNavigateToTempFlipBox = { }
        )
    }
}

@Preview(heightDp = 400)
@Composable
private fun AddFlipScreenPreview2() {

    FlipAppTheme {
        AddFlipScreen(
            categoriesState = CategoriesState(categories = CategoriesTestData),
            newPostState = NewPostState(),
            addPostState = AddPostState(),
            addTempPostState = AddTempPostState(),
            modalState = ModalState.Idle,
            selectedCategory = null,
            hideModal = { },
            onUiEvent = { },
            onBackPress = { },
            onNavigateToTempFlipBox = { }
        )
    }
}
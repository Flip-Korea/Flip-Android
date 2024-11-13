package com.team.presentation.editcategories.view

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.component.chip.FlipMediumChip
import com.team.designsystem.component.topbar.FlipTopBar
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.component.utils.disableMultiTouch
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.category.Category
import com.team.presentation.R
import com.team.presentation.common.speechbubble.SpeechBubbleView
import com.team.presentation.common.speechbubble.SpeechBubbleWrapper
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.editcategories.EditMyCategoriesUiEvent
import com.team.presentation.editcategories.state.MyCategoriesState
import com.team.presentation.editcategories.state.MyCategoriesUpdateState
import com.team.presentation.editcategories.util.DragDirection
import com.team.presentation.editcategories.util.dragContainer
import com.team.presentation.editcategories.util.draggableItems
import com.team.presentation.editcategories.util.rememberDragAndDropState
import com.team.presentation.util.CategoriesTestData
import com.team.presentation.util.CategoryIconsMap

@Composable
fun EditMyCategoriesScreen(
    modifier: Modifier = Modifier,
    nickname: String,
    myCategoriesState: MyCategoriesState,
    speechBubbleState: Boolean,
    myCategoriesUpdateState: MyCategoriesUpdateState,
    uiEvent: (EditMyCategoriesUiEvent) -> Unit,
    updateMyCategories: (List<Category>) -> Unit,
    onBackPress: () -> Unit
) {

    var speechBubbleShowed by remember { mutableStateOf(true) }
    var selectedCategoriesHeight by remember { mutableIntStateOf(0) }
    var tipStartOffset by remember { mutableStateOf(IntOffset(10, 0)) }

    SpeechBubbleWrapper(
        enabled = speechBubbleState,
        showed = speechBubbleShowed,
        onDismissRequest = { speechBubbleShowed = false },
        tipStartOffset = tipStartOffset,
        speechBubbleView = {
            SpeechBubbleView(
                modifier = it.padding(Horizontal),
                containerColor = FlipTheme.colors.main.copy(.9f),
                text = stringResource(id = R.string.edit_interest_categories_speechbubble)
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .disableMultiTouch(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FlipTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = CommonPaddingValues.TopBarVertical),
                title = stringResource(id = R.string.edit_interest_categories_top_bar_title),
                onBackPress = {
                    speechBubbleShowed = false
                    onBackPress()
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(ContentVertical),
                verticalArrangement = Arrangement.spacedBy(27.dp, alignment = Alignment.Top)
            ) {
                TitleSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Horizontal),
                    nickname = nickname,
                    myCategoriesSize = myCategoriesState.myCategories.size
                )
                SelectedCategoriesSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { selectedCategoriesHeight = it.height }
                        .onGloballyPositioned { coordinates ->
                            val position = coordinates
                                .positionInWindow()
                                .round()
                            tipStartOffset =
                                tipStartOffset.copy(y = position.y + selectedCategoriesHeight)
                        },
                    myCategories = myCategoriesState.myCategories,
                    onChangedMyCategories = { uiEvent(EditMyCategoriesUiEvent.MoveCategory(it)) },
                    onUnSelect = { category -> uiEvent(EditMyCategoriesUiEvent.UnSelectCategory(category)) },
                )
                CategoriesSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Horizontal),
                    categories = myCategoriesState.exclusiveCategories,
                    onSelect = { category -> uiEvent(EditMyCategoriesUiEvent.SelectCategory(category)) },
                    onSelectAll = { uiEvent(EditMyCategoriesUiEvent.SelectAll) }
                )
            }

            FlipMediumButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Horizontal),
                text = stringResource(id = R.string.edit_interest_categories_btn_finish),
                onClick = {
                    updateMyCategories(myCategoriesState.myCategories)
                },
                isLoading = myCategoriesUpdateState.loading
            )
        }
    }

}

@Composable
private fun TitleSection(
    modifier: Modifier = Modifier,
    nickname: String,
    myCategoriesSize: Int
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = FlipTheme.colors.point,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(nickname)
            }
            append(stringResource(id = R.string.edit_interest_categories_title_1))
            append("\n")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("${myCategoriesSize}개")
            }
            append(stringResource(id = R.string.edit_interest_categories_title_2))
        },
        style = FlipTheme.typography.headline7,
        maxLines = 2,
        textAlign = TextAlign.Start
    )
}

@Composable
private fun SelectedCategoriesSection(
    modifier: Modifier = Modifier,
    myCategories: List<Category>,
    onChangedMyCategories: (List<Category>) -> Unit,
    onUnSelect: (Category) -> Unit,
) {

    var reorderedCategories by remember { mutableStateOf(emptyList<Category>()) }
    LaunchedEffect(myCategories) {
        reorderedCategories = myCategories
//        reorderedCategories = List(7) { Category(listId = it+100+1, id = it+1, name = "$it") }
        Log.d("reorderedCategories_Log", "LaunchedEffect(myCategories)")
    }

    val draggableItems by remember { derivedStateOf { reorderedCategories.size } }
    val lazyListState = rememberLazyListState()
    val dragAndDropState = rememberDragAndDropState(
        dragDirection = DragDirection.Horizontal,
        lazyListState = lazyListState,
        onMove = { fromIndex, toIndex ->
            reorderedCategories = reorderedCategories.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
            onChangedMyCategories(reorderedCategories)
        },
        draggableItemsNum = draggableItems
    )

    LazyRow(
        modifier = modifier.dragContainer(dragAndDropState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Start),
        contentPadding = Horizontal,
        state = lazyListState
    ) {
        draggableItems(
            items = reorderedCategories,
            keyProvider = { index, category -> category.id },
            dragAndDropState = dragAndDropState
        ) { modifier, index, category ->

            val selectedModifier = if (dragAndDropState.draggingItemIndex == index) {
                Modifier.alpha(0.5f)
            } else Modifier

            FlipMediumChip(
                modifier = modifier.then(selectedModifier),
                text = category.name,
                icon = CategoryIconsMap[category.id],
                onClick = { onUnSelect(category) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoriesSection(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onSelectAll: () -> Unit,
    onSelect: (Category) -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp, alignment = Alignment.Top),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.CenterStart),
                text = stringResource(id = R.string.edit_interest_categories_list_title_1),
                style = FlipTheme.typography.headline1,
                color = FlipTheme.colors.gray6
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.CenterEnd)
                    .clickableSingle { onSelectAll() },
                text = stringResource(id = R.string.edit_interest_categories_list_title_2),
                style = FlipTheme.typography.body5,
                color = FlipTheme.colors.point
            )
        }
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(15.5.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = categories,
                key = { it.id }
            ) { category ->
                CategoryItem(
                    modifier = Modifier
                        .clip(FlipTheme.shapes.roundedCornerSmall)
                        .animateItemPlacement()
                        .clickableSingle { onSelect(category) },
                    category = category
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category
) {
    Box(
        modifier = modifier
            .width(104.dp)
            .height(76.dp)
            .background(FlipTheme.colors.gray1),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            CategoryIconsMap[category.id]?.let { icon ->
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = category.name,
                    tint = FlipTheme.colors.gray6
                )
            }
            Text(
                text = category.name,
                style = FlipTheme.typography.body5,
                color = FlipTheme.colors.gray6
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EditMyCategoriesScreenPreview() {
    FlipAppTheme {
        EditMyCategoriesScreen(
            modifier = Modifier.fillMaxSize(),
            nickname = "Miffy",
            myCategoriesState = MyCategoriesState(
                exclusiveCategories = CategoriesTestData.filter { !listOf(6,7,8).contains(it.id) },
                myCategories = listOf(
                    Category(106, "예술/문화"),
                    Category(107, "디자인"),
                    Category(108, "컴퓨터/IT"),
                )
            ),
            speechBubbleState = false,
            myCategoriesUpdateState = MyCategoriesUpdateState(),
            uiEvent = {},
            onBackPress = {},
            updateMyCategories = {}
        )
    }
}

/**
 * 중앙 컨텐츠의 Top, Bottom Padding
 */
private val ContentVertical = PaddingValues(vertical = 28.dp)
private val Horizontal = PaddingValues(horizontal = 16.dp)

@Preview(showBackground = true)
@Composable
private fun TitleSectionPreview() {
    FlipAppTheme {
        TitleSection(
            nickname = "Miffy",
            myCategoriesSize = 3
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesSectionPreview() {
    CategoriesSection(
        modifier = Modifier.fillMaxWidth(),
        categories = CategoriesTestData,
        onSelect = {},
        onSelectAll = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    CategoryItem(category = Category(1, "일상"))
}
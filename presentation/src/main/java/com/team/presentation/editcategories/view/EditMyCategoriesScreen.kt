package com.team.presentation.editcategories.view

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.category.Category
import com.team.presentation.R
import com.team.presentation.common.speechbubble.SpeechBubbleView
import com.team.presentation.common.speechbubble.SpeechBubbleWrapper
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.editcategories.state.MyCategoriesState
import com.team.presentation.util.CategoriesTestData
import com.team.presentation.util.CategoryIconsMap

@Composable
fun EditMyCategoriesScreen(
    modifier: Modifier = Modifier,
    nickname: String,
    myCategoriesState: MyCategoriesState,
    onBackPress: () -> Unit
) {

    var speechBubbleShowed by remember { mutableStateOf(true) }
    var selectedCategoriesHeight by remember { mutableIntStateOf(0) }
    var tipStartOffset by remember { mutableStateOf(IntOffset(10, 0)) }

    SpeechBubbleWrapper(
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
            modifier = modifier.fillMaxSize(),
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
                    myCategories = myCategoriesState.myCategories
                )
                SelectedCategoriesSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { selectedCategoriesHeight = it.height }
                        .onGloballyPositioned { coordinates ->
                            val position = coordinates.positionInWindow().round()
                            tipStartOffset = tipStartOffset.copy(y = position.y + selectedCategoriesHeight)
                        },
                    myCategories = myCategoriesState.myCategories,
                    onItemClick = { iCategory ->

                    },
                )
                CategoriesSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Horizontal),
                    categories = myCategoriesState.exclusiveCategories
                )
            }

            FlipMediumButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Horizontal),
                text = stringResource(id = R.string.edit_interest_categories_btn_finish),
                onClick = {
                    //TODO 테스트용 코드, 나중에 반드시 삭제할 것
                    speechBubbleShowed = !speechBubbleShowed
                }
            )
        }
    }

}

@Composable
private fun TitleSection(
    modifier: Modifier = Modifier,
    nickname: String,
    myCategories: List<Category>
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
                append("${myCategories.size}개")
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
    onItemClick: (Category) -> Unit,
) {

    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Start),
        contentPadding = Horizontal
    ) {
        items(
            items = myCategories,
            key = { iCategory -> iCategory.id }
        ) { iCategory ->
            FlipMediumChip(
                text = iCategory.name,
                icon = CategoryIconsMap[iCategory.id],
                onClick = { onItemClick(iCategory) }
            )
        }
    }
}

@Composable
private fun CategoriesSection(
    modifier: Modifier = Modifier,
    categories: List<Category>
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
                    .clickableSingle {  },
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
                        .clickableSingle { },
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
                    Category(6, "예술/문화"),
                    Category(7, "디자인"),
                    Category(8, "컴퓨터/IT"),
                )
            ),
            onBackPress = {}
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
            myCategories = listOf(
                Category(0, ""),
                Category(1, ""),
                Category(2, "")
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesSectionPreview() {
    CategoriesSection(
        modifier = Modifier.fillMaxWidth(),
        categories = CategoriesTestData
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    CategoryItem(category = Category(1, "일상"))
}
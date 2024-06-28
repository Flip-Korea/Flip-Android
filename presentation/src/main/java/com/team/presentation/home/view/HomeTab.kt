package com.team.presentation.home.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.category.Category
import com.team.presentation.home.util.HomeTabMockIcons
import com.team.presentation.home.util.HomeTabMockItems
import kotlinx.coroutines.launch

@Composable
fun HomeTab(
    modifier: Modifier = Modifier,
    items: List<Category>,
    itemSplitSize: Int,
    onItemClick: (Int) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    var selectedItemId by rememberSaveable { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Start),
        contentPadding = PaddingValues(horizontal = 16.dp),
        state = listState
    ) {
        itemsIndexed(
            items = items,
            key = { idx, category -> category.id }
        ) { index, category ->
            TabItem(
                selected = category.id == selectedItemId,
                text = category.name,
                icon = HomeTabMockIcons[category.id],
                onClick = {
                    if (category.id != selectedItemId) {
                        selectedItemId = category.id
                        onItemClick(selectedItemId)
                        coroutineScope.launch {
                            listState.animateScrollToItem(
                                index = if (index - 1 < 0) 0 else index - 1
                            )
                        }
                    }
                }
            )

            if (index == itemSplitSize - 1) {
                VerticalDivider(
                    modifier = Modifier
                        .height(32.dp)
                        .padding(start = 10.dp, end = 2.dp),
                    thickness = 1.dp,
                    color = FlipTheme.colors.gray3
                )
            }
        }
    }
}

@Composable
private fun TabItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: String,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit,
) {

    val rowPadding = if (icon == null) {
        Modifier.padding(horizontal = 16.dp, vertical = 6.5.dp)
    } else {
        Modifier.padding(start = 12.dp, end = 16.dp, top = 5.dp, bottom = 5.dp)
    }

    val contentColor = if (selected) FlipTheme.colors.white else FlipTheme.colors.main

    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(FlipTheme.shapes.roundedCornerSmall)
            .border(1.dp, FlipTheme.colors.main, FlipTheme.shapes.roundedCornerSmall)
            .background(if (selected) FlipTheme.colors.main else FlipTheme.colors.white)
            .clickableSingle { onClick() },
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .then(rowPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            icon?.let { ic ->
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(ic),
                    contentDescription = text,
                    tint = contentColor
                )
            }
            Text(
                modifier = Modifier,
                text = text,
                style = if (icon != null) {
                    FlipTheme.typography.body5
                } else FlipTheme.typography.headline1,
                color = contentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeTabPreview() {
    FlipAppTheme {
        HomeTab(
            modifier = Modifier.fillMaxWidth(),
            items = HomeTabMockItems,
            itemSplitSize = 3,
            onItemClick = { }
        )
    }
}
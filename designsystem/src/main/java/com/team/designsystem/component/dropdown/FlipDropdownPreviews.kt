package com.team.designsystem.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.button.FlipIconButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Preview(showBackground = true)
@Composable
private fun FlipDropdownMenuPreview() {
    val dropDownItems = listOf(
        DropdownItem(0, "Text"),
        DropdownItem(1, "Text"),
        DropdownItem(2, "Text"),
    )

    var expanded by rememberSaveable { mutableStateOf(true) }

    FlipAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FlipDropdownWrapper(
                modifier = Modifier.align(Alignment.TopEnd),
                space = 2.dp,
                button = {
                    FlipIconButton(
                        modifier = Modifier.background(Color.LightGray),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_more),
                        contentDescription = null,
                        tint = FlipTheme.colors.main,
                        onClick = { expanded = true }
                    )
                },
                menu = { modifier, offset ->
                    FlipDropdownMenu(
                        modifier = modifier,
                        expanded = expanded,
                        offset = offset,
                        dropDownItems = dropDownItems,
                        onDismissRequest = { expanded = false },
                        onItemClick = { item ->
                            if (item.id == 1) expanded = false
                        }
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 250)
@Composable
private fun FlipDropdownMenu2Preview() {
    val dropDownItems = listOf(
        DropdownItem(0, "Option 1"),
        DropdownItem(1, "Option 2"),
        DropdownItem(2, "Option 3"),
    )

    var expanded by rememberSaveable { mutableStateOf(true) }

    FlipAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FlipDropdownWrapper(
                modifier = Modifier.align(Alignment.TopEnd),
                button = {
                    FlipIconButton(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_more),
                        contentDescription = null,
                        tint = FlipTheme.colors.main,
                        onClick = { expanded = true }
                    )
                },
                menu = { modifier, offset ->
                    FlipDropdownMenu2(
                        modifier = modifier,
                        expanded = expanded,
                        offset = offset,
                        dropDownItems = dropDownItems,
                        onDismissRequest = { expanded = false },
                        onItemClick = { item ->
                            if (item.id == 1) expanded = false
                        }
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipDropdownButtonPreview() {
    val dropDownItems = listOf(
        DropdownItem(0, "인기순"),
        DropdownItem(1, "최신순"),
        DropdownItem(2, "댓글순"),
        DropdownItem(3, "스크랩순"),
    )

    var expanded by rememberSaveable { mutableStateOf(true) }

    var selectedText by remember { mutableStateOf(dropDownItems[0].text) }

    FlipAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FlipDropdownWrapper(
                modifier = Modifier.align(Alignment.CenterEnd).padding(16.dp),
                space = 4.dp,
                button = {
                    FlipDropdownButton(
                        isSelect = expanded,
                        text = selectedText,
                        onClick = { expanded = true }
                    )
                },
                menu = { modifier, offset ->
                    FlipDropdownMenu2(
                        modifier = modifier,
                        expanded = expanded,
                        offset = offset,
                        dropDownItems = dropDownItems,
                        onDismissRequest = { expanded = false },
                        onItemClick = { item ->
                            selectedText = item.text
                            expanded = false
                        }
                    )
                }
            )
        }
    }
}
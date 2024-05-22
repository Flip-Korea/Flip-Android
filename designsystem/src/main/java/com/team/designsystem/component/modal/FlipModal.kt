package com.team.designsystem.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
private fun ModalButton(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color,
    contentColor: Color,
    accent: Boolean = false,
    onClick: () -> Unit
) {

    val clickableSingle = remember { ClickableSingle.get() }

    Button(
        onClick = { clickableSingle.onEvent(onClick) },
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp),
        shape = FlipTheme.shapes.roundedCornerSmall,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(9.dp)
    ) {
        Text(
            text = text,
            style = if (accent) {
                FlipTheme.typography.headline4
            } else FlipTheme.typography.body6,
            color = contentColor
        )
    }
}

@Composable
fun FlipModal(
    modifier: Modifier = Modifier,
    mainTitle: String,
    subTitle: String? = null,
    itemText: String,
    itemText2: String,
    onItemClick: () -> Unit,
    onItem2Click: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(FlipTheme.shapes.roundedCornerMedium)
            .wrapContentSize()
            .background(FlipTheme.colors.white)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 16.dp, end = 16.dp, top = 21.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp, alignment = Alignment.CenterVertically)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp, alignment = Alignment.CenterVertically)
            ) {
                Text(text = mainTitle, style = FlipTheme.typography.headline4)
                if (subTitle != null) {
                    Text(
                        text = subTitle,
                        style = FlipTheme.typography.body3,
                        color = FlipTheme.colors.gray6
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                ModalButton(
                    modifier = Modifier.weight(1f),
                    text = itemText,
                    containerColor = FlipTheme.colors.point,
                    contentColor = FlipTheme.colors.white,
                    onClick = onItemClick,
                    accent = true
                )
                ModalButton(
                    modifier = Modifier.weight(1f),
                    text = itemText2,
                    containerColor = FlipTheme.colors.gray1,
                    contentColor = FlipTheme.colors.main,
                    onClick = onItem2Click
                )
            }
        }
    }
}

@Preview
@Composable
private fun ModalButtonPreview() {
    FlipAppTheme {
        ModalButton(
            text = "Text",
            containerColor = FlipTheme.colors.point,
            contentColor = FlipTheme.colors.white,
            onClick = {}
        )
    }
}

@Preview(name = "Medium", showBackground = true)
@Composable
fun FlipMediumModalPreview() {
    FlipAppTheme {
        FlipModal(
            modifier = Modifier.size(290.dp, 195.dp),
            mainTitle = "Main Title",
            subTitle = "sub title",
            itemText = "Text",
            itemText2 = "Text",
            onItemClick = { },
            onItem2Click = { }
        )
    }
}

@Preview(name = "Medium(sub title X)", showBackground = true)
@Composable
fun FlipMediumModalWithoutSubTitlePreview() {
    FlipAppTheme {
        FlipModal(
            modifier = Modifier.size(290.dp, 195.dp),
            mainTitle = "Main Title",
            itemText = "Text",
            itemText2 = "Text",
            onItemClick = { },
            onItem2Click = { }
        )
    }
}

@Preview(name = "Small", showBackground = true)
@Composable
fun FlipSmallModalPreview() {
    FlipAppTheme {
        FlipModal(
            modifier = Modifier.size(262.dp, 195.dp),
            mainTitle = "Main Title",
            subTitle = "sub title",
            itemText = "Text",
            itemText2 = "Text",
            onItemClick = { },
            onItem2Click = { }
        )
    }
}

@Preview(name = "Small(sub title x)", showBackground = true)
@Composable
fun FlipMediumModalWithoutSubTitlePreview2() {
    FlipAppTheme {
        FlipModal(
            modifier = Modifier.size(262.dp, 195.dp),
            mainTitle = "Main Title",
            itemText = "Text",
            itemText2 = "Text",
            onItemClick = { },
            onItem2Click = { }
        )
    }
}

@Preview(name = "Interaction-Medium", showBackground = true, widthDp = 300, heightDp = 300)
@Composable
private fun FlipMediumModalPreview2() {

    var isOpen by remember { mutableStateOf(false) }

    FlipAppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FlipModalWrapper(isOpen = isOpen, onDismissRequest = { isOpen = false }) {
                FlipModal(
                    modifier = Modifier.size(290.dp, 195.dp),
                    mainTitle = "Main Title",
                    subTitle = "sub title",
                    itemText = "Text",
                    itemText2 = "Text",
                    onItemClick = { isOpen = false },
                    onItem2Click = { isOpen = false }
                )
            }

            Button(onClick = { isOpen = true }) {
                Text(text = if (isOpen) "modal opened" else "modal not open")
            }
        }
    }
}

@Preview(name = "Interaction-Small", showBackground = true, widthDp = 300, heightDp = 300)
@Composable
private fun FlipSmallModalPreview2() {

    var isOpen by remember { mutableStateOf(false) }

    FlipAppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FlipModalWrapper(isOpen = isOpen, onDismissRequest = { isOpen = false }) {
                FlipModal(
                    modifier = Modifier.size(262.dp, 195.dp),
                    mainTitle = "Main Title",
                    subTitle = "sub title",
                    itemText = "Text",
                    itemText2 = "Text",
                    onItemClick = { isOpen = false },
                    onItem2Click = { isOpen = false }
                )
            }

            Button(onClick = { isOpen = true }) {
                Text(text = if (isOpen) "modal opened" else "modal not open")
            }
        }
    }
}
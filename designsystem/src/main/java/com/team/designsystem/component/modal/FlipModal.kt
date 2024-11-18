package com.team.designsystem.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

/**
 * Flip Modal(Dialog)
 * @param modalContent [FlipModalContent]
 */
@Composable
fun FlipModal(
    modifier: Modifier = Modifier,
    modalContent: FlipModalContent,
) {
    Box(
        modifier = modifier
            .padding(horizontal = 40.dp)
            .clip(FlipTheme.shapes.roundedCornerMedium)
            .height(IntrinsicSize.Max)
            .width(
                when(modalContent.modalStyle) {
                    FlipModalStyle.MEDIUM -> 290.dp
                    FlipModalStyle.SMALL -> 262.dp
                }
            )
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
//                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp, alignment = Alignment.CenterVertically)
            ) {
                Text(text = modalContent.mainTitle, style = FlipTheme.typography.headline4)
                if (modalContent.subTitle != null) {
                    Text(
                        text = modalContent.subTitle,
                        style = FlipTheme.typography.body3,
                        color = FlipTheme.colors.gray6,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                ModalButton(
                    modifier = Modifier.weight(1f),
                    text = modalContent.itemText,
                    containerColor = FlipTheme.colors.point,
                    contentColor = FlipTheme.colors.white,
                    onClick = modalContent.onItemClick,
                    accent = true
                )
                ModalButton(
                    modifier = Modifier.weight(1f),
                    text = modalContent.itemText2,
                    containerColor = FlipTheme.colors.gray1,
                    contentColor = FlipTheme.colors.main,
                    onClick = modalContent.onItem2Click
                )
                if (modalContent.itemText3 != null) {
                    ModalButton(
                        modifier = Modifier.weight(1f),
                        text = modalContent.itemText3,
                        containerColor = FlipTheme.colors.gray1,
                        contentColor = FlipTheme.colors.main,
                        onClick = modalContent.onItem3Click
                    )
                }
            }
        }
    }
}

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
            .heightIn(min = 42.dp),
        shape = FlipTheme.shapes.roundedCornerSmall,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(0.dp)
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

@Preview(name = "Medium", showBackground = true, backgroundColor = 0x00444444)
@Composable
private fun FlipMediumModalPreview() {
    FlipAppTheme {
        FlipModal(
            modalContent = FlipModalContent(
                modalStyle = FlipModalStyle.MEDIUM,
                mainTitle = "Main Title",
                subTitle = "지금 나가면 작성 중인 글이 삭제됩니다.\n저장한 글은 임시저장에서 이어서 작성할 수 있어요.",
                itemText = "Text",
                itemText2 = "Text",
                onItemClick = { },
                onItem2Click = { }
            )
        )
    }
}

@Preview(name = "Medium(sub title X)", showBackground = true, backgroundColor = 0x00444444)
@Composable
private fun FlipMediumModalWithoutSubTitlePreview() {
    FlipAppTheme {
        FlipModal(
            modalContent = FlipModalContent(
                modalStyle = FlipModalStyle.MEDIUM,
                mainTitle = "Main Title",
                itemText = "Text",
                itemText2 = "Text",
                onItemClick = { },
                onItem2Click = { }
            )
        )
    }
}

@Preview(name = "Small", showBackground = true, backgroundColor = 0x00444444)
@Composable
private fun FlipSmallModalPreview() {
    FlipAppTheme {
        FlipModal(
            modalContent = FlipModalContent(
                modalStyle = FlipModalStyle.SMALL,
                mainTitle = "Main Title",
                subTitle = "sub title",
                itemText = "Text",
                itemText2 = "Text",
                onItemClick = { },
                onItem2Click = { }
            )
        )
    }
}

@Preview(name = "Small(sub title x)", showBackground = true, backgroundColor = 0x00444444)
@Composable
private fun FlipMediumModalWithoutSubTitlePreview2() {
    FlipAppTheme {
        FlipModal(
            modalContent = FlipModalContent(
                modalStyle = FlipModalStyle.SMALL,
                mainTitle = "Main Title",
                itemText = "Text",
                itemText2 = "Text",
                onItemClick = { },
                onItem2Click = { }
            )
        )
    }
}

@Preview(name = "Interaction-Medium", showBackground = true, widthDp = 300, heightDp = 300, backgroundColor = 0x00444444)
@Composable
private fun FlipMediumModalPreview2() {

    var isOpen by remember { mutableStateOf(true) }

    FlipAppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FlipModalWrapper(isOpen = isOpen, onDismissRequest = { isOpen = false }) {
                FlipModal(
                    modalContent = FlipModalContent(
                        modalStyle = FlipModalStyle.MEDIUM,
                        mainTitle = "Main Title",
                        subTitle = "sub titlesub titlesub titlesub title",
                        itemText = "Text",
                        itemText2 = "Text",
                        onItemClick = { isOpen = false },
                        onItem2Click = { isOpen = false }
                    )
                )
            }

            Button(onClick = { isOpen = true }) {
                Text(text = if (isOpen) "modal opened" else "modal not open")
            }
        }
    }
}

@Preview(name = "Interaction-Small", showBackground = true, widthDp = 300, heightDp = 300, backgroundColor = 0x00444444)
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
                    modalContent = FlipModalContent(
                        mainTitle = "Main Title",
                        subTitle = "sub title",
                        itemText = "Text",
                        itemText2 = "Text",
                        onItemClick = { isOpen = false },
                        onItem2Click = { isOpen = false }
                    )
                )
            }

            Button(onClick = { isOpen = true }) {
                Text(text = if (isOpen) "modal opened" else "modal not open")
            }
        }
    }
}

@Preview(
    name = "3 Option",
    showBackground = true,
    backgroundColor = 0x00444444,
    widthDp = 1000
)
@Composable
private fun FlipModalPreview() {

    var isOpen by remember {
        mutableStateOf(true)
    }

    FlipModalWrapper(isOpen = isOpen, onDismissRequest = { isOpen = false }) {
        FlipModal(
            modalContent = FlipModalContent(
                mainTitle = "Main Title",
                subTitle = "지금 나가면 작성 중인 글이 삭제됩니다.\n저장한 글은 임시저장에서 이어서 작성할 수 있어요.",
                itemText = "Text",
                itemText2 = "Text",
                itemText3 = "Text",
                onItemClick = { isOpen = false },
                onItem2Click = { isOpen = false },
                onItem3Click = { isOpen = false }
            )
        )
    }

    FlipAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(modifier = Modifier.align(Alignment.Center), onClick = { isOpen = true }) {
                Text(text = "Modal Open")
            }
        }
    }
}
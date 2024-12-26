package com.team.presentation.register.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipIconButton
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.designsystem.util.toColor
import com.team.presentation.R
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.state.RegisterContract

@Composable
fun TermsOfServiceScreen(
    modifier: Modifier = Modifier,
    state: RegisterContract.UiState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    var checkedAllItems by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(checkedAllItems) {
        if (checkedAllItems) {
            onUiEvent(RegisterContract.UiEvent.CheckAll)
        } else {
            onUiEvent(RegisterContract.UiEvent.UnCheckAll)
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(top = SCREEN_TOP_PADDING, bottom = SCREEN_BOTTOM_PADDING),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        // 헤더 타이틀 (회원가입 단계 동안 반복되는 부분)
        Title(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = HORIZONTAL_PADDING),
        )
        // 약관 동의 항목들 (회원가입 단계 동안 반복되는 부분)
        AgreementItems(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = HORIZONTAL_PADDING_WITH_TOUCH_TARGET),
            agreementItems = state.agreementItems,
            checkedAllItems = checkedAllItems,
            onAllCheckClicked = { checkedAllItems = !checkedAllItems },
        )
        // 버튼 (회원가입 단계 동안 반복되는 부분)
        FlipMediumButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = HORIZONTAL_PADDING),
            text = stringResource(id = R.string.terms_of_service_screen_agreement_btn),
            onClick = { onUiEvent(RegisterContract.UiEvent.AgreementAndRegister) },
        )
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text =
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FlipTheme.typography.headline8.fontWeight)) {
                    append(stringResource(id = R.string.terms_of_service_screen_title_1))
                }
                append(stringResource(id = R.string.terms_of_service_screen_title_2))
                append("\n")
                append(stringResource(id = R.string.terms_of_service_screen_title_3))
            },
        style = FlipTheme.typography.headline7,
        textAlign = TextAlign.Start,
    )
}

@Composable
private fun AgreementItems(
    modifier: Modifier = Modifier,
    agreementItems: List<AgreementItem>,
    checkedAllItems: Boolean,
    onAllCheckClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        AgreementAllItems(
            modifier = Modifier.fillMaxWidth(),
            checked = checkedAllItems,
            onClick = onAllCheckClicked,
        )
        Spacer(Modifier.height(30.dp))
        agreementItems.forEach { agreementItem ->
            var isClicked by rememberSaveable { mutableStateOf(false) }
            AgreementItem(
                modifier = Modifier,
                agreementItem = agreementItem,
                isClicked = isClicked,
                onClick = { isClicked = !isClicked },
                onOptionClicked = {
                    // TODO: 보기 클릭 시 웹뷰로 약관 내용 표시
                },
            )
        }
    }
}

@Composable
fun AgreementAllItems(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onClick: () -> Unit,
) {
    val checkedIconRes =
        if (checked) {
            R.drawable.ic_filled_agreement_circle
        } else {
            R.drawable.ic_outlined_agreement_circle
        }

    val title =
        buildAnnotatedString {
            append(
                "${
                    stringResource(
                        id = R.string.terms_of_service_screen_agreement_all_items_1,
                    )
                } ",
            )
            withStyle(FlipTheme.typography.body6.toSpanStyle()) {
                append(
                    stringResource(
                        id = R.string.terms_of_service_screen_agreement_all_items_2,
                    ),
                )
            }
        }

    Row(
        modifier =
            modifier
                .padding(start = 8.dp)
                .clickableSingleWithoutRipple { onClick() },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(id = checkedIconRes),
            contentDescription =
                stringResource(
                    id = R.string.terms_of_service_screen_content_desc_agreement_all_items,
                ),
        )
        Text(
            text = title,
            style = FlipTheme.typography.headline3,
        )
    }
}

@Composable
private fun AgreementItem(
    modifier: Modifier = Modifier,
    agreementItem: AgreementItem,
    isClicked: Boolean,
    onOptionClicked: () -> Unit = {},
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.clickableSingleWithoutRipple { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FlipIconButton(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_agreement_check),
            contentDescription = stringResource(agreementItem.displayName),
            onClick = onClick,
            tint = if (isClicked) FlipTheme.colors.main else FlipTheme.colors.gray4,
        )
        Text(
            text =
                buildAnnotatedString {
                    withStyle(SpanStyle(color = agreementItem.getEssentialColor().toColor())) {
                        append("${stringResource(id = agreementItem.getEssentialText())}  ")
                    }
                    append(stringResource(id = agreementItem.displayName))
                },
            style = FlipTheme.typography.body5,
        )
        if (agreementItem.isRequireDetail) {
            Spacer(modifier = Modifier.width(4.dp))
            AgreementItemDetail(onClick = onOptionClicked)
        }
    }
}

@Composable
private fun AgreementItemDetail(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Text(
        modifier = modifier.clickableSingleWithoutRipple { onClick() },
        text = stringResource(id = R.string.terms_of_service_screen_agreement_item_detail),
        style = FlipTheme.typography.body4Underline,
        color = FlipTheme.colors.gray4,
        maxLines = 1,
    )
}

private val HORIZONTAL_PADDING = 16.dp
private val HORIZONTAL_PADDING_WITH_TOUCH_TARGET = 10.dp
private val SCREEN_TOP_PADDING = 65.dp
private val SCREEN_BOTTOM_PADDING = 47.dp

@Preview(showBackground = true)
@Composable
private fun TitlePreview() {
    FlipAppTheme {
        Title()
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsOfServiceItemPreview() {
    FlipAppTheme {
        AgreementItem(
            agreementItem = AgreementItem.AGE,
            isClicked = true,
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AgreementItemsPreview() {
    var checkedAllItems by remember {
        mutableStateOf(false)
    }

    FlipAppTheme {
        AgreementItems(
            agreementItems = AgreementItem.entries.map { it },
            checkedAllItems = checkedAllItems,
            onAllCheckClicked = { checkedAllItems = !checkedAllItems },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsOfServiceScreenPreview() {
    FlipAppTheme {
        TermsOfServiceScreen(
            state = UiStateTestData,
            onUiEvent = { },
        )
    }
}

private val UiStateTestData =
    RegisterContract.UiState(
        agreementItems =
            AgreementItem.entries.map {
                it
            },
    )

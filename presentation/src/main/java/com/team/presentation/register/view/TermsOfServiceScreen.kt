package com.team.presentation.register.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.designsystem.util.toColor
import com.team.presentation.R
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.state.RegisterContract

/** 회원가입 단계 1 (서비스 이용약관) */
@Composable
fun TermsOfServiceScreen(
    modifier: Modifier = Modifier,
    agreementItems: List<AgreementItem>,
    agreementItemChecks: List<Boolean>,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        // 헤더 타이틀 (회원가입 단계 동안 반복되는 부분)
        Title(Modifier.fillMaxWidth())
        // 약관 동의 항목들 (회원가입 단계 동안 반복되는 부분)
        AgreementItems(
            modifier = Modifier.fillMaxWidth(),
            agreementItems = agreementItems,
            agreementItemChecks = agreementItemChecks,
            checkedAllItems = agreementItemChecks.all { it },
            onCheckAllItems = {
                if (agreementItemChecks.all { it }) {
                    onUiEvent(RegisterContract.UiEvent.UnCheckAll)
                } else {
                    onUiEvent(RegisterContract.UiEvent.CheckAll)
                }
            },
            onItemClick = { index ->
                onUiEvent(RegisterContract.UiEvent.OnToggleAgreementItem(index))
            },
        )
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text =
            buildAnnotatedString {
                append(stringResource(id = R.string.terms_of_service_screen_title_1))
                append("\n")
                withStyle(SpanStyle(fontWeight = FlipTheme.typography.headline8.fontWeight)) {
                    append(stringResource(id = R.string.terms_of_service_screen_title_2))
                }
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
    agreementItemChecks: List<Boolean>,
    checkedAllItems: Boolean,
    onCheckAllItems: () -> Unit,
    onItemClick: (Int) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        AgreementAllItems(
            modifier = Modifier.fillMaxWidth(),
            checked = checkedAllItems,
            onClick = onCheckAllItems,
        )
        Spacer(Modifier.fillMaxWidth().height(30.dp))
        agreementItems.forEachIndexed { index, agreementClickableItem ->
            AgreementItem(
                modifier = Modifier.padding(start = 4.dp),
                agreementItem = agreementClickableItem,
                isClicked = agreementItemChecks[index],
                onClick = { onItemClick(index) },
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
                .background(FlipTheme.colors.gray1, FlipTheme.shapes.roundedCornerSmall)
                .padding(
                    start = 14.dp,
                    top = 20.dp,
                    bottom = 20.dp,
                ).clickableSingleWithoutRipple { onClick() },
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
            agreementItem = AgreementItem.Age,
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

    var agreementItemChecks by remember {
        mutableStateOf(AgreementItemChecksTestData)
    }

    FlipAppTheme {
        AgreementItems(
            agreementItems = AgreementItemsTestData,
            agreementItemChecks = agreementItemChecks,
            checkedAllItems = checkedAllItems,
            onCheckAllItems = { checkedAllItems = !checkedAllItems },
            onItemClick = { index ->
                val mutableChecks = agreementItemChecks.toMutableList()
                mutableChecks[index] = !mutableChecks[index]
                agreementItemChecks = mutableChecks
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsOfServiceScreenPreview() {
    FlipAppTheme {
        TermsOfServiceScreen(
            agreementItems = AgreementItemsTestData,
            agreementItemChecks = AgreementItemChecksTestData,
            onUiEvent = { },
        )
    }
}

private val AgreementItemsTestData = AgreementItem.allItems
private val AgreementItemChecksTestData = List(AgreementItemsTestData.size) { false }

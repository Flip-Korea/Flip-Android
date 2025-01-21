package com.team.presentation.register.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.InputAgreementsState
import com.team.presentation.register.state.RegisterContract

/** 회원가입 단계 1 (서비스 이용약관) */
@Composable
fun TermsOfServiceScreen(
    modifier: Modifier = Modifier,
    inputAgreementsState: InputAgreementsState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            // 헤더 타이틀 (회원가입 단계 동안 반복되는 부분)
            Title(Modifier.fillMaxWidth())
            // 약관 동의 항목들 (회원가입 단계 동안 반복되는 부분)
            AgreementItems(
                modifier = Modifier.fillMaxWidth(),
                inputAgreementsState = inputAgreementsState,
                checkedAllItems = inputAgreementsState.agreementCheckItems.checkedAllItems(),
                onCheckAllItems = {
                    if (inputAgreementsState.agreementCheckItems.checkedAllItems()) {
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

        RegisterScreenBottomBar(
            title = stringResource(id = R.string.register_screen_terms_of_service_agreement_btn),
            enabled = inputAgreementsState.agreementCheckItems.checkEssentialItems(),
            isLoading = false,
            onClick = {
                onUiEvent(
                    RegisterContract.UiEvent.RequestToNextPage(RegisterScreenPage.TermsOfService),
                )
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
                append(stringResource(id = R.string.register_screen_terms_of_service_title_1))
                append("\n")
                withStyle(SpanStyle(fontWeight = FlipTheme.typography.headline8.fontWeight)) {
                    append(stringResource(id = R.string.register_screen_terms_of_service_title_2))
                }
                append(stringResource(id = R.string.register_screen_terms_of_service_title_3))
            },
        style = FlipTheme.typography.headline7,
        textAlign = TextAlign.Start,
    )
}

@Composable
private fun AgreementItems(
    modifier: Modifier = Modifier,
    inputAgreementsState: InputAgreementsState,
    checkedAllItems: Boolean,
    onCheckAllItems: () -> Unit,
    onItemClick: (AgreementItem) -> Unit,
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
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(30.dp),
        )
        AgreementItem.allItems.forEach { agreementItem ->
            AgreementItem(
                modifier = Modifier.padding(start = 4.dp),
                agreementItem = agreementItem,
                isClicked = inputAgreementsState.agreementCheckItems[agreementItem] ?: false,
                onClick = { onItemClick(agreementItem) },
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
                        id = R.string.register_screen_terms_of_service_agreement_all_items_1,
                    )
                } ",
            )
            withStyle(FlipTheme.typography.body6.toSpanStyle()) {
                append(
                    stringResource(
                        id = R.string.register_screen_terms_of_service_agreement_all_items_2,
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
                    id = R.string.register_screen_content_desc_agreement_all_items,
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
        text = stringResource(id = R.string.register_screen_terms_of_service_agreement_item_detail),
        style = FlipTheme.typography.body4Underline,
        color = FlipTheme.colors.gray4,
        maxLines = 1,
    )
}

private fun Map<AgreementItem, Boolean>.checkEssentialItems(): Boolean {
    val checkedEssentialItemCount = this.filter { it.value }.count { it.key.isEssential }
    val isSameItemSize = checkedEssentialItemCount == AgreementItem.essentialItems.size
    return isSameItemSize
}

private fun Map<AgreementItem, Boolean>.checkedAllItems(): Boolean = this.all { it.value }

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
    var agreementMap by remember {
        mutableStateOf(AgreementItemsTestData.associateWith { false })
    }

    FlipAppTheme {
        AgreementItems(
            inputAgreementsState = InputAgreementsState(agreementCheckItems = agreementMap),
            checkedAllItems = agreementMap.all { it.value },
            onCheckAllItems = {
                agreementMap = AgreementItemsTestData.associateWith { true }
            },
            onItemClick = { agreementItem ->
                val mutableChecks = agreementMap.toMutableMap()
                mutableChecks[agreementItem] = !mutableChecks[agreementItem]!!
                agreementMap = mutableChecks
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsOfServiceScreenPreview() {
    FlipAppTheme {
        TermsOfServiceScreen(
            modifier = Modifier.fillMaxSize(),
            inputAgreementsState =
                InputAgreementsState(
                    AgreementItemsTestData.associateWith { false },
                ),
            onUiEvent = { },
        )
    }
}

private val AgreementItemsTestData = AgreementItem.allItems

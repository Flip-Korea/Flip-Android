package com.team.presentation.register.state

import com.team.presentation.register.AgreementItem

data class InputAgreementsState(
    val agreementCheckItems: Map<AgreementItem, Boolean> = mapOf(),
)

fun InputAgreementsState.isAdsAgree(): Boolean =
    agreementCheckItems[AgreementItem.ReceiveAdTypeInformation] ?: false

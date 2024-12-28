package com.team.presentation.register

import com.team.designsystem.theme.FlipLightColors
import com.team.designsystem.util.toArgbInt
import com.team.presentation.R

enum class AgreementItem(
    val displayName: Int,
    private val isEssential: Boolean,
    val isRequireDetail: Boolean,
) {
    AGE(
        R.string.terms_of_service_screen_agreement_item_age,
        true,
        false,
    ),
    TERMS_OF_SERVICE(
        R.string.terms_of_service_screen_agreement_item_service,
        true,
        true,
    ),
    COLLECT_PERSONAL_INFORMATION(
        R.string.terms_of_service_screen_agreement_item_personal_information,
        true,
        true,
    ),
    RECEIVE_AD_TYPE_INFORMATION(R.string.terms_of_service_screen_agreement_item_ad, false, false),
    ;

    fun getEssentialText(): Int =
        if (isEssential) {
            R.string.terms_of_service_screen_agreement_item_essential
        } else {
            R.string.terms_of_service_screen_agreement_item_not_essential
        }

    fun getEssentialColor(): Int =
        if (isEssential) {
            FlipLightColors.statusRed.toArgbInt()
        } else {
            FlipLightColors.statusBlue.toArgbInt()
        }

    companion object {
        val allItems: List<AgreementItem> = AgreementItem.entries.map { it }
    }
}

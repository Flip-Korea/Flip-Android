package com.team.presentation.register

import androidx.annotation.StringRes
import com.team.presentation.R

enum class RegisterScreenPage(
    val order: Int,
    val route: String,
    @StringRes val buttonTitle: Int,
) {
    TermsOfService(0, "terms_of_service", R.string.terms_of_service_screen_agreement_btn),
    InputNickname(1, "input_nickname", R.string.terms_of_service_screen_input_nickname_btn),
    InputID(2, "input_id", R.string.terms_of_service_screen_input_id_btn),
    InputPhoto(3, "input_photo", R.string.terms_of_service_screen_input_photo_btn),
    ;

    companion object {
        val defaultButtonTitle = R.string.register_screen_common_btn_title
    }
}

fun List<RegisterScreenPage>.findByOrder(order: Int): RegisterScreenPage? =
    this.find { it.order == order }

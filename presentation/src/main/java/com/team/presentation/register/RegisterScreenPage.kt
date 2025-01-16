package com.team.presentation.register

import androidx.annotation.StringRes
import com.team.presentation.R

enum class RegisterScreenPage(
    val order: Int,
    val route: String,
    @StringRes val buttonTitle: Int,
) {
    TermsOfService(0, "terms_of_service", R.string.register_screen_terms_of_service_agreement_btn),
    InputNickname(1, "input_nickname", R.string.register_screen_input_nickname_btn),
    InputID(2, "input_id", R.string.register_screen_input_id_btn),
    InputImage(3, "input_image", R.string.register_screen_input_image_btn),
    ;

    companion object {
        val defaultButtonTitle = R.string.register_screen_common_btn_title
        val routes = RegisterScreenPage.entries.map { it.route }
    }
}

fun List<RegisterScreenPage>.findByOrder(order: Int): RegisterScreenPage? =
    this.find { it.order == order }

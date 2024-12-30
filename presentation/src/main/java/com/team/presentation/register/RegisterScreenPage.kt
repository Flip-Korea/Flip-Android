package com.team.presentation.register

import androidx.annotation.StringRes
import com.team.presentation.R

enum class RegisterScreenPage(
    val order: Int,
    @StringRes val buttonTitle: Int,
) {
    TERMS_OF_SERVICE(0, R.string.terms_of_service_screen_agreement_btn),
    INPUT_NAME(1, R.string.terms_of_service_screen_input_name_btn),
    INPUT_ID(2, R.string.terms_of_service_screen_input_id_btn),
    INPUT_PHOTO(3, R.string.terms_of_service_screen_input_photo_btn),
    ;

    companion object {
        val REGISTER_SCREEN_PAGES =
            listOf(
                TERMS_OF_SERVICE,
                INPUT_NAME,
                INPUT_ID,
                INPUT_PHOTO,
            )
    }
}

fun List<RegisterScreenPage>.findByOrder(order: Int): RegisterScreenPage? =
    this.find { it.order == order }

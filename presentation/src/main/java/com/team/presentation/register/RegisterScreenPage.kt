package com.team.presentation.register

import androidx.annotation.StringRes
import com.team.presentation.R

enum class RegisterScreenPage(
    val order: Int,
    val route: String,
    @StringRes val buttonTitle: Int,
) {
    TermsOfService(0, "terms_of_service", R.string.terms_of_service_screen_agreement_btn),
    InputName(1, "input_name", R.string.terms_of_service_screen_input_name_btn),
    InputID(2, "input_id", R.string.terms_of_service_screen_input_id_btn),
    InputPhoto(3, "input_photo", R.string.terms_of_service_screen_input_photo_btn),
    ;

    companion object {
        val RegisterScreenPages =
            listOf(
                TermsOfService,
                InputName,
                InputID,
                InputPhoto,
            )
    }
}

fun List<RegisterScreenPage>.findByOrder(order: Int): RegisterScreenPage? =
    this.find { it.order == order }

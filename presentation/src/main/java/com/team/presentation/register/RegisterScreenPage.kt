package com.team.presentation.register

enum class RegisterScreenPage(
    val route: String,
) {
    TermsOfService("terms_of_service"),
    InputNickname("input_nickname"),
    InputID("input_id"),
    InputImage("input_image"),
    Finish("finish_screen"),
}

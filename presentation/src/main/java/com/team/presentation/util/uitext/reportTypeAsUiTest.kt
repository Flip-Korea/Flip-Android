package com.team.presentation.util.uitext

import com.team.domain.type.ReportType
import com.team.presentation.R

fun ReportType.asUiText(): UiText =
    when (this) {
        ReportType.DontLike -> UiText.StringResource(
            R.string.report_dont_Like
        )
        ReportType.SpamAndAdvertising -> UiText.StringResource(
            R.string.report_spam_and_advertising
        )
        ReportType.Inappropriate -> UiText.StringResource(
            R.string.report_inappropriate
        )
        ReportType.Fake -> UiText.StringResource(
            R.string.report_fake
        )
        ReportType.HateSpeech -> UiText.StringResource(
            R.string.report_hate_speech
        )
        is ReportType.Etc -> UiText.DynamicString(this.reason)
    }
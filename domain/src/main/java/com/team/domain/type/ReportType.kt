package com.team.domain.type

sealed class ReportType {

    data object DontLike: ReportType()
    data object SpamAndAdvertising: ReportType()
    data object Inappropriate: ReportType()
    data object Fake: ReportType()
    data object HateSpeech: ReportType()
    data class Etc(val reason: String): ReportType()

    fun asString(): String {
        return when(this) {
            DontLike -> { "마음에 들지 않음" }
            SpamAndAdvertising -> { "스팸 및 광고성 게시글" }
            Inappropriate -> { "부적절함" }
            Fake -> { "사기 또는 거짓" }
            HateSpeech -> { "혐오 발언" }
            is Etc -> { this.reason }
        }
    }
}
package com.team.domain.type

/**
 * Flip(Post) 글의 배경 컬러 타입
 *
 * 주의 사항: 네트워크 통신 시 Value의 name으로 전달해야 한다.
 * [BackgroundColorType.DEFAULT.name]
 *
 * @see BackgroundColorType.asString
 * @see String.asBackgroundColorType
 */
enum class BackgroundColorType {
    DEFAULT,
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE
}

/**
 * [BackgroundColorType]을 View 용 텍스트로 변환
 * //TODO 임시 하드코딩
 * @see BackgroundColorType
 */
fun BackgroundColorType.asString(): String =
    when (this) {
        BackgroundColorType.DEFAULT -> "기본"
        BackgroundColorType.RED -> "레드"
        BackgroundColorType.YELLOW -> "옐로우"
        BackgroundColorType.GREEN -> "그린"
        BackgroundColorType.BLUE -> "블루"
        BackgroundColorType.PURPLE -> "퍼플"
    }

/**
 * 문자열을 [BackgroundColorType] 으로 변환
 * @see BackgroundColorType
 */
fun String.asBackgroundColorType(): BackgroundColorType =
    when (this) {
        "DEFAULT" -> BackgroundColorType.DEFAULT
        "RED" -> BackgroundColorType.RED
        "YELLOW" -> BackgroundColorType.YELLOW
        "GREEN" -> BackgroundColorType.GREEN
        "BLUE" -> BackgroundColorType.BLUE
        "PURPLE" -> BackgroundColorType.PURPLE
        else -> BackgroundColorType.DEFAULT
    }
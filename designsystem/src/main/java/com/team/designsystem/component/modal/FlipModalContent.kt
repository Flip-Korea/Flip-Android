package com.team.designsystem.component.modal

enum class FlipModalStyle { MEDIUM, SMALL }

/**
 * @param modalStyle 모달 스타일(크기) [FlipModalStyle]
 * @param mainTitle 메인 제목
 * @param subTitle 서브 제목 (선택)
 * @param itemText 첫 번째 항목 텍스트
 * @param itemText2 두 번째 항목 텍스트
 * @param itemText3 세 번째 항목 텍스트 (선택)
 * @param onItemClick 첫 번째 항목 클릭 시
 * @param onItem2Click 두 번째 항목 클릭 시
 * @param onItem3Click 세 번째 항목 클릭 시 (선택)
 */
data class FlipModalContent(
    val modalStyle: FlipModalStyle = FlipModalStyle.SMALL,
    val mainTitle: String,
    val subTitle: String? = null,
    val itemText: String,
    val itemText2: String,
    val itemText3: String? = null,
    val onItemClick: () -> Unit,
    val onItem2Click: () -> Unit,
    val onItem3Click: () -> Unit = {},
)
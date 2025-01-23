package com.team.presentation.addflip.state

enum class AddPostLoadingType {
    NotLoading,
    Post,
    TempPost,
}

/** 플립(Post)을 작성 및 저장할 때 사용되는 상태 모델 */
data class PostSaveState(
    val tempPostSave: Boolean = false,
    val postSave: Boolean = false,
    val loading: AddPostLoadingType = AddPostLoadingType.NotLoading,
)

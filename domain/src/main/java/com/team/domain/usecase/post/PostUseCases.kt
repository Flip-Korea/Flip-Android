package com.team.domain.usecase.post

import javax.inject.Inject

class PostUseCases @Inject constructor(
    val getPostsByTypeUseCase: GetPostsByTypeUseCase,
    val getCachedPostsUseCase: GetCachedPostsUseCase,
    val getPostsUseCase: GetPostsUseCase,
)

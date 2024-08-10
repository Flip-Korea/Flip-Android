package com.team.domain.usecase.post

import javax.inject.Inject

class GetPostUseCases @Inject constructor(
    val getPostsByTypeUseCase: GetPostsByTypeUseCase,
    val getCachedPostsUseCase: GetCachedPostsUseCase,
    val getPostsUseCase: GetPostsUseCase,
)

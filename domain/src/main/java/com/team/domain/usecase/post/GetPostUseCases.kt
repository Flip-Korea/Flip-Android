package com.team.domain.usecase.post

import javax.inject.Inject

class GetPostUseCases @Inject constructor(
    val getPostsByTypeUseCase: GetPostsByTypeUseCase,
    val getPostsUseCase: GetPostsUseCase,
)

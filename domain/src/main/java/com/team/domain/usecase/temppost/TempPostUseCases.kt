package com.team.domain.usecase.temppost

import javax.inject.Inject

class TempPostUseCases @Inject constructor(
    val addTempPostUseCase: AddTempPostUseCase,
    val getTempPostsUseCase: GetTempPostsPaginationUseCase,
    val editTempPostUseCase: EditTempPostUseCase,
    val deleteTempPostUseCase: DeleteTempPostUseCase,
)
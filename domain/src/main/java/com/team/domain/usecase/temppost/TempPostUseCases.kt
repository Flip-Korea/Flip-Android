package com.team.domain.usecase.temppost

import javax.inject.Inject

class TempPostUseCases @Inject constructor(
    val addTempPostUseCase: AddTempPostUseCase,
    val getTempPostsPaginationUseCase: GetTempPostsPaginationUseCase,
    val editTempPostUseCase: EditTempPostUseCase,
    val deleteTempPostUseCase: DeleteTempPostUseCase,
)
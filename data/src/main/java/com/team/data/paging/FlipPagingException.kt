package com.team.data.paging

import com.team.domain.util.ErrorBody
import com.team.domain.util.ErrorType

class FlipPagingException(
    val errorType: ErrorType,
    val errorBody: ErrorBody?,
    message: String = "",
    cause: Throwable? = null
) : Exception(message, cause)
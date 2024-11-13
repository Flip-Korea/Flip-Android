package com.team.data

import com.squareup.moshi.Moshi
import com.team.domain.util.ErrorBody

class ErrorBodyFactory(private val moshi: Moshi) {

    private val errorBody = ErrorBody(
        code = "404",
        message = "ErrorBody Test",
        errors = emptyList(),
    )

    fun createObject(): ErrorBody = errorBody

    fun createJson(): String {
        val adapter = moshi.adapter(ErrorBody::class.java)
        val json = adapter.toJson(errorBody)
        return json
    }
}
package com.team.data.util

import android.util.Log
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.random.Random

// TODO 매개변수들 다른 서비스들 참고해서 실제 수치로 변경하기
// TODO 주의사항: 올바른 사용자 인증 정보가 제공되기 전까지는 승인되지 않는 HTTP 요청은 다시 시도해서는 안됨 (By Google)
/** Retry Function, Exponential Backoff With Jitter **/
suspend fun <T> retry(
    attempt: Int = 2,
    initialDelayMillis: Long = 500,
    maxDelayMillis: Long = 2500,
    factor: Double = 2.0,
    block: suspend () -> T
): T {

    repeat(attempt) {
        val temp =
            maxDelayMillis.coerceAtMost(initialDelayMillis * factor.pow(attempt - 1).toLong())
        val fullJitterDelay = (temp / 2) + Random.nextLong(0, temp / 2)
        Log.d("network_retry_log", "network call retry delay: $fullJitterDelay")

        try {
            return block()
        } catch (e: Exception) {
            // logging or analysis
            Log.d("network_retry_log", "(${e.localizedMessage}) exception occurred")
        }

        delay(fullJitterDelay)
    }

    // last attempt (In order to handle exceptions where this function is used)
    // so, if attempt -> 2,
    // total retry count -> 3 (attempt + last attempt)
    return block()
}
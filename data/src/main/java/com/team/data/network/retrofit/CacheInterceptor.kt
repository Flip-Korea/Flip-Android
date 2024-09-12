package com.team.data.network.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
//        if (request.header("Cache-control") == null) {
//        }

        val maxAge = 5

        request = request
            .newBuilder()
            .removeHeader("Cache-Control")
            .removeHeader("Pragma")
            .header("Cache-Control", "public, max-age=${maxAge}")
            .build()

        return chain.proceed(request)
    }
}
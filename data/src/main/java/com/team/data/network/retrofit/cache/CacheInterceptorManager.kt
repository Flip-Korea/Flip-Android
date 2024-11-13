package com.team.data.network.retrofit.cache

import android.content.Context
import android.util.Log
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Cache 인터셉트 매니저 클래스
 */
class CacheInterceptorManager{

    /**
     * Create [CacheInterceptor]
     *
     * @param maxAge 적용할 캐시 시간(초 단위)
     */
    fun createCacheInterceptor(maxAge: Int): CacheInterceptor {
        return CacheInterceptor(maxAge)
    }

    /**
     * Create [ForceCacheInterceptor]
     */
    fun createForceCacheInterceptor(networkCheckUtil: NetworkCheckUtil): ForceCacheInterceptor {
        return ForceCacheInterceptor(networkCheckUtil)
    }

    /**
     * [cacheSize]에 맞게 캐시를 반환한다.
     *
     * @param cacheSize 캐시 사이즈 (MB 단위)
     */
    fun getCache(
        context: Context,
        cacheSize: Int,
    ): Cache {
        val calculatedCacheSize = (cacheSize * 1024 * 1024).toLong()
        return Cache(context.cacheDir, calculatedCacheSize)
    }

    /**
     * 캐시 인터셉트를 적용한다.
     *
     * @param maxAge 적용할 캐시 시간(초 단위)
     */
    class CacheInterceptor(private val maxAge: Int): Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(maxAge, TimeUnit.SECONDS)
                .build()

            val newResponse = response.newBuilder()
                .removeHeader("Cache-Control")
                .removeHeader("Pragma")
                .header("Cache-Control", cacheControl.toString())
                .build()

            Log.d("okhttp_interceptor", "CacheInterceptor Triggered!")
            return newResponse
        }
    }

    /**
     * 네트워크 상태를 확인하고 강제 캐시 인터셉트를 적용한다.
     *
     * @param networkCheckUtil app 모듈에 있는 NetworkCheckImpl를 통해 주입된다. (DIP)
     */
    class ForceCacheInterceptor(private val networkCheckUtil: NetworkCheckUtil): Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            if (!networkCheckUtil.hasNetwork()) {
                builder.cacheControl(CacheControl.FORCE_CACHE)
            }
            Log.d("okhttp_interceptor", "ForceCacheInterceptor Triggered!")
            return chain.proceed(builder.build())
        }
    }
}
package com.team.flip

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FlipApplication: Application(), ImageLoaderFactory {

    // 약 10MB 정도
    private val imageCacheMaxSize: Long = 10 * 1024 * 1024
    // 최대 메모리의 10퍼센트
    private val memoryCacheMaxSizePercent: Double = 0.10

    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "ce32139ee6d69fd0b8b94704eff03893")
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCachePolicy(CachePolicy.ENABLED) // 메모리 캐시 활성화
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(memoryCacheMaxSizePercent)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)   // 디스크 캐시 활성화
            .diskCache {
                DiskCache.Builder()
                    .maxSizeBytes(imageCacheMaxSize)
                    .directory(cacheDir.resolve("flip_image_cache"))
                    .build()
            }
            .apply {
                if (BuildConfig.DEBUG) { logger(DebugLogger()) }
            }
            .respectCacheHeaders(true)  // 서버의 캐시 제어 헤더 사용 여부
            .build()
    }
}
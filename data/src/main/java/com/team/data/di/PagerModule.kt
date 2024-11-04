package com.team.data.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.team.data.network.model.response.post.TempPostResponse
import com.team.data.network.source.PostNetworkDataSource
import com.team.data.paging.FlipPagingSource
import com.team.domain.util.paging.FlipPagingTokens
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PagerModule {

    @Provides
    @Singleton
    fun provideTempPostPager(
        tempPostNetworkDataSource: PostNetworkDataSource
    ): Pager<Long, TempPostResponse> {
        val pageSize = FlipPagingTokens.TEMP_POST_PAGE_SIZE
        val prefetchDistance = FlipPagingTokens.TEMP_POST_PREFETCH_DISTANCE
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = prefetchDistance
            ),
            pagingSourceFactory = {
                FlipPagingSource(
                    pageSize = pageSize,
                    apiCall = { loadKey: Long? ->
                        tempPostNetworkDataSource.getTemporaryPosts("$loadKey", pageSize)
                    }
                )
            }
        )
    }
}
package com.team.data.di

import com.team.domain.repository.AccountRepository
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
// 해당 모듈을 interface 로 바꾸는 거랑 뭔 차이지?

    @DefaultPostRepository
    @Binds
    fun bindsPostRepository(
        impl: com.team.data.repository.DefaultPostRepository
    ): PostRepository

//    @NetworkFirstPostRepository
//    @Binds
//    abstract fun bindsNetworkFirstPostRepository(
//        impl: com.team.data.repository.NetworkFirstPostRepository
//    ): PostRepository

    @DefaultAuthRepository
    @Binds
    fun bindsAuthRepository(
        impl: com.team.data.repository.DefaultAccountRepository
    ): AccountRepository

    @DefaultCommentRepository
    @Binds
    fun bindsCommentRepository(
        impl: com.team.data.repository.DefaultCommentRepository
    ): CommentRepository
}

@Qualifier
annotation class DefaultPostRepository

@Qualifier
annotation class NetworkFirstPostRepository

@Qualifier
annotation class DefaultAuthRepository

@Qualifier
annotation class DefaultCommentRepository
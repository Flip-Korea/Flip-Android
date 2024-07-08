package com.team.data.di

import com.team.data.repository.DefaultAccountRepository
import com.team.data.repository.DefaultCategoryRepository
import com.team.data.repository.DefaultCommentRepository
import com.team.data.repository.DefaultPostRepository
import com.team.data.repository.DefaultUserRepository
import com.team.domain.repository.AccountRepository
import com.team.domain.repository.CategoryRepository
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.PostRepository
import com.team.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
// 해당 모듈을 interface 로 바꾸는 거랑 뭔 차이지?

    @Binds
    fun bindsPostRepository(
        impl: DefaultPostRepository
    ): PostRepository

    @Binds
    fun bindsAuthRepository(
        impl: DefaultAccountRepository
    ): AccountRepository

    @Binds
    fun bindsCommentRepository(
        impl: DefaultCommentRepository
    ): CommentRepository

    @Binds
    fun bindsUserRepository(
        impl: DefaultUserRepository
    ): UserRepository

    @Binds
    fun bindsCategoryRepository(
        impl: DefaultCategoryRepository
    ): CategoryRepository
}
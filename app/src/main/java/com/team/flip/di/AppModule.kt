package com.team.flip.di

import android.content.Context
import com.team.data.network.retrofit.cache.NetworkCheckUtil
import com.team.flip.util.NetworkCheckUtilImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideNetworkCheckUtil(
        @ApplicationContext context: Context
    ): NetworkCheckUtil {
        return NetworkCheckUtilImpl(context)
    }
}
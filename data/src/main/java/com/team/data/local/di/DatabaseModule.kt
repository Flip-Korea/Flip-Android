package com.team.data.local.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.local.FlipDatabase
import com.team.data.local.dao.CategoryDao
import com.team.data.local.dao.PostDao
import com.team.data.local.dao.MyProfileDao
import com.team.data.local.dao.RecentSearchDao
import com.team.data.local.type_converter.ListTypeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideFlipDatabase(
        @ApplicationContext context: Context,
        moshi: Moshi
    ): FlipDatabase {

        val listTypeConverter = ListTypeConverter(moshi)

        return Room.databaseBuilder(
            context,
            FlipDatabase::class.java,
            "flip_database.db"
        )
            .addTypeConverter(listTypeConverter)
            .build()
    }

    @Singleton
    @Provides
    fun provideMyProfileDao(flipDatabase: FlipDatabase): MyProfileDao = flipDatabase.myProfileDao()

    @Singleton
    @Provides
    fun providePostDao(flipDatabase: FlipDatabase): PostDao = flipDatabase.postDao()

    @Singleton
    @Provides
    fun provideCategoryDao(flipDatabase: FlipDatabase): CategoryDao = flipDatabase.categoryDao()

    @Singleton
    @Provides
    fun provideRecentSearchDao(flipDatabase: FlipDatabase): RecentSearchDao = flipDatabase.recentSearchDao()
}
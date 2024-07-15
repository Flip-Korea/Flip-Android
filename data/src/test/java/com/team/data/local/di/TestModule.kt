package com.team.data.local.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.local.FlipDatabase
import com.team.data.local.typeconverter.ListTypeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDatabase(@ApplicationContext context: Context): FlipDatabase {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val listTypeConverter = ListTypeConverter(moshi)

        return Room.inMemoryDatabaseBuilder(context, FlipDatabase::class.java)
            .allowMainThreadQueries()
            .addTypeConverter(listTypeConverter)
            .build()
    }
}
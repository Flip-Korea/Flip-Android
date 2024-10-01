package com.team.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.team.data.local.dao.CategoryDao
import com.team.data.local.dao.MyProfileDao
import com.team.data.local.dao.RecentSearchDao
import com.team.data.local.entity.CategoryEntity
import com.team.data.local.entity.RecentSearchEntity
import com.team.data.local.entity.profile.MyProfileEntity
import com.team.data.local.typeconverter.ListTypeConverter

@TypeConverters(
    ListTypeConverter::class
)
@Database(
    version = 1,
    entities = [
        MyProfileEntity::class,
        CategoryEntity::class,
        RecentSearchEntity::class
    ],
//    exportSchema = false
)
abstract class FlipDatabase: RoomDatabase() {

    abstract fun myProfileDao(): MyProfileDao
    abstract fun categoryDao(): CategoryDao
    abstract fun recentSearchDao(): RecentSearchDao
}
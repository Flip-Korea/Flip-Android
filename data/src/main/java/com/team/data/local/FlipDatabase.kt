package com.team.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.team.data.local.dao.CategoryDao
import com.team.data.local.dao.PostDao
import com.team.data.local.dao.MyProfileDao
import com.team.data.local.entity.CategoryEntity
import com.team.data.local.entity.post.PostEntity
import com.team.data.local.entity.profile.MyProfileEntity
import com.team.data.local.type_converter.ListTypeConverter

@TypeConverters(
    ListTypeConverter::class
)
@Database(
    version = 1,
    entities = [
        MyProfileEntity::class,
        PostEntity::class,
        CategoryEntity::class
    ],
//    exportSchema = false
)
abstract class FlipDatabase: RoomDatabase() {

    abstract fun myProfileDao(): MyProfileDao
    abstract fun postDao(): PostDao
    abstract fun categoryDao(): CategoryDao
}
package com.team.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.team.data.local.entity.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Query("SELECT * FROM recentsearchentity")
    fun getRecentSearchList(): Flow<List<RecentSearchEntity>>

    @Upsert
    suspend fun upsertRecentSearch(recentSearchEntity: RecentSearchEntity)

    @Query("DELETE FROM recentsearchentity WHERE id=:id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM recentsearchentity")
    suspend fun clearAll()
}
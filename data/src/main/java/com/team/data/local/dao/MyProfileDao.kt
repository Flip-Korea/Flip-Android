package com.team.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.team.data.local.entity.profile.MyProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MyProfileDao {

    @Query("SELECT * FROM myprofileentity")
    fun getAllProfile(): Flow<List<MyProfileEntity>>

    @Query("SELECT * FROM myprofileentity WHERE profileId=:profileId")
    fun getProfileById(profileId: String): Flow<MyProfileEntity?>

    @Upsert
    suspend fun upsertProfile(profileEntity: MyProfileEntity)

    @Upsert
    suspend fun upsertAll(profileEntities: List<MyProfileEntity>)

    @Delete
    suspend fun deleteProfile(profileEntity: MyProfileEntity)

    @Query("DELETE FROM myprofileentity")
    suspend fun deleteAll()
}
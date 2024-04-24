package com.team.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.team.data.local.entity.post.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM postentity")
    fun getPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM postentity WHERE postId=:postId")
    fun getPostById(postId: Long): Flow<PostEntity?>

    @Upsert
    suspend fun upsertPost(postEntity: PostEntity)

    @Upsert
    suspend fun upsertAll(postEntities: List<PostEntity>)

    @Delete
    suspend fun deletePost(postEntity: PostEntity)

    @Query("DELETE FROM postentity")
    suspend fun deleteAll()

    @Transaction
    suspend fun refresh(postEntities: List<PostEntity>) {
        deleteAll()
        upsertAll(postEntities)
    }
}
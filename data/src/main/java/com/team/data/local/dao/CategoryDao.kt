package com.team.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.team.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categoryentity")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categoryentity WHERE id=:id")
    fun getCategoryById(id: Int): Flow<CategoryEntity?>

    @Query("SELECT * FROM categoryentity WHERE id IN (:ids)")
    fun getCategoryByIds(ids: List<Int>): Flow<List<CategoryEntity>>

    @Upsert
    fun upsertCategory(categoryEntity: CategoryEntity)

    @Upsert
    fun upsertCategories(categories: List<CategoryEntity>)

    @Delete
    fun deleteCategory(categoryEntity: CategoryEntity)

    @Query("DELETE FROM categoryentity")
    fun clearAll()
}
package com.team.data.repository.fake

import com.team.data.local.dao.CategoryDao
import com.team.data.local.entity.toDomainModel
import com.team.data.network.model.response.category.toEntity
import com.team.data.network.source.CategoryNetworkDataSource
import com.team.domain.model.category.Category
import com.team.domain.repository.CategoryRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FakeCategoryRepository(
    private val categoryDao: CategoryDao,
    private val categoryNetworkDataSource: CategoryNetworkDataSource,
): CategoryRepository {

    private val ioDispatcher = Dispatchers.IO

    override fun getCategoriesFromLocal(): Flow<List<Category>> =
        categoryDao.getCategories().map { it.toDomainModel() }

    override suspend fun refreshCategories(): Result<Boolean, ErrorType> {

        return when (val result =
            categoryNetworkDataSource.getCategories()) {
            is Result.Success -> {
                val categoryEntities = withContext(ioDispatcher) {
                    result.data.map { it.toEntity() }
                }
                categoryDao.upsertCategories(categoryEntities)

                val categories = withContext(ioDispatcher) {
                    categoryEntities.toDomainModel()
                }

                Result.Success(true)
            }
            is Result.Error -> { Result.Success(false) }
            Result.Loading -> { Result.Success(false) }
        }
    }
}
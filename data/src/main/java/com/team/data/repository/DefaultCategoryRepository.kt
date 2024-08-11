package com.team.data.repository

import com.team.data.di.IODispatcher
import com.team.data.local.dao.CategoryDao
import com.team.data.local.entity.toDomainModel
import com.team.data.network.model.response.category.toEntity
import com.team.data.network.source.CategoryNetworkDataSource
import com.team.domain.model.category.Category
import com.team.domain.repository.CategoryRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Category Repository
 */
class DefaultCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val categoryNetworkDataSource: CategoryNetworkDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
): CategoryRepository {

    override fun getCategoriesFromLocal(): Flow<List<Category>> =
        categoryDao.getCategories()
            .map { it.toDomainModel() }
            .flowOn(ioDispatcher)

    override suspend fun refreshCategories(): Result<Boolean, ErrorType> {

        val result = withContext(ioDispatcher) {
            categoryNetworkDataSource.getCategories()
        }

        return when (result) {
            is Result.Success -> {
                val categoryEntities = withContext(ioDispatcher) {
                    result.data.map { it.toEntity() }
                }

                withContext(ioDispatcher) {
                    categoryDao.upsertCategories(categoryEntities)
                }

                withContext(ioDispatcher) {
                    categoryEntities.toDomainModel()
                }

                Result.Success(true)
            }
            is Result.Error -> {
                Result.Error(errorBody = result.errorBody, error = result.error)
            }
            Result.Loading -> { Result.Loading }
        }
    }
}
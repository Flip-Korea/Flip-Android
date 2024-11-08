package com.team.domain.usecase.category

import com.team.domain.model.category.Category
import com.team.domain.repository.CategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) {

    /**
     * Local DB 에서 모든 카테고리를 가져온다.
     *
     * 만약 비어 있다면 네트워크에서 가져온다.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getCategoriesFromLocal().flatMapLatest { categories ->
            if (categories.isNotEmpty()) {
                flowOf(categories)
            } else {
                flow {
                    categoryRepository.refreshCategories()
                    val result = categoryRepository.getCategoriesFromLocal()
                    emitAll(result)
                }
                    .catch { emit(emptyList()) }
            }
        }
    }
}


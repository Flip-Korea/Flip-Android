package com.team.domain.usecase.interestcategory

import com.team.domain.model.category.Category
import com.team.domain.model.category.fixedCategories
import com.team.domain.usecase.category.GetCategoriesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFilteredMyCategoriesUseCase @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMyCategoriesUseCase: GetMyCategoriesUseCase,
) {

    /**
     * 모든 카테고리를 가져 오고 나의 관심 카테고리로 필터링 한다.
     *
     * 결과: 고정 카테고리 + 나의 관심 카테고리
     *
     * 주의 사항: 오류 발생 시 빈 리스트 반환, 오류 처리 필요 시 처리 요망
     */
    operator fun invoke(): Flow<List<Category>> =
        combine(getCategoriesUseCase(), getMyCategoriesUseCase()) { categories, myCategoryIds ->
            myCategoryIds?.let { myCateIds ->
                fixedCategories + myCateIds.mapNotNull { id ->
                    categories.find { it.id == id }
                }
            } ?: fixedCategories
        }
}
package com.team.domain.usecase.interestcategory

import com.team.domain.model.category.Category
import com.team.domain.repository.UserRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateMyCategoriesUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(myCategories: List<Category>): Flow<Result<Boolean, ErrorType>> =
        userRepository.updateMyCategories(myCategories.map { it.id })
}
package com.team.data.testdoubles.local

import com.team.data.local.entity.CategoryEntity

fun makeCategoriesTestData(categoryIds: List<Int>): List<CategoryEntity> {

    val list = mutableListOf<CategoryEntity>()
    categoryIds.forEach {  id ->
        list.add(
            CategoryEntity(
                id = id,
                name = "TestCategory($id)",
            )
        )
    }

    return list
}

fun makeCategoryTestData(categoryId: Int): CategoryEntity =
    CategoryEntity(
        id = categoryId,
        name = "TestCategory($categoryId)",
    )
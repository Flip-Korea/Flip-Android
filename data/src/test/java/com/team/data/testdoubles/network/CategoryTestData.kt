package com.team.data.testdoubles.network

import com.team.data.local.entity.CategoryEntity
import com.team.domain.model.category.Category

val networkCategoriesTestData = """
    [
          {
            "categoryId": 1,
            "categoryName": "일상"
          },
          {
            "categoryId": 2,
            "categoryName": "독서"
          },
          {
            "categoryId": 3,
            "categoryName": "자기계발"
          },
          {
            "categoryId": 4,
            "categoryName": "경제"
          },
          {
            "categoryId": 5,
            "categoryName": "재테크"
          },
          {
            "categoryId": 6,
            "categoryName": "예술/문화"
          },
          {
            "categoryId": 7,
            "categoryName": "디자인"
          },
          {
            "categoryId": 8,
            "categoryName": "컴퓨터/IT"
          },
          {
            "categoryId": 9,
            "categoryName": "과학"
          },
          {
            "categoryId": 10,
            "categoryName": "여행"
          },
          {
            "categoryId": 11,
            "categoryName": "건강/운동"
          },
          {
            "categoryId": 12,
            "categoryName": "글로벌"
          }
        ]
""".trimIndent()

val categoriesTestData =
    listOf(
        Category(1, "일상"),
        Category(2, "독서"),
        Category(3, "자기계발"),
        Category(4, "경제"),
        Category(5, "재테크"),
        Category(6, "예술/문화"),
        Category(7, "디자인"),
        Category(8, "컴퓨터/IT"),
        Category(9, "과학"),
        Category(10, "여행"),
        Category(11, "건강/운동"),
        Category(12, "글로벌"),
    )

val categoryEntitiesTestData =
    listOf(
        CategoryEntity(1, "일상"),
        CategoryEntity(2, "독서"),
        CategoryEntity(3, "자기계발"),
        CategoryEntity(4, "경제"),
        CategoryEntity(5, "재테크"),
        CategoryEntity(6, "예술/문화"),
        CategoryEntity(7, "디자인"),
        CategoryEntity(8, "컴퓨터/IT"),
        CategoryEntity(9, "과학"),
        CategoryEntity(10, "여행"),
        CategoryEntity(11, "건강/운동"),
        CategoryEntity(12, "글로벌"),
    )
package com.team.presentation.home.viewmodel.testdoubles

import com.team.domain.model.category.Category

val categoriesTestData = listOf(
    Category(1, "일상"),
    Category(2, "독서"),
    Category(3, "자기계발"),
    Category(4, "경제"),
    Category(5, "제테크"),
    Category(6, "예술/문화"),
    Category(7, "디자인"),
    Category(8, "컴퓨터/IT"),
    Category(9, "과학"),
    Category(10, "여행"),
    Category(11, "건강/운동"),
    Category(12, "글로벌"),
)

val fixedCategoriesTestData = listOf(
    Category(100, "전체"),
    Category(101, "팔로잉"),
    Category(102, "인기 플립"),
)

val myCategoriesTestData = listOf(6, 7, 8)
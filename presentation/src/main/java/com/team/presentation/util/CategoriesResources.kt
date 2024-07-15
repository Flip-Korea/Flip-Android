package com.team.presentation.util

import com.team.domain.model.category.Category
import com.team.presentation.R

val CategoriesTestData = listOf(
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

/**
 * 임시 아이콘 Map
 */
val CategoryIconsMap = mapOf(
    1 to R.drawable.ic_category_daily,
    2 to R.drawable.ic_category_book,
    3 to R.drawable.ic_category_self_development,
    4 to R.drawable.ic_category_economy,
    5 to R.drawable.ic_category_investment,
    6 to R.drawable.ic_category_art_culture,
    7 to R.drawable.ic_category_design,
    8 to R.drawable.ic_category_computer_it,
    9 to R.drawable.ic_category_science,
    10 to R.drawable.ic_category_travel,
    11 to R.drawable.ic_category_health_exercise,
    12 to R.drawable.ic_category_global,
    100 to R.drawable.ic_category_following,
    101 to R.drawable.ic_category_following,
    102 to R.drawable.ic_category_popular
)
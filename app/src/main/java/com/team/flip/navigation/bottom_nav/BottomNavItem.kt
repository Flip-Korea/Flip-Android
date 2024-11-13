package com.team.flip.navigation.bottom_nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.team.flip.R
import com.team.presentation.ScreenItem

/**
 * 메인 화면에서 보여지는 BottomNavigationBar 항목을 위한 클래스
 * @param route 각 화면을 나타내는 식별 값
 * @param title 항목의 이름 리소스(contentDescription 에서도 쓰임)
 * @param icon 항목의 아이콘 리소스
 */
sealed class BottomNavItem(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    data object Home: BottomNavItem(ScreenItem.HOME.name, R.string.bottom_nav_home, R.drawable.ic_bottom_nav_home)
    data object Flip: BottomNavItem(ScreenItem.FLIP.name, R.string.bottom_nav_flip, R.drawable.ic_bottom_nav_flip)
    data object AddFlip: BottomNavItem(ScreenItem.ADD_FLIP.name, R.string.bottom_nav_add_flip, R.drawable.ic_bottom_nav_add_flip)
    data object Profile: BottomNavItem(ScreenItem.PROFILE.name, R.string.bottom_nav_profile, R.drawable.ic_bottom_nav_profile)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Flip,
    BottomNavItem.AddFlip,
    BottomNavItem.Profile,
)

val allowedBottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Flip,
    BottomNavItem.Profile,
)
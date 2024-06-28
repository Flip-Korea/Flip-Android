package com.team.flip.navigation.bottom_nav

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.team.designsystem.component.utils.dropShadow1
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.ScreenItem

/**
 * Flip의 BottomNavigationBar
 */
@Composable
fun FlipBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

//    AnimatedVisibility(
//        visible = bottomNavItems.map { it.route }.contains(currentRoute)
//    ) {
//    }
    if (allowedBottomNavItems.map { it.route }.contains(currentRoute)) {
        NavigationBar(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 61.dp)
                .dropShadow1(),
            containerColor = FlipTheme.colors.white,
            contentColor = FlipTheme.colors.gray5
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomNavItems.forEach { item ->
                    val color = if (currentRoute == item.route) {
                        FlipTheme.colors.main
                    } else FlipTheme.colors.gray5
                    val fontStyle = if (currentRoute == item.route) {
                        FlipTheme.typography.body2
                    } else FlipTheme.typography.body1

                    FlipNavigationBarItem(
                        selected = currentRoute == item.route,
                        label = {
                            Text(
                                text = stringResource(id = item.title),
                                style = fontStyle,
                                color = color,
                                maxLines = 1
                            )
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = ImageVector.vectorResource(id = item.icon),
                                contentDescription = stringResource(id = item.title),
                                tint = color
                            )
                        },
                        onClick = { onItemClickWithOptions(navController, item.route) },
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.FlipNavigationBarItem(
    selected: Boolean,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .weight(1f)
//            .background(Color.LightGray)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp, alignment = Alignment.CenterVertically)
    ) {
        icon()
        label()
    }
}

/**
 * 선택한 화면으로 이동하게 해주는 함수
 * 1. popUpTo(it) { saveState = true }:
 * 첫 번째 화면만 스택에 쌓이게 하고 백버튼 클릭 시 첫 번째 화면으로 이동한다.
 * 2. launchSingleTop: true 일 때 화면 인스턴스가 하나만 만들어진다.
 * 3. restoreState: true 일 때 버튼을 재 클릭 했을 때 이전 상태가 남아있게 한다.
 */
private fun onItemClickWithOptions(
    navController: NavHostController,
    route: String
) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let {
            // 첫번째 화면만 스택에 쌓이게 -> 백버튼 클릭 시 첫번째 화면으로 감
            if (route != ScreenItem.ADD_FLIP.name) {
                popUpTo(it) { saveState = true }
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}
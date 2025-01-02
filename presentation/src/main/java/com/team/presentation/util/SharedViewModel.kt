package com.team.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    useHiltViewModel: Boolean = true,
): T {
    val navGraphRoute =
        if (useHiltViewModel) {
            destination.parent?.route ?: return hiltViewModel()
        } else {
            destination.parent?.route ?: return viewModel()
        }
    val parentEntry =
        remember(this) {
            navController.getBackStackEntry(navGraphRoute)
        }

    val viewModel: T =
        if (useHiltViewModel) {
            hiltViewModel(parentEntry)
        } else {
            viewModel(parentEntry)
        }
    return viewModel
}

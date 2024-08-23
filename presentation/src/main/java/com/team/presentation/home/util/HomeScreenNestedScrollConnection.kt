package com.team.presentation.home.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

class HomeScreenNestedScrollConnection(
    private val onPreScrollAction: (Offset) -> Unit,
    private val onPostFlingAction: () -> Unit
): NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

        onPreScrollAction(available)

        return Offset.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {

        onPostFlingAction()

        return Velocity.Zero
    }
}
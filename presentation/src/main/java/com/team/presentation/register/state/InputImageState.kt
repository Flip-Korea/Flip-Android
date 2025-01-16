package com.team.presentation.register.state

import com.team.presentation.common.image.FlipImage

data class InputImageState(
    val image: FlipImage? = null,
    val loading: Boolean = false,
)

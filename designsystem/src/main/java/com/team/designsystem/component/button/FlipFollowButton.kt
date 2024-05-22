package com.team.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

enum class FlipFollowButtonSize {
    Large, Medium, Small
}

@Composable
fun FlipFollowButton(
    modifier: Modifier = Modifier,
    size: FlipFollowButtonSize = FlipFollowButtonSize.Medium,
    isFollowing: Boolean,
    isFollower: Boolean,
    onClick: () -> Unit,
) {

    val clickableSingle = remember { ClickableSingle.get() }

    val (textRes, containerColor, contentColor) = when {
        isFollowing -> Triple(R.string.btn_following, FlipTheme.colors.gray1, FlipTheme.colors.main)
        isFollower -> Triple(R.string.btn_follow_back, FlipTheme.colors.main, FlipTheme.colors.white)
        else -> Triple(R.string.btn_follow, FlipTheme.colors.main, FlipTheme.colors.white)
    }

    val height = when(size) {
        FlipFollowButtonSize.Large -> 34.dp
        FlipFollowButtonSize.Medium -> 34.dp
        FlipFollowButtonSize.Small -> 30.dp
    }

    Button(
        onClick = { clickableSingle.onEvent(onClick) },
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = FlipTheme.shapes.roundedCornerSmall,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = stringResource(id = textRes),
            style = LocalTextStyle.current.merge(
                FlipTheme.typography.headline1
            ),
            color = contentColor
        )
    }
}

@Preview(name = "isFollowing(false), isFollower(false)", showBackground = true)
@Composable
private fun FlipFollowButtonPreview() {
    FlipAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FlipFollowButton(
                modifier = Modifier.width(343.dp),
                isFollowing = false,
                isFollower = false,
                onClick = {}
            )
            FlipFollowButton(
                modifier = Modifier.width(144.dp),
                isFollowing = false,
                isFollower = false,
                onClick = {}
            )
            FlipFollowButton(
                modifier = Modifier.width(81.dp),
                isFollowing = false,
                isFollower = false,
                onClick = {}
            )
        }
    }
}

@Preview(name = "isFollowing(true), isFollower(false)", showBackground = true)
@Composable
private fun FlipFollowButton2Preview() {
    FlipAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FlipFollowButton(
                modifier = Modifier.width(343.dp),
                isFollowing = true,
                isFollower = false,
                onClick = {}
            )
            FlipFollowButton(
                modifier = Modifier.width(144.dp),
                isFollowing = true,
                isFollower = false,
                onClick = {}
            )
            FlipFollowButton(
                modifier = Modifier.width(81.dp),
                isFollowing = true,
                isFollower = false,
                onClick = {}
            )
        }
    }
}

@Preview(name = "isFollowing(false), isFollower(true)", showBackground = true)
@Composable
private fun FlipFollowButton3Preview() {
    FlipAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FlipFollowButton(
                modifier = Modifier.width(343.dp),
                isFollowing = false,
                isFollower = true,
                onClick = {}
            )
            FlipFollowButton(
                modifier = Modifier.width(144.dp),
                isFollowing = false,
                isFollower = true,
                onClick = {}
            )
            FlipFollowButton(
                modifier = Modifier.width(81.dp),
                isFollowing = false,
                isFollower = true,
                onClick = {}
            )
        }
    }
}

@Preview(name = "isFollowing(true), isFollower(true)", showBackground = true)
@Composable
private fun FlipFollowButton4Preview() {
    FlipAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FlipFollowButton(
                modifier = Modifier.width(343.dp),
                isFollowing = true,
                isFollower = true,
                onClick = {}
            )
            FlipFollowButton(
                modifier = Modifier.width(144.dp),
                isFollowing = true,
                isFollower = true,
                onClick = {}
            )
            FlipFollowButton(
                modifier = Modifier.width(81.dp),
                isFollowing = true,
                isFollower = true,
                onClick = {}
            )
        }
    }
}
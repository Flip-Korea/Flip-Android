package com.team.designsystem.component.dropdown

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipDropdownButton(
    modifier: Modifier = Modifier,
    isSelect: Boolean,
    text: String,
    onClick: () -> Unit
) {

    val rotationAnimated by animateFloatAsState(
        targetValue = if (isSelect) 180f else 0f,
        animationSpec = tween(),
        label = "rotationAnimation"
    )

    OutlinedButton(
        modifier = modifier.size(94.dp, 30.dp),
        onClick = onClick,
        contentPadding = PaddingValues(start = 14.dp, end = 10.dp, top = 6.dp, bottom = 6.dp),
        border = BorderStroke(1.dp, if (isSelect) FlipTheme.colors.point else FlipTheme.colors.gray4),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = FlipTheme.colors.main,
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                style = FlipTheme.typography.body3,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotationAnimated
                },
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_under),
                contentDescription = stringResource(id = R.string.content_desc_check),
                tint = FlipTheme.colors.gray5
            )
        }
    }
}

@Preview(showBackground = true )
@Composable
private fun FlipDropdownButtonPreview() {

    var isSelect by remember { mutableStateOf(false) }

    FlipDropdownButton(
        isSelect = isSelect,
        text = "Option",
        onClick = {isSelect = !isSelect}
    )
}
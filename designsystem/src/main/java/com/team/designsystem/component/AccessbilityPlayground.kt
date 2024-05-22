package com.team.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//TODO 나중에 삭제
@Preview(name = "IconButton-TouchTarget-MinimumSize", showBackground = true)
@Composable
private fun AccessibilityPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
        // default
        Icon(
            imageVector = Icons.Default.Delete,
            modifier = Modifier.clickable(onClick = { }),
            contentDescription = null
        )

        // adding padding and define size to helps us to meet the requirements of 48dp touch area
        Icon(
            imageVector = Icons.Default.Delete,
            modifier = Modifier
                .clickable(onClick = { })
                .padding(12.dp)
                .size(24.dp),
            contentDescription = null
        )

        // Use IconButton
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }
    }
}

@Preview(name = "SelectionControl-Wrapper", showBackground = true)
@Composable
private fun AccessibilityPreview2() {
    Column {

        // Not Wrapped
        var checked by remember { mutableStateOf(false) }
        Checkbox(checked = checked, onCheckedChange = { checked = !checked })

        // Wrapped
        var checked2 by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .toggleable(
                    value = checked2,
                    role = Role.Checkbox,
                    onValueChange = { checked2 = !checked2 }
                )
                .padding(horizontal = 16.dp)
                .width(200.dp)
        ) {
            Text("Option", Modifier.weight(1f))
            Checkbox(checked = checked2, onCheckedChange = null)
        }
    }
}

@Preview(name = "How to measure touch target When small box", showBackground = true)
@Composable
fun AccessibilityPreview3() {

    var clicked by remember { mutableStateOf(false) }

    Box(
        Modifier
            .size(100.dp)
            .background(if (clicked) Color.DarkGray else Color.LightGray)
    ) {
        Box(
            Modifier
                .align(Alignment.Center)
                .clickable { clicked = !clicked }
                .background(Color.Black)
//                .size(10.dp)
                .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
        )
    }
}
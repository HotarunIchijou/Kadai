package org.kaorun.kadai.ui.screens.main.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.animatedStrikethrough(
    progress: Float,
    color: Color,
    strokeWidth: Dp = 1.5.dp
): Modifier = this.drawWithContent {
    drawContent()
    if (progress > 0f) {
        val y = size.height / 2f
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width * progress, y),
            strokeWidth = strokeWidth.toPx(),
            cap = StrokeCap.Round
        )
    }
}
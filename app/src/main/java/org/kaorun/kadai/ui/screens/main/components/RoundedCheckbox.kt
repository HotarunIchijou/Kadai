package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun RoundedCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onValueChange: (Boolean) -> Unit
) {
    val transition = updateTransition(targetState = checked, label = "checkbox")

    val checkboxColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 150) },
        label = "checkboxColor"
    ) { checked ->
        if (checked) MaterialTheme.colorScheme.primary else Color.Transparent
    }

    val borderColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 150) },
        label = "borderColor"
    ) { checked ->
        if (checked) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant
    }

    val checkColor = MaterialTheme.colorScheme.onPrimary

    val checkDrawFraction by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                tween(durationMillis = 200, easing = FastOutSlowInEasing)
            } else {
                snap(delayMillis = 100)
            }
        },
        label = "checkDrawFraction"
    ) { checked ->
        if (checked) 1f else 0f
    }

    val strokeWidthPx = with(LocalDensity.current) { 2.dp.toPx() }
    val checkCache = remember { CheckDrawingCache() }

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(checkboxColor)
            .border(2.dp, borderColor, CircleShape)
            .toggleable(
                value = checked,
                role = Role.Checkbox,
                onValueChange = onValueChange
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
            drawCheck(
                checkColor = checkColor,
                checkFraction = checkDrawFraction,
                stroke = Stroke(
                    width = strokeWidthPx,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                ),
                drawingCache = checkCache
            )
        }
    }
}

private fun DrawScope.drawCheck(
    checkColor: Color,
    checkFraction: Float,
    stroke: Stroke,
    drawingCache: CheckDrawingCache,
) {
    if (checkFraction == 0f) return

    val width = size.width

    val checkCrossX = 0.4f
    val checkCrossY = 0.7f
    val leftX = 0.2f
    val leftY = 0.5f
    val rightX = 0.8f
    val rightY = 0.3f

    with(drawingCache) {
        checkPath.rewind()
        checkPath.moveTo(width * leftX, width * leftY)
        checkPath.lineTo(width * checkCrossX, width * checkCrossY)
        checkPath.lineTo(width * rightX, width * rightY)

        pathMeasure.setPath(checkPath, false)
        pathToDraw.rewind()
        pathMeasure.getSegment(0f, pathMeasure.length * checkFraction, pathToDraw, true)
    }

    drawPath(drawingCache.pathToDraw, checkColor, style = stroke)
}

@Immutable
private class CheckDrawingCache(
    val checkPath: Path = Path(),
    val pathMeasure: PathMeasure = PathMeasure(),
    val pathToDraw: Path = Path(),
)
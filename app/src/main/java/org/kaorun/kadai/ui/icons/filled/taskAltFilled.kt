package org.kaorun.kadai.ui.icons.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val task_alt_filled: ImageVector
  get() {
    if (_task_alt_filled != null) {
      return _task_alt_filled!!
    }
    _task_alt_filled =
      ImageVector.Builder(
          name = "task_alt",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(9.98f, 20f)
            quadTo(6.63f, 20f, 4.29f, 17.68f)
            reflectiveQuadTo(1.95f, 12f)
            reflectiveQuadTo(4.29f, 6.32f)
            reflectiveQuadTo(9.98f, 4f)
            horizontalLineTo(14f)
            quadToRelative(3.35f, 0f, 5.69f, 2.32f)
            reflectiveQuadTo(22.03f, 12f)
            reflectiveQuadToRelative(-2.34f, 5.68f)
            reflectiveQuadTo(14f, 20f)
            horizontalLineTo(9.98f)
            close()
            moveToRelative(0.6f, -6.33f)
            lineTo(8.45f, 11.55f)
            quadTo(8.18f, 11.25f, 7.76f, 11.25f)
            reflectiveQuadToRelative(-0.71f, 0.3f)
            reflectiveQuadToRelative(-0.3f, 0.71f)
            reflectiveQuadToRelative(0.3f, 0.69f)
            lineToRelative(2.47f, 2.47f)
            quadToRelative(0.45f, 0.45f, 1.08f, 0.45f)
            reflectiveQuadToRelative(1.05f, -0.45f)
            lineToRelative(5.3f, -5.3f)
            quadToRelative(0.3f, -0.28f, 0.3f, -0.7f)
            reflectiveQuadToRelative(-0.3f, -0.7f)
            quadToRelative(-0.3f, -0.3f, -0.71f, -0.3f)
            reflectiveQuadToRelative(-0.71f, 0.3f)
            lineToRelative(-4.95f, 4.95f)
            close()
          }
        }
        .build()
    return _task_alt_filled!!
  }

private var _task_alt_filled: ImageVector? = null

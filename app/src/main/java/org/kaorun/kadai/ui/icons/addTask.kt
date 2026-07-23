package org.kaorun.kadai.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val add_task: ImageVector
  get() {
    if (_add_task != null) {
      return _add_task!!
    }
    _add_task =
      ImageVector.Builder(
          name = "add_task",
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
            moveTo(12f, 12f)
            close()
            moveTo(9.98f, 4f)
            horizontalLineTo(14f)
            quadToRelative(3.35f, 0f, 5.69f, 2.32f)
            reflectiveQuadTo(22.03f, 12f)
            quadToRelative(0f, 0.42f, -0.29f, 0.71f)
            reflectiveQuadTo(21.03f, 13f)
            reflectiveQuadTo(20.31f, 12.71f)
            quadTo(20.03f, 12.43f, 20.03f, 12f)
            quadToRelative(0f, -2.5f, -1.78f, -4.25f)
            quadTo(16.48f, 6f, 13.98f, 6f)
            horizontalLineTo(10f)
            quadTo(7.5f, 6f, 5.73f, 7.75f)
            reflectiveQuadTo(3.95f, 12f)
            reflectiveQuadToRelative(1.78f, 4.25f)
            reflectiveQuadTo(10f, 18f)
            horizontalLineToRelative(2f)
            quadToRelative(0.43f, 0f, 0.7f, 0.29f)
            reflectiveQuadTo(12.98f, 19f)
            reflectiveQuadToRelative(-0.29f, 0.71f)
            reflectiveQuadTo(11.98f, 20f)
            horizontalLineToRelative(-2f)
            quadTo(6.63f, 20f, 4.29f, 17.68f)
            reflectiveQuadTo(1.95f, 12f)
            reflectiveQuadTo(4.29f, 6.32f)
            reflectiveQuadTo(9.98f, 4f)
            close()
            moveTo(19f, 14f)
            quadToRelative(0.43f, 0f, 0.71f, 0.29f)
            reflectiveQuadTo(20f, 15f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(2f)
            quadToRelative(0.43f, 0f, 0.71f, 0.29f)
            reflectiveQuadTo(23f, 18f)
            reflectiveQuadToRelative(-0.29f, 0.71f)
            reflectiveQuadTo(22f, 19f)
            horizontalLineTo(20f)
            verticalLineToRelative(2.02f)
            quadToRelative(0f, 0.43f, -0.29f, 0.7f)
            reflectiveQuadTo(19f, 22f)
            reflectiveQuadTo(18.29f, 21.71f)
            quadTo(18f, 21.43f, 18f, 21f)
            verticalLineTo(19f)
            horizontalLineTo(15.98f)
            quadToRelative(-0.43f, 0f, -0.7f, -0.29f)
            quadTo(15f, 18.43f, 15f, 18f)
            reflectiveQuadToRelative(0.29f, -0.71f)
            reflectiveQuadTo(16f, 17f)
            horizontalLineToRelative(2f)
            verticalLineTo(15f)
            quadToRelative(0f, -0.43f, 0.29f, -0.71f)
            reflectiveQuadTo(19f, 14f)
            close()
            moveTo(15.53f, 8.73f)
            lineToRelative(-4.95f, 4.95f)
            lineTo(8.45f, 11.55f)
            quadTo(8.15f, 11.27f, 7.75f, 11.26f)
            reflectiveQuadToRelative(-0.7f, 0.29f)
            reflectiveQuadToRelative(-0.3f, 0.71f)
            reflectiveQuadToRelative(0.3f, 0.69f)
            lineToRelative(2.47f, 2.47f)
            quadToRelative(0.45f, 0.45f, 1.06f, 0.45f)
            reflectiveQuadToRelative(1.06f, -0.45f)
            lineToRelative(5.3f, -5.3f)
            quadTo(17.23f, 9.85f, 17.23f, 9.44f)
            quadToRelative(0f, -0.41f, -0.28f, -0.71f)
            quadTo(16.63f, 8.45f, 16.23f, 8.44f)
            quadToRelative(-0.4f, -0.01f, -0.7f, 0.29f)
            close()
          }
        }
        .build()
    return _add_task!!
  }

private var _add_task: ImageVector? = null

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
val done_all: ImageVector
  get() {
    if (_done_all != null) {
      return _done_all!!
    }
    _done_all =
      ImageVector.Builder(
          name = "done_all",
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
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(1.75f, 13.05f)
            quadToRelative(-0.3f, -0.3f, -0.29f, -0.7f)
            reflectiveQuadToRelative(0.31f, -0.7f)
            quadToRelative(0.3f, -0.27f, 0.7f, -0.29f)
            reflectiveQuadToRelative(0.7f, 0.29f)
            lineTo(6.73f, 15.2f)
            quadToRelative(0.17f, 0.18f, 0.35f, 0.35f)
            quadToRelative(0.17f, 0.18f, 0.35f, 0.35f)
            quadToRelative(0.3f, 0.3f, 0.29f, 0.7f)
            reflectiveQuadTo(7.4f, 17.3f)
            quadTo(7.1f, 17.58f, 6.7f, 17.59f)
            reflectiveQuadTo(6f, 17.3f)
            lineTo(1.75f, 13.05f)
            close()
            moveToRelative(10.6f, 2.13f)
            lineToRelative(8.5f, -8.5f)
            quadToRelative(0.3f, -0.3f, 0.7f, -0.29f)
            reflectiveQuadToRelative(0.7f, 0.31f)
            quadTo(22.53f, 7f, 22.54f, 7.4f)
            reflectiveQuadTo(22.25f, 8.1f)
            lineToRelative(-9.2f, 9.2f)
            quadToRelative(-0.3f, 0.3f, -0.7f, 0.3f)
            reflectiveQuadToRelative(-0.7f, -0.3f)
            lineTo(7.4f, 13.05f)
            quadTo(7.13f, 12.77f, 7.13f, 12.36f)
            reflectiveQuadTo(7.4f, 11.65f)
            quadToRelative(0.3f, -0.3f, 0.71f, -0.3f)
            reflectiveQuadToRelative(0.71f, 0.3f)
            lineToRelative(3.53f, 3.53f)
            close()
            moveTo(16.58f, 8.13f)
            lineToRelative(-3.52f, 3.52f)
            quadToRelative(-0.28f, 0.28f, -0.69f, 0.28f)
            reflectiveQuadTo(11.65f, 11.65f)
            quadToRelative(-0.3f, -0.3f, -0.3f, -0.71f)
            quadToRelative(0f, -0.41f, 0.3f, -0.71f)
            lineTo(15.18f, 6.7f)
            quadTo(15.45f, 6.43f, 15.86f, 6.43f)
            quadToRelative(0.41f, 0f, 0.71f, 0.27f)
            quadToRelative(0.3f, 0.3f, 0.3f, 0.71f)
            reflectiveQuadToRelative(-0.3f, 0.71f)
            close()
          }
        }
        .build()
    return _done_all!!
  }

private var _done_all: ImageVector? = null

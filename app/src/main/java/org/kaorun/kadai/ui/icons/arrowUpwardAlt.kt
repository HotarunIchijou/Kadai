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
val arrow_upward_alt: ImageVector
  get() {
    if (_arrow_upward_alt != null) {
      return _arrow_upward_alt!!
    }
    _arrow_upward_alt =
      ImageVector.Builder(
          name = "arrow_upward_alt",
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
            moveTo(11f, 8.8f)
            lineTo(8.1f, 11.7f)
            quadTo(7.83f, 11.98f, 7.4f, 11.98f)
            reflectiveQuadTo(6.7f, 11.7f)
            reflectiveQuadTo(6.43f, 11f)
            reflectiveQuadTo(6.7f, 10.3f)
            lineTo(11.3f, 5.7f)
            quadTo(11.6f, 5.4f, 12f, 5.4f)
            reflectiveQuadToRelative(0.7f, 0.3f)
            lineToRelative(4.6f, 4.6f)
            quadToRelative(0.27f, 0.28f, 0.27f, 0.7f)
            reflectiveQuadTo(17.3f, 11.7f)
            reflectiveQuadToRelative(-0.7f, 0.28f)
            reflectiveQuadTo(15.9f, 11.7f)
            lineTo(13f, 8.8f)
            verticalLineTo(17f)
            quadToRelative(0f, 0.43f, -0.29f, 0.71f)
            reflectiveQuadTo(12f, 18f)
            reflectiveQuadTo(11.29f, 17.71f)
            quadTo(11f, 17.43f, 11f, 17f)
            verticalLineTo(8.8f)
            close()
          }
        }
        .build()
    return _arrow_upward_alt!!
  }

private var _arrow_upward_alt: ImageVector? = null

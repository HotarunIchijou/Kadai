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
val arrow_downward_alt: ImageVector
  get() {
    if (_arrow_downward_alt != null) {
      return _arrow_downward_alt!!
    }
    _arrow_downward_alt =
      ImageVector.Builder(
          name = "arrow_downward_alt",
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
            moveTo(11f, 14.2f)
            verticalLineTo(6f)
            quadTo(11f, 5.57f, 11.29f, 5.29f)
            reflectiveQuadTo(12f, 5f)
            reflectiveQuadToRelative(0.71f, 0.29f)
            reflectiveQuadTo(13f, 6f)
            verticalLineToRelative(8.2f)
            lineToRelative(2.9f, -2.9f)
            quadToRelative(0.28f, -0.28f, 0.7f, -0.28f)
            reflectiveQuadToRelative(0.7f, 0.28f)
            reflectiveQuadTo(17.58f, 12f)
            reflectiveQuadTo(17.3f, 12.7f)
            lineToRelative(-4.6f, 4.6f)
            quadTo(12.4f, 17.6f, 12f, 17.6f)
            reflectiveQuadTo(11.3f, 17.3f)
            lineTo(6.7f, 12.7f)
            quadTo(6.43f, 12.43f, 6.43f, 12f)
            reflectiveQuadTo(6.7f, 11.3f)
            reflectiveQuadTo(7.4f, 11.02f)
            reflectiveQuadTo(8.1f, 11.3f)
            lineTo(11f, 14.2f)
            close()
          }
        }
        .build()
    return _arrow_downward_alt!!
  }

private var _arrow_downward_alt: ImageVector? = null

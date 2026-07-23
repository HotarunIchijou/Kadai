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
val remove_done: ImageVector
  get() {
    if (_remove_done != null) {
      return _remove_done!!
    }
    _remove_done =
      ImageVector.Builder(
          name = "remove_done",
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
            moveTo(21.13f, 21.1f)
            lineToRelative(-5.9f, -5.9f)
            lineToRelative(-2.15f, 2.15f)
            quadToRelative(-0.3f, 0.3f, -0.7f, 0.3f)
            reflectiveQuadToRelative(-0.7f, -0.3f)
            lineTo(7.43f, 13.1f)
            quadTo(7.13f, 12.8f, 7.14f, 12.4f)
            reflectiveQuadTo(7.43f, 11.7f)
            quadToRelative(0.3f, -0.3f, 0.71f, -0.31f)
            reflectiveQuadToRelative(0.71f, 0.29f)
            lineToRelative(3.53f, 3.53f)
            lineToRelative(1.4f, -1.4f)
            lineTo(4.13f, 4.15f)
            quadTo(3.83f, 3.85f, 3.84f, 3.45f)
            reflectiveQuadTo(4.13f, 2.75f)
            quadTo(4.43f, 2.45f, 4.84f, 2.44f)
            quadTo(5.25f, 2.42f, 5.55f, 2.72f)
            lineTo(22.53f, 19.7f)
            quadToRelative(0.27f, 0.28f, 0.27f, 0.7f)
            quadToRelative(0f, 0.43f, -0.27f, 0.7f)
            reflectiveQuadToRelative(-0.7f, 0.27f)
            reflectiveQuadTo(21.13f, 21.1f)
            close()
            moveTo(6.03f, 17.35f)
            lineTo(1.78f, 13.1f)
            quadTo(1.5f, 12.83f, 1.5f, 12.4f)
            quadToRelative(0f, -0.42f, 0.28f, -0.7f)
            reflectiveQuadToRelative(0.7f, -0.28f)
            reflectiveQuadToRelative(0.7f, 0.28f)
            lineToRelative(3.55f, 3.55f)
            lineToRelative(1.4f, 1.4f)
            lineToRelative(-0.7f, 0.7f)
            quadToRelative(-0.3f, 0.3f, -0.7f, 0.3f)
            reflectiveQuadToRelative(-0.7f, -0.3f)
            close()
            moveToRelative(12f, -4.95f)
            lineTo(16.63f, 11f)
            lineTo(20.85f, 6.77f)
            quadTo(21.13f, 6.5f, 21.53f, 6.49f)
            quadToRelative(0.4f, -0.01f, 0.7f, 0.26f)
            quadToRelative(0.3f, 0.27f, 0.31f, 0.7f)
            reflectiveQuadTo(22.25f, 8.17f)
            lineTo(18.03f, 12.4f)
            close()
            moveTo(15.18f, 9.55f)
            lineToRelative(-1.4f, -1.4f)
            lineTo(15.23f, 6.7f)
            quadTo(15.5f, 6.43f, 15.93f, 6.43f)
            reflectiveQuadToRelative(0.7f, 0.27f)
            reflectiveQuadTo(16.9f, 7.4f)
            reflectiveQuadTo(16.63f, 8.1f)
            lineTo(15.18f, 9.55f)
            close()
          }
        }
        .build()
    return _remove_done!!
  }

private var _remove_done: ImageVector? = null

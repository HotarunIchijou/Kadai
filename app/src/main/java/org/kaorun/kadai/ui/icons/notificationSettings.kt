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
val notification_settings: ImageVector
  get() {
    if (_notification_settings != null) {
      return _notification_settings!!
    }
    _notification_settings =
      ImageVector.Builder(
          name = "notification_settings",
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
            moveTo(12f, 11.77f)
            close()
            moveTo(12f, 22f)
            quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
            reflectiveQuadTo(10f, 20f)
            horizontalLineToRelative(4f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(12f, 22f)
            close()
            moveTo(6f, 17f)
            verticalLineTo(10f)
            quadTo(6f, 8.2f, 6.94f, 6.77f)
            reflectiveQuadTo(9.45f, 4.57f)
            quadTo(9.8f, 4.4f, 10.1f, 4.56f)
            quadToRelative(0.3f, 0.16f, 0.45f, 0.51f)
            reflectiveQuadToRelative(0.04f, 0.75f)
            reflectiveQuadTo(9.98f, 6.55f)
            quadTo(9.05f, 7.13f, 8.53f, 8f)
            reflectiveQuadTo(8f, 10f)
            verticalLineToRelative(7f)
            horizontalLineToRelative(8f)
            verticalLineTo(14.95f)
            quadToRelative(0f, -0.42f, 0.29f, -0.71f)
            reflectiveQuadTo(17f, 13.95f)
            reflectiveQuadToRelative(0.71f, 0.29f)
            quadTo(18f, 14.53f, 18f, 14.95f)
            verticalLineTo(17f)
            horizontalLineToRelative(1f)
            quadToRelative(0.43f, 0f, 0.71f, 0.29f)
            reflectiveQuadTo(20f, 18f)
            reflectiveQuadToRelative(-0.29f, 0.71f)
            reflectiveQuadTo(19f, 19f)
            horizontalLineTo(5f)
            quadTo(4.58f, 19f, 4.29f, 18.71f)
            quadTo(4f, 18.43f, 4f, 18f)
            reflectiveQuadTo(4.29f, 17.29f)
            reflectiveQuadTo(5f, 17f)
            horizontalLineTo(6f)
            close()
            moveToRelative(9.7f, -6.5f)
            quadTo(15.4f, 10.38f, 15.14f, 10.24f)
            reflectiveQuadTo(14.6f, 9.9f)
            lineToRelative(-0.73f, 0.23f)
            quadToRelative(-0.32f, 0.1f, -0.64f, -0.03f)
            reflectiveQuadTo(12.75f, 9.7f)
            lineTo(12.55f, 9.35f)
            quadTo(12.38f, 9.05f, 12.43f, 8.7f)
            reflectiveQuadTo(12.75f, 8.13f)
            lineTo(13.3f, 7.65f)
            quadTo(13.25f, 7.32f, 13.25f, 7f)
            reflectiveQuadTo(13.3f, 6.35f)
            lineTo(12.75f, 5.88f)
            quadTo(12.48f, 5.65f, 12.43f, 5.31f)
            quadTo(12.38f, 4.97f, 12.55f, 4.67f)
            lineTo(12.78f, 4.3f)
            quadTo(12.95f, 4.02f, 13.25f, 3.9f)
            reflectiveQuadTo(13.88f, 3.88f)
            lineTo(14.6f, 4.1f)
            quadTo(14.88f, 3.9f, 15.14f, 3.76f)
            reflectiveQuadTo(15.7f, 3.5f)
            lineTo(15.85f, 2.77f)
            quadTo(15.93f, 2.42f, 16.19f, 2.21f)
            reflectiveQuadTo(16.8f, 2f)
            horizontalLineToRelative(0.4f)
            quadToRelative(0.35f, 0f, 0.61f, 0.22f)
            reflectiveQuadTo(18.15f, 2.8f)
            lineTo(18.3f, 3.5f)
            quadToRelative(0.3f, 0.13f, 0.56f, 0.26f)
            reflectiveQuadTo(19.4f, 4.1f)
            lineTo(20.13f, 3.88f)
            quadToRelative(0.33f, -0.1f, 0.64f, 0.02f)
            reflectiveQuadToRelative(0.49f, 0.4f)
            lineToRelative(0.2f, 0.35f)
            quadToRelative(0.17f, 0.3f, 0.13f, 0.65f)
            reflectiveQuadTo(21.25f, 5.88f)
            lineTo(20.7f, 6.35f)
            quadTo(20.75f, 6.68f, 20.75f, 7f)
            reflectiveQuadTo(20.7f, 7.65f)
            lineToRelative(0.55f, 0.48f)
            quadToRelative(0.28f, 0.22f, 0.33f, 0.56f)
            quadToRelative(0.05f, 0.34f, -0.13f, 0.64f)
            lineTo(21.23f, 9.7f)
            quadToRelative(-0.18f, 0.28f, -0.48f, 0.4f)
            reflectiveQuadToRelative(-0.63f, 0.03f)
            lineTo(19.4f, 9.9f)
            quadToRelative(-0.28f, 0.2f, -0.54f, 0.34f)
            quadTo(18.6f, 10.38f, 18.3f, 10.5f)
            lineToRelative(-0.15f, 0.72f)
            quadToRelative(-0.08f, 0.35f, -0.34f, 0.56f)
            reflectiveQuadTo(17.2f, 12f)
            horizontalLineTo(16.8f)
            quadToRelative(-0.35f, 0f, -0.61f, -0.23f)
            reflectiveQuadTo(15.85f, 11.2f)
            lineTo(15.7f, 10.5f)
            close()
            moveTo(18.41f, 8.41f)
            quadTo(19f, 7.82f, 19f, 7f)
            reflectiveQuadTo(18.41f, 5.59f)
            reflectiveQuadTo(17f, 5f)
            reflectiveQuadTo(15.59f, 5.59f)
            quadTo(15f, 6.18f, 15f, 7f)
            reflectiveQuadToRelative(0.59f, 1.41f)
            reflectiveQuadTo(17f, 9f)
            reflectiveQuadTo(18.41f, 8.41f)
            close()
          }
        }
        .build()
    return _notification_settings!!
  }

private var _notification_settings: ImageVector? = null

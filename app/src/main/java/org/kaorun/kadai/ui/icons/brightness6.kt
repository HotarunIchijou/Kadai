package org.kaorun.kadai.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val brightness_6: ImageVector
    get() {
        if (_brightness_6 != null) {
            return _brightness_6!!
        }
        _brightness_6 =
            Builder(
                name = "brightness_6",
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
                        moveTo(8.65f, 20f)
                        horizontalLineTo(6f)
                        quadTo(5.18f, 20f, 4.59f, 19.41f)
                        reflectiveQuadTo(4f, 18f)
                        verticalLineTo(15.35f)
                        lineTo(2.08f, 13.4f)
                        quadTo(1.8f, 13.1f, 1.65f, 12.74f)
                        quadTo(1.5f, 12.38f, 1.5f, 12f)
                        reflectiveQuadTo(1.65f, 11.26f)
                        reflectiveQuadTo(2.08f, 10.6f)
                        lineTo(4f, 8.65f)
                        verticalLineTo(6f)
                        quadTo(4f, 5.18f, 4.59f, 4.59f)
                        reflectiveQuadTo(6f, 4f)
                        horizontalLineTo(8.65f)
                        lineTo(10.6f, 2.07f)
                        quadTo(10.9f, 1.8f, 11.26f, 1.65f)
                        reflectiveQuadTo(12f, 1.5f)
                        reflectiveQuadToRelative(0.74f, 0.15f)
                        reflectiveQuadTo(13.4f, 2.07f)
                        lineTo(15.35f, 4f)
                        horizontalLineTo(18f)
                        quadToRelative(0.82f, 0f, 1.41f, 0.59f)
                        quadTo(20f, 5.18f, 20f, 6f)
                        verticalLineTo(8.65f)
                        lineToRelative(1.93f, 1.95f)
                        quadToRelative(0.28f, 0.3f, 0.43f, 0.66f)
                        reflectiveQuadTo(22.5f, 12f)
                        reflectiveQuadToRelative(-0.15f, 0.74f)
                        reflectiveQuadTo(21.93f, 13.4f)
                        lineTo(20f, 15.35f)
                        verticalLineTo(18f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(18f, 20f)
                        horizontalLineTo(15.35f)
                        lineTo(13.4f, 21.93f)
                        quadToRelative(-0.3f, 0.27f, -0.66f, 0.43f)
                        reflectiveQuadTo(12f, 22.5f)
                        reflectiveQuadTo(11.26f, 22.35f)
                        reflectiveQuadTo(10.6f, 21.93f)
                        lineTo(8.65f, 20f)
                        close()
                        moveTo(9.5f, 18f)
                        lineTo(12f, 20.5f)
                        lineTo(14.5f, 18f)
                        horizontalLineTo(18f)
                        verticalLineTo(14.5f)
                        lineTo(20.5f, 12f)
                        lineTo(18f, 9.5f)
                        verticalLineTo(6f)
                        horizontalLineTo(14.5f)
                        lineTo(12f, 3.5f)
                        lineTo(9.5f, 6f)
                        horizontalLineTo(6f)
                        verticalLineTo(9.5f)
                        lineTo(3.5f, 12f)
                        lineTo(6f, 14.5f)
                        verticalLineTo(18f)
                        horizontalLineTo(9.5f)
                        close()
                        moveTo(12f, 17f)
                        quadToRelative(2.08f, 0f, 3.54f, -1.46f)
                        reflectiveQuadTo(17f, 12f)
                        quadTo(17f, 9.92f, 15.54f, 8.46f)
                        reflectiveQuadTo(12f, 7f)
                        verticalLineTo(17f)
                        close()
                    }
                }
                .build()
        return _brightness_6!!
    }

private var _brightness_6: ImageVector? = null

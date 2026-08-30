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
val colors: ImageVector
    get() {
        if (_colors != null) {
            return _colors!!
        }
        _colors =
            Builder(
                name = "colors",
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
                        moveTo(8.65f, 20.5f)
                        lineTo(2.5f, 14.35f)
                        quadTo(2.25f, 14.1f, 2.13f, 13.8f)
                        reflectiveQuadTo(2f, 13.18f)
                        reflectiveQuadTo(2.13f, 12.55f)
                        reflectiveQuadTo(2.5f, 12f)
                        lineTo(8.25f, 6.27f)
                        lineTo(6.38f, 4.4f)
                        quadTo(6.05f, 4.07f, 6.04f, 3.63f)
                        quadTo(6.03f, 3.17f, 6.35f, 2.82f)
                        quadTo(6.68f, 2.47f, 7.15f, 2.47f)
                        reflectiveQuadTo(7.98f, 2.82f)
                        lineTo(17.15f, 12f)
                        quadToRelative(0.25f, 0.25f, 0.36f, 0.55f)
                        quadToRelative(0.11f, 0.3f, 0.11f, 0.63f)
                        reflectiveQuadTo(17.51f, 13.8f)
                        reflectiveQuadToRelative(-0.36f, 0.55f)
                        lineTo(11f, 20.5f)
                        quadToRelative(-0.25f, 0.25f, -0.55f, 0.38f)
                        reflectiveQuadTo(9.83f, 21f)
                        reflectiveQuadTo(9.2f, 20.88f)
                        reflectiveQuadTo(8.65f, 20.5f)
                        close()
                        moveTo(9.83f, 7.85f)
                        lineTo(4.48f, 13.2f)
                        horizontalLineToRelative(10.7f)
                        lineTo(9.83f, 7.85f)
                        close()
                        moveTo(19.8f, 21f)
                        quadToRelative(-0.9f, 0f, -1.52f, -0.64f)
                        reflectiveQuadTo(17.65f, 18.8f)
                        quadToRelative(0f, -0.68f, 0.34f, -1.28f)
                        quadToRelative(0.34f, -0.6f, 0.76f, -1.17f)
                        lineToRelative(0.48f, -0.6f)
                        quadToRelative(0.23f, -0.28f, 0.59f, -0.29f)
                        quadToRelative(0.36f, -0.01f, 0.59f, 0.26f)
                        lineToRelative(0.5f, 0.63f)
                        quadToRelative(0.4f, 0.57f, 0.75f, 1.17f)
                        quadTo(22f, 18.13f, 22f, 18.8f)
                        quadToRelative(0f, 0.93f, -0.65f, 1.56f)
                        reflectiveQuadTo(19.8f, 21f)
                        close()
                    }
                }
                .build()
        return _colors!!
    }

private var _colors: ImageVector? = null

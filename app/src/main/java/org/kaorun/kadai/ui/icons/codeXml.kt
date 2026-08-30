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
val code_xml: ImageVector
    get() {
        if (_code_xml != null) {
            return _code_xml!!
        }
        _code_xml =
            Builder(
                name = "code_xml",
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
                        moveTo(3.83f, 12f)
                        lineTo(6.7f, 14.88f)
                        quadToRelative(0.28f, 0.3f, 0.29f, 0.71f)
                        reflectiveQuadTo(6.7f, 16.3f)
                        reflectiveQuadTo(6f, 16.6f)
                        reflectiveQuadTo(5.3f, 16.3f)
                        lineTo(1.7f, 12.7f)
                        quadTo(1.4f, 12.4f, 1.4f, 12f)
                        reflectiveQuadTo(1.7f, 11.3f)
                        lineTo(5.3f, 7.7f)
                        quadTo(5.6f, 7.4f, 6f, 7.4f)
                        reflectiveQuadTo(6.7f, 7.7f)
                        reflectiveQuadTo(7f, 8.41f)
                        reflectiveQuadTo(6.7f, 9.13f)
                        lineTo(3.83f, 12f)
                        close()
                        moveToRelative(5.09f, 7.51f)
                        quadTo(8.73f, 19.15f, 8.85f, 18.75f)
                        lineToRelative(4.4f, -14.1f)
                        quadToRelative(0.13f, -0.4f, 0.49f, -0.59f)
                        reflectiveQuadTo(14.5f, 4f)
                        reflectiveQuadToRelative(0.59f, 0.49f)
                        reflectiveQuadToRelative(0.06f, 0.76f)
                        lineToRelative(-4.4f, 14.1f)
                        quadToRelative(-0.13f, 0.4f, -0.49f, 0.59f)
                        reflectiveQuadTo(9.5f, 20f)
                        reflectiveQuadTo(8.91f, 19.51f)
                        close()
                        moveTo(20.18f, 12f)
                        lineTo(17.3f, 9.13f)
                        quadTo(17f, 8.82f, 17f, 8.41f)
                        reflectiveQuadTo(17.3f, 7.7f)
                        reflectiveQuadTo(18f, 7.4f)
                        reflectiveQuadToRelative(0.7f, 0.3f)
                        lineToRelative(3.6f, 3.6f)
                        quadToRelative(0.3f, 0.3f, 0.3f, 0.7f)
                        reflectiveQuadToRelative(-0.3f, 0.7f)
                        lineToRelative(-3.6f, 3.6f)
                        quadTo(18.4f, 16.6f, 18f, 16.6f)
                        reflectiveQuadTo(17.3f, 16.3f)
                        reflectiveQuadTo(17f, 15.59f)
                        reflectiveQuadToRelative(0.3f, -0.71f)
                        lineTo(20.18f, 12f)
                        close()
                    }
                }
                .build()
        return _code_xml!!
    }

private var _code_xml: ImageVector? = null

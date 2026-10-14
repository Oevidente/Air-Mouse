package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.MouseActiveHighlight
import com.example.ui.theme.MouseBodyBase

enum class MouseButtonHighlight {
    LEFT,
    RIGHT,
    MIDDLE,
    NONE
}

@Composable
fun IosMouseIcon(
    highlight: MouseButtonHighlight,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 48.dp
) {
    Canvas(modifier = modifier.size(sizeDp, sizeDp * 1.35f)) {
        val w = size.width
        val h = size.height
        val cornerRadius = w * 0.42f

        // Draw outer body outline
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(GlassRimHighlight.copy(alpha = 0.8f), GlassRimHighlight.copy(alpha = 0.25f))
            ),
            topLeft = Offset.Zero,
            size = Size(w, h),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            style = Stroke(width = 2.dp.toPx())
        )

        // Mouse body background
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(MouseBodyBase.copy(alpha = 0.4f), Color(0xFF090D14).copy(alpha = 0.6f))
            ),
            topLeft = Offset(1.dp.toPx(), 1.dp.toPx()),
            size = Size(w - 2.dp.toPx(), h - 2.dp.toPx()),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )

        val buttonHeight = h * 0.45f
        val dividerWidth = 1.5.dp.toPx()

        // Center separator line
        drawLine(
            color = GlassRimHighlight.copy(alpha = 0.6f),
            start = Offset(w / 2f, 0f),
            end = Offset(w / 2f, buttonHeight),
            strokeWidth = dividerWidth
        )

        // Horizontal line
        drawLine(
            color = GlassRimHighlight.copy(alpha = 0.4f),
            start = Offset(w * 0.12f, buttonHeight),
            end = Offset(w * 0.88f, buttonHeight),
            strokeWidth = dividerWidth
        )

        // Highlight active click button
        when (highlight) {
            MouseButtonHighlight.LEFT -> {
                val leftPath = Path().apply {
                    moveTo(w / 2f, 0f)
                    lineTo(cornerRadius, 0f)
                    cubicTo(0f, 0f, 0f, cornerRadius, 0f, cornerRadius)
                    lineTo(0f, buttonHeight)
                    lineTo(w / 2f, buttonHeight)
                    close()
                }
                drawPath(
                    path = leftPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MouseActiveHighlight.copy(alpha = 0.95f),
                            AccentIceBlue.copy(alpha = 0.7f)
                        )
                    )
                )
            }
            MouseButtonHighlight.RIGHT -> {
                val rightPath = Path().apply {
                    moveTo(w / 2f, 0f)
                    lineTo(w - cornerRadius, 0f)
                    cubicTo(w, 0f, w, cornerRadius, w, cornerRadius)
                    lineTo(w, buttonHeight)
                    lineTo(w / 2f, buttonHeight)
                    close()
                }
                drawPath(
                    path = rightPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MouseActiveHighlight.copy(alpha = 0.95f),
                            AccentIceBlue.copy(alpha = 0.7f)
                        )
                    )
                )
            }
            MouseButtonHighlight.MIDDLE -> {
                // Highlight middle button area
            }
            MouseButtonHighlight.NONE -> {}
        }

        // Draw physical Scroll Wheel (Middle Click) in center
        val wheelWidth = w * 0.20f
        val wheelHeight = buttonHeight * 0.55f
        val wheelX = (w - wheelWidth) / 2f
        val wheelY = buttonHeight * 0.22f
        val wheelCorner = wheelWidth * 0.42f

        val isMiddleActive = highlight == MouseButtonHighlight.MIDDLE

        // Wheel background fill
        drawRoundRect(
            brush = if (isMiddleActive) {
                Brush.verticalGradient(listOf(MouseActiveHighlight, AccentIceBlue))
            } else {
                Brush.verticalGradient(listOf(Color(0xFF263348), Color(0xFF0F1520)))
            },
            topLeft = Offset(wheelX, wheelY),
            size = Size(wheelWidth, wheelHeight),
            cornerRadius = CornerRadius(wheelCorner, wheelCorner)
        )

        // Wheel border
        drawRoundRect(
            color = if (isMiddleActive) Color.White else GlassRimHighlight.copy(alpha = 0.8f),
            topLeft = Offset(wheelX, wheelY),
            size = Size(wheelWidth, wheelHeight),
            cornerRadius = CornerRadius(wheelCorner, wheelCorner),
            style = Stroke(width = 1.2.dp.toPx())
        )

        // Wheel textured ridges
        val ridgeColor = if (isMiddleActive) Color(0xFF0C1322) else GlassRimHighlight.copy(alpha = 0.6f)
        drawLine(
            color = ridgeColor,
            start = Offset(wheelX + 1.5.dp.toPx(), wheelY + wheelHeight * 0.33f),
            end = Offset(wheelX + wheelWidth - 1.5.dp.toPx(), wheelY + wheelHeight * 0.33f),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = ridgeColor,
            start = Offset(wheelX + 1.5.dp.toPx(), wheelY + wheelHeight * 0.66f),
            end = Offset(wheelX + wheelWidth - 1.5.dp.toPx(), wheelY + wheelHeight * 0.66f),
            strokeWidth = 1.dp.toPx()
        )
    }
}

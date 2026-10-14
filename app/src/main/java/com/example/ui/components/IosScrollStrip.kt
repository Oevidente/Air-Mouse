package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.GlassRimShadow
import com.example.ui.theme.GlassSurfaceBottom
import com.example.ui.theme.GlassSurfaceTop
import com.example.ui.theme.IosTextMuted
import com.example.ui.theme.IosTextPrimary
import com.example.ui.theme.IosTextSecondary
import com.example.ui.theme.KeyBaseGradientEnd
import com.example.ui.theme.StatusConnectedGreen
import kotlin.math.roundToInt

@Composable
fun IosScrollStrip(
    onScroll: (dy: Int, dx: Int) -> Unit,
    modifier: Modifier = Modifier,
    onMiddleClick: (() -> Unit)? = null,
    heightDp: Dp = 230.dp,
    testTag: String = "air_mouse_scroll_strip"
) {
    var isDragging by remember { mutableStateOf(false) }
    var accumulatedDrag by remember { mutableFloatStateOf(0f) }

    val shape = RoundedCornerShape(26.dp)

    Column(
        modifier = modifier
            .width(52.dp)
            .height(heightDp)
            .testTag(testTag)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF161F2E).copy(alpha = 0.90f),
                        Color(0xFF0C101A).copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.3.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        if (isDragging) AccentIceBlue.copy(alpha = 0.9f) else GlassRimHighlight.copy(alpha = 0.6f),
                        GlassRimShadow.copy(alpha = 0.3f)
                    )
                ),
                shape = shape
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Tap Zone: Scroll Up ▲
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A2436).copy(alpha = 0.6f))
                .clickable { onScroll(1, 0) }
                .testTag("btn_scroll_up"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Rolar para Cima",
                tint = if (isDragging) AccentIceBlue else IosTextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        // Center Interactive Trackpad Wheel Zone
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isDragging) {
                            listOf(Color(0xFF24344E), Color(0xFF111927))
                        } else {
                            listOf(Color(0xFF131A26), Color(0xFF090D14))
                        }
                    )
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { onMiddleClick?.invoke() },
                        onTap = { onMiddleClick?.invoke() }
                    )
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragStart = {
                            isDragging = true
                            accumulatedDrag = 0f
                        },
                        onDragEnd = {
                            isDragging = false
                            accumulatedDrag = 0f
                        },
                        onDragCancel = {
                            isDragging = false
                            accumulatedDrag = 0f
                        },
                        onVerticalDrag = { _, dragAmount ->
                            // Natural scroll: drag up scrolls up, drag down scrolls down
                            accumulatedDrag += -dragAmount
                            val step = 14f
                            if (accumulatedDrag >= step) {
                                val units = (accumulatedDrag / step).toInt()
                                onScroll(units, 0)
                                accumulatedDrag -= units * step
                            } else if (accumulatedDrag <= -step) {
                                val units = (-accumulatedDrag / step).toInt()
                                onScroll(-units, 0)
                                accumulatedDrag += units * step
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Visual ridges of the scroll wheel
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.UnfoldMore,
                    contentDescription = "Roda de Rolagem",
                    tint = if (isDragging) AccentIceBlue else IosTextMuted,
                    modifier = Modifier.size(16.dp)
                )

                Canvas(
                    modifier = Modifier
                        .width(28.dp)
                        .height(34.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val barColor = if (isDragging) AccentIceBlue else GlassRimHighlight.copy(alpha = 0.7f)

                    for (i in 0..4) {
                        val y = h * (i + 1) / 6f
                        drawLine(
                            color = barColor,
                            start = Offset(w * 0.15f, y),
                            end = Offset(w * 0.85f, y),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }

                Text(
                    text = "ROLA",
                    color = if (isDragging) AccentIceBlue else IosTextMuted,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Bottom Tap Zone: Scroll Down ▼
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A2436).copy(alpha = 0.6f))
                .clickable { onScroll(-1, 0) }
                .testTag("btn_scroll_down"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Rolar para Baixo",
                tint = if (isDragging) AccentIceBlue else IosTextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

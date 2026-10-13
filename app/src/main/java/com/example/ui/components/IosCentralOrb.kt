package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.GlassRimShadow
import kotlin.math.roundToInt

@Composable
fun IosCentralOrb(
    isAirMouseActive: Boolean,
    onMove: (dx: Float, dy: Float) -> Unit,
    onScroll: (dy: Int, dx: Int) -> Unit,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    sensitivity: Float = 1.6f,
    onRightClick: (() -> Unit)? = null,
    sizeDp: Dp = 270.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbPulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    Box(
        modifier = modifier
            .size(sizeDp)
            .drawBehind {
                // Outer diffuse atmospheric halo
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GlassRimHighlight.copy(alpha = if (isAirMouseActive) 0.28f * pulseGlow else 0.12f),
                            Color.Transparent
                        ),
                        radius = size.minDimension / 1.5f
                    )
                )
            }
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF131A27).copy(alpha = 0.55f),
                        Color(0xFF090C13).copy(alpha = 0.85f),
                        Color(0xFF05060A)
                    )
                )
            )
            .border(
                width = 1.4.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        GlassRimHighlight.copy(alpha = 0.85f),
                        GlassRimHighlight.copy(alpha = 0.25f),
                        GlassRimHighlight.copy(alpha = 0.7f),
                        GlassRimHighlight.copy(alpha = 0.85f)
                    )
                ),
                shape = CircleShape
            )
            .testTag("trackpad_orb")
            .pointerInput(sensitivity) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val pointers = event.changes.filter { it.pressed }
                        when (pointers.size) {
                            1 -> {
                                val change = pointers[0].positionChange()
                                if (change != Offset.Zero) {
                                    pointers[0].consume()
                                    onMove(change.x * sensitivity, change.y * sensitivity)
                                }
                            }
                            2 -> {
                                val change = pointers[0].positionChange()
                                if (change != Offset.Zero) {
                                    pointers.forEach { it.consume() }
                                    val scrollAmount = (-change.y * 0.45f).roundToInt()
                                    if (scrollAmount != 0) {
                                        onScroll(scrollAmount, 0)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap() },
                    onDoubleTap = { onTap() },
                    onLongPress = { onRightClick?.invoke() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Inner Core Glass Sphere (as in the reference image)
        Box(
            modifier = Modifier
                .size(sizeDp * 0.18f)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF334768).copy(alpha = 0.7f),
                            Color(0xFF111824).copy(alpha = 0.9f)
                        )
                    )
                )
                .border(
                    width = 1.2.dp,
                    color = AccentIceBlue.copy(alpha = 0.6f),
                    shape = CircleShape
                )
        )
    }
}

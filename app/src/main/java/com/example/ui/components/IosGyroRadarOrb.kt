package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.GlassRimShadow
import com.example.ui.theme.IosTextMuted
import com.example.ui.theme.IosTextPrimary
import com.example.ui.theme.StatusConnectedGreen
import kotlin.math.roundToInt

@Composable
fun IosGyroRadarOrb(
    isAirMouseActive: Boolean,
    tiltX: Float,
    tiltY: Float,
    onToggleAirMouse: () -> Unit,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 270.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gyroOrbPulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    // Smooth target reticle offsets
    val animatedOffsetX by animateFloatAsState(
        targetValue = (tiltX * 65f).coerceIn(-65f, 65f),
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "reticleX"
    )
    val animatedOffsetY by animateFloatAsState(
        targetValue = (tiltY * 65f).coerceIn(-65f, 65f),
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "reticleY"
    )

    Box(
        modifier = modifier
            .size(sizeDp)
            .drawBehind {
                // Outer diffuse atmospheric halo
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isAirMouseActive) AccentIceBlue.copy(alpha = 0.30f * pulseGlow)
                            else Color(0xFFF59E0B).copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        radius = size.minDimension / 1.45f
                    )
                )
            }
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF131A28).copy(alpha = 0.6f),
                        Color(0xFF090D15).copy(alpha = 0.88f),
                        Color(0xFF04060A)
                    )
                )
            )
            .border(
                width = 1.4.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        GlassRimHighlight.copy(alpha = if (isAirMouseActive) 0.85f else 0.4f),
                        GlassRimShadow.copy(alpha = 0.25f),
                        GlassRimHighlight.copy(alpha = if (isAirMouseActive) 0.75f else 0.3f),
                        GlassRimHighlight.copy(alpha = if (isAirMouseActive) 0.85f else 0.4f)
                    )
                ),
                shape = CircleShape
            )
            .testTag("gyro_radar_orb"),
        contentAlignment = Alignment.Center
    ) {
        // Concentric radar guide circles
        Canvas(modifier = Modifier.size(sizeDp * 0.75f)) {
            val r = size.minDimension / 2f
            drawCircle(
                color = GlassRimHighlight.copy(alpha = 0.15f),
                radius = r,
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(
                color = GlassRimHighlight.copy(alpha = 0.22f),
                radius = r * 0.65f,
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(
                color = GlassRimHighlight.copy(alpha = 0.28f),
                radius = r * 0.35f,
                style = Stroke(width = 1.dp.toPx())
            )

            // Crosshair lines
            drawLine(
                color = GlassRimHighlight.copy(alpha = 0.18f),
                start = Offset(center.x - r * 0.85f, center.y),
                end = Offset(center.x + r * 0.85f, center.y),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = GlassRimHighlight.copy(alpha = 0.18f),
                start = Offset(center.x, center.y - r * 0.85f),
                end = Offset(center.x, center.y + r * 0.85f),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Real-time Motion Laser Indicator Dot
        if (isAirMouseActive) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            animatedOffsetX.roundToInt(),
                            animatedOffsetY.roundToInt()
                        )
                    }
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White,
                                AccentIceBlue,
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // Center Glass Toggle Button (Ativar / Pausar Air Mouse)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF223048).copy(alpha = 0.88f),
                            Color(0xFF101724).copy(alpha = 0.95f)
                        )
                    )
                )
                .border(
                    width = 1.2.dp,
                    color = if (isAirMouseActive) AccentIceBlue.copy(alpha = 0.7f) else Color(0xFFF59E0B).copy(alpha = 0.5f),
                    shape = RoundedCornerShape(30.dp)
                )
                .clickable(onClick = onToggleAirMouse)
                .padding(horizontal = 18.dp, vertical = 12.dp)
                .testTag("btn_toggle_air_mouse"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isAirMouseActive) Icons.Default.NearMe else Icons.Default.Pause,
                    contentDescription = if (isAirMouseActive) "Pausar Air Mouse" else "Ativar Air Mouse",
                    tint = if (isAirMouseActive) AccentIceBlue else Color(0xFFFBBF24),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isAirMouseActive) "AIR MOUSE ATIVO" else "PAUSADO",
                    color = IosTextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.6.sp
                )

                Text(
                    text = if (isAirMouseActive) "Toque para pausar" else "Toque para ativar",
                    color = IosTextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

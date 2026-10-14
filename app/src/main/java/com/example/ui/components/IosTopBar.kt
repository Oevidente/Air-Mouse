package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OneHandedMode
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.GlassRimShadow
import com.example.ui.theme.IosTextPrimary
import com.example.ui.theme.IosTextSecondary
import com.example.ui.theme.StatusConnectedGreen

@Composable
fun IosTopBar(
    title: String,
    isConnected: Boolean,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    oneHandedMode: OneHandedMode = OneHandedMode.OFF,
    onCycleOneHandedMode: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(32.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF141C2B).copy(alpha = 0.85f),
                        Color(0xFF0A0E17).copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GlassRimHighlight.copy(alpha = 0.5f),
                        GlassRimShadow.copy(alpha = 0.2f)
                    )
                ),
                shape = shape
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left orbital gyroscope logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            OrbitalGyroIcon(modifier = Modifier.size(26.dp))

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    color = IosTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.2.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isConnected) StatusConnectedGreen else Color(0xFFEF4444))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isConnected) "Conectado" else "Desconectado",
                        color = IosTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onCycleOneHandedMode != null) {
                val handLabel = when (oneHandedMode) {
                    OneHandedMode.OFF -> "Ambas"
                    OneHandedMode.RIGHT_HAND -> "Destro"
                    OneHandedMode.LEFT_HAND -> "Canhoto"
                }
                val isCustomHand = oneHandedMode != OneHandedMode.OFF

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isCustomHand) EmeraldNeon.copy(alpha = 0.2f) else Color(0xFF1E2838).copy(alpha = 0.6f))
                        .border(
                            1.dp,
                            if (isCustomHand) EmeraldNeon else GlassRimHighlight.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(onClick = onCycleOneHandedMode)
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("btn_cycle_one_handed_topbar"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PanTool,
                            contentDescription = "Modo Uma Mão",
                            tint = if (isCustomHand) EmeraldNeon else IosTextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = handLabel,
                            color = if (isCustomHand) EmeraldNeon else IosTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))
            }

            // Settings Gear Icon
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("btn_top_settings")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configurações",
                    tint = IosTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun OrbitalGyroIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Outer tilted orbit ellipse
        drawOval(
            brush = Brush.linearGradient(
                colors = listOf(AccentIceBlue, Color(0xFF60A5FA))
            ),
            style = Stroke(width = 2.dp.toPx())
        )

        // Center orbit nucleus
        drawCircle(
            color = Color.White,
            radius = w * 0.16f,
            center = center
        )
    }
}

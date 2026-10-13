package com.example.ui.components

import android.view.MotionEvent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AirMouseMode
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ChassisSurface
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.KeyBaseGradientEnd
import com.example.ui.theme.KeyBaseGradientStart
import com.example.ui.theme.KeyBottomShadow
import com.example.ui.theme.KeyPressedEnd
import com.example.ui.theme.KeyPressedStart
import com.example.ui.theme.KeyTopHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AirMouseTriggerButton(
    isAirMouseActive: Boolean,
    currentMode: AirMouseMode,
    onModeChange: (AirMouseMode) -> Unit,
    onActivate: () -> Unit,
    onDeactivate: () -> Unit,
    onToggle: () -> Unit,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPhysicallyPressed by remember { mutableStateOf(false) }

    // Pulsing animation when air mouse is active
    val infiniteTransition = rememberInfiniteTransition(label = "gyroPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPhysicallyPressed) 0.93f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 600f),
        label = "triggerScale"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mode Switcher Pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(ChassisSurface)
                .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ModeChip(
                label = "Segurar",
                icon = Icons.Default.PanTool,
                isSelected = currentMode == AirMouseMode.HOLD_TO_MOVE,
                onClick = { onModeChange(AirMouseMode.HOLD_TO_MOVE) }
            )
            Spacer(modifier = Modifier.width(4.dp))
            ModeChip(
                label = "Alternar",
                icon = Icons.Default.Lock,
                isSelected = currentMode == AirMouseMode.TOGGLE,
                onClick = { onModeChange(AirMouseMode.TOGGLE) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Air Mouse Trigger & Accompanying Left/Right Click Volumetric Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Quick Left Click
            VolumetricButton(
                onClick = onLeftClick,
                label = "L",
                subLabel = "Esq",
                accentColor = if (isAirMouseActive) EmeraldNeon else TextPrimary,
                shape = RoundedCornerShape(18.dp),
                testTag = "air_mouse_left_click",
                modifier = Modifier.size(54.dp, 68.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Main Volumetric Air Mouse Trigger
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .scale(scale)
                    .drawBehind {
                        // Ambient glow when active
                        if (isAirMouseActive) {
                            drawCircle(
                                color = EmeraldNeon.copy(alpha = 0.25f),
                                radius = (size.minDimension / 2f) * pulseScale,
                                center = center
                            )
                        }
                        // Bottom drop shadow
                        if (!isPhysicallyPressed) {
                            drawCircle(
                                color = KeyBottomShadow.copy(alpha = 0.85f),
                                radius = size.minDimension / 2f,
                                center = center + Offset(0f, 6.dp.toPx())
                            )
                        }
                    }
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = if (isPhysicallyPressed) {
                                listOf(KeyPressedStart, KeyPressedEnd)
                            } else if (isAirMouseActive) {
                                listOf(EmeraldDark, KeyBaseGradientEnd)
                            } else {
                                listOf(KeyBaseGradientStart, KeyBaseGradientEnd)
                            }
                        )
                    )
                    .border(
                        width = if (isAirMouseActive) 2.5.dp else 1.5.dp,
                        brush = Brush.verticalGradient(
                            colors = if (isAirMouseActive) {
                                listOf(EmeraldNeon, EmeraldGlow)
                            } else {
                                listOf(KeyTopHighlight, BorderSubtle)
                            }
                        ),
                        shape = CircleShape
                    )
                    .pointerInteropFilter { motionEvent ->
                        when (motionEvent.action) {
                            MotionEvent.ACTION_DOWN -> {
                                isPhysicallyPressed = true
                                if (currentMode == AirMouseMode.HOLD_TO_MOVE) {
                                    onActivate()
                                }
                                true
                            }
                            MotionEvent.ACTION_UP -> {
                                isPhysicallyPressed = false
                                if (currentMode == AirMouseMode.HOLD_TO_MOVE) {
                                    onDeactivate()
                                } else {
                                    onToggle()
                                }
                                true
                            }
                            MotionEvent.ACTION_CANCEL -> {
                                isPhysicallyPressed = false
                                if (currentMode == AirMouseMode.HOLD_TO_MOVE) {
                                    onDeactivate()
                                }
                                true
                            }
                            else -> false
                        }
                    }
                    .testTag("air_mouse_trigger"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = "Gatilho Air Mouse Giroscópio",
                        tint = if (isAirMouseActive) EmeraldNeon else TextSecondary,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (isAirMouseActive) "AIR MOUSE" else "GIROSCÓPIO",
                        color = if (isAirMouseActive) EmeraldNeon else TextPrimary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = if (isAirMouseActive) "TRANSMITINDO" else (if (currentMode == AirMouseMode.HOLD_TO_MOVE) "SEGURE" else "TOQUE"),
                        color = if (isAirMouseActive) EmeraldGlow else TextMuted,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Quick Right Click
            VolumetricButton(
                onClick = onRightClick,
                label = "R",
                subLabel = "Dir",
                accentColor = if (isAirMouseActive) EmeraldNeon else TextPrimary,
                shape = RoundedCornerShape(18.dp),
                testTag = "air_mouse_right_click",
                modifier = Modifier.size(54.dp, 68.dp)
            )
        }
    }
}

@Composable
private fun ModeChip(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) EmeraldDark else Color.Transparent)
            .border(
                1.dp,
                if (isSelected) EmeraldNeon.copy(alpha = 0.6f) else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) EmeraldNeon else TextMuted,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = if (isSelected) EmeraldNeon else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

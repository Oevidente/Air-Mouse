package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ChassisSurface
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.KeyBaseGradientEnd
import com.example.ui.theme.KeyBaseGradientStart
import com.example.ui.theme.KeyBottomShadow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun TouchpadSurface(
    onMove: (dx: Float, dy: Float) -> Unit,
    onScroll: (dy: Int, dx: Int) -> Unit,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onMiddleClick: () -> Unit,
    modifier: Modifier = Modifier,
    sensitivity: Float = 1.6f
) {
    var touchFeedbackText by remember { mutableStateOf("Touchpad Multi-toque") }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Touchpad Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .drawBehind {
                    drawRoundRect(
                        color = KeyBottomShadow.copy(alpha = 0.9f),
                        topLeft = Offset(0f, 6.dp.toPx()),
                        size = size,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(20.dp.toPx())
                    )
                }
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(ChassisSurface, KeyBaseGradientEnd)
                    )
                )
                .border(1.5.dp, BorderSubtle, RoundedCornerShape(20.dp))
                .testTag("touchpad_canvas")
                // Multi-finger drag & movement detection
                .pointerInput(sensitivity) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val pointers = event.changes.filter { it.pressed }

                            when (pointers.size) {
                                1 -> {
                                    // 1 finger move
                                    val change = pointers[0].positionChange()
                                    if (change != Offset.Zero) {
                                        pointers[0].consume()
                                        onMove(change.x * sensitivity, change.y * sensitivity)
                                        touchFeedbackText = "Movendo cursor"
                                    }
                                }
                                2 -> {
                                    // 2 fingers scroll
                                    val change = pointers[0].positionChange()
                                    if (change != Offset.Zero) {
                                        pointers.forEach { it.consume() }
                                        val scrollAmount = (-change.y * 0.4f).roundToInt()
                                        if (scrollAmount != 0) {
                                            onScroll(scrollAmount, 0)
                                            touchFeedbackText = "Rolando página"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // Tap detection for single and double tap clicks
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onLeftClick()
                            touchFeedbackText = "Clique Esquerdo"
                        },
                        onDoubleTap = {
                            onLeftClick()
                            touchFeedbackText = "Duplo Clique"
                        },
                        onLongPress = {
                            onRightClick()
                            touchFeedbackText = "Clique Direito (Segurar)"
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = "Superfície Touchpad",
                    tint = TextMuted.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = touchFeedbackText,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "1 dedo: mover/clicar • 2 dedos: rolar • Toque longo: direito",
                    color = TextMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Volumetric Physical Buttons: Left, Middle, Right click
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Left Click
            VolumetricButton(
                onClick = onLeftClick,
                label = "Clique Esquerdo",
                accentColor = EmeraldNeon,
                shape = RoundedCornerShape(14.dp),
                testTag = "touchpad_btn_left",
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
            )

            // Middle Click / Scroll Wheel
            VolumetricButton(
                onClick = onMiddleClick,
                icon = Icons.Default.UnfoldMore,
                label = "Scroll/Meio",
                accentColor = AccentBlue,
                shape = RoundedCornerShape(14.dp),
                testTag = "touchpad_btn_middle",
                modifier = Modifier
                    .width(78.dp)
                    .height(64.dp)
            )

            // Right Click
            VolumetricButton(
                onClick = onRightClick,
                label = "Clique Direito",
                accentColor = EmeraldNeon,
                shape = RoundedCornerShape(14.dp),
                testTag = "touchpad_btn_right",
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
            )
        }
    }
}

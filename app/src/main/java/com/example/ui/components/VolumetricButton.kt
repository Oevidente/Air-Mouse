package com.example.ui.components

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.KeyBaseGradientEnd
import com.example.ui.theme.KeyBaseGradientStart
import com.example.ui.theme.KeyBottomShadow
import com.example.ui.theme.KeyPressedEnd
import com.example.ui.theme.KeyPressedStart
import com.example.ui.theme.KeyTopHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VolumetricButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    icon: ImageVector? = null,
    label: String? = null,
    subLabel: String? = null,
    accentColor: Color? = null,
    isActive: Boolean = false,
    contentDescription: String = label ?: "Remote Button",
    testTag: String = "volumetric_button",
    onPressDown: (() -> Unit)? = null,
    onPressUp: (() -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 600f),
        label = "btnScale"
    )

    val shadowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.2f else 0.85f,
        label = "shadowAlpha"
    )

    val effectiveAccent = accentColor ?: (if (isActive) Color(0xFF00E676) else TextPrimary)

    Box(
        modifier = modifier
            .testTag(testTag)
            .scale(scale)
            .drawBehind {
                // Drop shadow underneath the button simulating real physical thickness
                if (!isPressed) {
                    drawRoundRect(
                        color = KeyBottomShadow.copy(alpha = shadowAlpha),
                        topLeft = Offset(0f, 6.dp.toPx()),
                        size = size,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                    )
                }
            }
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isPressed) {
                        listOf(KeyPressedStart, KeyPressedEnd)
                    } else if (isActive) {
                        listOf(
                            (accentColor ?: Color(0xFF00E676)).copy(alpha = 0.35f),
                            KeyBaseGradientEnd
                        )
                    } else {
                        listOf(KeyBaseGradientStart, KeyBaseGradientEnd)
                    }
                )
            )
            .border(
                width = 1.2.dp,
                brush = Brush.verticalGradient(
                    colors = if (isActive) {
                        listOf(effectiveAccent, effectiveAccent.copy(alpha = 0.4f))
                    } else if (isPressed) {
                        listOf(KeyBottomShadow, BorderSubtle)
                    } else {
                        listOf(KeyTopHighlight, BorderSubtle)
                    }
                ),
                shape = shape
            )
            .pointerInteropFilter { motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isPressed = true
                        onPressDown?.invoke()
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        isPressed = false
                        onPressUp?.invoke()
                        onClick()
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        isPressed = false
                        onPressUp?.invoke()
                        true
                    }
                    else -> false
                }
            }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = effectiveAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            if (label != null) {
                if (icon != null) Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    color = if (isActive) effectiveAccent else TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }

            if (subLabel != null) {
                Text(
                    text = subLabel,
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

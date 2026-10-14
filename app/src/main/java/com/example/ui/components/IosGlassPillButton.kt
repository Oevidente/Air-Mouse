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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.GlassRimShadow
import com.example.ui.theme.GlassSurfaceBottom
import com.example.ui.theme.GlassSurfaceTop
import com.example.ui.theme.IosTextSecondary

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IosGlassPillButton(
    label: String,
    highlight: MouseButtonHighlight,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    heightDp: Dp = 155.dp,
    testTag: String = "ios_glass_pill_button",
    onPressDown: (() -> Unit)? = null,
    onPressUp: (() -> Unit)? = null,
    horizontal: Boolean = false,
    cornerRadiusDp: Dp = 28.dp
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 500f),
        label = "pillScale"
    )

    val shape = RoundedCornerShape(cornerRadiusDp)

    Box(
        modifier = modifier
            .height(heightDp)
            .scale(scale)
            .testTag(testTag)
            .drawBehind {
                drawRoundRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GlassRimHighlight.copy(alpha = if (isPressed) 0.25f else 0.12f),
                            Color.Transparent
                        ),
                        radius = size.maxDimension * 0.7f
                    ),
                    topLeft = Offset(0f, 4.dp.toPx()),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadiusDp.toPx())
                )
            }
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isPressed) {
                        listOf(GlassSurfaceBottom, Color(0xFF07090F))
                    } else {
                        listOf(GlassSurfaceTop.copy(alpha = 0.85f), GlassSurfaceBottom.copy(alpha = 0.95f))
                    }
                )
            )
            .border(
                width = 1.4.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GlassRimHighlight.copy(alpha = if (isPressed) 0.95f else 0.75f),
                        GlassRimShadow.copy(alpha = 0.35f)
                    )
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
            .padding(horizontal = if (horizontal) 12.dp else 6.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        if (horizontal) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IosMouseIcon(
                    highlight = highlight,
                    sizeDp = 22.dp
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    color = if (isPressed) Color.White else IosTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.2.sp
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IosMouseIcon(
                    highlight = highlight,
                    sizeDp = 28.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = label,
                    color = if (isPressed) Color.White else IosTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.2.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

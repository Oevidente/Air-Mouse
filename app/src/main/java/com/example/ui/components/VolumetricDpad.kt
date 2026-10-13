package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ChassisSurface
import com.example.ui.theme.KeyBaseGradientEnd
import com.example.ui.theme.KeyBaseGradientStart
import com.example.ui.theme.KeyBottomShadow

@Composable
fun VolumetricDpad(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onCenter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dpadSize = 220.dp
    val buttonSize = 56.dp
    val centerSize = 68.dp

    Box(
        modifier = modifier
            .size(dpadSize)
            .drawBehind {
                // Sunk bevel bezel enclosing the D-pad
                drawCircle(
                    color = KeyBottomShadow.copy(alpha = 0.9f),
                    radius = size.minDimension / 2f + 4.dp.toPx(),
                    center = center + Offset(0f, 6.dp.toPx())
                )
            }
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(ChassisSurface, KeyBaseGradientEnd)
                )
            )
            .border(2.dp, BorderSubtle, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Up Button
        VolumetricButton(
            onClick = onUp,
            icon = Icons.Default.KeyboardArrowUp,
            contentDescription = "Cima / Anterior",
            shape = RoundedCornerShape(14.dp),
            testTag = "dpad_up",
            modifier = Modifier
                .size(buttonSize)
                .align(Alignment.TopCenter)
                .offset(y = 10.dp)
        )

        // Down Button
        VolumetricButton(
            onClick = onDown,
            icon = Icons.Default.KeyboardArrowDown,
            contentDescription = "Baixo / Próximo",
            shape = RoundedCornerShape(14.dp),
            testTag = "dpad_down",
            modifier = Modifier
                .size(buttonSize)
                .align(Alignment.BottomCenter)
                .offset(y = (-10).dp)
        )

        // Left Button
        VolumetricButton(
            onClick = onLeft,
            icon = Icons.Default.KeyboardArrowLeft,
            contentDescription = "Esquerda",
            shape = RoundedCornerShape(14.dp),
            testTag = "dpad_left",
            modifier = Modifier
                .size(buttonSize)
                .align(Alignment.CenterStart)
                .offset(x = 10.dp)
        )

        // Right Button
        VolumetricButton(
            onClick = onRight,
            icon = Icons.Default.KeyboardArrowRight,
            contentDescription = "Direita",
            shape = RoundedCornerShape(14.dp),
            testTag = "dpad_right",
            modifier = Modifier
                .size(buttonSize)
                .align(Alignment.CenterEnd)
                .offset(x = (-10).dp)
        )

        // Center OK Button (Dish indented with blue/neon highlight)
        VolumetricButton(
            onClick = onCenter,
            label = "OK",
            subLabel = "Enter",
            accentColor = AccentBlue,
            shape = CircleShape,
            testTag = "dpad_center",
            modifier = Modifier
                .size(centerSize)
                .align(Alignment.Center)
        )
    }
}

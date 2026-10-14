package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OneHandedMode
import com.example.ui.components.IosCentralOrb
import com.example.ui.components.IosGlassPillButton
import com.example.ui.components.IosSensitivityCard
import com.example.ui.components.IosTopBar
import com.example.ui.components.MouseButtonHighlight
import com.example.ui.components.OneHandedMouseButtons
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.IosTextSecondary

@Composable
fun TouchpadScreen(
    isConnected: Boolean,
    onMove: (dx: Float, dy: Float) -> Unit,
    onScroll: (dy: Int, dx: Int) -> Unit,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onMiddleClick: () -> Unit,
    sensitivity: Float,
    onSensitivityChange: (Float) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    oneHandedMode: OneHandedMode = OneHandedMode.OFF,
    onCycleOneHandedMode: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Top Bar with Connection and One-Handed selector
        IosTopBar(
            title = "Trackpad",
            isConnected = isConnected,
            onSettingsClick = onSettingsClick,
            oneHandedMode = oneHandedMode,
            onCycleOneHandedMode = onCycleOneHandedMode,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 2. Updated Sensitivity Card in matching iOS Liquid Glass design
        IosSensitivityCard(
            title = "Sensibilidade do Trackpad",
            subtitle = "Velocidade e precisão do cursor",
            value = sensitivity,
            onValueChange = onSensitivityChange,
            valueRange = 0.5f..4.0f,
            testTag = "touchpad_sensitivity_slider"
        )

        // 3. Central Interactive Orb Surface
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IosCentralOrb(
                isAirMouseActive = true,
                onMove = onMove,
                onScroll = onScroll,
                onTap = onLeftClick,
                onRightClick = onRightClick,
                sensitivity = sensitivity,
                sizeDp = 250.dp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = "Gesto Trackpad",
                    tint = AccentIceBlue,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "  1 dedo: mover/clicar  •  2 dedos: rolar",
                    color = IosTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 4. Ergonomic Mouse Buttons: Adapts to One-Handed Mode for large phones
        OneHandedMouseButtons(
            onLeftClick = onLeftClick,
            onMiddleClick = onMiddleClick,
            onRightClick = onRightClick,
            oneHandedMode = oneHandedMode,
            onCycleOneHandedMode = onCycleOneHandedMode,
            testTagPrefix = "btn_trackpad",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

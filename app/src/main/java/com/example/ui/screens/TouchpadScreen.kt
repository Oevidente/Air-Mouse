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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.IosCentralOrb
import com.example.ui.components.IosGlassPillButton
import com.example.ui.components.IosSensitivityCard
import com.example.ui.components.IosTopBar
import com.example.ui.components.MouseButtonHighlight
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Top Bar
        IosTopBar(
            title = "Trackpad",
            isConnected = isConnected,
            onSettingsClick = onSettingsClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

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
                sizeDp = 260.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = "Gesto Trackpad",
                    tint = AccentIceBlue,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "  1 dedo: mover/clicar  •  2 dedos: rolar página",
                    color = IosTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 4. Bottom Giant Glass Pill Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            IosGlassPillButton(
                label = "Clique Esquerdo",
                highlight = MouseButtonHighlight.LEFT,
                onClick = onLeftClick,
                heightDp = 145.dp,
                testTag = "btn_trackpad_left_click",
                modifier = Modifier.weight(1f)
            )

            IosGlassPillButton(
                label = "Clique Direito",
                highlight = MouseButtonHighlight.RIGHT,
                onClick = onRightClick,
                heightDp = 145.dp,
                testTag = "btn_trackpad_right_click",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

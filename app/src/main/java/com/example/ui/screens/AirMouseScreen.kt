package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.IosGlassPillButton
import com.example.ui.components.IosGyroRadarOrb
import com.example.ui.components.IosSensitivityCard
import com.example.ui.components.IosTopBar
import com.example.ui.components.MouseButtonHighlight
import com.example.ui.components.OrbitalGyroIcon
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.IosTextMuted
import com.example.ui.theme.IosTextSecondary

@Composable
fun AirMouseScreen(
    isConnected: Boolean,
    isAirMouseActive: Boolean,
    gyroSensitivity: Float,
    tiltX: Float,
    tiltY: Float,
    onToggleAirMouse: () -> Unit,
    onRecalibrate: () -> Unit,
    onSensitivityChange: (Float) -> Unit,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
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
        // 1. Top Floating Pill
        IosTopBar(
            title = "Air Mouse",
            isConnected = isConnected,
            onSettingsClick = onSettingsClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        // 2. Gyroscope Sensitivity Card (in the same iOS liquid glass style)
        IosSensitivityCard(
            title = "Sensibilidade do Giroscópio",
            subtitle = "Velocidade de movimento no ar",
            value = gyroSensitivity,
            onValueChange = onSensitivityChange,
            valueRange = 0.5f..4.5f,
            testTag = "gyro_sensitivity_slider"
        )

        // 3. Center Spherical Gyroscope Radar Orb
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IosGyroRadarOrb(
                isAirMouseActive = isAirMouseActive,
                tiltX = tiltX,
                tiltY = tiltY,
                onToggleAirMouse = onToggleAirMouse,
                sizeDp = 260.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle with Gyro Icon & Recalibrate button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OrbitalGyroIcon(modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAirMouseActive) "Aponte o celular no ar para mover o cursor"
                    else "Air Mouse pausado • Toque para ativar",
                    color = if (isAirMouseActive) IosTextSecondary else Color(0xFFFBBF24),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Recalibrate pill button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF141C2B).copy(alpha = 0.8f))
                    .border(1.dp, GlassRimHighlight.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .clickable(onClick = onRecalibrate)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("btn_recalibrate_gyro"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Recalibrar Centro",
                        tint = AccentIceBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Recalibrar Centro",
                        color = AccentIceBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 4. Bottom Big Glass Pill Buttons (Clique Esquerdo & Clique Direito)
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
                testTag = "btn_left_click_giant",
                modifier = Modifier.weight(1f)
            )

            IosGlassPillButton(
                label = "Clique Direito",
                highlight = MouseButtonHighlight.RIGHT,
                onClick = onRightClick,
                heightDp = 145.dp,
                testTag = "btn_right_click_giant",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

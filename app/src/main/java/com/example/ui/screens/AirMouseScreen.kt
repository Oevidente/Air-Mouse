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
import com.example.data.model.OneHandedMode
import com.example.ui.components.IosGlassPillButton
import com.example.ui.components.IosGyroRadarOrb
import com.example.ui.components.IosScrollStrip
import com.example.ui.components.IosSensitivityCard
import com.example.ui.components.IosTopBar
import com.example.ui.components.MouseButtonHighlight
import com.example.ui.components.OneHandedMouseButtons
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
    onMiddleClick: () -> Unit,
    onRightClick: () -> Unit,
    onScroll: (dy: Int, dx: Int) -> Unit,
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
        // 1. Top Floating Pill with Connection + One-Handed quick toggle
        IosTopBar(
            title = "Air Mouse",
            isConnected = isConnected,
            onSettingsClick = onSettingsClick,
            oneHandedMode = oneHandedMode,
            onCycleOneHandedMode = onCycleOneHandedMode,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 2. Gyroscope Sensitivity Card
        IosSensitivityCard(
            title = "Sensibilidade do Giroscópio",
            subtitle = "Velocidade de movimento no ar",
            value = gyroSensitivity,
            onValueChange = onSensitivityChange,
            valueRange = 0.5f..4.5f,
            testTag = "gyro_sensitivity_slider"
        )

        // 3. Center Section: Gyroscope Radar Orb + Integrated Vertical Scroll Strip
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // If Left-Handed mode, place Scroll Strip on the left
                if (oneHandedMode == OneHandedMode.LEFT_HAND) {
                    IosScrollStrip(
                        onScroll = onScroll,
                        onMiddleClick = onMiddleClick,
                        heightDp = 220.dp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                // Center Spherical Gyroscope Radar Orb
                IosGyroRadarOrb(
                    isAirMouseActive = isAirMouseActive,
                    tiltX = tiltX,
                    tiltY = tiltY,
                    onToggleAirMouse = onToggleAirMouse,
                    sizeDp = if (oneHandedMode == OneHandedMode.OFF) 240.dp else 230.dp
                )

                // If Right-Handed or Default mode, place Scroll Strip on the right
                if (oneHandedMode != OneHandedMode.LEFT_HAND) {
                    IosScrollStrip(
                        onScroll = onScroll,
                        onMiddleClick = onMiddleClick,
                        heightDp = 220.dp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle with Gyro Icon & Recalibrate button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OrbitalGyroIcon(modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isAirMouseActive) "Aponte o celular no ar • Use a barra lateral para rolar"
                    else "Air Mouse pausado • Toque no orbe para ativar",
                    color = if (isAirMouseActive) IosTextSecondary else Color(0xFFFBBF24),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Recalibrate pill button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF141C2B).copy(alpha = 0.8f))
                    .border(1.dp, GlassRimHighlight.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .clickable(onClick = onRecalibrate)
                    .padding(horizontal = 14.dp, vertical = 5.dp)
                    .testTag("btn_recalibrate_gyro"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Recalibrar Centro",
                        tint = AccentIceBlue,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Recalibrar Centro",
                        color = AccentIceBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 4. Ergonomic Mouse Buttons: Adapts to One-Handed Mode for large phones
        OneHandedMouseButtons(
            onLeftClick = onLeftClick,
            onMiddleClick = onMiddleClick,
            onRightClick = onRightClick,
            oneHandedMode = oneHandedMode,
            onCycleOneHandedMode = onCycleOneHandedMode,
            testTagPrefix = "btn_air_mouse",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

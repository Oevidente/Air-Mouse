package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RemoteTab
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.GlassRimShadow
import com.example.ui.theme.IosTextMuted
import com.example.ui.theme.IosTextPrimary

@Composable
fun IosFloatingNavBar(
    currentTab: RemoteTab,
    onTabSelected: (RemoteTab) -> Unit,
    modifier: Modifier = Modifier,
    showMediaTab: Boolean = false
) {
    val pillShape = RoundedCornerShape(32.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(pillShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF141C2C).copy(alpha = 0.9f),
                            Color(0xFF090D15).copy(alpha = 0.96f)
                        )
                    )
                )
                .border(
                    width = 1.2.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GlassRimHighlight.copy(alpha = 0.45f),
                            GlassRimShadow.copy(alpha = 0.2f)
                        )
                    ),
                    shape = pillShape
                )
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavPillItem(
                label = "Air Mouse",
                icon = Icons.Default.NearMe,
                isSelected = currentTab == RemoteTab.AIR_MOUSE,
                onClick = { onTabSelected(RemoteTab.AIR_MOUSE) },
                testTag = "nav_air_mouse"
            )

            if (showMediaTab) {
                NavPillItem(
                    label = "Mídia",
                    icon = Icons.Default.PlayCircle,
                    isSelected = currentTab == RemoteTab.MEDIA_CONTROLS,
                    onClick = { onTabSelected(RemoteTab.MEDIA_CONTROLS) },
                    testTag = "nav_media"
                )
            }

            NavPillItem(
                label = "Trackpad",
                icon = Icons.Default.TouchApp,
                isSelected = currentTab == RemoteTab.TOUCHPAD,
                onClick = { onTabSelected(RemoteTab.TOUCHPAD) },
                testTag = "nav_touchpad"
            )

            NavPillItem(
                label = "Atalhos",
                icon = Icons.Default.Dashboard,
                isSelected = currentTab == RemoteTab.SHORTCUTS_DECK,
                onClick = { onTabSelected(RemoteTab.SHORTCUTS_DECK) },
                testTag = "nav_shortcuts"
            )

            NavPillItem(
                label = "PC",
                icon = Icons.Default.Computer,
                isSelected = currentTab == RemoteTab.CONNECTION_SETTINGS,
                onClick = { onTabSelected(RemoteTab.CONNECTION_SETTINGS) },
                testTag = "nav_settings"
            )
        }
    }
}

@Composable
private fun NavPillItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val activeBgColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF223047).copy(alpha = 0.75f) else Color.Transparent,
        label = "navBg"
    )

    val itemShape = RoundedCornerShape(24.dp)

    Row(
        modifier = Modifier
            .clip(itemShape)
            .background(activeBgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) AccentIceBlue else IosTextMuted,
            modifier = Modifier.size(18.dp)
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = IosTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

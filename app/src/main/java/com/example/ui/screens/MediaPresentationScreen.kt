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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.IosTopBar
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.AccentRed
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.GlassRimShadow
import com.example.ui.theme.GlassSurfaceBottom
import com.example.ui.theme.GlassSurfaceTop
import com.example.ui.theme.IosTextMuted
import com.example.ui.theme.IosTextPrimary
import com.example.ui.theme.IosTextSecondary
import com.example.ui.theme.StatusConnectedGreen

@Composable
fun MediaPresentationScreen(
    isConnected: Boolean,
    onSendKey: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Top Bar
        IosTopBar(
            title = "Mídia & Apresentações",
            isConnected = isConnected,
            onSettingsClick = onSettingsClick
        )

        // 1. Apresentador de Slides (iOS Glass Card)
        GlassSectionCard(title = "APRESENTAÇÃO DE SLIDES") {
            // Big Navigation Buttons (Slide Anterior & Slide Próximo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IosActionPill(
                    label = "Slide Anterior",
                    subLabel = "Seta Esquerda",
                    icon = Icons.Default.KeyboardArrowLeft,
                    accentColor = AccentIceBlue,
                    onClick = { onSendKey("left") },
                    modifier = Modifier.weight(1f).height(85.dp),
                    testTag = "btn_prev_slide"
                )

                IosActionPill(
                    label = "Próximo Slide",
                    subLabel = "Seta Direita",
                    icon = Icons.Default.KeyboardArrowRight,
                    accentColor = StatusConnectedGreen,
                    onClick = { onSendKey("right") },
                    modifier = Modifier.weight(1f).height(85.dp),
                    testTag = "btn_next_slide"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Presentation Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IosActionPill(
                    label = "Iniciar",
                    subLabel = "F5",
                    icon = Icons.Default.Slideshow,
                    accentColor = StatusConnectedGreen,
                    onClick = { onSendKey("F5") },
                    modifier = Modifier.weight(1f).height(74.dp),
                    testTag = "btn_f5"
                )

                IosActionPill(
                    label = "Atual",
                    subLabel = "Shift+F5",
                    icon = Icons.Default.PlayArrow,
                    accentColor = AccentIceBlue,
                    onClick = { onSendKey("SHIFT+F5") },
                    modifier = Modifier.weight(1f).height(74.dp),
                    testTag = "btn_shift_f5"
                )

                IosActionPill(
                    label = "Tela Preta",
                    subLabel = "B",
                    icon = Icons.Default.BrightnessLow,
                    accentColor = Color(0xFFFBBF24),
                    onClick = { onSendKey("b") },
                    modifier = Modifier.weight(1f).height(74.dp),
                    testTag = "btn_black_screen"
                )

                IosActionPill(
                    label = "Sair",
                    subLabel = "Esc",
                    icon = Icons.Default.Close,
                    accentColor = AccentRed,
                    onClick = { onSendKey("esc") },
                    modifier = Modifier.weight(1f).height(74.dp),
                    testTag = "btn_esc"
                )
            }
        }

        // 2. Multimídia & Volume (Control Center style)
        GlassSectionCard(title = "REPRODUÇÃO & VOLUME") {
            // Play / Pause Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IosActionPill(
                    label = "Retroceder",
                    subLabel = "Prev",
                    icon = Icons.Default.FastRewind,
                    accentColor = IosTextSecondary,
                    onClick = { onSendKey("left") },
                    modifier = Modifier.weight(1f).height(80.dp),
                    testTag = "btn_media_prev"
                )

                IosActionPill(
                    label = "Play / Pause",
                    subLabel = "Espaço",
                    icon = Icons.Default.PlayCircle,
                    accentColor = AccentIceBlue,
                    onClick = { onSendKey("play_pause") },
                    modifier = Modifier.weight(1.4f).height(80.dp),
                    testTag = "btn_media_play_pause"
                )

                IosActionPill(
                    label = "Avançar",
                    subLabel = "Next",
                    icon = Icons.Default.FastForward,
                    accentColor = IosTextSecondary,
                    onClick = { onSendKey("right") },
                    modifier = Modifier.weight(1f).height(80.dp),
                    testTag = "btn_media_next"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Volume Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IosActionPill(
                    label = "Diminuir Vol",
                    subLabel = "Vol -",
                    icon = Icons.Default.VolumeDown,
                    accentColor = AccentIceBlue,
                    onClick = { onSendKey("vol_down") },
                    modifier = Modifier.weight(1f).height(72.dp),
                    testTag = "btn_vol_down"
                )

                IosActionPill(
                    label = "Aumentar Vol",
                    subLabel = "Vol +",
                    icon = Icons.Default.VolumeUp,
                    accentColor = AccentIceBlue,
                    onClick = { onSendKey("vol_up") },
                    modifier = Modifier.weight(1f).height(72.dp),
                    testTag = "btn_vol_up"
                )

                IosActionPill(
                    label = "Mudo",
                    subLabel = "Mute",
                    icon = Icons.Default.VolumeMute,
                    accentColor = AccentRed,
                    onClick = { onSendKey("mute") },
                    modifier = Modifier.weight(1f).height(72.dp),
                    testTag = "btn_mute"
                )

                IosActionPill(
                    label = "Tela Cheia",
                    subLabel = "F11",
                    icon = Icons.Default.Fullscreen,
                    accentColor = Color(0xFF38BDF8),
                    onClick = { onSendKey("f11") },
                    modifier = Modifier.weight(1f).height(72.dp),
                    testTag = "btn_fullscreen"
                )
            }
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Composable
private fun GlassSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    val cardShape = RoundedCornerShape(28.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF131A28).copy(alpha = 0.75f),
                        Color(0xFF090D14).copy(alpha = 0.88f)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GlassRimHighlight.copy(alpha = 0.45f),
                        GlassRimShadow.copy(alpha = 0.15f)
                    )
                ),
                shape = cardShape
            )
            .padding(18.dp)
    ) {
        Text(
            text = title,
            color = IosTextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        content()
    }
}

@Composable
private fun IosActionPill(
    label: String,
    subLabel: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "ios_action_pill"
) {
    val pillShape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .clip(pillShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GlassSurfaceTop.copy(alpha = 0.8f),
                        GlassSurfaceBottom.copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GlassRimHighlight.copy(alpha = 0.5f),
                        GlassRimShadow.copy(alpha = 0.2f)
                    )
                ),
                shape = pillShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = IosTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                text = subLabel,
                color = IosTextMuted,
                fontSize = 9.sp,
                maxLines = 1
            )
        }
    }
}

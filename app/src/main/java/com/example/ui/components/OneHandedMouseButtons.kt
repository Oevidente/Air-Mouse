package com.example.ui.components

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.ViewAgenda
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OneHandedMode
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GlassRimHighlight
import com.example.ui.theme.IosTextMuted
import com.example.ui.theme.IosTextSecondary

enum class OneHandedLayoutType {
    THUMB_TIERS, // Clique Esquerdo grande embaixo + Meio e Direito em cima
    COMPACT_COLUMNS // 3 colunas compactas no canto do polegar
}

@Composable
fun OneHandedMouseButtons(
    onLeftClick: () -> Unit,
    onMiddleClick: () -> Unit,
    onRightClick: () -> Unit,
    oneHandedMode: OneHandedMode,
    modifier: Modifier = Modifier,
    testTagPrefix: String = "btn_mouse",
    onCycleOneHandedMode: (() -> Unit)? = null
) {
    var layoutType by remember { mutableStateOf(OneHandedLayoutType.THUMB_TIERS) }

    when (oneHandedMode) {
        OneHandedMode.OFF -> {
            // Standard mode: 3 full-width columns
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IosGlassPillButton(
                    label = "Clique\nEsquerdo",
                    highlight = MouseButtonHighlight.LEFT,
                    onClick = onLeftClick,
                    heightDp = 135.dp,
                    testTag = "${testTagPrefix}_left",
                    modifier = Modifier.weight(1f)
                )

                IosGlassPillButton(
                    label = "Botão\ndo Meio",
                    highlight = MouseButtonHighlight.MIDDLE,
                    onClick = onMiddleClick,
                    heightDp = 135.dp,
                    testTag = "${testTagPrefix}_middle",
                    modifier = Modifier.weight(0.9f)
                )

                IosGlassPillButton(
                    label = "Clique\nDireito",
                    highlight = MouseButtonHighlight.RIGHT,
                    onClick = onRightClick,
                    heightDp = 135.dp,
                    testTag = "${testTagPrefix}_right",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        OneHandedMode.RIGHT_HAND -> {
            // One-Handed RIGHT (Destro): Docked to the bottom-right corner (~76% width)
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                // Left margin indicator for right hand
                OneHandedSideMargin(
                    label = "Destro",
                    iconSideRight = true,
                    onClick = onCycleOneHandedMode,
                    onToggleLayout = {
                        layoutType = if (layoutType == OneHandedLayoutType.THUMB_TIERS) {
                            OneHandedLayoutType.COMPACT_COLUMNS
                        } else {
                            OneHandedLayoutType.THUMB_TIERS
                        }
                    },
                    currentLayout = layoutType,
                    modifier = Modifier
                        .weight(0.24f)
                        .padding(end = 8.dp)
                )

                // Docked cluster for right thumb
                Box(
                    modifier = Modifier
                        .weight(0.76f)
                ) {
                    Crossfade(targetState = layoutType, label = "rightHandLayout") { type ->
                        if (type == OneHandedLayoutType.THUMB_TIERS) {
                            // 2-tier ergonomic thumb layout
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IosGlassPillButton(
                                        label = "Meio",
                                        highlight = MouseButtonHighlight.MIDDLE,
                                        onClick = onMiddleClick,
                                        heightDp = 54.dp,
                                        horizontal = true,
                                        cornerRadiusDp = 16.dp,
                                        testTag = "${testTagPrefix}_middle",
                                        modifier = Modifier.weight(1f)
                                    )
                                    IosGlassPillButton(
                                        label = "Direito",
                                        highlight = MouseButtonHighlight.RIGHT,
                                        onClick = onRightClick,
                                        heightDp = 54.dp,
                                        horizontal = true,
                                        cornerRadiusDp = 16.dp,
                                        testTag = "${testTagPrefix}_right",
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                // Giant Left Click right under the right thumb
                                IosGlassPillButton(
                                    label = "Clique Esquerdo (Principal)",
                                    highlight = MouseButtonHighlight.LEFT,
                                    onClick = onLeftClick,
                                    heightDp = 76.dp,
                                    horizontal = true,
                                    cornerRadiusDp = 22.dp,
                                    testTag = "${testTagPrefix}_left",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            // Compact 3 columns docked to the right
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IosGlassPillButton(
                                    label = "Esq.",
                                    highlight = MouseButtonHighlight.LEFT,
                                    onClick = onLeftClick,
                                    heightDp = 135.dp,
                                    testTag = "${testTagPrefix}_left",
                                    modifier = Modifier.weight(1.2f)
                                )
                                IosGlassPillButton(
                                    label = "Meio",
                                    highlight = MouseButtonHighlight.MIDDLE,
                                    onClick = onMiddleClick,
                                    heightDp = 135.dp,
                                    testTag = "${testTagPrefix}_middle",
                                    modifier = Modifier.weight(0.9f)
                                )
                                IosGlassPillButton(
                                    label = "Dir.",
                                    highlight = MouseButtonHighlight.RIGHT,
                                    onClick = onRightClick,
                                    heightDp = 135.dp,
                                    testTag = "${testTagPrefix}_right",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        OneHandedMode.LEFT_HAND -> {
            // One-Handed LEFT (Canhoto): Docked to the bottom-left corner (~76% width)
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                // Docked cluster for left thumb
                Box(
                    modifier = Modifier
                        .weight(0.76f)
                ) {
                    Crossfade(targetState = layoutType, label = "leftHandLayout") { type ->
                        if (type == OneHandedLayoutType.THUMB_TIERS) {
                            // 2-tier ergonomic thumb layout for left hand
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IosGlassPillButton(
                                        label = "Direito",
                                        highlight = MouseButtonHighlight.RIGHT,
                                        onClick = onRightClick,
                                        heightDp = 54.dp,
                                        horizontal = true,
                                        cornerRadiusDp = 16.dp,
                                        testTag = "${testTagPrefix}_right",
                                        modifier = Modifier.weight(1f)
                                    )
                                    IosGlassPillButton(
                                        label = "Meio",
                                        highlight = MouseButtonHighlight.MIDDLE,
                                        onClick = onMiddleClick,
                                        heightDp = 54.dp,
                                        horizontal = true,
                                        cornerRadiusDp = 16.dp,
                                        testTag = "${testTagPrefix}_middle",
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                // Giant Left Click right under the left thumb
                                IosGlassPillButton(
                                    label = "Clique Esquerdo (Principal)",
                                    highlight = MouseButtonHighlight.LEFT,
                                    onClick = onLeftClick,
                                    heightDp = 76.dp,
                                    horizontal = true,
                                    cornerRadiusDp = 22.dp,
                                    testTag = "${testTagPrefix}_left",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            // Compact 3 columns docked to the left
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IosGlassPillButton(
                                    label = "Dir.",
                                    highlight = MouseButtonHighlight.RIGHT,
                                    onClick = onRightClick,
                                    heightDp = 135.dp,
                                    testTag = "${testTagPrefix}_right",
                                    modifier = Modifier.weight(1f)
                                )
                                IosGlassPillButton(
                                    label = "Meio",
                                    highlight = MouseButtonHighlight.MIDDLE,
                                    onClick = onMiddleClick,
                                    heightDp = 135.dp,
                                    testTag = "${testTagPrefix}_middle",
                                    modifier = Modifier.weight(0.9f)
                                )
                                IosGlassPillButton(
                                    label = "Esq.",
                                    highlight = MouseButtonHighlight.LEFT,
                                    onClick = onLeftClick,
                                    heightDp = 135.dp,
                                    testTag = "${testTagPrefix}_left",
                                    modifier = Modifier.weight(1.2f)
                                )
                            }
                        }
                    }
                }

                // Right margin indicator for left hand
                OneHandedSideMargin(
                    label = "Canhoto",
                    iconSideRight = false,
                    onClick = onCycleOneHandedMode,
                    onToggleLayout = {
                        layoutType = if (layoutType == OneHandedLayoutType.THUMB_TIERS) {
                            OneHandedLayoutType.COMPACT_COLUMNS
                        } else {
                            OneHandedLayoutType.THUMB_TIERS
                        }
                    },
                    currentLayout = layoutType,
                    modifier = Modifier
                        .weight(0.24f)
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun OneHandedSideMargin(
    label: String,
    iconSideRight: Boolean,
    onClick: (() -> Unit)?,
    onToggleLayout: () -> Unit,
    currentLayout: OneHandedLayoutType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(135.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F1522).copy(alpha = 0.7f))
            .border(1.dp, GlassRimHighlight.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Toggle hand mode icon
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF182236))
                .clickable { onClick?.invoke() }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PanTool,
                contentDescription = "Mudar mão",
                tint = EmeraldNeon,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = label,
            color = EmeraldNeon,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )

        // Switch layout type icon (2-tier vs 3-cols)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF182236))
                .clickable { onToggleLayout() }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (currentLayout == OneHandedLayoutType.THUMB_TIERS) Icons.Default.GridView else Icons.Default.ViewAgenda,
                contentDescription = "Alternar formato de botões",
                tint = IosTextSecondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GlassSurfaceFill
import com.example.ui.theme.GlassSurfaceTop
import com.example.ui.theme.KeyBaseGradientEnd
import com.example.ui.theme.KeyBaseGradientStart
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun KeyboardQuickBar(
    onSendText: (String) -> Unit,
    onSendKey: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("") }
    var isLiveMode by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(true) }
    var sendOnSpace by remember { mutableStateOf(true) }
    var pendingSpaceSend by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(GlassSurfaceFill)
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        // Bar Header: title + mode selector + collapse/expand
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp, horizontal = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Keyboard,
                    contentDescription = "Teclado Remoto",
                    tint = EmeraldNeon,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "TECLADO REMOTO",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Mode badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isLiveMode) EmeraldNeon.copy(alpha = 0.2f) else AccentIceBlue.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isLiveMode) "AO VIVO" else "EM BLOCO",
                        color = if (isLiveMode) EmeraldNeon else AccentIceBlue,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Live mode toggle pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(GlassSurfaceTop)
                        .border(1.dp, if (isLiveMode) EmeraldNeon else BorderSubtle, RoundedCornerShape(10.dp))
                        .clickable {
                            pendingSpaceSend?.cancel()
                            isLiveMode = !isLiveMode
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("toggle_keyboard_mode_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Alternar Modo",
                            tint = if (isLiveMode) EmeraldNeon else TextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isLiveMode) "Modo: Ao Vivo" else "Modo: Bloco",
                            color = if (isLiveMode) EmeraldNeon else TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .size(28.dp)
                        .testTag("btn_toggle_keyboard_expanded")
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Recolher Teclado" else "Expandir Teclado",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(10.dp))

                // Text Input Field + Send Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { newText ->
                            val previousText = textInput
                            pendingSpaceSend?.cancel()
                            if (isLiveMode) {
                                val oldText = textInput
                                // Calculate common prefix to correctly handle Gboard autocorrection,
                                // accent composition (e.g. ~ + a -> ã), word replacements (ora -> pra) and backspaces
                                var commonPrefixLen = 0
                                val minLen = minOf(oldText.length, newText.length)
                                while (commonPrefixLen < minLen && oldText[commonPrefixLen] == newText[commonPrefixLen]) {
                                    commonPrefixLen++
                                }
                                val charsToDelete = oldText.length - commonPrefixLen
                                val textToAdd = newText.substring(commonPrefixLen)

                                if (charsToDelete > 0) {
                                    repeat(charsToDelete) {
                                        onSendKey("backspace")
                                    }
                                }
                                if (textToAdd.isNotEmpty()) {
                                    onSendText(textToAdd)
                                }
                                textInput = newText
                            } else {
                                textInput = newText
                                if (sendOnSpace && newText.endsWith(" ") && newText != previousText && newText.trim().isNotEmpty()) {
                                    pendingSpaceSend = coroutineScope.launch {
                                        delay(500)
                                        val textToSend = textInput
                                        if (sendOnSpace && textToSend.endsWith(" ") && textToSend.trim().isNotEmpty()) {
                                            onSendText(textToSend)
                                            textInput = ""
                                        }
                                    }
                                }
                            }
                        },
                        placeholder = {
                            Text(
                                text = if (isLiveMode) "Digitação ao vivo no PC..." else "Digitar texto com acentos e enviar...",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrectEnabled = true,
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = if (isLiveMode) ImeAction.Default else ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (textInput.isNotBlank()) {
                                    pendingSpaceSend?.cancel()
                                    onSendText(textInput)
                                    textInput = ""
                                    focusManager.clearFocus()
                                }
                            }
                        ),
                        trailingIcon = {
                            if (textInput.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        pendingSpaceSend?.cancel()
                                        textInput = ""
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Limpar texto",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldNeon,
                            unfocusedBorderColor = BorderSubtle,
                            focusedContainerColor = KeyBaseGradientEnd,
                            unfocusedContainerColor = KeyBaseGradientEnd,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = EmeraldNeon
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("keyboard_text_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Send Button (only needed in Block mode, or sends Enter in live mode)
                    Button(
                        onClick = {
                            if (isLiveMode) {
                                onSendKey("enter")
                            } else {
                                if (textInput.isNotBlank()) {
                                    pendingSpaceSend?.cancel()
                                    onSendText(textInput)
                                    textInput = ""
                                } else {
                                    onSendKey("enter")
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldNeon,
                            contentColor = Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("btn_send_keyboard_text")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isLiveMode) "Enter" else "Enviar",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Helper note & space-send toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isLiveMode) "Sincronizando caractere a caractere" else "Acentos e autocorreção do teclado ativos",
                        color = TextMuted,
                        fontSize = 10.sp
                    )

                    if (!isLiveMode) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    pendingSpaceSend?.cancel()
                                    sendOnSpace = !sendOnSpace
                                }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (sendOnSpace) "Espaço envia palavra: Sim" else "Espaço envia palavra: Não",
                                color = if (sendOnSpace) EmeraldNeon else TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Keys Toolbar (Esc, Tab, Space, Backspace, Enter, Arrows)
                Text(
                    text = "TECLAS RÁPIDAS & NAVEGAÇÃO",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuickKeyChip(label = "Esc", keyCombo = "esc", onClick = onSendKey)
                    QuickKeyChip(label = "Tab", keyCombo = "tab", onClick = onSendKey)
                    QuickKeyChip(label = "Espaço", keyCombo = "space", onClick = onSendKey)
                    QuickKeyChip(label = "⌫ Backspace", keyCombo = "backspace", onClick = onSendKey)
                    QuickKeyChip(label = "⏎ Enter", keyCombo = "enter", onClick = onSendKey, accent = true)

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(BorderSubtle)
                    )

                    // Navigation arrows
                    QuickKeyChip(label = "◄", keyCombo = "left", onClick = onSendKey, isArrow = true)
                    QuickKeyChip(label = "▲", keyCombo = "up", onClick = onSendKey, isArrow = true)
                    QuickKeyChip(label = "▼", keyCombo = "down", onClick = onSendKey, isArrow = true)
                    QuickKeyChip(label = "►", keyCombo = "right", onClick = onSendKey, isArrow = true)
                }
            }
        }
    }
}

@Composable
private fun QuickKeyChip(
    label: String,
    keyCombo: String,
    onClick: (String) -> Unit,
    accent: Boolean = false,
    isArrow: Boolean = false
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (accent) EmeraldNeon.copy(alpha = 0.2f) else KeyBaseGradientStart)
            .border(
                1.dp,
                if (accent) EmeraldNeon else BorderSubtle,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick(keyCombo) }
            .padding(
                horizontal = if (isArrow) 12.dp else 10.dp,
                vertical = 8.dp
            )
            .testTag("chip_key_$keyCombo")
    ) {
        Text(
            text = label,
            color = if (accent) EmeraldNeon else TextPrimary,
            fontSize = if (isArrow) 13.sp else 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

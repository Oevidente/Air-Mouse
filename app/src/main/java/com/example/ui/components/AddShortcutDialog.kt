package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.CustomShortcut
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ChassisSurface
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.KeyBaseGradientEnd
import com.example.ui.theme.KeyBaseGradientStart
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AddShortcutDialog(
    onDismiss: () -> Unit,
    onSave: (CustomShortcut) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Produtividade") }
    var selectedKey by remember { mutableStateOf("C") }
    val selectedModifiers = remember { mutableStateListOf("CTRL") }
    var selectedColor by remember { mutableStateOf("#2979FF") }

    val categories = listOf("Apresentação", "Multimídia", "Produtividade", "Jogos", "Sistema")
    val commonKeys = listOf("F5", "ESC", "SPACE", "ENTER", "TAB", "A", "C", "V", "Z", "D", "S", "T", "F11")
    val colors = listOf("#00E676", "#2979FF", "#FFAB00", "#FF5252", "#E040FB", "#00E5FF")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = ChassisSurface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Criar Atalho Personalizado",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title Input
                Text(
                    text = "Nome do Botão",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Ex: Copiar Código, Próximo Slide, Mute Discord", color = TextMuted) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .testTag("input_shortcut_name"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = EmeraldNeon,
                        unfocusedBorderColor = BorderSubtle
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Category Selector
                Text(
                    text = "Categoria",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        CategoryChip(
                            label = cat,
                            isSelected = selectedCategory == cat,
                            onClick = { selectedCategory = cat }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Modifiers
                Text(
                    text = "Teclas Modificadoras",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("CTRL", "ALT", "SHIFT", "WIN").forEach { mod ->
                        val isChecked = selectedModifiers.contains(mod)
                        ModifierChip(
                            label = mod,
                            isSelected = isChecked,
                            onClick = {
                                if (isChecked) selectedModifiers.remove(mod)
                                else selectedModifiers.add(mod)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Primary Key Choice
                Text(
                    text = "Tecla Principal",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    commonKeys.take(5).forEach { k ->
                        ModifierChip(
                            label = k,
                            isSelected = selectedKey == k,
                            onClick = { selectedKey = k }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Color Picker
                Text(
                    text = "Cor do Botão",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    colors.forEach { hex ->
                        val color = Color(android.graphics.Color.parseColor(hex))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (selectedColor == hex) 3.dp else 1.dp,
                                    color = if (selectedColor == hex) Color.White else BorderSubtle,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = hex }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Key combination preview
                val finalCombo = (selectedModifiers + listOf(selectedKey)).joinToString("+")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(KeyBaseGradientStart)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Comando gerado:", color = TextSecondary, fontSize = 12.sp)
                    Text(
                        text = finalCombo,
                        color = EmeraldNeon,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Save Button
                Button(
                    onClick = {
                        val shortcutTitle = if (title.isBlank()) finalCombo else title
                        val newShortcut = CustomShortcut(
                            title = shortcutTitle,
                            category = selectedCategory,
                            keyCombo = finalCombo,
                            colorHex = selectedColor,
                            iconKey = "keyboard"
                        )
                        onSave(newShortcut)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_save_shortcut"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon)
                ) {
                    Text("Salvar Atalho no Controle", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) AccentBlue.copy(alpha = 0.25f) else KeyBaseGradientEnd)
            .border(
                1.dp,
                if (isSelected) AccentBlue else BorderSubtle,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) AccentBlue else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun ModifierChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) EmeraldNeon.copy(alpha = 0.2f) else KeyBaseGradientEnd)
            .border(
                1.dp,
                if (isSelected) EmeraldNeon else BorderSubtle,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) EmeraldNeon else TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

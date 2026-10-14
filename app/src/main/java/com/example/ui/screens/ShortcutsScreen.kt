package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.CustomShortcut
import com.example.ui.components.AddShortcutDialog
import com.example.ui.components.KeyboardQuickBar
import com.example.ui.components.VolumetricButton
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.KeyBaseGradientEnd
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ShortcutsScreen(
    shortcuts: List<CustomShortcut>,
    onSendKey: (String) -> Unit,
    onSendText: (String) -> Unit = {},
    onAddShortcut: (CustomShortcut) -> Unit,
    onDeleteShortcut: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Todos") }
    var showAddDialog by remember { mutableStateOf(false) }

    val categories = listOf("Todos", "Apresentação", "Multimídia", "Produtividade", "Sistema")

    val filteredShortcuts = if (selectedCategory == "Todos") {
        shortcuts
    } else {
        shortcuts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Top Header with Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DECK DE ATALHOS & MACROS",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${filteredShortcuts.size} comandos disponíveis",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.testTag("btn_open_add_shortcut")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Novo Atalho",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Novo",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Integrated Remote Keyboard Quick Bar
        KeyboardQuickBar(
            onSendText = onSendText,
            onSendKey = onSendKey,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) EmeraldNeon.copy(alpha = 0.2f) else KeyBaseGradientEnd)
                        .border(
                            1.dp,
                            if (isSelected) EmeraldNeon else BorderSubtle,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) EmeraldNeon else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grid of Volumetric Macro Buttons
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredShortcuts, key = { it.id }) { shortcut ->
                val parsedColor = try {
                    Color(android.graphics.Color.parseColor(shortcut.colorHex))
                } catch (e: Exception) {
                    EmeraldNeon
                }

                Box {
                    VolumetricButton(
                        onClick = { onSendKey(shortcut.keyCombo) },
                        label = shortcut.title,
                        subLabel = shortcut.keyCombo,
                        accentColor = parsedColor,
                        icon = Icons.Default.Keyboard,
                        shape = RoundedCornerShape(16.dp),
                        testTag = "shortcut_item_${shortcut.id}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                    )

                    // Delete button for custom shortcuts
                    if (!shortcut.isSystemDefault) {
                        IconButton(
                            onClick = { onDeleteShortcut(shortcut.id) },
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.TopEnd)
                                .padding(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Excluir atalho",
                                tint = TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddShortcutDialog(
            onDismiss = { showAddDialog = false },
            onSave = { newShortcut ->
                onAddShortcut(newShortcut)
                showAddDialog = false
            }
        )
    }
}

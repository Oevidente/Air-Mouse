package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RemoteServer
import com.example.network.PythonReceiverCode
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ChassisSurface
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.KeyBaseGradientEnd
import com.example.ui.theme.KeyBaseGradientStart
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun ConnectionSettingsScreen(
    currentIp: String,
    currentUdp: Int,
    currentTcp: Int,
    isConnected: Boolean,
    pingMs: Long,
    isTesting: Boolean,
    statusMessage: String?,
    savedServers: List<RemoteServer>,
    gyroSensitivity: Float,
    touchSensitivity: Float,
    invertX: Boolean,
    invertY: Boolean,
    hapticsEnabled: Boolean,
    showPresentationControls: Boolean,
    onSaveSettings: (ip: String, udp: Int, tcp: Int) -> Unit,
    onTestConnection: () -> Unit,
    onUpdateSensitivities: (gyro: Float, touch: Float) -> Unit,
    onToggleInvertX: () -> Unit,
    onToggleInvertY: () -> Unit,
    onToggleHaptics: () -> Unit,
    onTogglePresentationControls: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var ipInput by remember(currentIp) { mutableStateOf(currentIp) }
    var udpInput by remember(currentUdp) { mutableStateOf(currentUdp.toString()) }
    var tcpInput by remember(currentTcp) { mutableStateOf(currentTcp.toString()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Connection Setup Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ChassisSurface)
                .border(1.2.dp, BorderSubtle, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = "Configuração do Computador",
                        tint = EmeraldNeon,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Conexão Wi-Fi com o PC",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isConnected) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Online",
                            tint = EmeraldNeon,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${pingMs}ms", color = EmeraldNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // IP Field
            Text(text = "Endereço IP do Computador", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = ipInput,
                onValueChange = { ipInput = it },
                placeholder = { Text("Ex: 192.168.1.105", color = TextMuted) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .testTag("input_pc_ip"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = EmeraldNeon,
                    unfocusedBorderColor = BorderSubtle
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Port Fields Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Porta UDP (Mouse)", color = TextSecondary, fontSize = 11.sp)
                    OutlinedTextField(
                        value = udpInput,
                        onValueChange = { udpInput = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = EmeraldNeon,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Porta TCP (Teclado)", color = TextSecondary, fontSize = 11.sp)
                    OutlinedTextField(
                        value = tcpInput,
                        onValueChange = { tcpInput = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = EmeraldNeon,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actions: Connect / Save & Test
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val udp = udpInput.toIntOrNull() ?: 5005
                        val tcp = tcpInput.toIntOrNull() ?: 5006
                        onSaveSettings(ipInput, udp, tcp)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_save_connect")
                ) {
                    Text(text = "Salvar & Conectar", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onTestConnection,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KeyBaseGradientStart),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("btn_test_ping")
                ) {
                    if (isTesting) {
                        CircularProgressIndicator(
                            color = EmeraldNeon,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Testar Ping",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Testar Ping", color = TextPrimary, fontSize = 12.sp)
                    }
                }
            }

            if (!statusMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isConnected) Color(0xFF003816) else Color(0xFF381414))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isConnected) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = null,
                        tint = if (isConnected) EmeraldNeon else AccentRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = statusMessage,
                        color = if (isConnected) EmeraldNeon else AccentRed,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Script Receptor do PC (Windows & Linux)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ChassisSurface)
                .border(1.2.dp, BorderSubtle, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Terminal",
                        tint = AccentBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Script Receptor PC (Windows / Linux)",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Para controlar o computador, basta executar este script em Python no seu PC. Ele não precisa de instalação pesada e funciona nativamente no Windows e Linux.",
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Command snippet
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(text = "# 1. Instalar dependências leves:", color = TextMuted, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    Text(text = "pip install pynput pyautogui", color = EmeraldNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "# 2. Executar no Windows ou Linux:", color = TextMuted, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    Text(text = "python air_mouse_receiver.py", color = AccentBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Copy & Share buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("air_mouse_receiver.py", PythonReceiverCode.PYTHON_SCRIPT)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Script copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KeyBaseGradientStart),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.weight(1f).height(46.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copiar", tint = TextPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copiar Script", color = TextPrimary, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, PythonReceiverCode.PYTHON_SCRIPT)
                            putExtra(Intent.EXTRA_TITLE, "air_mouse_receiver.py")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Enviar script receiver"))
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KeyBaseGradientStart),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.weight(1f).height(46.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Compartilhar", tint = TextPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Compartilhar", color = TextPrimary, fontSize = 12.sp)
                }
            }
        }

        // Sensores & Resposta Tátil
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ChassisSurface)
                .border(1.2.dp, BorderSubtle, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Text(
                text = "Sensores & Resposta Tátil",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Gyro sensitivity slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sensibilidade do Giroscópio (Air Mouse)", color = TextSecondary, fontSize = 12.sp)
                Text(
                    text = String.format(Locale.US, "%.1fx", gyroSensitivity),
                    color = EmeraldNeon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Slider(
                value = gyroSensitivity,
                onValueChange = { onUpdateSensitivities(it, touchSensitivity) },
                valueRange = 0.5f..5.0f,
                colors = SliderDefaults.colors(
                    thumbColor = EmeraldNeon,
                    activeTrackColor = EmeraldNeon,
                    inactiveTrackColor = TextMuted.copy(alpha = 0.4f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Invert axes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Inverter Eixo X (Horizontal)", color = TextPrimary, fontSize = 13.sp)
                Switch(
                    checked = invertX,
                    onCheckedChange = { onToggleInvertX() },
                    colors = SwitchDefaults.colors(checkedThumbColor = EmeraldNeon, checkedTrackColor = EmeraldNeon.copy(alpha = 0.3f))
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Inverter Eixo Y (Vertical)", color = TextPrimary, fontSize = 13.sp)
                Switch(
                    checked = invertY,
                    onCheckedChange = { onToggleInvertY() },
                    colors = SwitchDefaults.colors(checkedThumbColor = EmeraldNeon, checkedTrackColor = EmeraldNeon.copy(alpha = 0.3f))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Haptics switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Vibration, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Vibração Háptica nos Botões", color = TextPrimary, fontSize = 13.sp)
                }
                Switch(
                    checked = hapticsEnabled,
                    onCheckedChange = { onToggleHaptics() },
                    colors = SwitchDefaults.colors(checkedThumbColor = EmeraldNeon, checkedTrackColor = EmeraldNeon.copy(alpha = 0.3f))
                )
            }
        }

        // Recursos Opcionais: Apresentação de Slides
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ChassisSurface)
                .border(1.2.dp, BorderSubtle, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Slideshow,
                        contentDescription = "Apresentação",
                        tint = EmeraldNeon,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Controles de Slides & Mídia",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Exibir aba dedicada de slides (F5, avançar, retroceder e tela preta)",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Switch(
                    checked = showPresentationControls,
                    onCheckedChange = { onTogglePresentationControls(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = EmeraldNeon,
                        checkedTrackColor = EmeraldNeon.copy(alpha = 0.3f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(75.dp))
    }
}

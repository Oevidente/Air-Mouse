package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.RemoteTab
import com.example.ui.components.IosFloatingNavBar
import com.example.ui.screens.AirMouseScreen
import com.example.ui.screens.ConnectionSettingsScreen
import com.example.ui.screens.MediaPresentationScreen
import com.example.ui.screens.ShortcutsScreen
import com.example.ui.screens.TouchpadScreen
import com.example.ui.theme.CanvasPitchBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.RemoteViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: RemoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(uiState.statusMessage) {
                    uiState.statusMessage?.let {
                        snackbarHostState.showSnackbar(it)
                        viewModel.clearStatusMessage()
                    }
                }

                // Return to Air Mouse home screen on Back press
                BackHandler(enabled = uiState.currentTab != RemoteTab.AIR_MOUSE) {
                    viewModel.setTab(RemoteTab.AIR_MOUSE)
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CanvasPitchBlack),
                    containerColor = CanvasPitchBlack,
                    contentWindowInsets = WindowInsets.safeDrawing,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        Box(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .padding(bottom = 6.dp)
                        ) {
                            IosFloatingNavBar(
                                currentTab = uiState.currentTab,
                                onTabSelected = { viewModel.setTab(it) },
                                showMediaTab = uiState.showPresentationControls
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .widthIn(max = 640.dp)
                        ) {
                            Crossfade(
                                targetState = uiState.currentTab,
                                label = "iosTabCrossfade",
                                modifier = Modifier.fillMaxSize()
                            ) { tab ->
                                when (tab) {
                                    RemoteTab.AIR_MOUSE -> {
                                        AirMouseScreen(
                                            isConnected = uiState.isConnected,
                                            isAirMouseActive = uiState.isAirMouseActive,
                                            gyroSensitivity = uiState.gyroSensitivity,
                                            tiltX = uiState.gyroTiltX,
                                            tiltY = uiState.gyroTiltY,
                                            oneHandedMode = uiState.oneHandedMode,
                                            onToggleAirMouse = { viewModel.toggleAirMouse() },
                                            onRecalibrate = { viewModel.recalibrateGyro() },
                                            onSensitivityChange = { viewModel.updateSensitivities(it, uiState.touchpadSensitivity) },
                                            onLeftClick = { viewModel.onLeftClick() },
                                            onMiddleClick = { viewModel.onMiddleClick() },
                                            onRightClick = { viewModel.onRightClick() },
                                            onScroll = { dy, dx -> viewModel.onScroll(dy, dx) },
                                            onCycleOneHandedMode = { viewModel.cycleOneHandedMode() },
                                            onSettingsClick = { viewModel.setTab(RemoteTab.CONNECTION_SETTINGS) }
                                        )
                                    }

                                    RemoteTab.MEDIA_CONTROLS -> {
                                        MediaPresentationScreen(
                                            isConnected = uiState.isConnected,
                                            onSendKey = { viewModel.sendKey(it) },
                                            onSettingsClick = { viewModel.setTab(RemoteTab.CONNECTION_SETTINGS) }
                                        )
                                    }

                                    RemoteTab.TOUCHPAD -> {
                                        TouchpadScreen(
                                            isConnected = uiState.isConnected,
                                            onMove = { dx, dy -> viewModel.onMouseMove(dx, dy) },
                                            onScroll = { dy, dx -> viewModel.onScroll(dy, dx) },
                                            onLeftClick = { viewModel.onLeftClick() },
                                            onMiddleClick = { viewModel.onMiddleClick() },
                                            onRightClick = { viewModel.onRightClick() },
                                            sensitivity = uiState.touchpadSensitivity,
                                            onSensitivityChange = { viewModel.updateSensitivities(uiState.gyroSensitivity, it) },
                                            oneHandedMode = uiState.oneHandedMode,
                                            onCycleOneHandedMode = { viewModel.cycleOneHandedMode() },
                                            onSettingsClick = { viewModel.setTab(RemoteTab.CONNECTION_SETTINGS) }
                                        )
                                    }

                                    RemoteTab.SHORTCUTS_DECK -> {
                                        ShortcutsScreen(
                                            shortcuts = uiState.shortcuts,
                                            onSendKey = { viewModel.sendKey(it) },
                                            onSendText = { viewModel.sendText(it) },
                                            onAddShortcut = { viewModel.addCustomShortcut(it) },
                                            onDeleteShortcut = { viewModel.deleteShortcut(it) }
                                        )
                                    }

                                    RemoteTab.CONNECTION_SETTINGS -> {
                                        ConnectionSettingsScreen(
                                            currentIp = uiState.serverIp,
                                            currentUdp = uiState.udpPort,
                                            currentTcp = uiState.tcpPort,
                                            isConnected = uiState.isConnected,
                                            pingMs = uiState.pingMs,
                                            isTesting = uiState.isTestingConnection,
                                            statusMessage = uiState.statusMessage,
                                            savedServers = uiState.savedServers,
                                            gyroSensitivity = uiState.gyroSensitivity,
                                            touchSensitivity = uiState.touchpadSensitivity,
                                            invertX = uiState.invertX,
                                            invertY = uiState.invertY,
                                            hapticsEnabled = uiState.hapticsEnabled,
                                            showPresentationControls = uiState.showPresentationControls,
                                            oneHandedMode = uiState.oneHandedMode,
                                            onSaveSettings = { ip, udp, tcp -> viewModel.updateServerSettings(ip, udp, tcp) },
                                            onTestConnection = { viewModel.testConnection() },
                                            onUpdateSensitivities = { gyro, touch -> viewModel.updateSensitivities(gyro, touch) },
                                            onToggleInvertX = { viewModel.toggleInvertX() },
                                            onToggleInvertY = { viewModel.toggleInvertY() },
                                            onToggleHaptics = { viewModel.toggleHaptics() },
                                            onTogglePresentationControls = { viewModel.togglePresentationControls(it) },
                                            onSetOneHandedMode = { viewModel.setOneHandedMode(it) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

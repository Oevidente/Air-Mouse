package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BotanikaDatabase
import com.example.data.local.UserPreferencesManager
import com.example.data.model.AirMouseMode
import com.example.data.model.CustomShortcut
import com.example.data.model.OneHandedMode
import com.example.data.model.RemoteServer
import com.example.data.model.RemoteTab
import com.example.data.repository.RemoteRepository
import com.example.haptics.HapticFeedbackManager
import com.example.network.RemoteClient
import com.example.sensors.GyroscopeAirMouseManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class RemoteUiState(
    val currentTab: RemoteTab = RemoteTab.AIR_MOUSE,
    val isAirMouseActive: Boolean = true,
    val airMouseMode: AirMouseMode = AirMouseMode.TOGGLE,
    val isConnected: Boolean = false,
    val pingMs: Long = -1L,
    val serverIp: String = "192.168.1.100",
    val udpPort: Int = 5005,
    val tcpPort: Int = 5006,
    val gyroSensitivity: Float = 2.4f,
    val gyroDeadzone: Float = 0.012f,
    val touchpadSensitivity: Float = 1.6f,
    val hapticsEnabled: Boolean = true,
    val invertX: Boolean = false,
    val invertY: Boolean = false,
    val showPresentationControls: Boolean = false,
    val oneHandedMode: OneHandedMode = OneHandedMode.OFF,
    val gyroTiltX: Float = 0f,
    val gyroTiltY: Float = 0f,
    val isGyroAvailable: Boolean = true,
    val shortcuts: List<CustomShortcut> = emptyList(),
    val savedServers: List<RemoteServer> = emptyList(),
    val isTestingConnection: Boolean = false,
    val statusMessage: String? = null
)

class RemoteViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = UserPreferencesManager(application)
    private val database = BotanikaDatabase.getDatabase(application, viewModelScope)
    private val repository = RemoteRepository(database.remoteDao())
    private val hapticManager = HapticFeedbackManager(application)
    private val remoteClient = RemoteClient(viewModelScope)

    private val gyroManager = GyroscopeAirMouseManager(application) { dx, dy ->
        remoteClient.sendMouseMove(dx, dy)
    }

    private val _uiState = MutableStateFlow(
        RemoteUiState(
            serverIp = preferencesManager.serverIp,
            udpPort = preferencesManager.udpPort,
            tcpPort = preferencesManager.tcpPort,
            gyroSensitivity = preferencesManager.gyroSensitivity,
            touchpadSensitivity = preferencesManager.touchpadSensitivity,
            invertX = preferencesManager.invertX,
            invertY = preferencesManager.invertY,
            hapticsEnabled = preferencesManager.hapticsEnabled,
            airMouseMode = preferencesManager.airMouseMode,
            showPresentationControls = preferencesManager.showPresentationControls,
            oneHandedMode = preferencesManager.oneHandedMode
        )
    )
    val uiState: StateFlow<RemoteUiState> = _uiState.asStateFlow()

    private var pingJob: Job? = null

    init {
        // Inicializar cliente de rede com endereço persistido
        remoteClient.updateTarget(
            _uiState.value.serverIp,
            _uiState.value.udpPort,
            _uiState.value.tcpPort
        )

        // Sincronizar parâmetros de vibração e giroscópio
        hapticManager.isEnabled = _uiState.value.hapticsEnabled
        gyroManager.sensitivity = _uiState.value.gyroSensitivity
        gyroManager.deadzone = _uiState.value.gyroDeadzone
        gyroManager.invertX = _uiState.value.invertX
        gyroManager.invertY = _uiState.value.invertY
        gyroManager.isEnabled = (_uiState.value.currentTab == RemoteTab.AIR_MOUSE && _uiState.value.isAirMouseActive)
        gyroManager.onMotionFeedback = { tx, ty ->
            _uiState.update { it.copy(gyroTiltX = tx, gyroTiltY = ty) }
        }
        gyroManager.startListening()
        _uiState.update { it.copy(isGyroAvailable = gyroManager.isSensorAvailable) }

        // Carregar atalhos do Room com garantia de deduplicação
        viewModelScope.launch {
            repository.ensureDefaultShortcuts()
            repository.shortcuts.collect { list ->
                _uiState.update { it.copy(shortcuts = list) }
            }
        }

        // Observar servidores salvos no banco local
        viewModelScope.launch {
            repository.servers.collect { list ->
                _uiState.update { it.copy(savedServers = list) }
            }
        }

        startPingMonitor()
    }

    private fun startPingMonitor() {
        pingJob?.cancel()
        pingJob = viewModelScope.launch {
            while (isActive) {
                val ip = _uiState.value.serverIp
                val tcp = _uiState.value.tcpPort
                if (ip.isNotBlank()) {
                    val latency = remoteClient.ping(ip, tcp, timeoutMs = 900)
                    _uiState.update {
                        it.copy(
                            isConnected = latency >= 0,
                            pingMs = latency
                        )
                    }
                }
                delay(3500)
            }
        }
    }

    fun setTab(tab: RemoteTab) {
        hapticManager.click()
        val enableGyro = (tab == RemoteTab.AIR_MOUSE && _uiState.value.isAirMouseActive)
        gyroManager.isEnabled = enableGyro
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setAirMouseMode(mode: AirMouseMode) {
        hapticManager.doubleTick()
        preferencesManager.airMouseMode = mode
        _uiState.update { it.copy(airMouseMode = mode) }
    }

    fun activateAirMouse() {
        if (!_uiState.value.isAirMouseActive) {
            hapticManager.click()
            gyroManager.isEnabled = (_uiState.value.currentTab == RemoteTab.AIR_MOUSE)
            _uiState.update { it.copy(isAirMouseActive = true) }
        }
    }

    fun deactivateAirMouse() {
        if (_uiState.value.isAirMouseActive) {
            hapticManager.subtleTick()
            gyroManager.isEnabled = false
            _uiState.update { it.copy(isAirMouseActive = false) }
        }
    }

    fun toggleAirMouse() {
        val nextState = !_uiState.value.isAirMouseActive
        if (nextState) {
            hapticManager.doubleTick()
        } else {
            hapticManager.subtleTick()
        }
        val enableGyro = (_uiState.value.currentTab == RemoteTab.AIR_MOUSE && nextState)
        gyroManager.isEnabled = enableGyro
        _uiState.update { it.copy(isAirMouseActive = nextState) }
    }

    fun recalibrateGyro() {
        hapticManager.doubleTick()
        gyroManager.recalibrate()
        _uiState.update { it.copy(statusMessage = "Giroscópio recalibrado com sucesso") }
    }

    fun onMouseMove(dx: Float, dy: Float) {
        remoteClient.sendMouseMove(dx, dy)
    }

    fun onLeftClick() {
        hapticManager.click()
        remoteClient.sendClick("left")
    }

    fun onRightClick() {
        hapticManager.click()
        remoteClient.sendClick("right")
    }

    fun onMiddleClick() {
        hapticManager.click()
        remoteClient.sendClick("middle")
    }

    fun onScroll(dy: Int, dx: Int = 0) {
        hapticManager.subtleTick()
        remoteClient.sendScroll(dy, dx)
    }

    fun sendKey(combo: String) {
        hapticManager.heavyClick()
        remoteClient.sendKey(combo)
    }

    fun sendText(text: String) {
        hapticManager.click()
        remoteClient.sendText(text)
    }

    fun updateServerSettings(ip: String, udpPort: Int, tcpPort: Int) {
        val trimmedIp = ip.trim()
        preferencesManager.serverIp = trimmedIp
        preferencesManager.udpPort = udpPort
        preferencesManager.tcpPort = tcpPort
        remoteClient.updateTarget(trimmedIp, udpPort, tcpPort)
        _uiState.update {
            it.copy(
                serverIp = trimmedIp,
                udpPort = udpPort,
                tcpPort = tcpPort
            )
        }
        viewModelScope.launch {
            repository.saveServer(
                RemoteServer(
                    ipAddress = trimmedIp,
                    udpPort = udpPort,
                    tcpPort = tcpPort,
                    name = "PC ($trimmedIp)"
                )
            )
            testConnection()
        }
    }

    fun testConnection() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTestingConnection = true, statusMessage = "Testando conexão...") }
            val ip = _uiState.value.serverIp
            val tcp = _uiState.value.tcpPort
            val latency = remoteClient.ping(ip, tcp, timeoutMs = 1500)
            val success = latency >= 0
            if (success) {
                hapticManager.doubleTick()
            }
            _uiState.update {
                it.copy(
                    isTestingConnection = false,
                    isConnected = success,
                    pingMs = latency,
                    statusMessage = if (success) "Conectado ao PC com sucesso (${latency}ms)!" else "Não foi possível conectar. Verifique se o script receptor está rodando no PC."
                )
            }
        }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }

    fun updateSensitivities(gyro: Float, touch: Float) {
        gyroManager.sensitivity = gyro
        preferencesManager.gyroSensitivity = gyro
        preferencesManager.touchpadSensitivity = touch
        _uiState.update {
            it.copy(
                gyroSensitivity = gyro,
                touchpadSensitivity = touch
            )
        }
    }

    fun toggleInvertX() {
        val inv = !_uiState.value.invertX
        gyroManager.invertX = inv
        preferencesManager.invertX = inv
        _uiState.update { it.copy(invertX = inv) }
    }

    fun toggleInvertY() {
        val inv = !_uiState.value.invertY
        gyroManager.invertY = inv
        preferencesManager.invertY = inv
        _uiState.update { it.copy(invertY = inv) }
    }

    fun toggleHaptics() {
        val next = !_uiState.value.hapticsEnabled
        hapticManager.isEnabled = next
        preferencesManager.hapticsEnabled = next
        _uiState.update { it.copy(hapticsEnabled = next) }
    }

    fun togglePresentationControls(enabled: Boolean) {
        preferencesManager.showPresentationControls = enabled
        _uiState.update { state ->
            val nextTab = if (!enabled && state.currentTab == RemoteTab.MEDIA_CONTROLS) {
                RemoteTab.AIR_MOUSE
            } else {
                state.currentTab
            }
            state.copy(
                showPresentationControls = enabled,
                currentTab = nextTab
            )
        }
        if (!enabled && _uiState.value.currentTab == RemoteTab.AIR_MOUSE) {
            gyroManager.isEnabled = _uiState.value.isAirMouseActive
        }
    }

    fun addCustomShortcut(shortcut: CustomShortcut) {
        hapticManager.click()
        viewModelScope.launch {
            repository.saveShortcut(shortcut)
        }
    }

    fun deleteShortcut(id: Long) {
        hapticManager.subtleTick()
        viewModelScope.launch {
            repository.deleteShortcut(id)
        }
    }

    fun setOneHandedMode(mode: OneHandedMode) {
        preferencesManager.oneHandedMode = mode
        _uiState.update { it.copy(oneHandedMode = mode) }
        hapticManager.click()
    }

    fun cycleOneHandedMode() {
        val next = when (_uiState.value.oneHandedMode) {
            OneHandedMode.OFF -> OneHandedMode.RIGHT_HAND
            OneHandedMode.RIGHT_HAND -> OneHandedMode.LEFT_HAND
            OneHandedMode.LEFT_HAND -> OneHandedMode.OFF
        }
        setOneHandedMode(next)
    }

    override fun onCleared() {
        super.onCleared()
        gyroManager.stopListening()
        remoteClient.close()
        pingJob?.cancel()
    }
}

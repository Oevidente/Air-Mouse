package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AirMouseMode
import com.example.data.model.OneHandedMode

class UserPreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("air_mouse_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_GYRO_SENSITIVITY = "gyro_sensitivity"
        private const val KEY_TOUCHPAD_SENSITIVITY = "touchpad_sensitivity"
        private const val KEY_INVERT_X = "invert_x"
        private const val KEY_INVERT_Y = "invert_y"
        private const val KEY_HAPTICS_ENABLED = "haptics_enabled"
        private const val KEY_SHOW_PRESENTATION = "show_presentation_controls"
        private const val KEY_SERVER_IP = "server_ip"
        private const val KEY_UDP_PORT = "udp_port"
        private const val KEY_TCP_PORT = "tcp_port"
        private const val KEY_AIR_MOUSE_MODE = "air_mouse_mode"
        private const val KEY_ONE_HANDED_MODE = "one_handed_mode"

        const val DEFAULT_GYRO_SENSITIVITY = 2.4f
        const val DEFAULT_TOUCHPAD_SENSITIVITY = 1.6f
        const val DEFAULT_INVERT_X = false
        const val DEFAULT_INVERT_Y = false
        const val DEFAULT_HAPTICS_ENABLED = true
        const val DEFAULT_SHOW_PRESENTATION = false
        const val DEFAULT_SERVER_IP = "192.168.1.100"
        const val DEFAULT_UDP_PORT = 5005
        const val DEFAULT_TCP_PORT = 5006
    }

    var gyroSensitivity: Float
        get() = prefs.getFloat(KEY_GYRO_SENSITIVITY, DEFAULT_GYRO_SENSITIVITY)
        set(value) = prefs.edit().putFloat(KEY_GYRO_SENSITIVITY, value).apply()

    var touchpadSensitivity: Float
        get() = prefs.getFloat(KEY_TOUCHPAD_SENSITIVITY, DEFAULT_TOUCHPAD_SENSITIVITY)
        set(value) = prefs.edit().putFloat(KEY_TOUCHPAD_SENSITIVITY, value).apply()

    var invertX: Boolean
        get() = prefs.getBoolean(KEY_INVERT_X, DEFAULT_INVERT_X)
        set(value) = prefs.edit().putBoolean(KEY_INVERT_X, value).apply()

    var invertY: Boolean
        get() = prefs.getBoolean(KEY_INVERT_Y, DEFAULT_INVERT_Y)
        set(value) = prefs.edit().putBoolean(KEY_INVERT_Y, value).apply()

    var hapticsEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTICS_ENABLED, DEFAULT_HAPTICS_ENABLED)
        set(value) = prefs.edit().putBoolean(KEY_HAPTICS_ENABLED, value).apply()

    var showPresentationControls: Boolean
        get() = prefs.getBoolean(KEY_SHOW_PRESENTATION, DEFAULT_SHOW_PRESENTATION)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_PRESENTATION, value).apply()

    var serverIp: String
        get() = prefs.getString(KEY_SERVER_IP, DEFAULT_SERVER_IP) ?: DEFAULT_SERVER_IP
        set(value) = prefs.edit().putString(KEY_SERVER_IP, value).apply()

    var udpPort: Int
        get() = prefs.getInt(KEY_UDP_PORT, DEFAULT_UDP_PORT)
        set(value) = prefs.edit().putInt(KEY_UDP_PORT, value).apply()

    var tcpPort: Int
        get() = prefs.getInt(KEY_TCP_PORT, DEFAULT_TCP_PORT)
        set(value) = prefs.edit().putInt(KEY_TCP_PORT, value).apply()

    var airMouseMode: AirMouseMode
        get() {
            val name = prefs.getString(KEY_AIR_MOUSE_MODE, AirMouseMode.TOGGLE.name)
            return try {
                AirMouseMode.valueOf(name ?: AirMouseMode.TOGGLE.name)
            } catch (e: Exception) {
                AirMouseMode.TOGGLE
            }
        }
        set(value) = prefs.edit().putString(KEY_AIR_MOUSE_MODE, value.name).apply()

    var oneHandedMode: OneHandedMode
        get() {
            val name = prefs.getString(KEY_ONE_HANDED_MODE, OneHandedMode.OFF.name)
            return try {
                OneHandedMode.valueOf(name ?: OneHandedMode.OFF.name)
            } catch (e: Exception) {
                OneHandedMode.OFF
            }
        }
        set(value) = prefs.edit().putString(KEY_ONE_HANDED_MODE, value.name).apply()
}

package com.example.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.sign

class GyroscopeAirMouseManager(
    context: Context,
    private val onMove: (dx: Float, dy: Float) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private val rotationVector: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
        ?: sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    val isSensorAvailable: Boolean = (gyroscope != null || rotationVector != null)

    var isEnabled: Boolean = true
        set(value) {
            field = value
            if (!value) {
                filteredDx = 0f
                filteredDy = 0f
                onMotionFeedback?.invoke(0f, 0f)
            }
        }

    var sensitivity: Float = 2.4f
    var deadzone: Float = 0.012f
    var invertX: Boolean = false
    var invertY: Boolean = false

    var onMotionFeedback: ((tiltX: Float, tiltY: Float) -> Unit)? = null

    private var filteredDx = 0f
    private var filteredDy = 0f
    private val smoothingFactor = 0.55f // Responsivo e suave

    // Variáveis para fallback por Vetor de Rotação
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private var lastYaw: Float? = null
    private var lastPitch: Float? = null
    private var lastRoll: Float? = null

    fun startListening() {
        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME)
        } else if (rotationVector != null) {
            sensorManager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        filteredDx = 0f
        filteredDy = 0f
        lastYaw = null
        lastPitch = null
        lastRoll = null
    }

    fun recalibrate() {
        filteredDx = 0f
        filteredDy = 0f
        lastYaw = null
        lastPitch = null
        lastRoll = null
        onMotionFeedback?.invoke(0f, 0f)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isEnabled || event == null) return

        var rawX = 0f
        var rawY = 0f

        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            val rawPitch = event.values[0]
            val rawRoll = event.values[1]
            val rawYaw = event.values[2]

            // Giroscópio em modo Air Mouse:
            // Apontando o celular para o monitor:
            // Horizontal (X): combinação de Yaw (apontar para os lados) e Roll (inclinar o pulso)
            // Vertical (Y): Pitch (inclinar para cima / para baixo)
            rawX = -(rawYaw * 0.95f + rawRoll * 0.45f)
            rawY = -rawPitch
        } else if (event.sensor.type == Sensor.TYPE_GAME_ROTATION_VECTOR ||
            event.sensor.type == Sensor.TYPE_ROTATION_VECTOR
        ) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val currentYaw = orientationAngles[0]
            val currentPitch = orientationAngles[1]
            val currentRoll = orientationAngles[2]

            val prevYaw = lastYaw
            val prevPitch = lastPitch
            val prevRoll = lastRoll

            lastYaw = currentYaw
            lastPitch = currentPitch
            lastRoll = currentRoll

            if (prevYaw == null || prevPitch == null || prevRoll == null) {
                return
            }

            var dYaw = currentYaw - prevYaw
            if (dYaw > Math.PI) dYaw -= (2 * Math.PI).toFloat()
            if (dYaw < -Math.PI) dYaw += (2 * Math.PI).toFloat()

            val dPitch = currentPitch - prevPitch
            val dRoll = currentRoll - prevRoll

            rawX = -(dYaw * 18f + dRoll * 9f)
            rawY = -(dPitch * 18f)
        } else {
            return
        }

        // Filtro de zona morta para estabilizar micromovimentos da mão
        val deadX = if (abs(rawX) > deadzone) rawX else 0f
        val deadY = if (abs(rawY) > deadzone) rawY else 0f

        if (deadX == 0f && deadY == 0f) {
            filteredDx *= 0.5f
            filteredDy *= 0.5f
            if (abs(filteredDx) < 0.01f && abs(filteredDy) < 0.01f) {
                filteredDx = 0f
                filteredDy = 0f
                onMotionFeedback?.invoke(0f, 0f)
                return
            }
        } else {
            filteredDx = filteredDx * (1f - smoothingFactor) + deadX * smoothingFactor
            filteredDy = filteredDy * (1f - smoothingFactor) + deadY * smoothingFactor
        }

        // Aceleração balística para precisão em movimentos finos e rapidez em gestos amplos
        val speedMagnitude = abs(filteredDx) + abs(filteredDy)
        val accelBoost = 1f + (speedMagnitude * 0.45f).coerceAtMost(2.5f)

        val speedMultiplier = 18f * sensitivity * accelBoost
        var finalDx = filteredDx * speedMultiplier
        var finalDy = filteredDy * speedMultiplier

        if (invertX) finalDx = -finalDx
        if (invertY) finalDy = -finalDy

        onMotionFeedback?.invoke(
            (finalDx / 10f).coerceIn(-1f, 1f),
            (finalDy / 10f).coerceIn(-1f, 1f)
        )

        if (abs(finalDx) > 0.04f || abs(finalDy) > 0.04f) {
            onMove(finalDx, finalDy)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}

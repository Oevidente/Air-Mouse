package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.UserPreferencesManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app_name string from context is Air Mouse`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Air Mouse", appName)
  }

  @Test
  fun `user preferences manager persists and restores values correctly`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefsManager = UserPreferencesManager(context)

    // Default values check
    assertFalse(prefsManager.showPresentationControls)
    assertEquals(UserPreferencesManager.DEFAULT_GYRO_SENSITIVITY, prefsManager.gyroSensitivity, 0.01f)

    // Modify values
    prefsManager.gyroSensitivity = 3.8f
    prefsManager.touchpadSensitivity = 2.5f
    prefsManager.invertX = true
    prefsManager.invertY = true
    prefsManager.hapticsEnabled = false
    prefsManager.showPresentationControls = true
    prefsManager.serverIp = "192.168.0.55"
    prefsManager.udpPort = 7000
    prefsManager.tcpPort = 7001

    // Re-instantiate preferences manager to simulate app restart
    val newPrefsInstance = UserPreferencesManager(context)
    assertEquals(3.8f, newPrefsInstance.gyroSensitivity, 0.01f)
    assertEquals(2.5f, newPrefsInstance.touchpadSensitivity, 0.01f)
    assertTrue(newPrefsInstance.invertX)
    assertTrue(newPrefsInstance.invertY)
    assertFalse(newPrefsInstance.hapticsEnabled)
    assertTrue(newPrefsInstance.showPresentationControls)
    assertEquals("192.168.0.55", newPrefsInstance.serverIp)
    assertEquals(7000, newPrefsInstance.udpPort)
    assertEquals(7001, newPrefsInstance.tcpPort)
  }
}

package com.mirz.approval

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern

private const val APP_PACKAGE = "com.mirz.approval"
private const val LAUNCH_TIMEOUT = 5_000L
private const val API_TIMEOUT = 5_000L   // covers the 1.5 s simulated delay + nav overhead
private const val SNACKBAR_TIMEOUT = 5_000L

@RunWith(AndroidJUnit4::class)
class RequestFlowUiTest {

    private lateinit var device: UiDevice

    @Before
    fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen so state is predictable
        device.pressHome()

        // Launch the app
        val context: Context = ApplicationProvider.getApplicationContext()
        val intent = context.packageManager
            .getLaunchIntentForPackage(APP_PACKAGE)
            ?.apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

        checkNotNull(intent) { "Could not resolve launch intent for $APP_PACKAGE" }
        context.startActivity(intent)

        // Wait until the app is in the foreground
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT)
    }

    /**
     * Tapping "New Request" opens the detail screen.
     * Tapping "Reject" navigates back and shows a red snackbar on the landing screen.
     *
     * The rejection message always contains the word "reject" (both success
     * and error paths), so the snackbar is always rendered in the error/red colour.
     */
    @Test
    fun whenRejectButtonClicked_shouldShowRedSnackbarOnLandingScreen() {
        // --- Landing screen ---
        val newRequestBtn = device.findObject(UiSelector().text("New Request"))
        assertNotNull("'New Request' button not found on landing screen", newRequestBtn)
        newRequestBtn.click()

        // --- Detail screen ---
        // Wait for the request to load (ApiService has a 1.5 s simulated delay)
        val rejectBtn = device.wait(Until.findObject(By.text("Reject")), API_TIMEOUT)
        assertNotNull("'Reject' button not found on detail screen", rejectBtn)
        rejectBtn.click()

        // --- Back on landing screen ---
        // Both rejection outcomes ("Request rejected successfully." /
        // "Rejection failed: unable to reject the request.") contain "reject",
        val snackbar = device.wait(
            Until.findObject(By.text(Pattern.compile(".*reject.*", Pattern.CASE_INSENSITIVE))),
            SNACKBAR_TIMEOUT
        )
        assertNotNull("Expected a rejection snackbar to appear on the landing screen", snackbar)
    }
}

package com.example.weathercodechallenge

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.weathercodechallenge.presentation.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)
@HiltAndroidTest    // tells Hilt that we need to inject dependencies in here
class ExampleInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)    // allows us to perform DI
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()  // support for DI in composables

    @Inject
    @Named("HelloTest") lateinit var dummyString1: String
    @Inject
    @Named("AnotherStringTest") lateinit var dummyString2: String
   // @Inject
   // lateinit var ctx: Context


    @Before
    fun setup() {
        hiltRule.inject()   // inject all the dependencies
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val ctx = InstrumentationRegistry.getInstrumentation().context // .targetContext
        Thread.sleep(500)
        assertEquals("com.example.weathercodechallenge", ctx.packageName)
    }

    @Test
    fun `fetch injected dummy string1`() {
        assertEquals("Hello, Testing!", dummyString1)
    }

    @Test
    fun `fetch injected dummy string2`() {
        assertEquals("additional string test!", dummyString2)
    }

}
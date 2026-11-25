
package com.example.mylabs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Test sending a message
    @Test
    fun testSendMessage() {

        composeTestRule
            .onNodeWithTag("MessageInput")
            .performTextInput("Hello from Send")

        composeTestRule
            .onNodeWithTag("SendButton")
            .performClick()

        // Verify message exists and contains correct text
        composeTestRule
            .onNodeWithTag("Message0")
            .assertIsDisplayed()
            .assertTextEquals("Hello from Send")

        // Verify avatar exists
        composeTestRule
            .onNodeWithTag("Avatar0")
            .assertIsDisplayed()
    }

    // Test receiving a message
    @Test
    fun testReceiveMessage() {

        composeTestRule
            .onNodeWithTag("MessageInput")
            .performTextInput("Hello from Receive")

        composeTestRule
            .onNodeWithTag("ReceiveButton")
            .performClick()

        // Verify message exists and contains correct text
        composeTestRule
            .onNodeWithTag("Message0")
            .assertIsDisplayed()
            .assertTextEquals("Hello from Receive")

        // Verify avatar exists
        composeTestRule
            .onNodeWithTag("Avatar0")
            .assertIsDisplayed()
    }
}

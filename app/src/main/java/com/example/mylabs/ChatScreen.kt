
package com.example.mylabs

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
// Message data structure
data class ChatMessage(
    val message: String,
    val isSent: Boolean,
    val timestamp: String
)

// Vibrate the phone when a message is added
@Suppress("DEPRECATION")
private fun vibratePhone(context: Context) {

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.getSystemService(VibratorManager::class.java).defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (vibrator.hasVibrator()) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    100,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(100)
        }
    }
}

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {

    var messageInput by remember {
        mutableStateOf("")
    }

    val messages = remember {
        mutableStateListOf<ChatMessage>()
    }

    val context = LocalContext.current

    // Add a new message to the conversation
    fun addMessage(isSent: Boolean) {

        val text = messageInput.trim()

        if (text.isNotEmpty()) {

            val currentTime = SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            ).format(Date())

            messages.add(
                ChatMessage(
                    message = text,
                    isSent = isSent,
                    timestamp = currentTime
                )
            )

            messageInput = ""

            // Vibrate when a message is added
            vibratePhone(context)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Chat Application",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Message input
        OutlinedTextField(
            value = messageInput,
            onValueChange = { messageInput = it },
            label = { Text("Enter a message") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("MessageInput"),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Send and Receive buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = { addMessage(true) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("SendButton")
            ) {
                Text("Send")
            }

            Button(
                onClick = { addMessage(false) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ReceiveButton")
            ) {
                Text("Receive")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider()

        // Display messages
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {

            itemsIndexed(messages) { index, message ->

                MessageRow(
                    message = message,
                    messageTag = "Message$index",
                    avatarTag = "Avatar$index"
                )
            }
        }
    }
}


@Composable
fun MessageRow(
    message: ChatMessage,
    messageTag: String,
    avatarTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isSent) {
            Arrangement.Start
        } else {
            Arrangement.End
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (message.isSent) {
            // Send: avatar -> bubble -> timestamp
            MessageAvatar(text = "ME", avatarTag = avatarTag)
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp)
            ) {
                Text(
                    text = message.message,
                    modifier = Modifier.testTag(messageTag),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = message.timestamp,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // Receive: timestamp -> bubble -> avatar
            Text(
                text = message.timestamp,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(6.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(12.dp)
            ) {
                Text(
                    text = message.message,
                    modifier = Modifier.testTag(messageTag),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            MessageAvatar(text = "FR", avatarTag = avatarTag)
        }
    }
}

@Composable
fun MessageAvatar(
    text: String,
    avatarTag: String
) {

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            modifier = Modifier.testTag(avatarTag),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

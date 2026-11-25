
package com.example.mylabs

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

data class ChatMessage(
    val message: String,
    val isSent: Boolean,
    val timestamp: String
)

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
    var messageInput by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var selectedMessage by remember { mutableStateOf<ChatMessage?>(null) }

    val context = LocalContext.current

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
            vibratePhone(context)
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {

        val isTablet = maxWidth >= 600.dp

        if (isTablet) {
            // TABLET: List on the left, details on the right
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    ChatList(
                        messages = messages,
                        messageInput = messageInput,
                        onMessageInputChange = { messageInput = it },
                        onSend = { addMessage(true) },
                        onReceive = { addMessage(false) },
                        onMessageClick = { selectedMessage = it }
                    )
                }

                VerticalDivider()

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    if (selectedMessage != null) {
                        MessageDetails(
                            message = selectedMessage!!,
                            onClose = { selectedMessage = null },
                            onRemove = {
                                messages.remove(selectedMessage)
                                selectedMessage = null
                            }
                        )
                    } else {
                        Text(
                            text = "Select a message to view its details.",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            // PHONE: Details replace the list when a message is selected
            if (selectedMessage != null) {
                MessageDetails(
                    message = selectedMessage!!,
                    onClose = { selectedMessage = null },
                    onRemove = {
                        messages.remove(selectedMessage)
                        selectedMessage = null
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            } else {
                ChatList(
                    messages = messages,
                    messageInput = messageInput,
                    onMessageInputChange = { messageInput = it },
                    onSend = { addMessage(true) },
                    onReceive = { addMessage(false) },
                    onMessageClick = { selectedMessage = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ChatList(
    messages: List<ChatMessage>,
    messageInput: String,
    onMessageInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onReceive: () -> Unit,
    onMessageClick: (ChatMessage) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Chat Application",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = messageInput,
            onValueChange = onMessageInputChange,
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onSend,
                modifier = Modifier
                    .weight(1f)
                    .testTag("SendButton")
            ) {
                Text("Send")
            }

            Button(
                onClick = onReceive,
                modifier = Modifier
                    .weight(1f)
                    .testTag("ReceiveButton")
            ) {
                Text("Receive")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider()

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
                    avatarTag = "Avatar$index",
                    onClick = { onMessageClick(message) }
                )
            }
        }
    }
}

@Composable
fun MessageRow(
    message: ChatMessage,
    messageTag: String,
    avatarTag: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = if (message.isSent) {
            Arrangement.Start
        } else {
            Arrangement.End
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (message.isSent) {
            MessageAvatar(text = "ME", avatarTag = avatarTag)
            Spacer(modifier = Modifier.width(8.dp))
        } else {
            Text(
                text = message.timestamp,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        Column(
            modifier = Modifier
                .widthIn(max = 250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (message.isSent) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    }
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.message,
                modifier = Modifier.testTag(messageTag),
                fontSize = 16.sp
            )
        }

        if (message.isSent) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = message.timestamp,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Spacer(modifier = Modifier.width(8.dp))
            MessageAvatar(text = "FR", avatarTag = avatarTag)
        }
    }
}

@Composable
fun MessageDetails(
    message: ChatMessage,
    onClose: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Message Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sent messages appear on the left.
        // Received messages appear on the right.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (message.isSent) {
                Arrangement.Start
            } else {
                Arrangement.End
            }
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (message.isSent) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = message.message,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message.timestamp,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = if (message.isSent) "Sent" else "Received",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onRemove,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("RemoveButton")
        ) {
            Text("Remove")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("CloseButton")
        ) {
            Text("Close")
        }
    }
}

@Composable
fun MessageAvatar(text: String, avatarTag: String) {
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

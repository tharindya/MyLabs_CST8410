package com.example.mylabs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = viewModel()
) {
    val firstName by viewModel.firstName.collectAsStateWithLifecycle()
    val lastName by viewModel.lastName.collectAsStateWithLifecycle()
    val address by viewModel.address.collectAsStateWithLifecycle()
    val saveDataEnabled by viewModel.saveDataEnabled.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "User Profile",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = viewModel::onFirstNameChange,
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = viewModel::onLastNameChange,
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = address,
            onValueChange = viewModel::onAddressChange,
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Data", modifier = Modifier.weight(1f))
            Switch(
                checked = saveDataEnabled,
                onCheckedChange = { checked ->
                    if (checked) {
                        showDialog = true          // Ask permission first
                    } else {
                        viewModel.onSaveDataChanged(false)  // Decline → clear data
                    }
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Data Collection Permission") },
                text = {
                    Text(
                        "Our company collects your first name, last name and address " +
                                "to understand where our customers live so we can decide where " +
                                "to build our next store. This information is not required for " +
                                "using our services. You can decline and still use the app."
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onSaveDataChanged(true)
                        showDialog = false
                    }) {
                        Text("Agree")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.onSaveDataChanged(false)
                        showDialog = false
                    }) {
                        Text("Decline")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    // Simple preview with sample data (no real ViewModel)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("User Profile", fontSize = 24.sp)
        OutlinedTextField(value = "Tharindya", onValueChange = {}, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = "Anjalika", onValueChange = {}, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = "123 Main St, Ottawa", onValueChange = {}, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Save Data", modifier = Modifier.weight(1f))
            Switch(checked = true, onCheckedChange = {})
        }
    }
}
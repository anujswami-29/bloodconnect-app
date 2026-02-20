package com.example.androidapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidapp.domain.RequestStatus
import com.example.androidapp.state.RequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorRequestsScreen(
    vm: RequestViewModel,
    onBack: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onAcceptRequest: (String) -> Unit = {},
) {
    val list = vm.activeRequests

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby requests") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Text(
                text = "In a real app this would use your live location. Here we show demo emergencies you can \"accept\".",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))

            if (list.isEmpty()) {
                Text("No active emergencies in this demo yet.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(list, key = { it.id }) { req ->
                        val canResume = req.status == RequestStatus.VOLUNTEER_EN_ROUTE || req.status == RequestStatus.DONOR_REACHED
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(
                                    if (canResume) Modifier.clickable { onAcceptRequest(req.id) }
                                    else Modifier
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(req.hospitalName, style = MaterialTheme.typography.titleMedium)
                                    if (req.isVerified) {
                                        Spacer(Modifier.width(8.dp))
                                        Text("✓", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                if (req.requiredGroup.isRare) {
                                    Text("⚠ Rare blood", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                }
                                Text("Group: ${req.requiredGroup}", style = MaterialTheme.typography.bodyMedium)
                                Text("Units: ${req.unitsNeeded}", style = MaterialTheme.typography.bodySmall)
                                Text("Status: ${req.status}", style = MaterialTheme.typography.bodySmall)

                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                ) {
                                    val canAccept = req.status == RequestStatus.SEARCHING
                                    Button(
                                        enabled = canAccept,
                                        onClick = {
                                            vm.acceptRequestAsDonor(req.id)
                                            onAcceptRequest(req.id)
                                        },
                                    ) {
                                        Text(if (canAccept) "I can donate" else "Accepted")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


package com.example.androidapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
fun AdminCommandCenterScreen(
    vm: RequestViewModel,
    onBack: () -> Unit,
) {
    val requests = vm.activeRequests
    val searching = requests.count { it.status == RequestStatus.SEARCHING }
    val inProgress = requests.count { it.status in listOf(RequestStatus.VOLUNTEER_EN_ROUTE, RequestStatus.DONOR_REACHED) }
    val completed = requests.count { it.status == RequestStatus.COMPLETED }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Command Center") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Live dashboard", style = MaterialTheme.typography.titleLarge)
            Text("Emergency heatmap view (demo)", style = MaterialTheme.typography.bodySmall)

            // Heatmap placeholder
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().height(120.dp),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.LocalHospital, contentDescription = null, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text("City heatmap – request clusters", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.fillMaxWidth())
                }
            }


            // Stats
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Searching", searching, Icons.Default.Warning)
                StatCard("In progress", inProgress, Icons.Default.People)
                StatCard("Completed", completed, Icons.Default.LocalHospital)
            }

            Text("Blood stock prediction (demo)", style = MaterialTheme.typography.titleMedium)
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("O−: Low", style = MaterialTheme.typography.bodyMedium)
                    LinearProgressIndicator(progress = { 0.2f }, modifier = Modifier.fillMaxWidth().height(8.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("A+: Adequate", style = MaterialTheme.typography.bodyMedium)
                    LinearProgressIndicator(progress = { 0.7f }, modifier = Modifier.fillMaxWidth().height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RowScope.StatCard(label: String, value: Int, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    ElevatedCard(
        modifier = Modifier.weight(1f),
    ) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null)
            Text("$value", style = MaterialTheme.typography.headlineSmall)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

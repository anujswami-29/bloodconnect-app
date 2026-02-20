package com.example.androidapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidapp.domain.BloodGroup
import com.example.androidapp.domain.EmergencyRequest
import com.example.androidapp.domain.GeoPoint
import com.example.androidapp.domain.RankedDonor
import com.example.androidapp.state.RequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRequestScreen(
    vm: RequestViewModel,
    onCreated: (EmergencyRequest) -> Unit,
) {
    var hospitalName by remember { mutableStateOf("City Hospital") }
    var units by remember { mutableStateOf("2") }
    var selectedGroup by remember { mutableStateOf(BloodGroup.O_NEG) }
    var isVerified by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New emergency request") },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            OutlinedTextField(
                value = hospitalName,
                onValueChange = { hospitalName = it },
                label = { Text("Hospital / blood bank name") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = units,
                onValueChange = { units = it.filter { c -> c.isDigit() }.take(2) },
                label = { Text("Units needed") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            Text("Required blood group", style = MaterialTheme.typography.bodyMedium)
            if (selectedGroup.isRare) {
                Text("⚠ Rare blood – priority alert to eligible donors", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(8.dp))
            BloodGroupChips(selected = selectedGroup, onSelected = { selectedGroup = it })

            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = isVerified, onCheckedChange = { isVerified = it })
                Text("Hospital-verified request (upload proof in production)")
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val unitsInt = units.toIntOrNull() ?: 1
                    val location = GeoPoint(28.6139, 77.2090)
                    val req = vm.createEmergencyRequest(
                        hospitalName = hospitalName.ifBlank { "City Hospital" },
                        location = location,
                        group = selectedGroup,
                        units = unitsInt,
                        isVerified = isVerified,
                    )
                    onCreated(req)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Create & find donors")
            }
        }
    }
}

@Composable
fun RequestListScreen(
    vm: RequestViewModel,
    onOpenDetails: (EmergencyRequest) -> Unit,
) {
    val list = vm.activeRequests

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Active requests", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        if (list.isEmpty()) {
            Text("No active requests yet.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(list, key = { it.id }) { req ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenDetails(req) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(req.hospitalName, style = MaterialTheme.typography.titleMedium)
                            Text("Group: ${req.requiredGroup}", style = MaterialTheme.typography.bodyMedium)
                            Text("Units: ${req.unitsNeeded}", style = MaterialTheme.typography.bodySmall)
                            Text("Status: ${req.status}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDetailsScreen(
    vm: RequestViewModel,
    requestId: String,
) {
    val req = vm.activeRequests.firstOrNull { it.id == requestId }
    val matches = vm.matchesFor(requestId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request details") },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            if (req == null) {
                Text("Request not found", style = MaterialTheme.typography.titleMedium)
            } else {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(req.hospitalName, style = MaterialTheme.typography.titleMedium)
                    if (req.isVerified) {
                        Spacer(Modifier.width(8.dp))
                        Text("✓ Verified", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }
                if (req.requiredGroup.isRare) {
                    Text("⚠ Rare blood – priority matching", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
                Text("Group: ${req.requiredGroup}")
                Text("Units: ${req.unitsNeeded}")
                Text("Status: ${req.status}")

                Spacer(Modifier.height(16.dp))
                Text("Suggested donors", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                if (matches.isEmpty()) {
                    Text("No compatible donors in demo data.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(matches, key = { it.donor.id }) { ranked ->
                            DonorMatchRow(ranked)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DonorMatchRow(match: RankedDonor) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(match.donor.name, style = MaterialTheme.typography.titleMedium)
            Text("Group: ${match.donor.bloodGroup}", style = MaterialTheme.typography.bodyMedium)
            Text("Match score: ${(match.score * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
            Text("Availability: ${(match.availabilityProbability * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
            Text("Distance: ${"%.1f".format(match.distanceKm)} km • ETA: ${match.etaMinutes} min", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun BloodGroupChips(
    selected: BloodGroup,
    onSelected: (BloodGroup) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BloodChip("O−", BloodGroup.O_NEG, selected, onSelected)
            BloodChip("O+", BloodGroup.O_POS, selected, onSelected)
            BloodChip("A−", BloodGroup.A_NEG, selected, onSelected)
            BloodChip("A+", BloodGroup.A_POS, selected, onSelected)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BloodChip("B−", BloodGroup.B_NEG, selected, onSelected)
            BloodChip("B+", BloodGroup.B_POS, selected, onSelected)
            BloodChip("AB−", BloodGroup.AB_NEG, selected, onSelected)
            BloodChip("AB+", BloodGroup.AB_POS, selected, onSelected)
        }
    }
}

@Composable
private fun BloodChip(
    label: String,
    group: BloodGroup,
    selected: BloodGroup,
    onSelected: (BloodGroup) -> Unit,
) {
    androidx.compose.material3.FilterChip(
        selected = group == selected,
        onClick = { onSelected(group) },
        label = { Text(label) },
    )
}


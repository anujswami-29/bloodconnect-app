package com.example.androidapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidapp.domain.Badge
import com.example.androidapp.domain.DonationRecord
import com.example.androidapp.state.GamificationViewModel
import com.example.androidapp.state.RequestViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorPassportScreen(
    vm: RequestViewModel,
    gamificationVm: GamificationViewModel = viewModel(),
    onBack: () -> Unit = {},
    onOpenGamification: () -> Unit = {},
) {
    val records = vm.donorDonations
    LaunchedEffect(records) { gamificationVm.refresh(records) }
    val stats = gamificationVm.donorStats

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Donor Passport") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            // Impact score & eligibility
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text("Impact Score", style = MaterialTheme.typography.labelMedium)
                            Text("${stats.impactScore} lives", style = MaterialTheme.typography.headlineMedium)
                        }
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Eligibility: ${if (stats.eligibilityDaysLeft > 0) "Next donation in ${stats.eligibilityDaysLeft} days" else "Eligible now"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Health tip: Drink plenty of water before donating.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Badges
            if (stats.badges.isNotEmpty()) {
                Text("Badges", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(stats.badges, key = { it.name }) { badge ->
                        BadgeChip(badge)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            Text("Donation history", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            if (records.isEmpty()) {
                Text("No donations recorded yet.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(records, key = { it.id }) { record ->
                        DonationCard(record)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onOpenGamification,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Celebration, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.padding(8.dp))
                    Text("View badges & leaderboard", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun BadgeChip(badge: Badge) {
    val label = when (badge) {
        Badge.FIRST_DONATION -> "First Donation"
        Badge.LIFESAVER_3 -> "Lifesaver x3"
        Badge.LIFESAVER_10 -> "Lifesaver x10"
        Badge.RARE_HERO -> "Rare Hero"
        Badge.SPEED_DEMON -> "Speed Demon"
        Badge.STREAK_3 -> "Streak"
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun DonationCard(record: DonationRecord) {
    val dateText = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        .format(Date(record.dateMillis))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(record.locationName, style = MaterialTheme.typography.titleMedium)
            Text("Group: ${record.bloodGroup}", style = MaterialTheme.typography.bodyMedium)
            Text("Units: ${record.units}", style = MaterialTheme.typography.bodySmall)
            Text(dateText, style = MaterialTheme.typography.bodySmall)
        }
    }
}

package com.example.androidapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(
    onBack: () -> Unit,
    activeRequestCount: Int,
    pendingOver30Min: Int,
) {
    var query by remember { mutableStateOf("") }
    var response by remember { mutableStateOf<String?>(null) }

    val suggestions = listOf(
        "Which volunteers are inactive this month?",
        "Show pending requests older than 30 min",
        "Predict donor availability this weekend",
    )

    fun processQuery(q: String): String {
        return when {
            q.contains("inactive") -> "Demo: Top inactive volunteers – Amit (45 days), Rahul (30 days). Consider sending re-engagement alerts."
            q.contains("pending") || q.contains("30") -> "You have $pendingOver30Min pending request(s) older than 30 min. Prioritize follow-up."
            q.contains("predict") || q.contains("weekend") -> "Predicted availability: O− 65%, A+ 80%. Weekend donors typically respond 20% faster."
            else -> "Ask me about: inactive volunteers, pending requests, donor predictions. (AI assistant – demo mode)"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Ops Assistant") },
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SmartToy, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.padding(8.dp))
                    Text("Ask: volunteers, requests, predictions", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Suggested questions", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))
            suggestions.forEach { s ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        query = s
                        response = processQuery(s)
                    },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Text(s, Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(4.dp))
            }

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Type your question...") },
                trailingIcon = {
                    IconButton(onClick = { response = processQuery(query) }) {
                        Text("Ask", style = MaterialTheme.typography.labelLarge)
                    }
                },
            )

            if (response != null) {
                Spacer(Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                ) {
                    Text(response!!, Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

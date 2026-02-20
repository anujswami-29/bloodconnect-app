package com.example.androidapp.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.androidapp.domain.DonationRecord
import com.example.androidapp.domain.GamificationEngine
import com.example.androidapp.domain.InMemoryDonorRepository

class GamificationViewModel : ViewModel() {
    private val donorRepo = InMemoryDonorRepository()

    var donorStats by mutableStateOf(com.example.androidapp.domain.DonorStats(0, 0, emptyList(), 90))
        private set

    var leaderboard by mutableStateOf<List<Pair<String, Int>>>(emptyList())
        private set

    fun refresh(donations: List<DonationRecord>) {
        donorStats = GamificationEngine.computeDonorStats(donations)
        leaderboard = GamificationEngine.leaderboard(donorRepo.allDonors(), donations.size)
    }
}

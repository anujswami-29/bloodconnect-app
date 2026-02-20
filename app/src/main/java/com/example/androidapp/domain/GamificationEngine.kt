package com.example.androidapp.domain

/**
 * Gamification engine: badges, impact score, leaderboard.
 */
object GamificationEngine {

    fun computeDonorStats(records: List<DonationRecord>): DonorStats {
        val totalDonations = records.size
        val impactScore = records.sumOf { it.units }
        val badges = mutableListOf<Badge>()

        if (totalDonations >= 1) badges.add(Badge.FIRST_DONATION)
        if (totalDonations >= 3) badges.add(Badge.LIFESAVER_3)
        if (totalDonations >= 10) badges.add(Badge.LIFESAVER_10)
        if (records.any { it.bloodGroup.isRare }) badges.add(Badge.RARE_HERO)

        val lastDonation = records.maxByOrNull { it.dateMillis }
        val eligibilityDaysLeft = if (lastDonation != null) {
            val daysSince = ((System.currentTimeMillis() - lastDonation.dateMillis) / (1000L * 60 * 60 * 24)).toInt()
            (90 - daysSince).coerceAtLeast(0)
        } else 90

        return DonorStats(
            totalDonations = totalDonations,
            impactScore = impactScore,
            badges = badges,
            eligibilityDaysLeft = eligibilityDaysLeft,
        )
    }

    /** Demo leaderboard: donors with simulated donation counts. */
    fun leaderboard(donors: List<Donor>, currentUserDonations: Int): List<Pair<String, Int>> {
        val simulated = donors.mapIndexed { i, d -> d.name to (d.responseScore * 15).toInt() + i }
        val currentUser = "You" to currentUserDonations
        return (simulated + currentUser).sortedByDescending { it.second }
    }
}

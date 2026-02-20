package com.example.androidapp.domain

import kotlin.math.max

/**
 * AI-powered donor matching engine.
 * Factors: blood group compatibility, distance, last donation date,
 * availability probability, response history.
 * Example query: "Find nearest O− donor within 8km who is most likely to respond in 5 min."
 */
class MatchingEngine(
    private val donors: () -> List<Donor>,
) {

    /**
     * Find compatible donors within maxDistanceKm, ranked by composite score.
     */
    fun compatibleDonors(
        request: EmergencyRequest,
        maxDistanceKm: Double = 20.0,
    ): List<RankedDonor> {
        val now = System.currentTimeMillis()
        return donors()
            .filter { isCompatible(it.bloodGroup, request.requiredGroup) }
            .mapNotNull { donor ->
                val distanceKm = distanceKm(donor.location, request.hospitalLocation)
                if (distanceKm > maxDistanceKm) return@mapNotNull null

                val availabilityScore = availabilityScore(donor.lastDonationDaysAgo)
                val distanceScore = max(0.0, 1.0 - (distanceKm / maxDistanceKm))
                val responseScore = donor.responseScore

                // Rare blood boost: when O− or AB− needed, boost rare donors
                val rareBoost = if (request.requiredGroup.isRare && donor.bloodGroup.isRare) 0.15 else 0.0

                val availabilityProbability = availabilityScore * 0.4 + responseScore * 0.6

                val totalScore = (0.35 * availabilityScore +
                    0.30 * distanceScore +
                    0.30 * responseScore +
                    rareBoost).coerceIn(0.0, 1.0)

                val etaMinutes = (distanceKm / 0.4).toInt().coerceAtLeast(3).coerceAtMost(60)

                RankedDonor(
                    donor = donor,
                    score = totalScore,
                    distanceKm = distanceKm,
                    etaMinutes = etaMinutes,
                    availabilityProbability = availabilityProbability,
                )
            }
            .sortedByDescending { it.score }
    }

    private fun availabilityScore(lastDonationDaysAgo: Int): Double {
        return when {
            lastDonationDaysAgo >= 90 -> 1.0
            lastDonationDaysAgo <= 0 -> 0.2
            else -> (lastDonationDaysAgo / 90.0).coerceIn(0.0, 1.0)
        }
    }

    private fun isCompatible(donor: BloodGroup, required: BloodGroup): Boolean {
        return when (required) {
            BloodGroup.O_NEG -> donor == BloodGroup.O_NEG
            BloodGroup.O_POS -> donor == BloodGroup.O_POS || donor == BloodGroup.O_NEG
            BloodGroup.A_NEG -> donor == BloodGroup.A_NEG || donor == BloodGroup.O_NEG
            BloodGroup.A_POS -> donor in setOf(
                BloodGroup.A_POS, BloodGroup.A_NEG,
                BloodGroup.O_POS, BloodGroup.O_NEG,
            )
            BloodGroup.B_NEG -> donor == BloodGroup.B_NEG || donor == BloodGroup.O_NEG
            BloodGroup.B_POS -> donor in setOf(
                BloodGroup.B_POS, BloodGroup.B_NEG,
                BloodGroup.O_POS, BloodGroup.O_NEG,
            )
            BloodGroup.AB_NEG -> donor in setOf(
                BloodGroup.AB_NEG,
                BloodGroup.A_NEG, BloodGroup.B_NEG, BloodGroup.O_NEG,
            )
            BloodGroup.AB_POS -> true
        }
    }
}

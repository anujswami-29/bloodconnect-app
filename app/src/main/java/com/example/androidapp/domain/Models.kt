package com.example.androidapp.domain

import com.example.androidapp.state.UserRole
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

enum class BloodGroup {
    A_POS, A_NEG,
    B_POS, B_NEG,
    AB_POS, AB_NEG,
    O_POS, O_NEG,
    ;

    val isRare: Boolean get() = this == O_NEG || this == AB_NEG
}

data class GeoPoint(
    val lat: Double,
    val lon: Double,
)

enum class RequestStatus {
    SEARCHING,
    MATCHED,
    VOLUNTEER_EN_ROUTE,
    DONOR_REACHED,
    COMPLETED,
    CANCELLED,
}

data class Donor(
    val id: String,
    val name: String,
    val bloodGroup: BloodGroup,
    val location: GeoPoint,
    val lastDonationDaysAgo: Int,
    val responseScore: Double, // 0–1, higher = more responsive
    val role: UserRole = UserRole.DONOR,
)

data class EmergencyRequest(
    val id: String,
    val hospitalName: String,
    val hospitalLocation: GeoPoint,
    val requiredGroup: BloodGroup,
    val unitsNeeded: Int,
    val createdAtMillis: Long,
    val status: RequestStatus,
    val matchedDonorIds: List<String> = emptyList(),
    val isVerified: Boolean = false, // Hospital-verified request
)

data class DonationRecord(
    val id: String,
    val requestId: String?,
    val dateMillis: Long,
    val bloodGroup: BloodGroup,
    val units: Int,
    val locationName: String,
)

data class RankedDonor(
    val donor: Donor,
    val score: Double,
    val distanceKm: Double,
    val etaMinutes: Int,
    val availabilityProbability: Double = 0.0, // 0-1, likelihood to respond
)

// Gamification
enum class Badge {
    FIRST_DONATION,
    LIFESAVER_3,
    LIFESAVER_10,
    RARE_HERO,      // Donated rare blood
    SPEED_DEMON,    // Responded within 5 min
    STREAK_3,       // 3 donations in 6 months
}

data class DonorStats(
    val totalDonations: Int,
    val impactScore: Int,       // Lives saved approximation
    val badges: List<Badge>,
    val eligibilityDaysLeft: Int,
)

fun distanceKm(a: GeoPoint, b: GeoPoint): Double {
    // Simple Euclidean approximation in degrees, good enough for offline demo
    val dLat = a.lat - b.lat
    val dLon = a.lon - b.lon
    val approx = sqrt(dLat.pow(2) + dLon.pow(2)) * 111.0
    return abs(approx)
}


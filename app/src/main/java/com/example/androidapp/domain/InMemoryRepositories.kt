package com.example.androidapp.domain

import java.util.UUID

class InMemoryDonorRepository {
    private val donors = listOf(
        Donor(
            id = "d1",
            name = "Amit (O−)",
            bloodGroup = BloodGroup.O_NEG,
            location = GeoPoint(28.6139, 77.2090),
            lastDonationDaysAgo = 120,
            responseScore = 0.9,
        ),
        Donor(
            id = "d2",
            name = "Priya (A+)",
            bloodGroup = BloodGroup.A_POS,
            location = GeoPoint(28.7041, 77.1025),
            lastDonationDaysAgo = 45,
            responseScore = 0.8,
        ),
        Donor(
            id = "d3",
            name = "Rahul (B+)",
            bloodGroup = BloodGroup.B_POS,
            location = GeoPoint(28.5355, 77.3910),
            lastDonationDaysAgo = 10,
            responseScore = 0.6,
        ),
        Donor(
            id = "d4",
            name = "Sara (AB−)",
            bloodGroup = BloodGroup.AB_NEG,
            location = GeoPoint(28.4595, 77.0266),
            lastDonationDaysAgo = 200,
            responseScore = 0.7,
        ),
    )

    fun allDonors(): List<Donor> = donors

    fun donorById(id: String): Donor? = donors.firstOrNull { it.id == id }
}

class InMemoryRequestRepository(
    private val donorRepo: InMemoryDonorRepository,
) {
    private val requests = mutableListOf<EmergencyRequest>()

    fun createRequest(
        hospitalName: String,
        hospitalLocation: GeoPoint,
        requiredGroup: BloodGroup,
        units: Int,
        isVerified: Boolean = false,
    ): EmergencyRequest {
        val req = EmergencyRequest(
            id = UUID.randomUUID().toString(),
            hospitalName = hospitalName,
            hospitalLocation = hospitalLocation,
            requiredGroup = requiredGroup,
            unitsNeeded = units,
            createdAtMillis = System.currentTimeMillis(),
            status = RequestStatus.SEARCHING,
            isVerified = isVerified,
        )
        requests.add(req)
        return req
    }

    fun allRequests(): List<EmergencyRequest> = requests

    fun update(updated: EmergencyRequest) {
        val index = requests.indexOfFirst { it.id == updated.id }
        if (index >= 0) {
            requests[index] = updated
        }
    }
}


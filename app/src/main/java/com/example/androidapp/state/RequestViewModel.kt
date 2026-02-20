package com.example.androidapp.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.androidapp.domain.BloodGroup
import com.example.androidapp.domain.DonationRecord
import com.example.androidapp.domain.EmergencyRequest
import com.example.androidapp.domain.GeoPoint
import com.example.androidapp.domain.InMemoryDonorRepository
import com.example.androidapp.domain.InMemoryRequestRepository
import com.example.androidapp.domain.MatchingEngine
import com.example.androidapp.domain.RankedDonor
import com.example.androidapp.domain.RequestStatus

class RequestViewModel : ViewModel() {
    private val donorRepo = InMemoryDonorRepository()
    private val requestRepo = InMemoryRequestRepository(donorRepo)
    private val matchingEngine = MatchingEngine { donorRepo.allDonors() }

    var activeRequests by mutableStateOf<List<EmergencyRequest>>(emptyList())
        private set

    var currentMatches by mutableStateOf<List<RankedDonor>>(emptyList())
        private set

    var donorDonations by mutableStateOf<List<DonationRecord>>(emptyList())
        private set

    fun createEmergencyRequest(
        hospitalName: String,
        location: GeoPoint,
        group: BloodGroup,
        units: Int,
        isVerified: Boolean = false,
    ): EmergencyRequest {
        val req = requestRepo.createRequest(
            hospitalName = hospitalName,
            hospitalLocation = location,
            requiredGroup = group,
            units = units,
            isVerified = isVerified,
        )
        refresh()
        currentMatches = matchingEngine.compatibleDonors(req)
        return req
    }

    fun refresh() {
        activeRequests = requestRepo.allRequests()
    }

    fun matchesFor(requestId: String, maxDistanceKm: Double = 20.0): List<RankedDonor> {
        val req = activeRequests.firstOrNull { it.id == requestId } ?: return emptyList()
        return matchingEngine.compatibleDonors(req, maxDistanceKm)
    }

    fun acceptRequestAsDonor(requestId: String) {
        val req = activeRequests.firstOrNull { it.id == requestId } ?: return
        val updated = req.copy(status = RequestStatus.VOLUNTEER_EN_ROUTE)
        requestRepo.update(updated)
        refresh()

        val record = DonationRecord(
            id = "rec_${updated.id}",
            requestId = updated.id,
            dateMillis = System.currentTimeMillis(),
            bloodGroup = updated.requiredGroup,
            units = updated.unitsNeeded,
            locationName = updated.hospitalName,
        )
        donorDonations = donorDonations + record
    }

    fun markDonorReached(requestId: String) {
        val req = activeRequests.firstOrNull { it.id == requestId } ?: return
        val updated = req.copy(status = RequestStatus.DONOR_REACHED)
        requestRepo.update(updated)
        refresh()
    }

    fun markDonationComplete(requestId: String) {
        val req = activeRequests.firstOrNull { it.id == requestId } ?: return
        val updated = req.copy(status = RequestStatus.COMPLETED)
        requestRepo.update(updated)
        refresh()
    }
}


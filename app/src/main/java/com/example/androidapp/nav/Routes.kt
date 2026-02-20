package com.example.androidapp.nav

import android.net.Uri

sealed class Route(val value: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object DonorHome : Route("donor_home")
    data object OpsHome : Route("ops_home")
    data object DonorRequests : Route("donor_requests")
    data object DonationJourney : Route("donation_journey/{id}") {
        fun create(id: String): String = "donation_journey/$id"
    }
    data object DonorPassport : Route("donor_passport")
    data object CreateRequest : Route("create_request")
    data object RequestDetails : Route("request_details/{id}") {
        fun create(id: String): String = "request_details/$id"
    }
    data object Settings : Route("settings")
    data object HelpCenter : Route("help_center")
    data object FAQs : Route("faqs")
    data object Contact : Route("contact")
    data object About : Route("about")
    data object DonationTips : Route("donation_tips")
    data object Gamification : Route("gamification")
    data object AdminCommandCenter : Route("admin_command_center")
    data object AIAssistant : Route("ai_assistant")
    data object PredictiveAnalytics : Route("predictive_analytics")
    data object Detail : Route("detail/{id}") {
        fun create(id: String): String = "detail/${Uri.encode(id)}"
    }
}


package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal

data class AssignmentRouteStop(
        val stopID: Int,
        val stopName: String,
        val index: Int,
        val address: String,
        val latitude: BigDecimal,
        val longitude: BigDecimal
)
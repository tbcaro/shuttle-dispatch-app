package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal

data class AssignmentStopDrop(
        val stopID: Int,
        val stopName: String,
        val address: String,
        val latitude: BigDecimal,
        val longitude: BigDecimal
)
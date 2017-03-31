package com.polaris.app.dispatch.repository.entity

import java.math.BigDecimal

data class AssignmentRouteStopEntity(
        val stopID: Int,
        val stopName: String,
        val index: Int,
        val address: String,
        val latitude: BigDecimal,
        val longitude: BigDecimal
)

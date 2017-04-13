package com.polaris.app.dispatch.repository.entity

import java.math.BigDecimal

data class RouteStopEntity(
        val routeID: Int,
        val stopID: Int,
        val index: Int,
        val name: String?,
        val address: String?,
        val latitude: BigDecimal,
        val longitude: BigDecimal
)
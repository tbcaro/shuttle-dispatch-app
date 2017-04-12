package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal

data class RouteStop(
        val stopID: Int,
        val index: Int,
        val name: String?,
        val address: String?,
        val latitude: BigDecimal,
        val longitude: BigDecimal
)
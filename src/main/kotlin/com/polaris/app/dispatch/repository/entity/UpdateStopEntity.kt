package com.polaris.app.dispatch.repository.entity

import java.math.BigDecimal

data class UpdateStopEntity(
        val stopID: Int,
        val stopName: String,
        val stopAddress: String,
        val stopLat: BigDecimal,
        val stopLong: BigDecimal
)
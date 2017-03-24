package com.polaris.app.dispatch.repository.entity

import java.math.BigDecimal
import java.time.LocalTime

data class StopEntity(
        val stopName: String,
        val stopAddress: String,
        val stopLat: BigDecimal,
        val stopLong: BigDecimal
)

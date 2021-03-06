package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal

data class Stop(
        val stopID: Int,
        val stopName: String?,
        val stopAddress: String?,
        val stopLat: BigDecimal,
        val stopLong: BigDecimal
)
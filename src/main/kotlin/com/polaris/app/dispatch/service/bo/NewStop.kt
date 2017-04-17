package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal

data class NewStop(
        val stopName: String?,
        val stopAddress: String?,
        val stopLat: BigDecimal,
        val stopLong: BigDecimal
)
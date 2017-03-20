package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal

data class MapShuttle(
        val ShuttleID: Int,
        val ShuttleName: String,
        val ShuttleIconColor: Int,
        val ShuttleLat: BigDecimal,
        val ShuttleLong: BigDecimal,
        val ShuttleStatus: String
)
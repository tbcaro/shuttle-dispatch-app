package com.polaris.app.dispatch.repository.entity

import java.math.BigDecimal

data class MapShuttleEntity(
    val ShuttleID: Int,
    val ShuttleName: String,
    val ShuttleIconColor: Int,
    val ShuttleLat: BigDecimal,
    val ShuttleLong: BigDecimal,
    val ShuttleStatus: String
)
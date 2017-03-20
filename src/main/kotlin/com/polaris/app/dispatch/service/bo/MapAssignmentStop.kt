package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal
import java.time.LocalTime

data class MapAssignmentStop(
        val stopName: String,
        val stopAddress: String,
        val stopLat: BigDecimal,
        val stopLong: BigDecimal,
        val stopArrive: LocalTime,
        val stopDepart: LocalTime,
        val stopArriveEst: LocalTime,
        val stopDepartEst: LocalTime
)
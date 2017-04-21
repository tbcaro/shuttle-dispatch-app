package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal
import java.time.LocalDateTime

data class MapAssignmentStop(
        val assignmentStopId: Int,
        val stopId: Int?,
        val index: Int,
        val stopName: String,
        val stopAddress: String,
        val stopLat: BigDecimal,
        val stopLong: BigDecimal,
        val stopArrive: LocalDateTime?,
        val stopDepart: LocalDateTime?,
        val stopArriveEst: LocalDateTime?,
        val stopDepartEst: LocalDateTime?
)
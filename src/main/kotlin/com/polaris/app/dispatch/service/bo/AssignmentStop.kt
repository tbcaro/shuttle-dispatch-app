package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal
import java.time.LocalDateTime

data class AssignmentStop(
        val stopId: Int,
        val stopName: String,
        val stopAddress: String,
        val stopLat: BigDecimal,
        val stopLong: BigDecimal,
        val stopArrive: LocalDateTime?,
        val stopDepart: LocalDateTime?,
        val stopArriveEst: LocalDateTime?,
        val stopDepartEst: LocalDateTime?
)
package com.polaris.app.dispatch.repository.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class AssignmentStopEntity(
        val assignmentStopID: Int,
        val assignmentID: Int,
        val stopID: Int,
        val stopName: String,
        val stopIndex: Int,
        val stopAddress: String,
        val stopLat: BigDecimal,
        val stopLong: BigDecimal,
        val stopArrive: LocalDateTime?,
        val stopDepart: LocalDateTime?,
        val stopArriveEst: LocalDateTime?,
        val stopDepartEst: LocalDateTime?
)
